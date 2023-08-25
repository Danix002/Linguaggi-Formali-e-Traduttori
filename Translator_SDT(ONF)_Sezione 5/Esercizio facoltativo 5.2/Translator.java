/* Modificare la grammatica del linguaggio P per permettere l'utilizzo dei connettivi logici && (congiunzione, operatore binario), 
 * || (disgiunzione, operatore binario) e ! (negazione, operatore unario) in notazione prefissa nelle espressioni booleane. 

Segue un esempio di un input che corrisponde alla grammatica con connettivi logici:

read(x);
if (|| < x 10 && > x 20 ! > x 30) 
    print(+(x,100))
else
    print(x)
end


Si scriva uno SDT e si estenda il traduttore con un'implementazione corrispondente. */

import java.io.*;

public class Translator {
	
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
    	
        lex = l;
        pbr = br;
        move();
        
    }

    void move() { 
    	
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
        
    }

    void error(String s) { 
    	
        throw new Error("near line " + Lexer.line + ": " + s);
        
    }

    void match(int t) {
    	
        if (look.tag == t) {
        	
            if (look.tag != Tag.EOF) move();
            
        } else error("syntax error");
        
    }

    public void prog() { 
    	
        switch(look.tag){
            case '{':
            	
            case Tag.PRINT:
            	
            case Tag.READ:
            	
            case Tag.ASSIGN:
            	
            case Tag.IF:
            	
            case Tag.WHILE:
            	{
	                int statlist_next = code.newLabel();
	                statlist(statlist_next);
	                code.emitLabel(statlist_next);
	                match(Tag.EOF);
	                
	                try {
	                    code.toJasmin();
	                }
	                catch(java.io.IOException e) {
	                    System.out.println("IO error\n");
	                }
	                
	                break;
            	}
            
            default:
                error("\nTRANSLATOR: syntax error in PROG\nDetected: " + String.valueOf(look.tag) + "\nExpected: {, print, read, assign, if, while");
                break;
        }    
    }

    public void statlist(int statlist_next){ 
    	
        switch(look.tag){
        	case '{':
        		
        	case Tag.PRINT:
        		
        	case Tag.READ:
        		
        	case Tag.ASSIGN:
        		
        	case Tag.IF:
        		
        	case Tag.WHILE:
        		{
	                int stat_next = code.newLabel();
	                stat(stat_next);
	                int statlistp_next = statlist_next;
	                code.emitLabel(stat_next);
	                statlistp(statlistp_next);
	                break;
        		}
        		
            default:
                error("\nTRANSLATOR: syntax error in STATLIST\nDetected: " + String.valueOf(look.tag) + "\nExpected: {, print, read, assign, if, while");
                break;
        }
    }

    public void statlistp(int father_statilistp_next){ 
    	
        switch(look.tag){
            case ';':
            	{
	                match(';');
	                int stat_next = code.newLabel();
	                stat(stat_next);
	                int statlistp_next = father_statilistp_next;
	                code.emitLabel(stat_next);
	                statlistp(statlistp_next);
	                break;
            	}
            	
            case Tag.EOF:
            	
            case '}':
            	
                code.emit(OpCode.GOto, father_statilistp_next);
                break;
        
            default:
                error("\nTRANSLATOR: syntax error in STATLISTP\nDetected: " + String.valueOf(look.tag) + "\nExpected: ;, EOF, }");
                break;
        }
    }

    public void stat(int father_stat_next) { 
        
    	int stat_next;
    	
        switch(look.tag){
            case Tag.ASSIGN:		
            	{
	            	match(Tag.ASSIGN);
	            	
	            	int identity = 1;							
	            	
	            	int val = expr();	            	
	            	match(Tag.TO);	            		            		
	        		idlist(val,identity);	        		
	        		code.emit(OpCode.GOto, father_stat_next);        	
	                break;
            	}

            case Tag.PRINT:
            	{
	                match(Tag.PRINT);
	                
	                int identity = 2;							
	                
	                match('(');	                
	                exprlist(identity);
	                code.emit(OpCode.invokestatic, 1);
	                match(')');	                	               	                
	                code.emit(OpCode.GOto, father_stat_next);   
	                break;
            	}
            	
            case Tag.READ:
            	{
	                match(Tag.READ);
	                match('(');
	                
	                int identity = 0;							
	                
	                code.emit(OpCode.invokestatic, 0);
	                
	                int id_counter = idlist(identity,identity);	 
	                System.out.println("Get " + id_counter + " input.");
	                
		            match(')');		            
					code.emit(OpCode.GOto, father_stat_next);
	                break;
            	}
            	
            case Tag.IF:							
            	{
            		match(Tag.IF);
                    match('(');
                    int bexpr_true = code.newLabel();
                    int bexpr_false = code.newLabel();
                    stat_next = code.newLabel();
                    code.emitLabel(stat_next);
                    bexpr(bexpr_true, bexpr_false);
                    match(')');                   
                    code.emitLabel(bexpr_true);
                    stat(father_stat_next);
                    code.emitLabel(bexpr_false);
                    stattwo(father_stat_next);
                    break;	
            	}

            case Tag.WHILE:
            	{
            		match(Tag.WHILE);
                    match('(');
                    int bexpr_true = code.newLabel();
                    int bexpr_false = father_stat_next;
                    stat_next = code.newLabel();
                    code.emitLabel(stat_next);
                    bexpr(bexpr_true, bexpr_false);
                    match(')');
                    code.emitLabel(bexpr_true);
                    stat(stat_next);
                    break;
            	}
            	
            case '{':
            	{
	                match('{');
	                int statlist_next = father_stat_next;
	                statlist(statlist_next);
	                match('}');
	                break;
            	}
            	
            default:
                error("\nTRANSLATOR: syntax error in STAT\nDetected: " + String.valueOf(look.tag) + "\nExpected: {, print, read, assign, if, while");
                break;  
        }
    }

    public int idlistp(int valuetwo, int idtwo, int counter){
    	   	
        switch (look.tag) {
        case ',':
        	{             		
	        	match(',');
	        	
	        	if (look.tag==Tag.ID) {
	       
	            int id_addr = st.lookupAddress(((Word)look).lexeme); 
	            
	            if (id_addr==-1){
	                id_addr = count;
	                st.insert(((Word)look).lexeme, count++);
	            } 
	            
	            if(idtwo == 0) {
	            	
	            	code.emit(OpCode.invokestatic, 0);
	            	match(Tag.ID);
	                	                
	            }else { 
	            	
	            	match(Tag.ID);		            	            	
	            	code.emit(OpCode.ldc,valuetwo);	            	
	            }
	            
	            code.emit(OpCode.istore, id_addr);
                counter = idlistp(valuetwo, idtwo, counter) + 1;	
                
	        	}else 
	                error("Error in grammar (idlistp) after , with " + look);
	                   	
	            break;
        	}
        
        case ';':				
			
		case ')':
				
		case '}':
			
		case Tag.END:
			
		case Tag.ELSE:
			
		case Tag.EOF:
		
			break;
			
		default:
            error("\nTRANSLATOR: syntax error in IDLISTP\nDetected: " + String.valueOf(look.tag) + "\nExpected: , , ; , ), }, else, end");
        	
        }
        
        return counter;
    }

    public int idlist(int value, int id){ 
    	
	    int counter = 0;  
	    
    	if (look.tag==Tag.ID) {
    		   		
    		int id_addr = st.lookupAddress(((Word)look).lexeme);
            
            if (id_addr==-1) {
                id_addr = count;
                st.insert(((Word)look).lexeme,count++);
            }
                                    
            match(Tag.ID);
            code.emit(OpCode.istore, id_addr);
            counter = idlistp(value, id, counter) + 1;                                  
        }
        else 
            error("Error in grammar (idlist) after ID with " + look);
    	
    	return counter;
    }

    public void stattwo(int father_stattwo_next){ 
    	    	
    	switch(look.tag) {
    	case Tag.ELSE:
    		{
    			match(Tag.ELSE);
    			int stat_next = father_stattwo_next;
                stat(stat_next);
                match(Tag.END);                 
                break;
    		}
    		
    	case Tag.END:
    		match(Tag.END);    		
    		break;
    		
    	default:
            error("\nTRANSLATOR: syntax error in STATTWO\nDetected: " + String.valueOf(look.tag) + "\nExpected: else, end");
    	}
    }  

    public void bexpr(int bexpr_true, int bexpr_false){
    	
    	int bexprtwo_true, bexprtwo_false, bexprthree_true, bexprthree_false; 
    	
        switch(look.tag){       
            case Tag.RELOP:
            	{
	                String relop_sign = ((Word)look).lexeme;
	                
	                match(Tag.RELOP);
	                expr();
	                expr();
	                
	                switch(relop_sign){
	                    case "==" : code.emit(OpCode.if_icmpeq, bexpr_true); break;
	                    
	                    case "<=" : code.emit(OpCode.if_icmple, bexpr_true); break;
	                    
	                    case "<" : code.emit(OpCode.if_icmplt, bexpr_true); break;
	                    
	                    case ">=" : code.emit(OpCode.if_icmpge, bexpr_true); break;
	                    
	                    case ">" : code.emit(OpCode.if_icmpgt, bexpr_true); break;
	                    
	                    case "<>" : code.emit(OpCode.if_icmpne, bexpr_true); break;
	                    
	                    default: error("\nTRANSLATOR: segno " + relop_sign + " non riconosciuto. Impossibile trovare l'op-code corrispondente.");
	                }
            
            
	                code.emit(OpCode.GOto, bexpr_false);
	                break;
            	} 
            	
            case Tag.AND:
            	
	                match(Tag.AND);
	                bexprtwo_true = code.newLabel();
	                bexprtwo_false = bexpr_false;
	                bexpr(bexprtwo_true, bexprtwo_false);
	                bexprthree_true = bexpr_true;
	                bexprthree_false = bexpr_false;
	                code.emitLabel(bexprtwo_true);
	                bexpr(bexprthree_true, bexprthree_false);	                
	                break;
             
            	
            case Tag.OR:
            	
	                match(Tag.OR);               
	                bexprtwo_true = bexpr_true;
	                bexprtwo_false = code.newLabel();
	                bexpr(bexprtwo_true, bexprtwo_false);
	                bexprthree_true = bexpr_true;
	                bexprthree_false = bexpr_false;
	                code.emitLabel(bexprtwo_false);
	                bexpr(bexprthree_true, bexprthree_false);
	                break;
            
            	
            case '!':

	                match('!');
	                bexprtwo_true = bexpr_false;
	                bexprtwo_false = bexpr_true;
	                bexpr(bexprtwo_true, bexprtwo_false);
	                break;
            
            	
            default:
            	error("\nTRANSLATOR: syntax error in BEXPR\nDetected: " + String.valueOf(look.tag) + "\nExpected: RELOP");
                break;
        }       
    }

    private int expr() { 
    	
        int expr_counter=0;
        int identity = 0;
        
        switch(look.tag){
            case '+':
            	
                match('+');
                match('(');
                expr_counter = exprlist(identity) - 1;
                match(')');
                
                for(int i = 0; i< expr_counter; i++)
                    code.emit(OpCode.iadd);
                
                break;

            case '*':
            	{
	                match('*');
	                match('(');
	                expr_counter = exprlist(identity) - 1;
	                match(')');
	                
	                for(int i = 0; i< expr_counter; i++)
	                    code.emit(OpCode.imul);
	                break;
            	}
            	
            case '-':
            	
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            
            case '/':
            	
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;  
                
            case Tag.NUM:
            	{
            		expr_counter = ((NumberTok) look).value;
	                code.emit(OpCode.ldc, expr_counter);
	                match(Tag.NUM);	                
	                break;
            	}
            	
            case Tag.ID:
            	{
	                int id_addr = st.lookupAddress(((Word)look).lexeme);
	                
	                if (id_addr==-1){
	                    id_addr = count;
	                    st.insert(((Word)look).lexeme, count++);
	                }
	                
	                match(Tag.ID);
	                code.emit(OpCode.iload, id_addr); 	                	                
	                break;
            	}
            default:
                error("\nTRANSLATOR: syntax error in EXPR\nDetected: " + String.valueOf(look.tag) + "\nExpected: +, *, -, /, NUM, ID");
                break;
        }
        return expr_counter; 
    }

    public int exprlist(int id){ 
    	
        int ret_value = 0;
               
        switch(look.tag){
            case '+':
            	
            case '*':
            	
            case '-':
            	
            case '/':
            	
            case Tag.NUM:
            	
            case Tag.ID:
            	           	           	
                expr();
                ret_value = 1 + exprlistp(id); 
                                              
                break;

            default:
                error("\nTRANSLATOR: syntax error in EXPRLIST\nDetected: " + String.valueOf(look.tag) + "\nExpected: +, *, -, /, NUM, ID");
                break;
        }
        
        return ret_value;
        
    }

    public int exprlistp(int idtwo){ 
    	
        int ret_value = 0;

        switch(look.tag){
            case ',':
            	
            	match(',');
            	
            	if(idtwo == 2)
            		
            		code.emit(OpCode.invokestatic, 1);
            	
                expr();
                ret_value = 1 + exprlistp(idtwo);
                break;
            
            case ')':
            	
                ret_value = 0;
                break;

            default:
                error("\nTRANSLATOR: syntax error in EXPRLISTP\nDetected: " + String.valueOf(look.tag) + "\nExpected: , , )");
                break;
        }
        
        return ret_value;
        
    }

    public static void main(String[] args) {
    	
        Lexer lex = new Lexer();
        String path = "Test/connettivi_logici.lft";
        try {
        	
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
            
        } catch (IOException e) {e.printStackTrace();}
        
    }
}
/*compilare e eseguire Translator*/
/*chiamare jasmin sull'output.j prodotto dal translator (mettere file nella cartella Jasmin): java -jar jasmin.jar Output.j*/
/*chiamare java Output sul file class prodotto da jasmin*/


