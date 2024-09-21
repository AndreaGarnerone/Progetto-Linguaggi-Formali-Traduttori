/*Sequenza non vuota di lettere, numeri, ed il "simbolo di “underscore” _ che non comincia
con un numero e che non puo essere composto solo dal simbolo _.
Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x_1”, “lft_lab”, “_temp”, “x_1_y_2”,
“x_”, “_5” */

public class es2 {
    
    public static boolean scan (String s) {
        int i = 0; int state = 0;

        while (state >= 0 && i<s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {

                case 0:
                    if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z')
                        state = 1;
                    else if (ch == '_')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z'
                                        || ch == '_' || ch >= 49 && ch <= 57)
                        state = 1;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == '_')
                        state = 2;
                    else if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z'
                                                        || ch >= '0' && ch <= '9')
                            state = 1;
                    else
                        state = -1;
                    break;
            }

        }

        return state == 1;
    }


    public static void main(String[] args) {
        if (scan(args[0])) {
            System.out.println("Stringa riconoscuta");
        } else {
            System.out.println("Stringa non riconoscuta");
        }
    }
}
