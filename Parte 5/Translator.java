import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	    throw new Error("near line " + Lexer.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error match");
    }

    public void prog() {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag. WHILE || look.tag == Tag.COND || look.tag == '{') {
                int lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                }
                catch(java.io.IOException e) {
                System.out.println("IO error\n");
                };
        } else {
            error ("Syntax error prog");
        }
    }

    private void statlist(int lnext) {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag. WHILE || look.tag == Tag.COND || look.tag == '{') {
            stat(lnext);        
            statlistp(lnext);
        } else {
            error ("Syntax error statlist");
        }
    }

    private void statlistp(int lnext) {
        lnext = code.newLabel();
        code.emit(OpCode.GOto, lnext);
        code.emitLabel(lnext);
        switch (look.tag) {
            case ';':
                match(';');
                stat(lnext);
                statlistp(lnext);
                break;
            
            case Tag.EOF:
            case '}':
                break;
            
            default:
                error ("Syntax error statlistp");
        }
    }

    public void stat(int lnext) {
        switch(look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist("");
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist("print");
                match(']');
                code.emit(OpCode.invokestatic, 1);
                break;

            case Tag.READ:
                match(Tag.READ);
                match('[');
                code.emit(OpCode.invokestatic, 0);
	            idlist("read");
				match(']');
                break;
            
            case Tag.WHILE:
                int lstart_while = code.newLabel();
                int ltrue_while = code.newLabel();
                int lend_while = code.newLabel();
                
                match(Tag.WHILE);
                match('(');

                code.emitLabel(lstart_while);           //Etichetta di entrata del while
                
                bexpr(ltrue_while, lend_while);
                match(')');
                
                stat(lend_while);
                
                code.emit(OpCode.GOto, lstart_while);   //Riporta alla condizione da verificare
                code.emitLabel(lend_while);             //Etichetta dopo il while (se la condizione non e' verificata)
                break;

            case Tag.COND:
                int ltrue_cond = code.newLabel();
                int lfalse_cond = code.newLabel();
                int lelse_cond = code.newLabel();
                int lend_cond = code.newLabel();

                match(Tag.COND);
                match('[');
                optlist(ltrue_cond, lfalse_cond, lelse_cond, lend_cond);
                match(']');
                

                if (look.tag == Tag.ELSE) {
                    code.emitLabel(lelse_cond); //Se non e' entrato in nessuna condizione allora passa a questa etichetta
                    match(Tag.ELSE);
                    stat(lelse_cond);
                }
                code.emitLabel(lend_cond);      //Arriva direttamente qui se non fa l'else (se lo fa ci arriva dopo)
                match(Tag.END);
                break;
                
            case '{':
                match('{');
                statlist(lnext);
                match('}');
                break;
                
            default:
                error("Syntax error stat");
        }
     }

    private void idlist(String s) {
        switch(look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
                idlistp(s);
                code.emit(OpCode.istore, id_addr);
                break;

            default:
                error("Syntax error idlist");
    	}
    }

    private void idlistp(String s) {
        switch (look.tag){
            case ',':
                match(',');
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
                if (s == "read") {
                    code.emit(OpCode.invokestatic, 0);
                } else {
                    code.emit(OpCode.dup);
                }
                idlistp(s);
                code.emit(OpCode.istore, id_addr);
                break;

            case ';':
            case ']':
            case '}':
            case Tag.EOF:
            case Tag.END:
                break;

            default:
                error("Syntax error idlintp");
        }
    }

    private void optlist(int ltrue, int lfalse, int lelse, int lend) {
        if (look.tag == Tag.OPTION) {
            optitem(ltrue, lfalse, lelse, lend);
            optlistp(ltrue, lfalse, lelse, lend);
        } else {
            error("Syntax error optlist");
        }
    }

    private void optlistp(int ltrue, int lfalse, int lelse, int lend) {
        switch(look.tag) {
            case Tag.OPTION:
            ltrue = code.newLabel();
            lfalse = code.newLabel();
            optitem(ltrue, lfalse, lelse, lend);
            optlistp(ltrue, lfalse, lelse, lend);
            break;

            case ']':
                break;
        default:
            error("Syntax error optlist");
        }
    }

    private void optitem(int ltrue, int lfalse, int lelse, int lend) {
        if (look.tag == Tag.OPTION) {
            match(Tag.OPTION);
            match('(');
            bexpr(ltrue, lfalse);
            match(')');

            match(Tag.DO);

            stat(lend);
            
            code.emit(OpCode.GOto, lend);
            code.emitLabel(lfalse);
            
        } else {
            error("Syntax error optitem");
        }
    }

    private void bexpr(int ltrue, int lfalse) {
        if (look.tag == Tag.RELOP) {
            String relop = (((Word)look).lexeme);
            match(Tag.RELOP);
            expr();
            expr();
            switch (relop) { 
                case "<":
                    code.emit(OpCode.if_icmplt, ltrue);
                    break;

                case ">":
                    code.emit(OpCode.if_icmpgt, ltrue);
                    break;

                case "==":
                    code.emit(OpCode.if_icmpeq, ltrue);
                    break;

                case "<>": 
                    code.emit(OpCode.if_icmpne, ltrue);
                    break;

                case "<=":
                    code.emit(OpCode.if_icmple, ltrue);
                    break;

                case ">=":
                    code.emit(OpCode.if_icmpge, ltrue);
                    break;

                default:
                    error("Errore nel simbolo");
            }
        } else {
            error("Syntax error bexpr");
        }
        code.emit(OpCode.GOto, lfalse);
        code.emitLabel(ltrue);
    }

    private void expr() {
        switch(look.tag) {
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            case '+':
                match('+');
                match('(');
                exprlist("+");
                match(')');
                break;
    
            case '*':
                match('*');
                match('(');
                exprlist("*");
                match(')');
                break;

            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
    
            case Tag.NUM:
                int num_val = ((NumberTok)look).lexeme;
                code.emit(OpCode.ldc, num_val);
                match(Tag.NUM);
                break;

            case Tag.ID:
                int read_id_addr = st.lookupAddress(((Word) look).lexeme);
                if (read_id_addr == -1) {
                    error("Variable not initialized");
				}
				code.emit(OpCode.iload, read_id_addr);
                match(Tag.ID);
                break;
    
            default:
                error("Syntax error expr");
        }
    }

    private void exprlist(String s) {
        if (look.tag == '+' || look.tag == '-' || look.tag == '/' || look.tag == '*' ||
            look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            exprlistp(s);
        } else {
            error("Syntax error exprlist");
        }
    }

    private void exprlistp(String s) {
        switch (look.tag) {
            case ',':
            match(',');
            switch (s) {
                case "+":
                expr();
                code.emit(OpCode.iadd);
                break;
                
                case "*":
                expr();
                code.emit(OpCode.imul);
                break;
                
                case "print":
                code.emit(OpCode.invokestatic, 1);
                expr();
                break;
                
                default:
                error("Unknown operation");
            }
            exprlistp(s);
            break;
            
            case ']':
            case ')':
            break;
            
            default:
                error("Error in grammar: exprlistp");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/Users/dedeg/Documents/LFT/AndreaGarnerone/ProvaGenerale.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
