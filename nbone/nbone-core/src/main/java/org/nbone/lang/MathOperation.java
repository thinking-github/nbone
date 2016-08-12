package org.nbone.lang;
/**
 * 
 * @author thinking
 * @since 2016-08-08
 */
public enum MathOperation {
	
	ADD('+'),         //+ 加
	SUBTRACT('-'),    //- 减
	MULTIPLY('*'),    //* 乘
	DIVIDE('/');      // /除
	
	
	private final char mark;

	private MathOperation(char mark){
		this.mark = mark;
	}

	public char getMark() {
		return mark;
	}

	
	public static void main(String[] args) {
		System.out.println(ADD.getMark());
		System.out.println(SUBTRACT.getMark());
		System.out.println(MULTIPLY.getMark());
	}
	
}
