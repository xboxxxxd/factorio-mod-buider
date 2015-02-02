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
		
		while (! luaTokenLoader.eof()) {
		
		String token = luaTokenLoader.getToken();
		
			switch (luaFileLoaderState){
			
			case NOSTATE :
				if(token.equals("data")){
					if (luaTokenLoader.getToken().equals(":")) {
						if (luaTokenLoader.getToken().equals("extend")) {
							Logger.logDEBUG("LuaFileLoader", "sortTokens", "State", LuaFileLoaderState.DATAEXTENDED.toString());
							
							LuaDescriptiveFile luaDescriptiveFileNew = new LuaDescriptiveFile(LuaFileLoaderState.DATAEXTENDED.toString());
							luaDescriptiveFile.add(luaDescriptiveFileNew);
							LuaFileLoader.sortTokens(luaDescriptiveFileNew, luaTokenLoader, LuaFileLoaderState.DATAEXTENDED);
							break;
						}
					}
				}
				break;
				
			case DATAEXTENDED :
				
				
				break;
				
				
			}		

		}
		
		Logger.logDEBUG("LuaFileLoader", "sortTokens", "Token", "No More Token in file");
		return luaDescriptiveFile;
	}
	
	
		
		
		
	
	
}
