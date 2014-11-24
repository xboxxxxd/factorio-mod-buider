package luaFileLoader;

import static luaFileLoader.ScanState.*;
import static luaFileLoader.LuaDescriptiveFileStats.BODDY;
import static luaFileLoader.LuaDescriptiveFileStats.ERROR_CODE;
import static luaFileLoader.LuaDescriptiveFileStats.ERROR_MOD;
import static luaFileLoader.LuaDescriptiveFileStats.ERROR_MOD_REPET;
import static luaFileLoader.LuaDescriptiveFileStats.FIRST_LINE_FINISH;
import static luaFileLoader.LuaDescriptiveFileStats.FIRST_LINE_WAIT;
import static luaFileLoader.LuaDescriptiveFileStats.INIT;
import static luaFileLoader.LuaDescriptiveFileStats.INIT_READY;
import static luaFileLoader.LuaDescriptiveFileStats.LAST_LINE_FINISH;
import static luaFileLoader.LuaDescriptiveFileStats.LAST_LINE_WAIT;
import static luaFileLoader.LuaDescriptiveFileStats.WAIT_FOR_GKA;
import static luaFileLoader.LuaDescriptiveFileStats.WAIT_FOR_GKZ;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import logger.Logger;

public class LuaDescriptiveFile {
	private String file;
	private LuaDescriptiveFileStats redStat; 
	private HashMap<String, LuaDescriptiveObjekt> dataMap;
	private ArrayList<String> tokens;
	private boolean isString;
	private FileReader fileToRead;
	
	//IMORTANT STRINGS
	private String charString = "";
	private final static String FIRST_LINE = "data:extend({";
	private final static String LAST_LINE = "})";
	private final static String GKA = "}";
	private final static String GKZ = "{";
	
	public LuaDescriptiveFile(String file) {
		this.redStat = INIT;
		this.dataMap = new HashMap<String, LuaDescriptiveObjekt>();
		this.setFile(file);
		this.tokens = new ArrayList<String>();
		this.isString = false;
		this.redStat = INIT_READY;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	public void setFileReader(FileReader fileToRead) {
		this.fileToRead = fileToRead;
		
	}
	
	public boolean generateTokens() throws IOException {
		if(fileToRead == null){
			Logger.logErrorFatale("LuaDescriptiveFile", "generateTokens", "fileToRead is null", redStat.toString());
			redStat = ERROR_CODE;
			return false;
		}
		
		//initialize Reader and Over Var's
		int[] actChar = new int[1];
		actChar[0] = 0;
		ScanState state = OTHER;
		String single1 = "";
		String single2 = "";
		String collector = "";
		
		while (actChar[0] != -1) {	
			actChar[0] = fileToRead.read();
			single2 = single1;
			single1 = new String(actChar, 0, 1);
			
			//Need to Filter for following symbols to divide Tokens: .:(){}[]=
			
			//Look if command ends by newline
			
			//Look if  command ends by Newline
			if(state == COMMENT && (single1.equals("\r")  || single1.equals("\n"))) { tokens.add(collector );  collector = "";}
			//Look if a command Starts by --
			else if(state == OTHER && single1.equals("-") && single2.equals("-")){ tokens.remove(tokens.size() - 1); state = COMMENT;  tokens.add(collector );}
			//Look if long command ends by: ]]
			else if(state == LONGCOMMAND && single1.equals("]") && single2.equals("]")) { state = OTHER;  tokens.add(collector + "]");  collector = "";}
			//Look if long command Starts by: --[[
			else if(state == COMMENT && collector.equals("--[[")) { state = LONGCOMMAND; }
			//Then The String Ends: Add the Sting to The TokenList And reset the Collector
			else if(state == STRING && single1.equals("\"")){ tokens.add(collector + single1); collector = ""; single1 = ""; }
			//Then the String Starts toggle to String
			else if(state == OTHER && single1.equals("\"") ){ state = STRING; }
			
			switch (state){
			case COMMENT :
				collector = collector + single1;
				break;
				
			case LONGCOMMAND :
				break;
				
			case STRING :
				collector = collector + single1;
				break;
				
			case MODLODERCOMMENT :
				break;
				
			case OTHER :
				//Need to Filter for following symbols to divide Tokens: .:(){}[]=Zeilenumbruch Wheitspace
				//if(Character.isWhitespace(single1.charAt(0) || single1.equals(anObject))
				break;
				
			default:
				Logger.logErrorFatale("LuaDescriptiveFile", "generateTokens", "EnumNotRight", state.toString());
			}
			
			
		}
		return false;
	}
	
	/**
	 * Method to Parse Next Char to Token
	 * @param string A String of a single Character
	 */
	public void putCharToToken(String string) {
		//Need to filter for Strings to catch Whitspaces
		//Need to Filter for following symbols to divide Tokens: .:(){}[]=
		
		
		if(isString){
				
		} else{
			
		}
	}
		
	public void putPrototypeFile(String string) {
	
		switch (redStat) {
		case INIT_READY :
			redStat = FIRST_LINE_WAIT;
			this.firstLine(string);
			break;
		case FIRST_LINE_WAIT :
			this.firstLine(string);
			break;
		case FIRST_LINE_FINISH :
			redStat = WAIT_FOR_GKA;
			this.body(string);
			break;
		case LAST_LINE_WAIT :
			this.lastLine(string);
			break;
		case ERROR_MOD :
			Logger.logErrorFatale("LuaDescriptiveFile", "put", "UnnoneChareCase", redStat.toString());
			redStat = ERROR_MOD_REPET;
			break;
		case ERROR_MOD_REPET :
			break;
		default:
			Logger.logErrorFatale("LuaDescriptiveFile", "put", "EnumNotRight", redStat.toString());
			redStat = ERROR_MOD_REPET;
		}
		
	}

	/**
	 * Method to accept the First Line: data:extend({
	 * @param string
	 */
	private void firstLine(String string) {
		charString = charString.concat(string);
		if(charString.length() >= FIRST_LINE.length()){
			switch (charString) {
			case FIRST_LINE :
				Logger.logJustSo("LuaDescriptiveFile", "firstLine", "FIRST_LINE_FINISH", ":" + charString + ":");
				redStat = FIRST_LINE_FINISH;
				break;
			default:
				Logger.logErrorMod("LuaDescriptiveFile", "firstLine", "Line not Right", ":" + charString + ":");
				redStat = ERROR_MOD;
			}
			charString = "";
		}
	}
	
	private void body(String string) {
		switch (redStat) {
		case WAIT_FOR_GKA :
			switch (string) {
			case GKA :
				redStat = BODDY;
				break;
			default :
				redStat = LAST_LINE_WAIT;
			} 
		case BODDY :
		case WAIT_FOR_GKZ :
		default:
			Logger.logErrorFatale("LuaDescriptiveFile", "body", "EnumNotRight", redStat.toString());
			redStat = ERROR_MOD_REPET;
		}
		
		
		charString = charString.concat(string);
		if(redStat != WAIT_FOR_GKZ){  }
	}
	
	/**
	 * Method to accept the Last Line: })
	 * @param string
	 */
	private void lastLine(String string) {
		charString = charString.concat(string);
		if(charString.length() >= LAST_LINE.length()){
			switch (charString) {
			case FIRST_LINE :
				redStat = LAST_LINE_FINISH;
				Logger.logJustSo("LuaDescriptiveFile", "lastLine", "LAST_LINE_FINISH", redStat.toString());
				break;
			default:
				redStat = ERROR_MOD;
				Logger.logErrorMod("LuaDescriptiveFile", "lastLine", "Line not Right", charString);
			}
			charString = "";
		}
	}


}
