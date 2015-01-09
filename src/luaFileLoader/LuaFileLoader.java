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
		
		while (!luaTokenLoader.eof()) {
			Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "Token", luaTokenLoader.getToken());
		}
		
		Logger.logDEBUG("LuaFileLoader", "loadLuaFile", "start", "end");
	}
	
	
	public static LuaDescriptiveFile sortTokens(LuaDescriptiveFile luaDescriptiveFile, LuaTokenLoader luaTokenLoader){
		
		
		return luaDescriptiveFile;
	}
	
	
	
	
}
