package luaFileLoader;

import static constants.Constants.EMPTY_STRING;
import static constants.Constants.MINUS;
import static constants.Constants.MINUS_ONE;
import static constants.Constants.OPEN_SQUARE_BRACE;
import static constants.Constants.PLUSS_ONE;
import static constants.Constants.QUOTATION_MARK;
import static constants.Constants.ZERO;
import static luaFileLoader.LuaTokenLoaderState.COMMENT;
import static luaFileLoader.LuaTokenLoaderState.LONGCOMMAND;
import static luaFileLoader.LuaTokenLoaderState.NUMBER;
import static luaFileLoader.LuaTokenLoaderState.NUMBER_DOT;
import static luaFileLoader.LuaTokenLoaderState.OTHER;
import static luaFileLoader.LuaTokenLoaderState.STRING;

import java.util.ArrayList;

import logger.Logger;
import logger.Priority;

public class LuaTokenLoader {

	private LuaCharLoader luaCharLoader;
	
	private LuaTokenLoaderState state;
	
	TokenIs tokenIs;
	String collector =  EMPTY_STRING;
	
	//Holding list for the tokens
	private ArrayList<String> token;
	//Pointer on the momentary in the token array
	private int tokenPointer;
	
	/**
	 * Creates a LuaTokenLoader with a LuaCharLoader to extract token
	 * @param luaCharLoader A luaCharLoader
	 */
	public LuaTokenLoader(LuaCharLoader luaCharLoader){
		
		/* Possibility
		System.getProperty("line.separator");
		Pattern pattern = Pattern.compile(fREGEXP, Pattern.COMMENTS);
		*/
		
		Logger.log(Priority.INFO, "LuaTokenLoader", "LuaTokenLoader", "start", "start");
		
		this.luaCharLoader = luaCharLoader;
		
		this.tokenIs = new TokenIs();
		
		this.state = OTHER;
		
		this.token = new ArrayList<String>();
		this.tokenPointer = MINUS_ONE;
		
		this.loadTokens();
		
		Logger.log(Priority.INFO, "LuaTokenLoader", "LuaTokenLoader", "end", "end");
	}
	
	/**
	 * Returns the current Token
	 * @return Current Token
	 */
	public String getCurrentToken(){
		if( ( tokenPointer < 0 ) || ( tokenPointer >= token.size() ) ) {
			Logger.logERRORCODE("LuaTokenLoader", "getCurrentToken", "Give last Token", "tokenPointer is aut of Range : " + tokenPointer);
			return EMPTY_STRING;
		}
		
		return token.get(tokenPointer);
	}
	
	/**
	 * Offset's The token Pointer
	 * @param number Bigger 0 smaller the number of elements before
	 * @return returns if successful
	 */
	public boolean offsetPointer(int number) {
		if ( ( ( this.tokenPointer + number ) >= token.size() ) || ( ( this.tokenPointer + number ) < ZERO ) ) {
			Logger.logERRORCODE("LuaTokenLoader", "offsetPointer", "offsetPointer not possibel is out of range", number + " <: Number : Result :> " + this.tokenPointer + number );
			return false;
		}
		this.tokenPointer = this.tokenPointer + number;
		Logger.logJUSTSO("LuaTokenLoader", "offsetPointer", "Old Pointer: " + ( this.tokenPointer - number ), "New Pointer: " + this.tokenPointer);
		return true;
	}

	/**
	 * Loads the next Token and returns if more token are available
	 * @return if more token are available true else false
	 */
	public boolean loadNextToken() {
		
		if ( ( tokenPointer + 1 ) < ( token.size() ) ) {
			tokenPointer++;
			Logger.logJUSTSO("LuaTokenLoader", "loadToken", "Loded Token for the second Time, Token Nr: " + tokenPointer + " ausgeben", QUOTATION_MARK + this.getCurrentToken() + QUOTATION_MARK);
			return true;
		}
		
		if( eof() ) {
			Logger.logJUSTSO("LuaTokenLoader", "loadToken", "No More Token, EOF, last Token Nr: " + (token.size() - 1) + " ausgeben", QUOTATION_MARK + this.getCurrentToken() + QUOTATION_MARK);
			return ! eof();
		}
		
		String temp = getNextToken();
		
		if( temp.equals(EMPTY_STRING) && eof() ) {
			Logger.logJUSTSO("LuaTokenLoader", "loadToken", "No new Token last Token Nr: " + (token.size() - 1) + " ausgeben", QUOTATION_MARK + this.getCurrentToken() + QUOTATION_MARK);
			return ! eof();
		} else if( temp.equals(EMPTY_STRING) ) {
			Logger.logERRORCODE("LuaTokenLoader", "loadToken", "No new Token but not EOF last Token Nr: " + (token.size() - 1) + " ausgeben", QUOTATION_MARK + this.getCurrentToken() + QUOTATION_MARK);
		}
		
		token.add( temp );
		tokenPointer++;
		Logger.logJUSTSO("LuaTokenLoader", "loadToken", "Token Nr: " + tokenPointer + " ausgeben", QUOTATION_MARK + this.getCurrentToken() + QUOTATION_MARK);
		return ! eof();
	}
	
