package luaFileLoader;

import logger.Logger;
import static constants.Constants.*;

public class LuaFileLoader {

	/** Method to load a Factorio Lua Files
	 *
	 * @param file the path of the File to load
	 */
	public static LuaDescriptiveFile loadLuaFile(String file) {
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "start", "start");
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "file", file);
		
		LuaCharLoader luaCharLoader = new LuaCharLoader(file);
		LuaTokenLoader luaTokenLoader = new LuaTokenLoader(luaCharLoader);
		LuaDescriptiveFile luaDescriptiveFile = new LuaDescriptiveFile(file);
		
		LuaDescriptiveFile temp = LuaFileLoader.sortTokens(luaDescriptiveFile, luaTokenLoader, LuaFileLoaderState.NOSTATE);
		
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "end", "end");
		
		return temp;
	}
	
	/**
	 * Sorts a all available tokens from a LuaTokenLoader in an LuaDescriptiveFile of the same level
	 * @param luaDescriptiveFile
	 * @param luaTokenLoader
	 * @param luaFileLoaderState
	 * @return filled luaDescriptiveFile
	 */
	public static LuaDescriptiveFile sortTokens(LuaDescriptiveFile luaDescriptiveFile, LuaTokenLoader luaTokenLoader,  LuaFileLoaderState luaFileLoaderState) {
		Logger.logJUSTSO("LuaFileLoader", "sortTokens", "Startstate: " + luaFileLoaderState.toString(), "start");	
		
		while ( luaTokenLoader.loadNextToken() ) {

			switch (luaFileLoaderState) {
			
			case NOSTATE :

				luaTokenLoader.matches("data:extended");
				
				
				// Case for data:extended(
				if(luaTokenLoader.tokenIs( "data" ) ) {
					if ( receive(luaTokenLoader, COLON, true, "Missing " + COLON + " to open data:extended" + "State: " + LuaFileLoaderState.NOSTATE.toString() ) ) {
						if ( receive(luaTokenLoader, "extend", true, "Missing " + "extend" + " to open data:extended" + "State: " + LuaFileLoaderState.NOSTATE.toString() ) ) {
							if ( receive(luaTokenLoader, "(", true, "Missing " + "(" + " to open data:extended" + "State: " + LuaFileLoaderState.NOSTATE.toString() ) ) {
								if ( receive(luaTokenLoader, "{", true, "Missing " + "{" + " to open data:extended" + "State: " + LuaFileLoaderState.NOSTATE.toString() ) ) {
									
									Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended({", luaFileLoaderState.toString() + " : Start");
								
									// Creation of the new Object for the recursive descent
									LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( LuaFileLoaderState.DATAEXTENDED.toString() );
									// Recursive descent and then adding of the element
									luaDescriptiveFile.add( sortTokens( luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED ) );
	
									//receive(luaTokenLoader, "}", true, "Missing } to close the data:extended Block");
									receive(luaTokenLoader, ")", true, "Missing ) to close the data:extended Block");
									
									Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended({", luaFileLoaderState.toString() + " : End");
									
									break;
								}
							}
						}
					}
				}
				// Case end for data:extended( thru )
				
				Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token",
						luaFileLoaderState.toString() + " : " + luaTokenLoader.getCurrentToken());
				break;
				
			case DATAEXTENDED :
				
				if( isClosedClip( luaTokenLoader ) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State: DATAEXTENDEDLIST", "Up: " + luaTokenLoader.getCurrentToken() + ", File: " + luaDescriptiveFile.toString() );

					return luaDescriptiveFile;
					
				} else if( isOpenClip( luaTokenLoader ) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State: " + luaFileLoaderState.toString(), "Down: " + luaTokenLoader.getCurrentToken());
						
					//Check for augment List: "{element,element}"
					if ( checkForTooAhead(luaTokenLoader, COMMA) ) {
						
						Logger.logJUSTSO( "LuaFileLoader", "sortTokens", "State: " + luaFileLoaderState.toString(), " In double list alocation ");
						
						luaTokenLoader.loadNextToken();
						String tempElemOne = luaTokenLoader.getCurrentToken();
						luaTokenLoader.loadNextToken();
						luaTokenLoader.loadNextToken();
						
						LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( tempElemOne, luaTokenLoader.getCurrentToken() );
						Logger.logINFO( "LuaFileLoader", "sortTokens", "State: DATAEXTENDEDLIST", "Forward Too Items: " + luaDescriptiveFileNew.toString() );
						luaDescriptiveFile.add( luaDescriptiveFileNew );
						
						receive( luaTokenLoader, "}" , true, "Missing Closing { after: " + luaDescriptiveFileNew.toString() );
			
						break;
						
					}
					
					// Creation of the new Object for the recursive descent
					LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( luaTokenLoader.getCurrentToken() );
					// recursive descent and then adding of the element
					luaDescriptiveFile.add( LuaFileLoader.sortTokens( luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED ) );
					
					break;
				
				} else if ( luaTokenLoader.tokenIs( COMMA ) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State: DATAEXTENDEDLIST", "Forward found colon");
					
					break;
					
				} else {
	
					luaDescriptiveFile.add( allocation( luaTokenLoader, luaFileLoaderState ) );
					
					break;
				}			
			}		
		}
		
		Logger.logDEBUG("LuaFileLoader", "sortTokens", "Token", "No More Token in file");
		return luaDescriptiveFile;
	}
	
	
	private static LuaDescriptiveFile allocation( LuaTokenLoader luaTokenLoader, LuaFileLoaderState luaFileLoaderState) {

		Logger.logINFO("LuaFileLoader", "allocation", "State: " + luaFileLoaderState.toString(), "Need a ALLOCATION for Type: " + luaTokenLoader.getCurrentToken() );
		
		String tempType = luaTokenLoader.getCurrentToken();
		luaTokenLoader.loadNextToken();
		
		//Precondition check if it is a allocation
		if ( ! luaTokenLoader.tokenIs( ALLOCATION ) ) {
			
			Logger.logERRORMOD( "LuaFileLoader", "sortTokens", "State: " + luaFileLoaderState.toString() +  " For type: " + tempType,
					"Expectet \"" + ALLOCATION + "\" but was \"" + luaTokenLoader.getCurrentToken() + "\"");
		}
		
		luaTokenLoader.loadNextToken();
		
		Logger.logINFO( "LuaFileLoader", "allocation", "State: " + luaFileLoaderState.toString(), "isOpenClip() = " + isOpenClip( luaTokenLoader ) );
		
		//Starts List
		if ( isOpenClip( luaTokenLoader ) ) {
			
			if( checkForTooAhead(luaTokenLoader, "}") ) {
				
				luaTokenLoader.loadNextToken();
				
				// Creation of the new Object to replace the branch with a leave
				LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( tempType, luaTokenLoader.getCurrentToken() );
				
				luaTokenLoader.loadNextToken();
				
				Logger.logINFO( "LuaFileLoader", "sortTokens", "State: " + luaFileLoaderState.toString(), "Special Forward: " + luaDescriptiveFileNew.toString() );
				
				return luaDescriptiveFileNew;
			}
			
			// Creation of the new Object for the recursive descent
			LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(tempType);
			
			Logger.logINFO( "LuaFileLoader", "allocation", "State: " + luaFileLoaderState.toString(), "Forward Down: " +  luaDescriptiveFileNew.toString() );
			
			// recursive descent and then adding of the element
			luaDescriptiveFileNew = LuaFileLoader.sortTokens( luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED );

			return luaDescriptiveFileNew;
			
		} else {
			
			// Creation of the new Object 
			LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( tempType, luaTokenLoader.getCurrentToken() );
			
			Logger.logINFO( "LuaFileLoader", "allocation", "State: " + luaFileLoaderState.toString(), "Forward: " + luaDescriptiveFileNew.toString() );
			
			return luaDescriptiveFileNew;
		}
	}
	
	
	/**
	 * Do's check for a given token too ahead without changing the current token
	 */
	private static boolean checkForTooAhead(LuaTokenLoader luaTokenLoader, String token) {
		luaTokenLoader.loadNextToken();
		luaTokenLoader.loadNextToken();
		if( luaTokenLoader.tokenIs( token ) ) {
			luaTokenLoader.offsetPointer(MINUS_TOO);
			return true;
		} else {
			luaTokenLoader.offsetPointer(MINUS_TOO);
			return false;
		}
	}
	
	
	/**
	 * Loads the next token and awaits a given Token if not go's back a token, is able to print error-massage
	 * @param luaTokenLoader
	 * @param token
	 * @param error Print error-massage or not
	 * @param massage
	 * @return if Token is found successful
	 */
	private static boolean receive(LuaTokenLoader luaTokenLoader, String token, boolean error, String massage) {
		
		luaTokenLoader.loadNextToken();
		
		if ( luaTokenLoader.tokenIs(token) ) {
			
			return true;
	
		} else if ( error ) {
			
			Logger.logERRORMOD("LuaFileLoader", "recive",  
					"token not expectet shoud be \" " + token + " \" was: \"", 
					luaTokenLoader.getCurrentToken() + "\", Massage : " + massage);
		} 
		
		if ( ! luaTokenLoader.offsetPointer( MINUS_ONE ) ) {

			Logger.logERRORFATALE( "LuaFileLoader",  "recive", 
					"token not expectet, and cood not go back a Token, shoud be \" " + token + " \" was", 
					luaTokenLoader.getCurrentToken() + ", Massage : " + massage );
		}
		
		return false;
	}	
	
	/**
	 * Checks if the actual token is from { "(", "{", "[" }
	 * @param luaTokenLoade
	 * @return true or false
	 */
	private static boolean isOpenClip(LuaTokenLoader luaTokenLoader) {
		return ( luaTokenLoader.tokenIsIn( new String[]{ "(", "{", "[" } ) );
	}
	
	/**
	 * Checks if the actual token is from { ")", "}", "]" }
	 * @param luaTokenLoade
	 * @return true or false
	 */
	private static boolean isClosedClip(LuaTokenLoader luaTokenLoader) {
		return ( luaTokenLoader.tokenIsIn( new String[]{ ")", "}", "]" } ) );
	}
		

}
