package luaFileLoader;

import java.io.FileReader;
import java.io.IOException;

import logger.Logger;
import logger.Priority;

public class LuaCharLoader {

	private char[] fileChars;
	private int pos;

	public LuaCharLoader(String file){
		Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "start", "start");
		
		pos = 0;
		
			try {
				
				FileReader fileReader = new FileReader(file);
				
				Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "Encoding:", fileReader.getEncoding());
				
				fileReader.read(fileChars);
				
				fileReader.close();
				
			} catch (IOException e) {
				Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "IOException:", "IOException:");
				e.printStackTrace();
			}
		
		Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "end", "end");
	}
	
	public String getLastChar(){
		return String.valueOf(fileChars[pos - 1]);
	}
	
	public void getSub() {
		pos--;	
	}
	
	public void getAdd(){
		pos++;
	}
	
	public String getChar(){
		return String.valueOf(fileChars[pos]);
	}
	
	public String getCharAdd(){
		pos++;
		return String.valueOf(fileChars[pos - 1]);
	}
	
	public String getNextChar(){
		if(fileChars.length >=  (pos + 1)){ return null; }
		return String.valueOf(fileChars[pos + 1]);
	}

	public boolean eof() {
		return (fileChars.length >=  pos);
	}
}
