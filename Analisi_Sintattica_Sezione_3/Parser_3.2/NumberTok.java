public class NumberTok extends Token {
	
	public int value = 0;
	
	public NumberTok(int tag, int v) {
		
		super(Tag.NUM); 
		value = v; 	
		
	}

	public String toString() {
		
		return "<" + Tag.NUM + ", " + value + ">";
		
	}
}
