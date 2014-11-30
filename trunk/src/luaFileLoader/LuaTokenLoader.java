package luaFileLoader;

import static luaFileLoader.ScanState.COMMENT;
import static luaFileLoader.ScanState.LONGCOMMAND;
import static luaFileLoader.ScanState.OTHER;
import static luaFileLoader.ScanState.STRING;
import logger.Logger;
import logger.Priority;

public class LuaTokenLoader {

	private LuaCharLoader luaCharLoader;
	
	public LuaTokenLoader(LuaCharLoader luaCharLoader){
		Logger.log(Priority.INFO, "LuaTokenLoader", "LuaTokenLoader", "start", "start");
		this.luaCharLoader = luaCharLoader;
		Logger.log(Priority.INFO, "LuaTokenLoader", "LuaTokenLoader", "end", "end");
	}
	
	public boolean eof(){
		return luaCharLoader.eof();
	}
	
	public String getToken(){
		
		boolean wait_for_token = true;
		String single = null;
		String collector = "";
		ScanState state = OTHER;
		
		while (wait_for_token) {
			
			if(luaCharLoader.eof()){ return collector; }
			
			single = luaCharLoader.getCharAdd();

			switch (state){
			
			case OTHER :
				//Look for the beginning of a String by "
				if(state == OTHER && single.equals("\"") ){ 
					state = STRING; collector = collector.concat(single); 
					break;
				} else
				//Look for the beginning of a Command by --
				if(single.equals("-") && luaCharLoader.getNextChar().equals("-")){ 
					state = COMMENT; collector = collector.concat(single); 
					break;
				} else
				//lock for a whitespace
				if( single.equals("\t") || single.equals("\b") || single.equals("\n") || 
					single.equals("\r") || single.equals("\f")) {	
					break;
				} else
				//Look for a next token Charater	
				if(	single.equals(".") || single.equals(":") || single.equals("(") || single.equals(")") || 
					single.equals("{") || single.equals("}") || single.equals("[") || single.equals("]") ||
					single.equals("+") || single.equals("-") || single.equals("*") || single.equals("/") || 
					single.equals("=") || single.equals(",") || single.equals(";")) {
					if(collector.isEmpty()){
						return single;
					} else {
						luaCharLoader.getSub();
						return collector;
					}
				}
				collector = collector + single;
				break;
				
			case COMMENT :
				
				//Look if  command ends by Newline
				if((single.equals("\r")  || single.equals("\n"))) { 
					return collector ; 
				} else
				//Look if long command Starts by: --[[
				if(single.equals("[") && collector.equals("--[")) { 
					state = LONGCOMMAND; 
				}
				
				collector = collector + single;
				break;
				
			case LONGCOMMAND :
				
				////Look if Longcommand ends by "
				if(single.equals("]") && luaCharLoader.getLastChar().equals("]")) { 
					return collector.concat(single); 
				}
				
				collector = collector + single;
				break;
				
			case STRING :
			
				//Look if String ends by "
				if(state == STRING && single.equals("\"")){ 
					return collector.concat(single);
				}
				
				collector = collector + single;
				break;
				
			case MODLODERCOMMENT :
				break;
				
			default:
				Logger.log(Priority.ERRORCODE, "LuaTokenLoader", "getToken", "Enumtype not known", state.toString());
			}
		}
		Logger.log(Priority.ERRORFATALE, "LuaTokenLoader", "getToken", "ERROR", "Something went terrible worng");
		throw new RuntimeException("LuaTokenLoader getToken ERROR Something went terrible worng");
	}
	
	
	
	
	
	
	
	
}