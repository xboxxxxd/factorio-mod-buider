package luaFileLoader;

import java.io.FileReader;
import java.io.IOException;

import logger.Logger;

public class LuaFileLoader {

	/**Method to load a Factorio Lua File
	 * 
	 * @param file the path of the Files
	 */
	public static LuaDescriptiveFile loadLuaFile(String file) {
		Logger.logInfo("LuaFileLoader", "loadLuaFile", "Loding now", file);
		LuaDescriptiveFile desFile = new LuaDescriptiveFile(file);
		
		
		try {
			
			FileReader fileReader = new FileReader(file);
			
			desFile.setFileReader(new FileReader(file));
			if(desFile.generateTokens()){
				Logger.logInfo("LuaFileLoader", "loadLuaFile", "generateTokens Ended", "Was Successful");
			} else {
				Logger.logInfo("LuaFileLoader", "loadLuaFile", "generateTokens Ended", "Was Unsuccessful");
			}
			
		
			fileReader.close();
			
		} catch (IOException e) {
			Logger.logErrorFatale("LuaFileLoader", "loadLuaFile", "IOException", "Loading of Mod File has fron an Error: " + file);
		} finally {
			Logger.logInfo("LuaFileLoader", "loadLuaFile", "Loding ended", file);
		}
		Logger.logErrorFatale("LuaFileLoader", "loadLuaFile", "Funktion End", file);
		return desFile;
	}
	
}
