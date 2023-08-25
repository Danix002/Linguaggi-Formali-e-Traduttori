/*Esercizio 1.4
    Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono il tuo nome e tutte le
    stringhe ottenute dopo la sostituzione di un carattere del nome con un altro qualsiasi.
    Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA:

    Esempio di stringhe accettate:
        Paolo       Dani
        Pjolo       Danj 
        caolo       Mani
        Pa%lo       D%ni
        Paola       .
        Parlo       .

    Esempio di stringhe non accettate:
        Eva
        Peola
        Pietro 
        P*o*o
*/
public class StringheConNome{

    public static boolean scan(String s){

		int state = 0;
		int i = 0; 

		while (state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);

			switch(state){

				case 0: 
                    if (ch == 'D' || ch == 'd')
                        state = 1;
                    else 
                        state = 4;

                    break;

				case 1: 
					if (ch == 'A' || ch == 'a')
                        state = 2;
                    else 
                        state = 5;
                    
                    break;

				case 2: 
                    if (ch == 'N' || ch == 'n')
                        state = 3;
                    else 
                        state = 6;
            
                    break;

				case 3: 
                    state = 3;
                    break;

				case 4: 
                    if (ch == 'A' || ch == 'a')
                        state = 5;

                    else 
                        state = -1;

                    break;

                case 5: 
                    if (ch == 'N' || ch == 'n')
                        state = 6;

                    else 
                        state = -1;
                        
                    break;

                case 6:	
                    if (ch == 'I' || ch == 'i')	
                        state = 6; //state = 7

                    else 
                        state = -1;

                    break;

                /*case 7:
                    state = 7;
                    break;
                */					
			}
		}

		return state == 3 || state == 6; //state == 3 || state == 7
	}

	public static void main(String[] args){

		System.out.println(scan(args[0]) ? "OK" : "NOPE");

	}

}