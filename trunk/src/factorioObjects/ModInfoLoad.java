package factorioObjects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import logger.Logger;

public class ModInfoLoad {

	public static void loadInfo(ModInfo modInfo, String pfad, String filename){
		ArrayList<String> file = new ArrayList<String>();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pfad + "\\" + filename));
			line = reader.readLine();
			while (line != null) {
				file.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Load Mod Fiele not Found: " + pfad + "\\" + filename );
		}
		
		String[] infoline;
		for(String text : file){
			infoline = text.split(":", 2);
			if(infoline.length == 2 && modInfo.posibleEntityInfo.contains(infoline[0].trim())){
				modInfo.infoParts.put(infoline[0].trim(), infoline[1].replace("\",", "").trim());
			} else if(infoline.length == 1 && (infoline[0].trim().equals("}") || infoline[0].trim().equals("{") || infoline[0].trim().equals(""))){
			} else {
				//Logger.logErrorMod("ModInfoLoad", "loadInfo", "InfoFiel Line konnte nicht gelesen werden", text);
			}
		}
	}
}
