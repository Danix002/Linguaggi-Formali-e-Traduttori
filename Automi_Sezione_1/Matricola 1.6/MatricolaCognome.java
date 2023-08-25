/*Esercizio 1.6
    Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono un numero di matricola seguito (subito) 
    da un cognome, dove la combinazione di matricola e cognome corrisponde a studenti del turno T2 o del turno T3 del laboratorio di 
    Linguaggi Formali e Traduttori. Si ricorda le regole per suddivisione di studenti in turni:

        - Turno T1: cognomi la cui iniziale e compresa tra A e K, e la penultima cifra del numero di 
        matricola e dispari; 
        - Turno T2: cognomi la cui iniziale e compresa tra A e K, e la penultima cifra del numero di 
        matricola e pari; 
        - Turno T3: cognomi la cui iniziale e compresa tra L e Z, e la penultima cifra del numero di 
        matricola e dispari; 
        - Turno T4: cognomi la cui iniziale e compresa tra L e Z, e la penultima cifra del numero di 
        matricola e pari.

    Esempio di stringhe accettate:
        654321Bianchi
        123456Rossi 
        221B

    Esempio di stringhe non accettate:
        123456Bianchi 
        654321Rossi
        5
        654322
        Rossi
        2Bianchi
*/ 
public class MatricolaCognome {

    public static boolean scan(String s) {

        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 1;

                        else
                            state = 2;

                    }else
                        state = -1;

                    break;

                case 1:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 3;

                        else
                            state = 4;
                        
                    }else
                        state = -1;
                    
                    break;

                case 2:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 5;

                        else
                            state = 6;
                        
                    }else
                        state = -1;
                    
                    break;

                case 3:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 5;

                        else
                            state = 6;
                        
                    }else{
                        if(ch>= 'A' && ch <= 'K')
                            state = 7;

                        else{
                            if(ch>= 'L' && ch <= 'Z'){
                                state = 9;

                            }else{
                                state = -1;

                            }
                        }
                    }
                    break;

                case 4:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 3;

                        else
                            state = 4;
                        
                    }else{
                        if(ch>= 'A' && ch <= 'K')
                            state = 7;

                        else{
                            if(ch>= 'L' && ch <= 'Z')
                                state = 9;

                            else                          
                                state = -1;                           
                        }
                    }
                    break;

                case 5:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 5;

                        else
                            state = 6;
                        
                    }else{
                        if(ch>= 'A' && ch <= 'K')
                            state = 8;

                        else{
                            if(ch>= 'L' && ch <= 'Z'){
                                state = 10;

                            }else
                                state = -1;                           
                        }
                    }
                    break;

                case 6:
                    if(ch >= '0' && ch <= '9'){
                        if(ch % 2 == 0)
                            state = 3;

                        else
                            state = 4;
                        
                    }else{
                        if(ch>= 'A' && ch <= 'K')
                            state = 8;

                        else{
                            if(ch>= 'L' && ch <= 'Z')
                                state = 10;

                            else
                                state = -1;
                        }
                    }
                    
                    break;

                case 7:
                    if(ch >= 'a' && ch <= 'z')
                        state = 7;

                    else
                        state = -1;
                    
                    break;

                case 8:
                    if(ch >= 'a' && ch <= 'z')
                        state = 8;

                    else                       
                        state = -1;
                    
                    break;

                case 9:
                    if(ch >= 'a' && ch <= 'z')
                        state = 9;

                    else                        
                        state = -1;
                    
                    break;

                case 10:
                    if(ch >= 'a' && ch <= 'z')                       
                        state = 10;

                    else
                        state = -1;
                    
                    break;
            }
        }
        return state == 8 || state == 9;
    }
    public static void main(String[] args) {
        //System.out.println(scan(args[0]) ? "OK" : "NOPE");
        System.out.println(scan("654321Bianchi") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("123456Rossi") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("221B") ? "OK" : "NOPE"); //atteso ok

        System.out.println(scan("123456Bianchi") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("654321Rossi") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("5") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("654322") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("Rossi") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("2Bianchi") ? "OK" : "NOPE"); //atteso nope        
    }
}