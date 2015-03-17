package luaFileLoader;

import javax.swing.text.AbstractDocument.BranchElement;

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
	
	
	public static LuaDescriptiveFile sortTokens(LuaDescriptiveFile luaDescriptiveFile, LuaTokenLoader luaTokenLoader,  LuaFileLoaderState luaFileLoaderState){
		Logger.logJUSTSO("LuaFileLoader", "sortTokens", "Startstate: " + luaFileLoaderState.toString(), "start");
		
		while (luaTokenLoader.loadToken()) {
		
		String token = luaTokenLoader.getToken();
		
			switch (luaFileLoaderState){
			
			case NOSTATE :
				
				// Case for data:extended(
				if(token.equals("data")){
					luaTokenLoader.loadToken();
					if (luaTokenLoader.getToken().equals(":")) {
						luaTokenLoader.loadToken();
						if (luaTokenLoader.getToken().equals("extend")) {
							luaTokenLoader.loadToken();
							if (luaTokenLoader.getToken().equals("(")) {
								
								Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended(", LuaFileLoaderState.NOSTATE.toString() + " : Start");
								
								// Erzeugen des Rekusiven Abstigs
								LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(LuaFileLoaderState.DATAEXTENDED.toString());
								// Abstig und in sliste Aufnehmen
								luaDescriptiveFile.add(LuaFileLoader.sortTokens(luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED));

								Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended(", LuaFileLoaderState.NOSTATE.toString() + " : End");
								
								break;
							} else {
								Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" (\"", 
										LuaFileLoaderState.NOSTATE.toString() + " : " + luaTokenLoader.getToken());
							}
						} else {
							Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" extended\"",
									LuaFileLoaderState.NOSTATE.toString() + " : " + luaTokenLoader.getToken());
						}
					} else {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" : \"",
								LuaFileLoaderState.NOSTATE.toString() + " : " + luaTokenLoader.getToken());
					}
				}
				// Case end for data:extended( thru )
				
				Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token",
						luaFileLoaderState.toString() + " : " + luaTokenLoader.getToken());
				break;
				
			case DATAEXTENDED :				
				if( isOpenClip(luaTokenLoader.getToken()) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Down: " + luaTokenLoader.getToken());
					
					// Erzeugen des Rekusiven Abstigs
					LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(luaTokenLoader.getToken());
					// Abstig und in sliste Aufnehmen
					luaDescriptiveFile.add(LuaFileLoader.sortTokens(luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED));
					
					break;
				}
				
				if( isClosedClip(luaTokenLoader.getToken()) ) {
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Up: " + luaTokenLoader.getToken());
					
					return luaDescriptiveFile;
				}
				
				String tempSE = luaTokenLoader.getToken();
				boolean tempBE = luaTokenLoader.loadToken();
				
				if ( tempBE && ( luaTokenLoader.getToken().equals( "=" )  ) ) {
					
					luaTokenLoader.loadToken();
					
					if(isOpenClip(luaTokenLoader.getToken())){

						// Creation of the new Object for the Recursive descent
						LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(tempSE);
						
						Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Forward Down: " +  luaDescriptiveFileNew.toString());
						
						// Recursive descent and adding to the List
						luaDescriptiveFile.add(LuaFileLoader.sortTokens(luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDEDLIST));

						luaTokenLoader.loadToken();
						reciveComma(luaTokenLoader.getToken());
						
						break;
						
					} else {
						
						// Creation of the new Object 
						LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(tempSE, luaTokenLoader.getToken());
						
						Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Forward: " + luaDescriptiveFileNew.toString());
						
						// Adding to the List
						luaDescriptiveFile.add(luaDescriptiveFileNew);

						luaTokenLoader.loadToken();
						reciveComma(luaTokenLoader.getToken());
						
						break;
					}
				/*	
				} else if ( tempB && ( luaTokenLoader.getToken().equals( "}" )  ) ) {
					
					// Creation of the new Object 
					LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(luaDescriptiveFile.getKey(), tempS);
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Special Forward: " + luaDescriptiveFileNew.toString());
					
					return luaDescriptiveFileNew;
				*/
				} else {
					Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" = \"",
							LuaFileLoaderState.DATAEXTENDED.toString() + " : Token before : " + tempSE + " : Token Mismatcht : " + luaTokenLoader.getToken());
				}
				
				break;
				
			case DATAEXTENDEDLIST :
				if( isClosedClip( luaTokenLoader.getToken() ) ) {
					
					return luaDescriptiveFile;
					
				} else if( isOpenClip( luaTokenLoader.getToken() ) ) {
					
					luaTokenLoader.loadToken();
					
					String tempSEL = luaTokenLoader.getToken();
					boolean tempBEL = luaTokenLoader.loadToken();
					
					if ( tempBEL && ( luaTokenLoader.getToken().equals( "=" ) ||  luaTokenLoader.getToken().equals( "," ) ) ) {
						
						
						
						
						
						
					} else {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" = or , \"",
							LuaFileLoaderState.DATAEXTENDEDLIST.toString() + " : Token before : " + tempSEL + " : Token Mismatcht : " + luaTokenLoader.getToken());
					}
					
					break;
				
				} else {
					
					// TODO Auflistungen
					
					
					// Creation of the new Object 
					LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(luaDescriptiveFile.getKey(), luaTokenLoader.getToken());
					
					Logger.logINFO("LuaFileLoader", "sortTokens", "State : DATAEXTENDED", " Special Forward: " + luaDescriptiveFileNew.toString());
					
					luaTokenLoader.loadToken();
					
					if( ! luaTokenLoader.getToken().equals( "}" ) ) {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: \" } \"",
								LuaFileLoaderState.DATAEXTENDEDLIST.toString() + " : Token before : " + luaDescriptiveFile.getKey() + " : Token Mismatcht : " + luaTokenLoader.getToken());
					}
					
					return luaDescriptiveFileNew;
				}

			}		

		}
		
		Logger.logDEBUG("LuaFileLoader", "sortTokens", "Token", "No More Token in file");
		return luaDescriptiveFile;
	}
	
	private static boolean reciveComma(String token){
		return token.equals(",");
	}
	
	private static boolean isOpenClip(String token){
		return ( token.equals( "(" ) || token.equals( "{" ) || token.equals( "[" ) );
	}
	
	private static boolean isClosedClip(String token){
		return ( token.equals( ")" ) || token.equals( "}" ) || token.equals( "]" ) );
	}
		
		
	
	
}
