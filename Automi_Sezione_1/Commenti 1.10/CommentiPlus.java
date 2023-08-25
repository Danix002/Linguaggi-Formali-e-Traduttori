/* Esercizio 1.10
    Modificare l'automa dell'esercizio precedente in modo che riconosca il linguaggio di stringhe 
    (sull'alfabeto {/, *, a}) che contengono "commenti" delimitati da / * e * /, ma con la possibilita' 
    di avere stringhe prima e dopo come specificato qui di seguito. L'idea e' che sia possibile 
    avere eventualmente commenti (anche multipli) immersi in una sequenza di simboli dell'alfabeto. 
    Quindi l'unico vincolo e' che l'automa deve accettare le stringhe in cui un'occorrenza della sequenza 
    / * deve essere seguita (anche non immediatamente) da un'occorrenza della sequenza * /. Le stringhe del 
    linguaggio possono non avere nessuna occorrenza della sequenza / * (caso della sequenza di simboli senza commenti). 

    Esempi di stringhe accettate: 
        a a a / * * * / a a
        a a / * a * a * /
        a a a a
        / * * * * /
        / * a a * /
        * / a
        a / * * / * * * a 
        a / * * / * * * / a
        a / * * / a a / * * * / a 
        
    Esempi di stringhe non accettate: 
        a a a / * / a a 
        a / * * / / * * * a
        a a / * a a 
*/
public class CommentiPlus{

    public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0: 
                    if(ch == '/') 
                        state = 1;

                    else if(ch == 'a' | ch == '*')
                        state = 0;

                    else
                        state = -1;

                    break;

                case 1: 
                    if(ch == '/')
                        state = 1;

                    else if(ch == 'a')
                        state = 0;

                    else if(ch == '*')
                        state = 2;

                    else
                        state = -1;

                    break;

                case 2: 
                    if(ch == '/' || ch == 'a')
                        state = 2;

                    else if(ch == '*')
                        state = 3;

                    else
                        state = -1;

                    break;

                case 3: 
                    if(ch == '*')
                        state = 3;

                    else if(ch == '/')
                        state = 0;

                    else if(ch == 'a')
                        state = 2;

                    else
                        state = -1;

                    break;
            }
        }

        return (state == 0 || state == 1);
    }

    public static void main(String[] args) {
        //System.out.println(scan(args[0]) ? "OK" : "NOPE");
        System.out.println(scan("aaa/***/aa") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("aa/*a*a*/") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("aaaa") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("a/**/aa/***/a") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("/****/") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("/*aa*/") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("*/a") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("a/**/***a") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("a/**/***/a") ? "OK" : "NOPE"); //atteso ok
        System.out.println(scan("aaa/*/aa") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("a/**//***a") ? "OK" : "NOPE"); //atteso nope
        System.out.println(scan("aa/*aa") ? "OK" : "NOPE"); //atteso nope
    }
}