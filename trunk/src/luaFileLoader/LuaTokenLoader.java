package luaFileLoader;

import static luaFileLoader.LuaTokenLoaderState.COMMENT;
import static luaFileLoader.LuaTokenLoaderState.LONGCOMMAND;
import static luaFileLoader.LuaTokenLoaderState.OTHER;
import static luaFileLoader.LuaTokenLoaderState.STRING;
import static constants.Constants.*;
import logger.Logger;
import logger.Priority;

public class LuaTokenLoader {

	private LuaCharLoader luaCharLoader;
	private String lastToken = "";
	
	/**
	 * Creates a LuaTokenLoader with a LuaCharLoader to extract token
	 * @param luaCharLoader A luaCharLoader
	 */
	public LuaTokenLoader(LuaCharLoader luaCharLoader){
		Logger.log(Priority.INFO, "LuaTokenLoader", "LuaTokenLoader", "start", "start");
		this.luaCharLoader = luaCharLoader;
		Logger.log(Priority.INFO, "LuaTokenLoader", "LuaTokenLoader", "end", "end");
	}
	
	/**
	 * Returns the current Token
	 * @return Current Token
	 */
	public String getToken(){
		return lastToken;
	}
	
	/**
	 * Loads the next Token and returns if more token are available
	 * @return if more token are available True else False
	 */
	public boolean loadToken(){
		lastToken = getTokenWork();
		Logger.logINFO("LuaTokenLoader", "loadToken", "Token ausgeben", lastToken);
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
		return this.getToken().equals(token);
	}
	
	/**
	 * Checks for the given Token is in the Array
	 * @param strings
	 * @return
	 */
	public boolean tokenIsIn(String[] strings) {
		for(String elem : strings){
			if( this.getToken().equals(elem)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Function to load the next Token
	 * @return
	 */
	private String getTokenWork() {
		
		boolean wait_for_token = true;
		String single = "";
		String collector = "";
		LuaTokenLoaderState state = OTHER;
		
		while ( wait_for_token ) {
			
			if( ! luaCharLoader.setCharAdd() ) { return collector; }
			
			single = luaCharLoader.getChar();

			switch ( state ) {
			
			case OTHER :
				// Look for the beginning of a String by "
				if( state == OTHER && single.equals( QUOTATION_MARK ) ) { 
					state = STRING; collector = collector.concat(single); 
					break;
				} else
				// Look for the beginning of a Command by --
				if(single.equals( MINUS ) && luaCharLoader.setSecChar( MINUS_ONE ) && luaCharLoader.getSecChar().equals( MINUS ) ) {
					state = COMMENT; collector = collector.concat(single);  luaCharLoader.setCharAdd();
					break;
				} else
				// Lock for a whitespace
				if( tokenIsWhitespace( single ) ) {
					break;
				} else
				// Look for a next token Charater	
				if(	single.equals(".") || single.equals(":") || single.equals("(") || single.equals(")") || 
					single.equals("{") || single.equals("}") || single.equals("[") || single.equals("]") ||
					single.equals("+") || single.equals("-") || single.equals("*") || single.equals("/") || 
					single.equals("=") || single.equals(",") || single.equals(";") ){
					if(collector.isEmpty()){
						return single;
					} else {
						luaCharLoader.setCharSub();
						return collector;
					}
				}
				collector = collector + single;
				break;
				
			case COMMENT :
				
				//Look if  command ends by Newline
				if((single.equals("\r")  || single.equals("\n"))) { 
					return collector ; 
				} else
				//Look if long command Starts by: --[[
				if(single.equals("[") && collector.equals("--[")) { 
					state = LONGCOMMAND; 
				}
				
				collector = collector + single;
				break;
				
			case LONGCOMMAND :
				
				////Look if Longcommand ends by "
				if(single.equals( OPEN_SQUARE_BRACE ) && luaCharLoader.setSecChar( MINUS_ONE ) && luaCharLoader.getSecChar().equals( OPEN_SQUARE_BRACE ) ) { 
					return collector.concat( single ); 
				}
				
				collector = collector + single;
				break;
				
			case STRING :
			
				//Look if String ends by "
				if(state == STRING && single.equals("\"")){ 
					return collector.concat(single);
				}
				
				collector = collector + single;
				break;
				
			case MODLODERCOMMENT :
				break;
				
			default:
				Logger.log(Priority.ERRORCODE, "LuaTokenLoader", "getToken", "Enumtype not known", state.toString());
			}
		}
		Logger.log(Priority.ERRORFATALE, "LuaTokenLoader", "getToken", "ERROR", "Something went terrible worng");
		throw new RuntimeException("LuaTokenLoader getToken ERROR Something went terrible worng");
	}

	public boolean tokenIsWhitespace(String string) {
		return ( string.equals("\t") || string.equals("\b") || string.equals("\n") || 
				 string.equals("\r") || string.equals("\f") || string.equals(" " ) );
	}
	
	
	
	
	
	
	
}
