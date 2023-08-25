/* Esercizio 1.2
	Progettare e implementare un DFA che riconosca il linguaggio degli identificatori in stile Java, le cui
	caratteristiche sono:
	- sono sequenze non vuote di lettere, numeri e _
	- non possono cominciare con un numero
	- non possono essere composti solo da _

	Esempi di stringhe accettate: 
		x
		flag1
		x2y2
		x_1
		lft_lab
		_temp
		x_1 y_2
		x___
		__5
        
	Esempi di stringhe non accettate: 
		5
		221B
		123
		9_to_5
		___
*/

public class Identificatori{
	// L'automa viene implementato in un metodo scan che prende come parametro una stringa s e restituisce un
	// booleano a seconda che la stringa di input appartenga o meno al linguaggio riconosciuto dal DFA (o -1 se legge
	// un simbolo non contenuto nell'alfabeto del DFA)
	public static boolean scan(String s){

		int state = 0;
		int i = 0; 
		
		while (state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);

			switch(state){

				case 0: 
					if (65 <= ch && ch <= 90 || 97 <= ch && ch <= 122) 
						state = 1;

					else if (ch == ' ' || ch == '_')
						state = 2;

					else if (48 <= ch && ch <= 57) 
						state = 3;

					else
						state = -1;

					break;

				case 1: 
					if (65 <= ch && ch <= 90 || 97 <= ch && ch <= 122 || 48 <= ch && ch <= 57 || ch == '_' || ch == ' ') 
						state = 1;

					else 
						state = -1;

					break;

				case 2: 
					if (65 <= ch && ch <= 90 || 97 <= ch && ch <= 122 || 48 <= ch && ch <= 57) 
						state = 1;

					else if (ch == ' ' || ch == '_')
						state = 2; 

					else
						state = -1;

					break;

				case 3: 
					if (65 <= ch && ch <= 90 || 97 <= ch && ch <= 122 || 48 <= ch && ch <= 57 || ch == '_' || ch == ' ') 
						state = 3;

					else
						state = -1;

					break;
			}
		}
		return state == 1; 
	}

	public static void main(String[] args){
        
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}