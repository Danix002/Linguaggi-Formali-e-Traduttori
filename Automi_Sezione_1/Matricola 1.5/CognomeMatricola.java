/* Esercizio 1.5
    Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il linguaggio di stringhe che contengono matricola e cognome 
    degli studenti del corso A che hanno unnumero di matricola pari oppure a studenti del corso B che hanno un numero di matricola dispari, 
    ma in cui il cognome precede il numero di matricola (in altre parole, le posizioni del cognomee matricola sono scambiate rispetto 
    all'Esercizio 1.3). 
*/
public class CognomeMatricola {

    public static boolean scan(String s) {

        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                
            case 0:
                if ( (ch >='A' && ch <='K') || (ch >='a' && ch <='k') )	
                    state = 1;

                else if ( (ch >='L' && ch <='Z') || (ch >='l' && ch <='z') )		
                    state = 2;

                else
                    state = -1;

                break;

            case 1:
                if (Character.isLetter(ch))
                    state = 1;

                else if (Character.isDigit(ch) && ch % 2 == 0)
                    state = 3;

                else if (Character.isDigit(ch) && ch % 2 == 1)
                    state = 5;

                else
                    state = -1;

                break;

            case 2:
                if (Character.isLetter(ch))
                    state = 2;

                else if (Character.isDigit(ch) && ch % 2 == 0)
                    state = 6;

                else if (Character.isDigit(ch) && ch % 2 == 1)
                    state = 4;

                else
                    state = -1;

                break;
            
            case 3:
                if (Character.isDigit(ch) && ch % 2 == 0)
                    state = 3;

                else if (Character.isDigit(ch) && ch % 2 == 1)
                    state = 5;

                else
                    state = -1;

                break;
            
            case 4:
                if (Character.isDigit(ch) && ch % 2 == 0)
                    state = 6;

                else if (Character.isDigit(ch) && ch % 2 == 1)
                    state = 4;

                else
                    state = -1;

                break;
            
            case 5:
                if (Character.isDigit(ch) && ch % 2 == 0)
                    state = 3;

                else if (Character.isDigit(ch) && ch % 2 == 1)
                    state = 5;

                else
                    state = -1;

                break;
            
            case 6:
                if (Character.isDigit(ch) && ch % 2 == 0)
                    state = 6;

                else if (Character.isDigit(ch) && ch % 2 == 1)
                    state = 4;

                else
                    state = -1;

                break;

            }
        }
        return state == 3 || state == 4;
	}

    public static void main(String[] args) {

	    System.out.println(scan(args[0]) ? "OK" : "NOPE");

    }
}
