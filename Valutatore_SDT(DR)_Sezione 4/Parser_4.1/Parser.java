
/* <stat>  ::= <expr>EOF { print(expr.val) }
 * <expr>  ::= <term> { exprp.i = term.val } <exprp> {expr.val = exprp.val }
 * <exprp> ::= +<term> { exprp1.i = exprp.i + term.val } <exprp1> {exprp.val = exprp1.val }
 * 			|  -<term> { exprp1.i = exprp.i - term.val } <exprp1> {exprp.val = exprp1.val }
 * 			| eps { exprp.val = exprp.i } 
 * <term>  ::= <fact> { temrmp.i = fact.val } <termp> { term.val = termp.val }
 * <termp> ::= *<fact> { termp1.i = termp.i * fact.val } <termp1> { termp.val = termp1.val }
 *			|  /<fact> { termp1.i = termp.i / fact.val } <termp1> { termp.val = termp1.val }
 * 			| eps { termp.val = termp.i } 
 * <fact> ::= (<expr>) { fact.val = expr.val }
 * 			| NUM { fact.val = NUM.value}
 * */

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

	public void start() { 				// <stat> ::= <expr>EOF { print(expr.val) }
		
		int expr_val;

		switch (look.tag) {
			
			case '(': 					// GUIDA(S -> E) = {(, num}
			
			case Tag.NUM:
				
				expr_val = expr();
				match(Tag.EOF);
				System.out.println(expr_val);
				break;

			default:
				error("Error in start");
		}
		
	}

	private int expr() { 				// <expr> ::= <term> { exprp.i = term.val } <exprp> { expr.val = exprp.val }
		
		int term_val = 0, exprp_val = 0;

		switch (look.tag) {
			
			case '(': 					// GUIDA(E -> TE') = {(, num}
			
			case Tag.NUM: 				
				
				term_val = term();
				exprp_val = exprp(term_val);
				break;

			default:
				error("Error in expr");
		}
		
		return exprp_val;
		
	}

	private int exprp(int exprp_i) { 	// <exprp> ::= +<term> { exprp1.i = exprp.i + term.val } <exprp1> {exprp.val =
										// exprp1.val} | -<term> { exprp1.i = exprp.i - term.val } <exprp1> {exprp.val
									 	// = exprp1.val} | eps { exprp.val = exprp.i }
		int term_val = 0, exprp_val = 0;

		switch (look.tag) {
			
			case '+': 				 	// GUIDA(E' -> +TE') = {+}
				
				match('+');
				term_val = term();
				exprp_val = exprp(exprp_i + term_val);
				break;

			case '-': 				 	// GUIDA(E' -> -TE') = {-}
				
				match('-');
				term_val = term();
				exprp_val = exprp(exprp_i - term_val);
				break;

			case ')': 				 	// GUIDA(E' -> eps) = {$, )}

			case Tag.EOF: 			 	// GUIDA(E' -> eps) = {$, )}
				
				exprp_val = exprp_i;
				break;

			default:
				error("Error in exprp");
		}

		return exprp_val;
		
	}

	private int term() { 			// <term> ::= <fact> { temrmp.i = fact.val } <termp> { term.val = termp.val }
		
		int fact_val = 0, termp_val = 0;

		switch (look.tag) {
			
			case '(': 				// GUIDA(T -> FT') = {(, num)}

			case Tag.NUM: 			// GUIDA(T -> FT') = {(, num)}
				
				fact_val = fact();
				termp_val = termp(fact_val);
				break;

			default:
				error("Error in term");
		}
		
		return termp_val;
		
	}

	private int termp(int termp_i) { // <termp> ::= *<fact> { termp1.i = termp.i * fact.val } <termp1> { termp.val =
									 // termp1.val}| /<fact> { termp1.i = termp.i / fact.val } <termp1> { termp.val
									 // = termp1.val} | eps { exprp.val = exprp.i }
		int fact_val = 0, termp_val = 0;

		switch (look.tag) {
			
			case '*': 				 // GUIDA(T' -> *FT') = {*}
				
				match('*');
				fact_val = fact();
				termp_val = termp(termp_i * fact_val);
				break;

			case '/': 				 // GUIDA(T' -> /FT') = {/}
				
				match('/');
				fact_val = fact();
				termp_val = termp(termp_i / fact_val);
				break;

			case '+': 				 // GUIDA(T' -> eps) ) {$, +, -, )}
			
			case '-': 				 
			
			case ')': 				 
			
			case Tag.EOF: 			 
				
				termp_val = termp_i;
				break;

			default:
				error("Error in exprp");
		}

		return termp_val;
		
	}

	private int fact() { 			 // <fact> ::= (<expr>) { fact.val = expr.val} | NUM { fact.val = NUM.value}
		
		int fact_val = 0;

		switch (look.tag) {
			case '(': 				 // GUIDA(F -> (E)) = {(}
				
				match(Token.lpt.tag);
				fact_val = expr();
				match(Token.rpt.tag);
				break;
				
			case Tag.NUM: 			 // GUIDA(F -> num) = {num}
				
				fact_val = (((NumberTok) look).value);
				match(Tag.NUM);
				break;

			default:
				error("Error in fact");
		}

		return fact_val;

	}

	public static void main(String[] args) {
		
		Lexer lex = new Lexer();
		String path = "Test/Input1.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser valutatore = new Parser(lex, br);
			valutatore.start();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}