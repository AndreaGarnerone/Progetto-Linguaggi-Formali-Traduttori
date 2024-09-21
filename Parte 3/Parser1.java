import java.io.*;

public class Parser1 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser1(Lexer l, BufferedReader br) {
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
	    } else error("syntax error");
    }

    public void start() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            expr();
	        match(Tag.EOF);
        } else {
            error("Invalid start of expression");
        }
    }

    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term();
            exprp();
        } else {
            error ("Syntax error");
        }
    }

    private void exprp() {
	    switch (look.tag) {
    	case '+':
            match('+');
            term();
            exprp();
            break;

        case '-':
            match('-');
            term();
            exprp();
            break;
        
        case Tag.EOF:
        case ')':
            break;

        default:
            error("Syntax error exprp");
        }
    }

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        } else {
            error ("Syntax error");
        }
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;

            case '/':
                match('/');
                fact();
                termp();
                break;
            
            case '+':
            case '-':
            case Tag.EOF:
            case ')':
                break;

            default:
                error ("Syntax error termp");
        }
    }

    private void fact() {
        switch(look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            default:
                error("Syntax error fact");
        }
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/Users/dedeg/Documents/LFT/AndreaGarnerone/ProvaGenerale.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser1 parser = new Parser1(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
