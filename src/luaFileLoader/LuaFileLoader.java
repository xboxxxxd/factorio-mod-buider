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
	
	
	public static LuaDescriptiveFile sortTokens(LuaDescriptiveFile luaDescriptiveFile, LuaTokenLoader luaTokenLoader,  LuaFileLoaderState luaFileLoaderState){
		Logger.logDEBUG("LuaFileLoader", "sortTokens", "start", "start");
		
		while (luaTokenLoader.loadToken()) {
		
		String token = luaTokenLoader.getToken();
		
			switch (luaFileLoaderState){
			
			case NOSTATE :
				
				// Case for data:extended(
				if(token.equals("data")){
					if (luaTokenLoader.getToken().equals(":")) {
						if (luaTokenLoader.getToken().equals("extend")) {
							if (luaTokenLoader.getToken().equals("(")) {
								Logger.logDEBUG("LuaFileLoader", "sortTokens", "State : data:extended(", LuaFileLoaderState.DATAEXTENDED.toString());
								
								// Erzeugen des Rekusiven Abstigs
								LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(LuaFileLoaderState.DATAEXTENDED.toString());
								// Abstig und in sliste Aufnehmen
								luaDescriptiveFile.add(LuaFileLoader.sortTokens(luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED));

								break;
							} else {
								Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: (", 
										LuaFileLoaderState.DATAEXTENDED.toString() + " : " + luaTokenLoader.getToken());
							}
						} else {
							Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: extended",
									LuaFileLoaderState.DATAEXTENDED.toString() + " : " + luaTokenLoader.getToken());
						}
					} else {
						Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token shoud be: :",
								LuaFileLoaderState.DATAEXTENDED.toString() + " : " + luaTokenLoader.getToken());
					}
				}
				// Case end for data:extended( thru )
				
				Logger.logERRORMOD("LuaFileLoader", "sortTokens", "State : Unexepectet Token",
						LuaFileLoaderState.DATAEXTENDED.toString() + " : " + luaTokenLoader.getToken());
				break;
				
			case DATAEXTENDED :
				if(luaTokenLoader.getToken().equals(")") || luaTokenLoader.getToken().equals("}") || luaTokenLoader.getToken().equals("]") ) {
					return luaDescriptiveFile;
				}
				
				break;
				
				
			}		

		}
		
		Logger.logDEBUG("LuaFileLoader", "sortTokens", "Token", "No More Token in file");
		return luaDescriptiveFile;
	}
	
	
		
		
		
	
	
}
