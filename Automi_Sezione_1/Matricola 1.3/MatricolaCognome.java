/* Esercizio 1.3
    Implementazione di un DFA (minimo) che riconosce stringhe del linguaggio
    L = {stringhe formate da matricola seguita (subito) da un cognome | A-K con matricola pari oppure L-Z con
    matricola dispari, almeno una cifra e almeno una lettera}
  
    Esempio di stringhe accettate:
        123456Bianchi
        654321Rossi
        2Bianchi
        122B
    
    Esempio di stringhe non accettate:
        654321Bianchi
        123456Rossi
        654322
        Rossi
 */

public class MatricolaCognome {
	
	public static boolean scan(String s){

		int state = 0;
		int i = 0; 

		while (state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);

			switch(state){

				case 0: 
					if (48 <= ch && ch <= 57) { 
						if (Character.getNumericValue(ch) % 2 == 0) 
							state = 2;

						else if (Character.getNumericValue(ch) % 2 != 0) 
							state = 1;
					}

					else if (65 <= ch && ch <= 90 || 97 <= ch && ch <= 122) 
						state = 4;

					else 
						state = -1;

					break;

				case 1: 
					if (48 <= ch && ch <= 57){ 
						if(Character.getNumericValue(ch) % 2 == 0) 
							state = 2;

						else if (Character.getNumericValue(ch) % 2 != 0) 
							state = 1;
					}

					else if (76 <= ch && ch <= 90 || 108 <= ch && ch <= 122) 
						state = 3;

					else if (65 <= ch && ch <= 75 || 97 <= ch && ch <= 107) 
						state = 4;

					else 
						state = -1;
					break;

				case 2: 
					if (48 <= ch && ch <= 57) { 
						if (Character.getNumericValue(ch) % 2 == 0) 
							state = 2;

						else if (Character.getNumericValue(ch) % 2 != 0)
							state = 1;                           
					}

					else if (65 <= ch && ch <= 75 || 97 <= ch && ch <= 107) 
						state = 3;

					else if (76 <= ch && ch <= 90 || 108 <= ch && ch <= 122) 
						state = 4;

					else 
						state = -1;
					break;

				case 3:
					if (65 <= ch && ch <= 90 || 97 <= ch && ch <= 122) 
						state = 3;

					else if (48 <= ch && ch <= 57) 
						state = 4;

					else
						state = -1;
					break;

				case 4:
					if (48 <= ch && ch <= 57 || 65 <= ch && ch <= 90 || 97 <= ch && ch <= 122) 
						state = 4;

					else
						state = -1;
					break;
			}
		}

		return state == 3; 
	}

	public static void main(String[] args){

		System.out.println(scan(args[0]) ? "OK" : "NOPE");

	}

}