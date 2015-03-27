package luaFileLoader;

import logger.Logger;

public class LuaFileLoader {

	/** Method to load a Factorio Lua Files
	 *
	 * @param file the path of the File to load
	 */
	public static void loadLuaFile(String file) {
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "start", "start");
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "file", file);
		
		LuaCharLoader luaCharLoader = new LuaCharLoader(file);
		LuaTokenLoader luaTokenLoader = new LuaTokenLoader(luaCharLoader);
		LuaDescriptiveFile luaDescriptiveFile = new LuaDescriptiveFile(file);
		
		LuaFileLoader.sortTokens(luaDescriptiveFile, luaTokenLoader, LuaFileLoaderState.NOSTATE);
		
		// while (!luaTokenLoader.eof()) {
		//	Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "Token", luaTokenLoader.getToken());
		// }
		
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "start", "end");
	}
	
	/**
	 * Sorts a all available tokens from a LuaTokenLoader in an LuaDescriptiveFile of the same level
	 * @param luaDescriptiveFile
	 * @param luaTokenLoader
	 * @param luaFileLoaderState
	 * @return filled luaDescriptiveFile
	 */
	public static LuaDescriptiveFile sortTokens(LuaDescriptiveFile luaDescriptiveFile, LuaTokenLoader luaTokenLoader,  LuaFileLoaderState luaFileLoaderState){
		Logger.logJUSTSO("LuaFileLoader", "sortTokens", "Startstate: " + luaFileLoaderState.toString(), "start");
		
		String TempTokenClipOrNoClip = "";
		
		
		while (luaTokenLoader.loadToken()) {

			switch (luaFileLoaderState){
			
			case NOSTATE :

				
				// Case for data:extended(
				if(luaTokenLoader.tokenIs( "data" ) ) {
					luaTokenLoader.loadToken();
					if ( luaTokenLoader.tokenIs( ":" ) ) {
						luaTokenLoader.loadToken();
						if ( luaTokenLoader.tokenIs( "extend" ) ) {
							luaTokenLoader.loadToken();
							if ( luaTokenLoader.tokenIs( "(" ) ) {
								luaTokenLoader.loadToken();
								if ( luaTokenLoader.tokenIs( "{" ) ) {
									
									Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended({", luaFileLoaderState.toString() + " : Start");
								
									// Creation of the new Object for the recursive descent
									LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( LuaFileLoaderState.DATAEXTENDED.toString() );
									// Recursive descent and then adding of the element
									luaDescriptiveFile.add( sortTokens( luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED ) );
	
									Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended({", LuaFileLoaderState.NOSTATE.toString() + " : End");
									
									break;
								} else {
									Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" { \"", 
											luaFileLoaderState.toString() + " : " + luaTokenLoader.getToken());
								}
							} else {
								Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" ( \"", 
										luaFileLoaderState.toString() + " : " + luaTokenLoader.getToken());
							}
						} else {
							Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" extended \"",
									luaFileLoaderState.toString() + " : " + luaTokenLoader.getToken());
						}
					} else {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" : \"",
								luaFileLoaderState.toString() + " : " + luaTokenLoader.getToken());
					}
				}
				// Case end for data:extended( thru )
				
				Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token",
						luaFileLoaderState.toString() + " : " + luaTokenLoader.getToken());
				break;
				
			case DATAEXTENDED :				
				
				if ( isOpenClip(luaTokenLoader) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Down: " + luaTokenLoader.getToken());
					
					// Creation of the new Object for the recursive descent
					LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( luaTokenLoader.getToken() );
					// recursive descent and then adding of the element
					luaDescriptiveFile.add( LuaFileLoader.sortTokens( luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED ) );
					
					break;
					
				} else if ( isClosedClip(luaTokenLoader) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Up: " + luaTokenLoader.getToken());
					
					return luaDescriptiveFile;
				}
				
				String tempStDaEx = luaTokenLoader.getToken();
				luaTokenLoader.loadToken();
				
				if ( luaTokenLoader.tokenIs( "=" ) ) {
					
					luaDescriptiveFile.add(clipOrNoClip(luaTokenLoader, luaFileLoaderState, tempStDaEx));
					break;

				} else {
					Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" = \"",
							LuaFileLoaderState.DATAEXTENDED.toString() + " : Token before : " + tempStDaEx + " : Token Mismatcht : " + luaTokenLoader.getToken());
				}
				
				break;
				
			case DATAEXTENDEDLIST :
				
				Logger.logINFO( "LuaFileLoader", "sortTokens", "State : " + luaFileLoaderState.toString(), " isOpenClip() = " + isOpenClip( luaTokenLoader ) );
				
				if( isClosedClip( luaTokenLoader ) ) {
					
					return luaDescriptiveFile;
					
				} else if( isOpenClip( luaTokenLoader ) ) {
						
					luaTokenLoader.loadToken();
					String tempStDaExLI = luaTokenLoader.getToken();
					luaTokenLoader.loadToken();
					
					if ( luaTokenLoader.tokenIs( "=" ) ) {
						
						luaDescriptiveFile.add(clipOrNoClip(luaTokenLoader, luaFileLoaderState, tempStDaExLI));
						break;
						
					} else if ( luaTokenLoader.tokenIs( "," ) ) {
						
						luaTokenLoader.loadToken();
						LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile( tempStDaExLI, luaTokenLoader.getToken() );
						Logger.logINFO( "LuaFileLoader", "sortTokens", "State : DATAEXTENDEDLIST", " Forward Too Items: " + luaDescriptiveFileNew.toString() );
						luaDescriptiveFile.add( luaDescriptiveFileNew );
						recive( luaTokenLoader, "}" );
						recive( luaTokenLoader, "," );
						break;
						
					} else {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" = \" or \" , \"",
							luaFileLoaderState.toString() + " : Token before : " + tempStDaExLI + " : Token Mismatcht : " + luaTokenLoader.getToken());
					}
					
					break;
				
				} else {
					
					String tempStDaExLI = luaTokenLoader.getToken();
					luaTokenLoader.loadToken();
					
					if ( luaTokenLoader.tokenIs( "}" ) ) {
						
						// Creation of the new Object to replace the branch with a leave
						LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(luaDescriptiveFile.getKey(), tempStDaExLI);
						
						Logger.logINFO( "LuaFileLoader", "sortTokens", "State : " + luaFileLoaderState.toString(), " Special Forward: " + tempStDaExLI );
						
						return luaDescriptiveFileNew;
					
					} else if ( luaTokenLoader.tokenIs( "=" ) ) {	
						
						luaDescriptiveFile.add(clipOrNoClip(luaTokenLoader, luaFileLoaderState, tempStDaExLI));
						break;
						
				 	} else {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" } \"",
								luaFileLoaderState.toString() + " : Token before : " + luaDescriptiveFile.getKey() + " : Token Mismatcht : " + luaTokenLoader.getToken());
					}
					break;
				}			
			}		
		}
		
		Logger.logDEBUG("LuaFileLoader", "sortTokens", "Token", "No More Token in file");
		return luaDescriptiveFile;
	}
	
	
	private static LuaDescriptiveFile clipOrNoClip(LuaTokenLoader luaTokenLoader, LuaFileLoaderState luaFileLoaderState, String typeToken) {

		luaTokenLoader.loadToken();
		
		if ( isOpenClip( luaTokenLoader ) ) {

			// Creation of the new Object for the recursive descent
			LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(typeToken);
			
			Logger.logINFO("LuaFileLoader", "clipOrNoClip", "State : " + luaFileLoaderState.toString(), " Forward Down: " +  luaDescriptiveFileNew.toString());
			
			// recursive descent and then adding of the element
			luaDescriptiveFileNew = LuaFileLoader.sortTokens(luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDEDLIST);

			reciveComma(luaTokenLoader);
			
			return luaDescriptiveFileNew;
			
		} else {
			
			// Creation of the new Object 
			LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(typeToken, luaTokenLoader.getToken());
			
			Logger.logINFO("LuaFileLoader", "clipOrNoClip", "State : " + luaFileLoaderState.toString(), " Forward: " + luaDescriptiveFileNew.toString());

			reciveComma(luaTokenLoader);
			
			return luaDescriptiveFileNew;
		}
	}
	
	
	
	/**
	 * Loads the next token and awaits a " , " if not writs a logERRORMOD
	 * @param luaTokenLoader
	 * @return true or false
	 */
	private static boolean reciveComma(LuaTokenLoader luaTokenLoader) {
		return recive(luaTokenLoader, ",");
	}
	
	/**
	 * Loads the next token and awaits token if not writs a logERRORMOD
	 * @param luaTokenLoader
	 * @param token
	 * @return true or false
	 */
	private static boolean recive(LuaTokenLoader luaTokenLoader, String token) {
		
		luaTokenLoader.loadToken();
		
		if ( luaTokenLoader.tokenIs(token) ) {
			return true;
		} else {
			Logger.logERRORMOD("LuaFileLoader", "reciveComma", "token not expectet shoud be \" " + token + " \" was", luaTokenLoader.getToken());
			return false;
		}
		
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
