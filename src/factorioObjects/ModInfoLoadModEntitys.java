package factorioObjects;

import java.io.File;
import java.util.ArrayList;

import luaFileLoader.LuaDescriptiveFile;
import luaFileLoader.LuaFileLoader;
import logger.Logger;

public class ModInfoLoadModEntitys {

	public static void loadModEntitys(ModInfo modInfo) {
		//Logger.logInfo("ModInfoLoadModEntitys", "loadModEntitys", "Loding now", modInfo.pfad);
		for(File file : listDir(modInfo.pfad + "\\" + modInfo.prototyps_)){
			loadModEntitys(modInfo, file.toString());
		}
	}	

	private static void loadModEntitys(ModInfo modInfo, String pfad) {
		//TEST
		//LuaDescriptiveFile test = 
		LuaFileLoader.loadLuaFile( pfad);
		/*
		//Logger.logInfo("loadModEntitys", "loadModEntitys", "Loding now", pfad);
		ArrayList<String> file = new ArrayList<String>();
		ArrayList<Integer> startlines = new ArrayList<Integer>();
		
		//Type start line's search
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pfad));
			String line;
			Integer linecount = 0;
			line = reader.readLine();
			while (line != null) {
				file.add(line);
				if(line.trim().startsWith("type")){
					//TODO Possible ERROR
					startlines.add(linecount);
				}
				linecount++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			//Logger.logErrorFatale("ModInfoLoadModEntitys", "loadEntityInfo", "IOException", "Load Mod Fiele not Found: " + pfad);
		}
		
		//Logger.logInfo("ModInfoLoadModEntitys", "Startlines", "Raw List", startlines.toString());
		//Logger.logAll("ModInfoLoadModEntitys", "Startlines", "Type List", modInfo.entityTypeList.keySet().toString());
		//Finde Ladbare Startlines
		//TODOO CHExk list
		for(int index = startlines.size() - 1; index >= 0; index--){
			String type = file.get(startlines.get(index)).split("=")[1].trim().replace("\",", "\"");
		//Logger.logAll("ModInfoLoadModEntitys", "Startlines", "Raw List", type);	
			HashMap<String, IPrototype> entityList = modInfo.entityTypeList.get(type);
			if(entityList == null){
				startlines.remove(index);
			}
		}	
		//Logger.logInfo("ModInfoLoadModEntitys", "Startlines", "Updated List", startlines.toString());
		//Alle Was Gelanden werden kann laden Laden
		startlines.add(file.size());
		for(int index = 0;  index < startlines.size() - 1; index++){
			String type = file.get(startlines.get(index)).split("=")[1].trim().replace("\",", "\"");
			modInfo.entityTypeList.get(type).get(modInfo.dummykey_).loadInfoFromMod(modInfo, startlines.get(index), startlines.get(index+1), file, pfad);
		}
		*/
		//Logger.logInfo("ModInfoLoadModEntitys", "loadModEntitys", "Ende", "Ende");
	}

	public static ArrayList<File> listDir(String dir) {
		return listDir(new File(dir));
	}

	//Verzeichnisse Rekusrive Auflisten
	private static ArrayList<File> listDir(File dir) {
		ArrayList<File> filelist = new ArrayList<File>();
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					//System.out.println("Found Directory: " + files[i] );
					filelist.addAll(listDir(files[i]));
				} else {
					filelist.add(files[i]);
					//System.out.println("Found File");
				}
			}
		}
		return filelist;
	}
}
