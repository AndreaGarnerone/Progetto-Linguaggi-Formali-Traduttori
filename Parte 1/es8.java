/* Due segmenti, il secondo dei quali e opzionale:
il primo segmento e una sequenza di cifre numeriche che:
    (1) puo' essere preceduta da un segno + o meno -
    (2) puo essere seguita da un segno punto ., che a sua volta deve essere seguito da
    una sequenza non vuota di cifre numeriche.
Il secondo segmento inizia con il simbolo e, che a sua volta e seguito da una sequenza di 
cifre numeriche che soddisfa i punti (1) e (2) scritti per il primo segmento.
Si nota che, sia nel primo segmento, sia in un eventuale secondo segmento, un segno punto . non
deve essere preceduto per forza da una cifra numerica */

public class es8 {
    
    static boolean scan (String s) {
        int state = 0; int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch >= '0' && ch <= '9') {
                        state = 1;
                    } else if (ch == '.' || ch == '+' || ch == '-') {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;

                case 1:
                    if (ch == '.') {
                        state = 2;
                    } else if (ch >= '0' && ch <= '9') {
                        state = 1;
                    } else if (ch == 'e') {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;

                case 2:
                    if (ch >= '0' && ch <= '9') {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;

                case 3:
                    if (ch >= '0' && ch <= '9') {
                        state = 3;
                    } else if (ch == 'e') {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;

                case 4:
                    if (ch >= '0' && ch <= '9') {
                        state = 5;
                    } else if (ch == '.') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;

                case 5:
                    if (ch >= '0' && ch <= '9') {
                        state = 5;
                    } else if (ch == 'e') {
                        state = 6;
                    } else if (ch == '.') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;

                case 6:
                    if (ch >= '0' && ch <= '9') {
                        state = 7;
                    } else if (ch == '.' || ch == '+' || ch == '-') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;

                case 7:
                    if (ch >= '0' && ch <= '9') {
                        state = 7;
                    } else if (ch == '.') {
                        state = 9;
                    } else if (ch == '+' || ch == '-') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;

                case 8:
                    if (ch >= '0' && ch <= '9') {
                        state = 7;
                    } else {
                        state = -1;
                    }
                    break;

                case 9:
                    if (ch >= '0' && ch <= '9') {
                        state = 10;
                    } else {
                        state = -1;
                    }
                    break;

                case 10:
                    if (ch >= '0' && ch <= '9') {
                        state = 10;
                    } else {
                        state = -1;
                    }
                    break;

            }
        }

            return (state == 1 || state == 3 || state == 5 || state == 7 || state == 10 );
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0])? "yeah" : "no");
    }
}
