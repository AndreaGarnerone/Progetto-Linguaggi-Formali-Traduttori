/*Riconosce matricole dei turni T2 e T3 anche con degli spazi (all'inizio, alla fine,
tra matricola e cognome, tra articolo del cognome e il resto). */

public class es4 {      // PROBLEMA!!!: Se legge dal main ok, se da cmd no. DA RISOLVERE

    public static boolean scan (String s) {
        int state = 0; int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8') {
                    state = 1;
                } else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9') {
                    state = 2;
                } else if (ch == 32) {
                    state = 0;
                } else {
                    state = -1;
                }
                break;

                case 1:
                if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8') {
                    state = 1;
                } else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9') {
                    state = 2;
                } else if (ch >= 'A' && ch <= 'K' || ch >= 'a' && ch <= 'k' || ch == 32) {
                    state = 3;
                } else {
                    state = -1;
                }
                break;

                case 2:
                if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9') {
                    state = 2;
                } else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8') {
                    state = 1;
                } else if (ch >= 'L' && ch <= 'Z' || ch >= 'l' && ch <= 'z' || ch == 32) {
                    state = 4;
                } else {
                    state = -1;
                }
                break;

                case 3:
                if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'K') {
                    state = 3;
                } else if (ch == 32) {
                    state = 5;
                } else {
                    state = -1;
                }
                break;

                case 4:
                if (ch >= 'a' && ch <= 'z' || ch >= 'L' && ch <= 'Z') {
                    state = 4;
                } else if (ch == 32) {
                    state = 6;
                } else {
                    state = -1;
                }
                break;

                case 5:
                if (ch >= 'A' && ch <= 'Z') {
                    state = 3;
                } else if (ch == 32) {
                    state = 5;
                } else {
                    state = -1;
                }
                break;

                case 6:
                if (ch >= 'A' && ch <= 'K') {
                    state = 4;
                } else if (ch == 32) {
                    state = 6;
                } else {
                    state = -1;
                }
                break;
            }
        }
        return (state == 3 || state == 4);
    }


    public static void main(String[] args) {
        System.out.println(scan(args[0])? "ok" : "nope");
    }
}
