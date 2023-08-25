import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
    	
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; 
        }
        
    }

    public Token lexical_scan(BufferedReader br) {
    	
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
            
        }
        
		switch (peek) {
		
			case ',':
				
				peek = ' ';
				return Token.comma;
				
			case '!':
				
				peek = ' ';
				return Token.not;
	
			case '(':
				
				peek = ' ';
				return Token.lpt;
	
			case ')':
				
				peek = ' ';
				return Token.rpt;
	
			case '{':
				
				peek = ' ';
				return Token.lpg;
	
			case '}':
				
				peek = ' ';
				return Token.rpg;
	
			case '+':
				
				peek = ' ';
				return Token.plus;
	
			case '-':
				
				peek = ' ';
				return Token.minus;
	
			case '*':
				
				peek = ' ';
				return Token.mult;
	
			case ';':
				
				peek = ' ';
				return Token.semicolon;
				
				 /* Estendere il metodo lexical_scan in modo tale che possa trattare la presenza
					di commenti nel file di input. I commenti possono essere scritti in due modi:
					- commenti delimitati con "/* e *(/);
					- commenti che iniziano con // e che terminano con un a capo oppure con EOF.
					- I commenti devono essere ignorati dal programma per l’analisi lessicale; */
				
			case '/':
				
				readch(br);
				
				switch (peek) {
					case '/': 						// e' un commento finche' non si va a capo
						
						while (!(peek == '\n' || peek == '\r' || peek == (char) -1))
							readch(br);
						return lexical_scan(br); 	// ricomincia a produrre token quando il commento e' finito
						
					case '*':
						
						boolean commentClosed = false;
						do {
							if (peek == '*') {
								readch(br);
								if (peek == '/')
									commentClosed = true;
							} else
								readch(br);
							if (peek == (char) -1) 	// se EOF concludo
								return new Token(Tag.EOF);
						} while (!commentClosed);
	
						peek = ' ';
						return lexical_scan(br);
						
					default: 						// simbolo di divisione
						return Token.div;
						
				}

			case '&':
				
				readch(br);
				
				if (peek == '&') {
					peek = ' ';
					return Word.and;
				} else {
					System.err.println("Erroneous character" + " after &: " + peek);
					return null;
				}
	
			case '|':
				
				readch(br);
				
				if (peek == '|') {
					peek = ' ';
					return Word.or;
				} else { 
					System.err.println("Erroneous character" + " after |: " + peek);
					return null;
				}
	
			case '<': 
				
				readch(br);
				
				if (peek == '=') {
					peek = ' ';
					return Word.le;
				} else if (peek == '>') {
					peek = ' ';
					return Word.ne;
				} else {
					return Word.lt;
				}
	
			case '>': 
				
				readch(br);
				
				if (peek == '=') {
					peek = ' ';
					return Word.ge;
				} else {
					return Word.gt;
				}
	
			case '=':
				
				readch(br);
				
				if (peek == '=') {
					peek = ' ';
					return Word.eq;
				} else {
					return Word.assign;
				}
	
			case (char) -1:
					return new Token(Tag.EOF);
	
			default:
		
				if (Character.isLetter(peek) || Character.valueOf(peek) == 95) { 
					
					StringBuffer buffer = new StringBuffer();
					boolean isValid = false;
					do {
						if (Character.valueOf(peek) != 95)
							isValid = true;
							buffer.append(peek);
							readch(br);
					} while (Character.isLetterOrDigit(peek) || Character.valueOf(peek) == 95);
	
					String id = buffer.toString();
	
					switch (id) {
						case "assign":
							return Word.assign;
							
						case "if":
							return Word.iftok;
							
						case "to":
							return Word.to;
							
						case "begin":
							return Word.begin;
							
						case "end":
							return Word.end;
							
						case "else":
							return Word.elsetok;
							
						case "while":
							return Word.whiletok;
							
						case "print":
							return Word.print;
							
						case "read":
							return Word.read;
							
						default: 
							if (isValid)
								return new Word(Tag.ID, id);
							else {
								System.err.println("Err: an ID must consist of other alphanumeric symbols after _");
								return null;
							}	
					}	
				} else if (Character.isDigit(peek)) {
					
					int num = 0;	
					do {
						num = num * 10 + Character.digit(peek, 10); 
						readch(br);
					} while (Character.isDigit(peek));	
						return new NumberTok(Tag.NUM, num);
				} else {
					System.err.println("Erroneous character: " + peek);
					return null;
				}
				
		}
		
    }
    
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "2.3/InputEs1.java";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }
    
}
