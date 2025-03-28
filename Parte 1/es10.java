// Modificare l’automa dell’esercizio precedente in modo che riconosca il linguaggio di
// stringhe (sull’alfabeto {/, *, a}) che contengono “commenti” delimitati da /* e */, ma con
// la possibilita di avere stringhe prima e dopo come specificato qui di seguito. */

public class es10 {    
    static boolean scan (String s) {
        int state = 0; int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
    
            switch (state) {
                case 0:
                    if (ch == '/') {
                        state = 1;
                    } else if (ch == 'a') {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
    
                case 1:
                    if (ch == '*') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
    
                case 2:
                    if (ch == 'a' || ch == '/') {
                        state = 2;
                    } else if (ch == '*') {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
    
                case 3:
                    if (ch == 'a') {
                        state = 2;
                    } else if (ch == '*') {
                        state = 3;
                    } else if (ch == '/') {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;
    
                case 4:
                    if (ch == 'a') {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;

                case 5:
                    if (ch == 'a') {
                        state = 5;
                    } else if (ch == '/') {
                        state = 1;
                    } else {
                        state = -1;
                    }
                    break;
    
                }
            }
    
        return (state == 4 || state == 5);
    }
    
    public static void main(String[] args) {
        System.out.println(scan(args[0])? "Yeah!" : "noooou");
    }
}
