
/* <prog> 		::= <statlist>EOF
 * <statlist> 	::= <stat><statlistp>
 * <statlistp> 	::= ; <stat><statlistp> | eps 
 * <stat> 		::= assign<expr> to<idlist>		
 * 				 |	print(<exprlist>)
 * 				 |	read(<idlist>)
 * 				 |	while(<bexpr>) <stat>
 * 				 |	if(<bexpr>) <stat> end
 * 				 |	if(<bexpr>) <stat> else <stat> end
 * 				 |	{<statlist>}
 * <idlist>		::= ID<idlistp>	
 * <idlistp>	::= , ID<idlistp> | eps
 * <bexpr> 		::= RELOP<expr><expr>
 * <expr> 		::= +(<exprlist>)| -<expr><expr> | * (<exprlist>) | /<expr><expr>
 * 				 | NUM | ID  
 * <exprlist> 	::= <expr> <exprlistp> 
 * <exprlistp> 	::= , <expr><exprlistp> | eps 
 * 
 */

import java.io.*;

public class Parser {
	
	private Lexer lex;
	private BufferedReader pbr;
	private Token look;

	public Parser (Lexer l, BufferedReader br) {
		
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

	public void prog() { 				// <prog> ::= <statlist>EOF
		
		switch (look.tag) {
			case Tag.ASSIGN: 			// GUIDA(PROG -> STATLIST EOF) = {ASSIGN, PRINT, READ, WHILE, IF, { }
			
			case Tag.PRINT:
			
			case Tag.READ:
			
			case Tag.WHILE:
			
			case Tag.IF:
			
			case '{':
				
				statlist();
				match(Tag.EOF); 
				break;
	
			default:
					error("Error in prog");
					break;
			}	
			
	}

	public void statlist() { 			// <statlist> ::= <stat> <statlistp>
		
		switch (look.tag) {
			case Tag.ASSIGN: 			// GUIDA(STATLIST -> STAT STATLISTP) =  {ASSIGN, PRINT, READ, WHILE, IF, { }
			
			case Tag.PRINT:
			
			case Tag.READ:
			
			case Tag.WHILE:
			
			case Tag.IF:
			
			case '{':	
				
				stat();
				statlistp();
				break;
	
			default:
				error("Error in statlist");
				break;
		}
		
	}

	public void statlistp() { 		// <statlistp> ::= ; <stat> <statlistp> | eps
		
		switch (look.tag) {
			case ';': 				// GUIDA(STATLISTP -> ; STAT STATLISTP) = {;}				
				
				match(';');
				stat();
				statlistp();
				break;

			case '}': 				// GUIDA(STATLISTP -> eps) = {}, EOF}					
			
			case Tag.EOF: 
				break;
		
			default:
				error("Error in statlistp");
				break;
		}
		
	}

	public void stat() { 			/*<stat> ::= assign <expr> to <idlist>	|	print( <exprlist> ) |	read( <idlist> )
					                | while( <bexpr> ) <stat> | if( <bexpr> ) <stat><stattwo> | { <statlist> }*/
		switch (look.tag) {
			case Tag.ASSIGN: 		// GUIDA(STAT -> ASSIGN EXPR TO IDLIST) = {assign}
				
				match(Tag.ASSIGN);
				expr();
				match(Tag.TO);
				idlist();
				break;
	
			case Tag.PRINT: 		// GUIDA(STAT -> PRINT ( EXPRLIST )) = {print}
				
				match(Tag.PRINT);
				match('(');
				exprlist();
				match(')');
				break;
	
			case Tag.READ: 			// GUIDA(STAT -> READ ( IDLIST )) = {read}
				
				match(Tag.READ);
				match('(');
				idlist();
				match(')');
				break;
	
			case Tag.WHILE: 		// GUIDA(STAT -> WHILE ( BEXPR ) STAT) = {while}
				
				match(Tag.WHILE);
				match('(');
				bexpr();
				match(')');
				stat();
				break;
				
			case Tag.IF:			// GUIDA(STAT -> IF ( BEXPR ) STAT STATTWO) = {if}
				
				match(Tag.IF);
				match('(');
				bexpr();
				match(')');
				stat();
				stattwo();
				break;
				
			case '{':				// GUIDA(STAT -> { STATLIST }) = {{}
				
				match('{');
				statlist();
				match('}');
				break;
				
			default:
				error("Error in stat");
				break;
		}
		
		
	}
	
	public void stattwo() {			//<stattwo> ::= else <stat> end | end
		
		switch (look.tag) {
			case Tag.ELSE:			//GUIDA(STATTWO -> ELSE STAT END) = {else}
								
				match (Tag.ELSE);
				stat();
				match (Tag.END);
				break;
				
			case Tag.END:			//GUIDA(STATTWO -> END) = {end}
				
				match (Tag.END);
				break;
			
			default:
				error("Error in stat");
				break;
		}
		
	}

	public void idlist() { 		 	// <idlist> ::= ID <idlistp>
		
		if(look.tag == Tag.ID) { 	// GUIDA(IDLIST -> ID IDLISTP) = {ID}	
			 				
			match(Tag.ID);
			idlistp();
				
		}else
			error("Error in idlist");
		
	}

	public void idlistp() { 		// <idlistp> ::= , ID <idlistp> | eps
		
		switch (look.tag) {
			case ',': 				// GUIDA(IDLISTP -> , ID IDLISTP) = {,}
				
				match(',');
				match(Tag.ID);
				idlistp();
				break;
			
			case ';':				// GUIDA(IDLISTP -> eps) = {EOF, }, ; ,), end, else}
			
			case ')':
					
			case '}':
				
			case Tag.END:
				
			case Tag.ELSE:
				
			case Tag.EOF:
			
				break;
			
			default:
				error("Error in idlistp");
				break;
		}
		
	}

	public void bexpr() { 			// <bexpr> ::= RELOP <expr> <expr>
		
		if(look.tag == Tag.RELOP){	// GUIDA(BEXPR -> RELOP EXPR EXPR) = {RELOP}
			
            match(Tag.RELOP);
            expr();
            expr();
            
        }else
			error("Error in bexpr");
		
	}
	
	public void expr() { 			// <expr> ::= +( <exprlist> )| -<expr> <expr> | * ( <exprlist> ) | /<expr> <expr> | NUM | ID  
		
		switch (look.tag) {
			case '+': 				// GUIDA(EXPRP -> + ( EXPRLIST )) = {+}
				
				match('+');
				match('(');
				exprlist();
				match(')');
				break;

			case '-': 				// GUIDA(EXPRP -> - EXPR EXPR) = {-}
				
				match('-');
				expr();
				expr();
				break;

			case '*': 				// GUIDA(EXPRP -> * ( EXPRLIST )) = {*}
				
				match('*');
				match('(');
				exprlist();
				match(')');
				break;

			case '/': 				// GUIDA(EXPRP -> / EXPR EXPR) = {/}
				
				match('/');
				expr();
				expr();
				break;
				
			case Tag.NUM:			// GUIDA(EXPRP -> NUM) = {NUM}
				
				match(Tag.NUM);
				break;
				
			case Tag.ID:			// GUIDA(EXPRP -> ID) = {ID}
				
				match(Tag.ID);
				break;
				
			default:
				error("Error in expr");
				break;
		}
		
	}

	public void exprlist() { 		// <exprlist> ::= <expr> <exprlistp>
		
		switch (look.tag) {
			 				
			case Tag.NUM: 			// GUIDA(EXPRLIST -> EXPR EXPRLISTP) = {+, -, *, NUM, ID}	
			
			case Tag.ID: 
			
			case '+':
			
			case '-':
		
			case '*':
				
			case '/':
				
				expr();
				exprlistp();
				break;
			
			default:
				error("Error in exprlist");
				break;
		}
		
	}

	public void exprlistp() { 		// <exprlistp> 	::= , <expr> <exprlistp> | eps
		
		switch (look.tag) {
			case ',': 				// GUIDA(EXPRLISTP -> , EXPR EXPRLISTP) = {,}
				
				match(',');
				expr();
				exprlistp();
				break;
				
			case ')':				// GUIDA(EXPRLISTP -> eps) = {)}
				
				break;
				
			default:
				error("Error in exprlistp");
				break;
		}
		
	}

	public static void main(String[] args) {
		
		Lexer lex = new Lexer();
		String path = "Test/esempio_semplice.lft";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser parser = new Parser(lex, br);
			parser.prog();
			System.out.println("Input OK");
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}