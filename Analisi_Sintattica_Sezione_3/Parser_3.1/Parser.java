
/* Obiettivo dell’esercizio 3.1: scrivere un programma per l’analisi sintattica perespressioni aritmetiche semplici,
 * scritte in notazione infissa.
 *
 * La grammatica che riconosce questo tipo di espressioni è:
 * 
 * <start> 	::= <expr>EOF
 * <expr> 	::= <term><exprp>
 * <exprpr> ::= +<term><exprp>
 *            | -<term><exprp>
 *            | eps
 * <term> 	::= <fact><termp>
 * <termp> 	::= *<fact><termp>
 * 			  | /<fact><termp>
 *            | eps
 *
 * <fact> ::= (<expr>) | NUM
 *
 */

import java.io.*;

public class Parser {
	
	private Lexer lex; 						
	private BufferedReader pbr;
	private Token look;

	public Parser(Lexer l, BufferedReader br) {
		lex = l;
		pbr = br;
		move();
	}

	void move() {
		
		look = lex.lexical_scan(pbr);		
		System.out.println("token = " + look);
		
	}

	void error(String s) {
		
		throw new Error("near line " + lex.line + ": " + s);
		
	}

	void match(int t) {
		
		if (look.tag == t) {
			
			if (look.tag != Tag.EOF)
				move(); 					
		
		} else
			error("syntax error"); 
		
	}
   
	public void start() { 					// S -> E
		
		switch (look.tag) {
			case '(': 						// GUIDA(S -> E) = {(, num}
			
			case Tag.NUM: 					
				
				expr();
				match(Tag.EOF); 			
				break;

			default:
				error("Error in start");
				break;

		}
		
	}

	private void expr() { 					// E -> TE'
		
		switch (look.tag) {
			
			case '(': 						// GUIDA(E -> TE') = {(, num}

			case Tag.NUM: 					
				
				term(); 					// T -> FT'
				exprp(); 					// E' -> +TE' | -TE' | eps
				break;

			default:
				error("Error in expr");
				break;
		}
		
	}

	private void exprp() { 					// E' -> +TE' | -TE' | eps
		
		switch (look.tag) {
			
			case '+': 						// GUIDA(E' -> +TE') = {+}
				
				match(Token.plus.tag);
				term(); 					
				exprp(); 					
				break;

			case '-': 						// GUIDA(E' -> -TE') = {-}
				
				match(Token.minus.tag);
				term();
				exprp();
				break;

			case ')': 						// GUIDA(E' -> eps) = {EOF, )}
			
			case Tag.EOF: 					
				break;

			default:
				error("Error in exprp");
				break;
		}
	}

	private void term() { 					// T -> FT'
		
		switch (look.tag) {
			
			case '(': 						// GUIDA(T -> FT') = {(, num)}
			
			case Tag.NUM: 					
				
				fact();
				termp();
				break;

			default:
				error("Error in term");
				break;

		}
	}

	private void termp() { 					// T' -> *FT' | /FT' | eps
		
		switch (look.tag) {
			
			case '*': 						// GUIDA(T' -> *FT') = {*}
				
				match(Token.mult.tag);
				fact();
				termp();
				break;

			case '/': 						// GUIDA(T' -> /FT') = {/}
				
				match(Token.div.tag);
				fact();
				termp();
				break;

			case '+': 						// GUIDA(T' -> eps) ) {$, +, -, )}
			
			case '-': 						
			
			case ')': 						
			
			case Tag.EOF: 					
				break;

			default:
				error("Error in termp");
				break;

		}
	}

	private void fact() { 					// F -> (E) | num
		
		switch (look.tag) {
			
			case '(': 						// GUIDA(F -> (E)) = {(}
				
				match(Token.lpt.tag);
				expr();
				match(Token.rpt.tag);
				break;

			case Tag.NUM: 					// GUIDA(F -> num) = {num}
				
				match(Tag.NUM);
				break;

			default:
				error("Error in fact");
				break;
		}
	}

	public static void main(String[] args) {
		
		Lexer lex = new Lexer();
		String path = "Test/Test_1.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser parser = new Parser(lex, br);
			parser.start();
			System.out.println("Input OK");
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
