import java.io.*; 

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        while (peek == '/') {
            readch(br);
            if (peek == '/') {
                while (peek != (char)-1 && peek != 10) {
                    readch(br);
                }
            } else if (peek == '*') {
                int state = 0;
                while(peek != (char) -1 && state != 2) {
                    readch(br);
                    switch (state) {
                        case 0:
                            if (peek == '*') {
                                state = 1;
                            } else {
                                state = 0;
                            }
                            break;
                        
                        case 1:
                            if (peek == '/') {
                                state = 2;
                            } else if (peek == '*') {
                                state = 1;
                            } else {
                                state = 0;
                            }
                            break;
                    }
                }
                if (state != 2) {
                    System.err.println("Comment /* */ without an end");
                    return null;
                }
                readch(br);
            } else {
                return Token.div;
            }
            while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
                if (peek == '\n') line++;
                readch(br);
            }
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;
	
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character after | : " + peek);
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character after = : " + peek);
                    return null;
                }
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {
                    String id = "";
                    do {
                        id += peek;
                        readch(br);
                    } while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');
                    switch (id) {
                        case "assign":
                            return Word.assign;
                        case "to":
                            return Word.to;
                        case "conditional":
                            return Word.conditional;
                        case "option":
                            return Word.option;
                        case "do":
                            return Word.dotok;
                        case "else":
                            return Word.elsetok;
                        case "while":
                            return Word.whiletok;
                        case "begin":
                            return Word.begin;
                        case "end":
                            return Word.end;
                        case "print":
                            return Word.print;
                        case "read":
                                return Word.read;
                        default:
                            if (id.charAt(0) == '_') {
                                int state = 0, i = 0;
                                while (i < id.length() && state != 1) {
                                    final char c = id.charAt(i++);
                                    switch (state) {
                                        case 0:
                                            if (c != '_') {
                                                state = 1;
                                            } else {
                                                state = 0;
                                            }
                                            break;
                                        case 1:
                                            break;
                                    }
                                }
                                if (state == 1) {
                                    return new Word (Tag.ID, id);
                                } else {
                                    System.err.println("Erroneous identifier: " + id);
                                    return null;
                                }
                            } else {
                                return new Word (Tag.ID, id);
                            }
                    }
                } else if (Character.isDigit(peek)) {
                    int n = 0;
                    do {
                        n = n * 10 + (peek - '0');
                        readch(br);
                    }
                    while (Character.isDigit(peek));
                    return new NumberTok(Tag.NUM, n);
                } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/Users/dedeg/Documents/LFT/AndreaGarnerone/ProvaGenerale.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}