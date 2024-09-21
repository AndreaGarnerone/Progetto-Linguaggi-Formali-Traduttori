import java.io.*;

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public static String s;

    public Parser2(Lexer l, BufferedReader br) {
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


    private void prog() {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag. WHILE || look.tag == Tag.COND || look.tag == '{') {
        statlist();
        match(Tag.EOF);
        } else {
            error ("Syntax error prog");
        }
    }

    private void statlist() {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag. WHILE || look.tag == Tag.COND || look.tag == '{') {
        stat();
        statlistp();
        } else {
            error ("Syntax error statlist");
        }
    }

    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                stat();
                statlistp();
                break;
            
            case Tag.EOF:
            case '}':
                break;
            
            default:
            error ("Syntax error statlistp");
        }
    }

    private void stat() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;

            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist();
                match(']');
                break;
            
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;

            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist();
                match(']');
                              
                    if (look.tag == Tag.END) {
                        match(Tag.END);
                    } else if (look.tag == Tag.ELSE) { 
                        match(Tag.ELSE);
                        stat();
                        match(Tag.END);
                    } else {
                        error ("Syntax error statlistp1");
                    }
                    break;
                
            case '{':
                match('{');
                statlist();
                match('}');
                break;
                
            default:
                error("Syntax error stat");
        }
    }

    private void idlist() {
        if (look.tag == Tag.ID) {
        match(Tag.ID);
        idlistp();
        } else {
            error("Syntax error idlist");
        }
    }

    private void idlistp() {
        switch (look.tag){
            case ',':
                match(',');
                idlistp();
                break;

            case Tag.ID:
                match(Tag.ID);
                idlistp();
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

    private void optlist() {
        if (look.tag == Tag.OPTION) {
            optitem();
            optlistp();
        } else {
            error("Syntax error optlist");
        }
    }

    private void optlistp() {
        switch(look.tag) {
            case Tag.OPTION:
            optitem();
            optlistp();
            break;

            case ']':
                break;
        default:
            error("Syntax error optlist");
        }
    }

    private void optitem() {
        if (look.tag == Tag.OPTION) {
            match(Tag.OPTION);
            match('(');
            bexpr();
            match(')');
            match(Tag.DO);
            stat();
        } else {
            error("Syntax error optitem");
        }
    }

    private void bexpr() {
        if (look.tag == Tag.RELOP) {
        match(Tag.RELOP);
        expr();
        expr();
        } else {
            error("Syntax error bexpr");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;

            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;

            case '-':
                match('-');
                expr();
                expr();
                break;

            case '/':
                match('/');
                expr();
                expr();
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            case Tag.ID:
                match(Tag.ID);
                break;

            default:
                error("Syntax error expr");
        }
    }

    private void exprlist() {
        if (look.tag == '+' || look.tag == '-' || look.tag == '/' || look.tag == '*' ||
            look.tag == Tag.NUM || look.tag == Tag.ID) {
        expr();
        exprlistp();
        } else {
            error("Syntax error exprlist");
        }
    }

    private void exprlistp() {
        switch(look.tag) {
            case ',':
                match(',');
                expr();
                exprlistp();
                break;

                case ']':
                case ')':
                    break;

            default:
                error("Syntax error exprlistp");
        }
    }


    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/Users/dedeg/Documents/LFT/AndreaGarnerone/ProvaGenerale.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
