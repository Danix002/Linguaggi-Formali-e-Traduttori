/* Esercizio 1.4
    Implementazione di un DFA che riconosce stringhe del linguaggio
    L = {stringhe formate da matricola seguita da un cognome, eventualmente separati da spazi, che possono essere
    precedute e/o seguite da sequenze eventualmente vuote di spazi | A-K con matricola pari oppure L-Z con matricola
    dispari, almeno una cifra e almeno una lettera}

    Esempio di stringhe accettate:
        123456 Bianchi 
        654321 Rossi
        123456De Gasperi
        2Bianchi
        122B
    
    Esempio di stringhe non accettate:
        1234 56Bianchi
        123456Bia nchi
        654321Bianchi
        123456Rossi
        654322
        Rossi
 
 */
import java.util.Scanner;
public class MatricolaSpazioCognome {
	
	public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0: 
                    if(Character.isDigit(ch)){
                        if( ( ch % 2 ) == 0)
                            state = 2;

                        else if( ( ch % 2 ) != 0)
                            state = 1;
                    }

                    else if(ch == ' ')  
                        state = 0;

                    else
                        state = -1;
                    break;

                case 1: 
                    if(Character.isDigit(ch)){
                        if( ( ch % 2 ) == 0)
                            state = 2;

                        else if( ( ch % 2 ) != 0)
                            state = 1;
                    }

                    else if(ch >= 'L' && ch <= 'Z')
                        state = 4;

                    else if(ch == ' ')
                        state = 3;

                    else
                        state = -1;

                    break;

                case 2:
                    if(Character.isDigit(ch)){
                        if( ( ch % 2 ) == 0)
                            state = 2;

                        else if( ( ch % 2 ) != 0)
                            state = 1;

                    }

                    else if(ch >= 'A' && ch <= 'K')
                        state = 4;

                    else if(ch == ' ')
                        state = 5;

                    else
                        state = -1;

                    break;

                case 3:
                    if(ch >= 'L' && ch <= 'Z')
                        state = 4;

                    else if(ch == ' ')
                        state = 3;

                    else
                        state = -1;
                    break;

                case 4:
                    if(ch >= 'a' && ch <= 'z')
                        state = 4;

                    else if(ch == ' ')
                        state = 6;

                    else
                        state = -1;

                    break;
                case 5:
                    if(ch >= 'A' && ch <= 'K')
                        state = 4;

                    else if(ch == ' ')
                        state = 5;

                    else
                        state = -1;
                    break;

                case 6:
                    if(ch == ' ')
                        state = 6;
                        
                    else if(ch >= 'A' && ch <= 'Z') state = 4; //==> cognomi composti

                    else
                        state = -1;
                    break;
            }
        }

        return (state == 4 || state == 6);
    }

	public static void main(String[] args) {
        String myInput;
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserisci la stringa: ");
        myInput = sc.nextLine();
        System.out.println(scan(myInput) ? "OK" : "NOPE");
        sc.close();
    }
	/*public static void main(String[] args){

		System.out.println(scan(args[0]) ? "OK" : "NOPE");

	}*/

}