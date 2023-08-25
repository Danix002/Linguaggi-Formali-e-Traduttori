/*Esercizio 1.8
    Progettare e implementare un DFA che riconosca il linguaggio delle costanti numeriche in virgola mobile 
    utilizzando la notazione scientifica dove il simbolo 'e' indica la funzione esponenziale con base 10. 
    L'alfabeto del DFA contiene i seguenti elementi: 

    - le cifre numeriche 0, 1, . . . , 9
    - il segno . (punto) che precede una eventuale parte decimale
    - i segni + (piu) e ` - (meno)

    Le stringhe accettate devono seguire le solite regole per la scrittura delle costanti numeriche.

    Esempi di stringhe accettate:
        123
        123.5
        .567
        +7.5
        -.7
        67e10
        1e-2
        -.7e2
        1e2.3

    Esempi di stringhe non accettate: 
        .
        e3
        123.
        +e6
        1.2.3
        4e5e6
        ++3
*/
public class VirgolaMobile {

    public static boolean scan(String s) {

        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch == '+' || ch == '-' || Character.isDigit(ch) || ch == '.')
                        state = 1;

                    else 
                        state = -1;

                    break;

                case 1:
                    if(ch == 'e')
                        state = 3;

                    else if(Character.isDigit(ch))
                        state = 2;

                    else if(ch == '.')
                        state = 4;

                    else 
                        state = -1;

                    break;

                case 2:
                    if(Character.isDigit(ch))
                        state = 2;

                    else if(ch == 'e' || ch == '.')
                        state = 5;

                    else
                        state = -1;

                    break;

                case 3:
                    if (ch == '+' || ch == '-')
                        state = 7;

                    else if(Character.isDigit(ch))
                        state = 6;

                    else 
                        state = -1;

                    break;

                case 4:
                    if(ch == '+' || ch == '-' || Character.isDigit(ch) || ch == 'e')
                        state = 7;

                    else 
                        state = -1;

                    break;

                case 5:
                    if(ch == '+' || ch == '-' || Character.isDigit(ch) || ch == 'e' || ch == '.') 
                        state = 7;

                    else 
                        state = -1;

                    break;

                case 6:
                    if(ch == '+' || ch == '-' || Character.isDigit(ch) || ch == '.') 
                        state = 7;

                    else 
                        state = -1;
                        
                    break;
                
                case 7:
                    if(Character.isDigit(ch)) 
                        state = 7;
                    
                    else if(ch == 'e')
                        state = 5;

                    else 
                        state = -1;

                    break;
            }
        }
        return state == 2 || state == 7;
    }

    public static void main(String[] args) {

        //System.out.println(scan(args[0]) ? "OK" : "NOPE");
        System.out.println(scan("123") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("123.5") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan(".567") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("+7.5") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("-.7") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("67e10") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("1e-2") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("-.7e2") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("1e2.3") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan(".") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("e3") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("123.") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("+e6") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("1.2.3") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("4e5e6") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("++3") ? "OK" : "NOPE"); //atteso nope
    }
}