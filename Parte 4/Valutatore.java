import java.io.*;

public class Valutatore {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;

  public Valutatore(Lexer l, BufferedReader br) {
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
      if (look.tag != Tag.EOF)
        move();
    } else
      error("Syntax error");
  }

  public void start() {
    if (look.tag == '(' || look.tag == Tag.NUM) {
      int expr_val;
      expr_val = expr();
      match(Tag.EOF);
      System.out.println("Risultato dell'espressione: " + expr_val);
    } else {
      error("Invalid start of expression");
    }
  }

  private int expr() {
    int term_val, exprp_val = 0;
    if (look.tag == '(' || look.tag == Tag.NUM) {
      term_val = term();
      exprp_val = exprp(term_val);
    } else {
      error("Syntax error");
    }
    return exprp_val;
  }

  private int exprp(int exprp_i) {
    int term_val, exprp_val = 0;
    switch (look.tag) {
      case '+':
        match('+');
        term_val = term();
        exprp_val = exprp(exprp_i + term_val);
        break;

      case '-':
        match('-');
        term_val = term();
        exprp_val = exprp(exprp_i - term_val);
        break;

      case Tag.EOF:
        exprp_val = exprp_i;
      case ')':
        exprp_val = exprp_i;
        break;

      default:
        exprp_val = exprp_i;
        error("Syntax error exprp");
    }
    return exprp_val;
  }

  private int term() {
    int termp_i, term_val = 0;
    if (look.tag == '(' || look.tag == Tag.NUM) {
      termp_i = fact();
      term_val = termp(termp_i);
    } else {
      error("Syntax error");
    }
    return term_val;
  }

  private int termp(int termp_i) {
    int fact_val, termp_val;
    switch (look.tag) {
      case '*':
        match('*');
        fact_val = fact();
        termp_val = termp(termp_i * fact_val);
        break;
      case '/':
        match('/');
        fact_val = fact();
        termp_val = termp(termp_i / fact_val);
        break;
      default:
        termp_val = termp_i;
        break;
    }

    return termp_val;
  }

  private int fact() {
    int fact_val = 0;
    switch (look.tag) {
      case '(':
        match('(');
        fact_val = expr();
        if (look.tag == ')')
          match(')');
        else
          error("Errore sintattico: fact()");
        break;
      case Tag.NUM:
        NumberTok value = (NumberTok) look;
        fact_val = value.lexeme;
        move();

      default:
        break;
    }

    return fact_val;
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "C:/Users/dedeg/Documents/LFT/AndreaGarnerone/ProvaGenerale.txt";
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Valutatore valutatore = new Valutatore(lex, br);
      valutatore.start();
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}