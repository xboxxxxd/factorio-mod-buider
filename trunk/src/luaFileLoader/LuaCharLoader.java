package luaFileLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import logger.Logger;
import logger.Priority;

public class LuaCharLoader {

	private String file;
	private char[] charfile;
	private int pos;
	private int posSec;
	
	
	public LuaCharLoader( String file ) {
		
		Logger.log( Priority.INFO, "LuaCharLoader", "LuaCharLoader", "start", "start" );
		
		this.file = file;
		this.pos = -1;
		this.posSec = -1;
		
			try {
				
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				ArrayList<Character> charlist = new ArrayList<Character>();

				Logger.log( Priority.JUSTSO, "LuaCharLoader", "LuaCharLoader", "Encoding:", fileReader.getEncoding() );

				String line = bufferedReader.readLine();
				
				while (line != null) {
					for( char schar : line.toCharArray() ) {
						charlist.add(schar);
					}
					line = bufferedReader.readLine();
				}
				
				int size = charlist.size();
				charfile = new char[size];
		
				int i = 0;
				for( char schar : charlist ) {
					charfile[i] = schar;
					i++;
				}
				
				fileReader.close();
				
			} catch (IOException e) {
				Logger.log( Priority.INFO, "LuaCharLoader", "LuaCharLoader", "IOException:", "IOException:" );
				e.printStackTrace();
			}
			
		Logger.log( Priority.INFO, "LuaCharLoader", "LuaCharLoader", "Length", String.valueOf(charfile.length));
		Logger.log( Priority.INFO, "LuaCharLoader", "LuaCharLoader", "end", "end");
	}
	
	
	public String getChar() {
		if( ! isVallidPos( this.pos ) ) {
			Logger.log( Priority.ERRORCODE, "LuaCharLoader", "getChar", "Empty File:", file );
			return "";
		}
		return String.valueOf( this.charfile[pos] );
	}
	
	public String getSecChar() {
		if( ! isVallidPos( this.posSec ) ) {
			Logger.log( Priority.ERRORCODE, "LuaCharLoader", "getSecChar", "Empty File:", file );
			return "";
		}
		return String.valueOf( this.charfile[posSec] );
	}
	
	public boolean setSecChar( int offset ) {
		if( this.isVallidPos( this.pos + offset ) ) {
			this.posSec = this.pos + offset;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setCharSub() {
		if( this.isVallidPos( this.pos - 1 ) ) {
			this.pos = this.pos - 1;
			return true;
		} else {
			this.pos = -1;
			return false;
		}
	}
	
	public boolean setCharAdd() {
		if( this.isVallidPos( this.pos + 1 ) ) {
			this.pos = this.pos + 1;
			Logger.log( Priority.ALL, "LuaCharLoader", "setCharAdd", "Number true", "" + ( this.pos) );
			return true;
		} else {
			this.pos = charfile.length;
			Logger.log( Priority.ALL, "LuaCharLoader", "setCharAdd", "Number false", "" + ( this.pos) );
			return false;
		}
	}
	
	private boolean isVallidPos(int posTemp) {
		if( ( posTemp >= 0 ) && ( posTemp < ( this.charfile.length) ) ) {
			return true;
		} else {
			Logger.log( Priority.ALL, "LuaCharLoader", "isVallidPos", "" + false, "" + posTemp );
			return false;
		}
	}
	
	public boolean eof() {
		return ( pos >= charfile.length );
	}
}
