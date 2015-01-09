package luaFileLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import logger.Logger;
import logger.Priority;

public class LuaCharLoader {

	private char[] charfile;
	private int pos;

	public LuaCharLoader(String file){
		
		Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "start", "start");
		
		pos = 0;
		
			try {
				
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				ArrayList<Character> charlist = new ArrayList<Character>();

				//Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "Encoding:", fileReader.getEncoding());

				String line = bufferedReader.readLine();
				
				while (line != null) {
					for(char schar : line.toCharArray()){
						charlist.add(schar);
					}
					line = bufferedReader.readLine();
				}
				
				int size = charlist.size();
				charfile = new char[size];
		
				int i = 0;
				for(char schar : charlist){
					charfile[i] = schar;
					i++;
				}
				
				fileReader.close();
				
			} catch (IOException e) {
				Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "IOException:", "IOException:");
				e.printStackTrace();
			}
			
		Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "Length", String.valueOf(charfile.length));
		Logger.log(Priority.INFO, "LuaCharLoader", "LuaCharLoader", "end", "end");
	}
	
	public String getLastChar(){
		return String.valueOf(charfile[pos - 1]);
	}
	
	public void getSub() {
		pos--;	
	}
	
	public void getAdd(){
		pos++;
	}
	
	public String getChar(){
		return String.valueOf(charfile[pos]);
	}
	
	public String getCharAdd(){
		pos++;
		return String.valueOf(charfile[pos - 1]);
	}
	
	public String getNextChar(){
		if(charfile.length <=  (pos + 1)){ return null; }
		return String.valueOf(charfile[pos + 1]);
	}

	public boolean eof() {
		return (charfile.length <=  pos);
	}
}
