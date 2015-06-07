package luaFileLoader;

import static constants.Constants.*;

import java.util.regex.Pattern;

/**
 * A class of static Methods for easy token identification
 * 
 * @author Jonathan
 *
 */
public final class TokenIs {

	private String string;
	
	public TokenIs() {
		
		this.string = "";
		
	};

	public void setToken(String stringt) {
		this.string = stringt;
	};
	
	public String getToken() {
		return this.string;
	};
	
	public boolean whitespace() {
		return (this.getToken().equals("\t") || this.getToken().equals("\b") || 
				this.getToken().equals("\n") || this.getToken().equals("\r") || 
				this.getToken().equals("\f") || this.getToken().equals(" " ) );
	}

	public boolean clip() {
		return (this.getToken().equals("(") || this.getToken().equals(")") || 
				this.getToken().equals("{") || this.getToken().equals("}") || 
				this.getToken().equals(OPEN_SQUARE_BRACE) || this.getToken().equals(CLOSED_SQUARE_BRACE));
	}
	
	public boolean elementaryArithmetic() {
		return (this.addition() || this.subtraction() || 
				this.multiplication() || this.division() );
	}

	public boolean addition() {
		return this.getToken().equals(PLUSS);
	}

	public boolean subtraction() {
		return this.getToken().equals(MINUS);
	}

	public boolean multiplication() {
		return this.getToken().equals(MULTIPLY);
	}

	public boolean division() {
		return this.getToken().equals(DIVIDE);
	}

	public boolean allocation() {
		return this.getToken().equals(ALLOCATION);
	}
	
	public boolean number() {
		return Pattern.matches("\\d", this.getToken());
	}
	
	public boolean tempEquals(String string) {
		return this.getToken().equals(string);
	}

	public boolean quotationMark() {
		return this.getToken().equals(QUOTATION_MARK);
	}
	
	/**
	 * Cheks for: this.dot() || this.comma() || this.semicolon() || this.colon()
	 * @return
	 */
	public boolean delimiter() {
		return ( this.dot() || this.comma() || this.semicolon() || this.colon() );
	}
	
	public boolean dot() {
		return this.getToken().equals(DOT);
	}
	
	public boolean comma() {
		return this.getToken().equals(COMMA);
	}
	
	public boolean semicolon() {
		return this.getToken().equals(SEMICOLON);
	} 
	
	public boolean colon() {
		return this.getToken().equals(COLON);
	} 
}