	/**
	 * Checks for eof
	 * @return true or false
	 */
	public boolean eof(){
		return luaCharLoader.eof();
	}
	
	/**
	 * Checks for the given Token
	 * @param luaTokenLoader
	 * @param token
	 * @return true or false
	 */
	public boolean tokenIs( String token ){
		return this.getCurrentToken().equals(token);
	}
	
	
	/**
	 * Checks for the given Token is in the Array
	 * @param strings
	 * @return
	 */
	public boolean tokenIsIn(String[] strings) {
		for(String elem : strings){
			if( this.getCurrentToken().equals(elem)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Loads all tokens
	 */
	public void loadTokens(){

		String temp = getNextToken();
		
		while( temp.equals(EMPTY_STRING) && eof() ) {
			token.add( temp );
			temp = getNextToken();
		}
	}
	
	/**
	 * Function to load the next Token
	 * @return
	 */
	private String getNextToken() {

		this.collector = EMPTY_STRING;
		
		while ( luaCharLoader.setCharNext() ) {
			
			tokenIs.setToken(luaCharLoader.getChar());
			
			switch ( state ) {
			
			case OTHER :		
				
				// Look for the beginning of a String by "
				if( tokenIs.quotationMark() ) { 
					state = STRING; this.collector = this.collector.concat(tokenIs.getToken()); 
					break;
				} else
				// Look for the beginning of a Command by --
				if(tokenIs.subtraction() && luaCharLoader.setSecChar( PLUSS_ONE ) && luaCharLoader.getSecChar().equals( MINUS ) ) {
					state = COMMENT; this.collector = this.collector.concat(tokenIs.getToken());  luaCharLoader.setCharNext();
					break;
				} else
				// Lock for a whitespace
				if( tokenIs.whitespace() ) {
					break;
				} else
				//Lock for Numbers to fiend them
				if ( tokenIs.number() ) {
					state = NUMBER; this.collector = this.collector + tokenIs.getToken();
					break;
				} else	
				// Look for a next token Charater	
				if(	tokenIs.clip() || tokenIs.elementaryArithmetic() || 
					tokenIs.allocation() || tokenIs.delimiter()) {
					if(this.collector.isEmpty()){
						return tokenIs.getToken();
					} else {
						luaCharLoader.setCharBefore();
						return this.collector;
					}
				}
				
				this.collector = this.collector + tokenIs.getToken();
				break;
				
			case COMMENT :
				
				// Look if  command ends by Newline
				if((tokenIs.tempEquals("\r")  || tokenIs.tempEquals("\n"))) { 
					return this.collector ; 
				} else
					
				// Look if long command Starts by: --[[
				if(tokenIs.equals("[") && this.collector.equals("--[")) { 
					state = LONGCOMMAND;
					this.collector = this.collector + tokenIs.getToken();
					break;
				} else
					
				// Look along the command
				{
					this.collector = this.collector + tokenIs.getToken();
					break;
				}
				
			case LONGCOMMAND :
				
				// Look if long-command ends by "]]"
				if(tokenIs.equals( OPEN_SQUARE_BRACE ) && luaCharLoader.setSecChar( MINUS_ONE ) && luaCharLoader.getSecChar().equals( OPEN_SQUARE_BRACE ) ) { 
					return collector.concat( tokenIs.getToken() ); 
				} else 
				
				// Look along the long-command
				{
					this.collector = this.collector + tokenIs.getToken();
					break;
				}
				
			case STRING :
			
				// Look if String ends by "
				if( ! tokenIs.quotationMark() ) { 
					this.collector = this.collector + tokenIs.getToken(); break;
				}
				
				state = OTHER; return this.collector.concat(tokenIs.getToken());
				
			case NUMBER :

				if(tokenIs.number()){
					this.collector = this.collector + tokenIs.getToken(); break; 
				} else if(tokenIs.dot()) {
					state = NUMBER_DOT; this.collector = this.collector + tokenIs.getToken(); break; 
				} 
				
				state = OTHER; luaCharLoader.setCharBefore(); return this.collector;
			case NUMBER_DOT :
				
				if(tokenIs.number()){
					this.collector = this.collector + tokenIs.getToken(); break; 
				}
					
				state = OTHER; luaCharLoader.setCharBefore(); return this.collector;

			case MODLODERCOMMENT :
				break;
				
			default:
				Logger.log(Priority.ERRORCODE, "LuaTokenLoader", "getToken", "Enumtype not known", state.toString());
			}
		}
		
		return this.collector;
	}

}
