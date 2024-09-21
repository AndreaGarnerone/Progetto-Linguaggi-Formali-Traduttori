/* Un DFA che riconosca il linguaggio di stringhe che
contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome
con un altro qualsiasi. */

public class es7 {
    
    static boolean scan(String s) {
        int i = 0; int state = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == 'A' || ch == 'a') {
                        state = 1;
                    } else if (ch != 'A' || ch != 'a') {
                        state = 7;
                    } else {
                        state = -1;
                    }
                    break;

                case 1:
                    if (ch == 'n') {
                        state = 2;
                    } else if (ch != 'n') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;

                case 2:
                    if (ch == 'd') {
                        state = 3;
                    } else if (ch != 'd') {
                        state = 9;
                    } else {
                        state = -1;
                    }
                    break;

                case 3:
                    if (ch == 'r') {
                        state = 4;
                    } else if (ch != 'r') {
                        state = 10;
                    } else {
                        state = -1;
                    }
                    break;

                case 4:
                    if (ch == 'e') {
                        state = 5;
                    } else if (ch != 'e') {
                        state = 11;
                    } else {
                        state = -1;
                    }
                    break;

                case 5:
                    if (ch == 'a') {
                        state = 5;
                    } else if (ch != 'a') {
                        state = 11;
                    } else {
                        state = -1;
                    }
                    break;

                case 7:
                    if (ch == 'n') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;

                case 8:
                    if (ch == 'd') {
                        state = 9;
                    } else {
                        state = -1;
                    }
                    break;

                case 9:
                    if (ch == 'r') {
                        state = 10;
                    } else {
                        state = -1;
                    }
                    break;

                case 10:
                    if (ch == 'e') {
                        state = 11;
                    } else {
                        state = -1;
                    }
                    break;

                case 11:
                    if (ch == 'a') {
                        state = 11;
                    } else {
                        state = -1;
                    }
                    break;


            }
        }

        return (state == 5 || state == 11);
    }
    
    public static void main(String[] args) {
        System.out.println(scan(args[0])? "yeah" : "noooou");
    }
}
