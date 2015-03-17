package startup;

import factorioObjects.ModInfo;
import gui.GuiEntityInfo;
import gui.GuiMenue;
import gui.GuiModInfo;
import gui.GuiModlist;
import gui.GuiPathChoice;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logger.Logger;
import logger.Priority;

public class Controller {

	public final static long FIVE_S_IN_MS = 5000;
	

	private static File[] modList;
	
	private static GuiMenue guiMenue;
	private static GuiModlist guiModlist;
	//Paths for Mod-folder
	private static ArrayList<String> path;
	private static HashMap<String, ModInfo> modInfoList;
	private static HashMap<String, GuiModInfo> guiModInfoList;
	private static HashMap<String, GuiEntityInfo> guiEntityInfoList;
	
	private Controller(){
		Controller.path = new ArrayList<String>();
		Controller.guiModInfoList = new HashMap<String, GuiModInfo>();
		Controller.modInfoList = new HashMap<String, ModInfo>();
		Controller.guiEntityInfoList = new HashMap<String, GuiEntityInfo>();
	}
	
	public static void init(){
		new Controller();
	}
	
	public static void main(String[] args) {
		Controller.init();
		GuiPathChoice guiPathChoice = new GuiPathChoice();
		guiPathChoice.setVisible(true);
	}

	public static String getPath() { return Controller.path.get(0); }
	public static void addPath(String path) { Controller.path.add(path); }
	
	public static File[] getModArray() { return Controller.modList; }
	public static void setModArray(File[] modArray) { Controller.modList = modArray; }
	
	public static void run() {
		//setPath(getPath().replace("//", "////"));
		Controller.path.add(0, pfade.Pfade.MEINPFAD2);
		Logger.init(Controller.getPath(), Priority.INFO, Priority.JUSTSO);
		Controller.oeffneGuiMenue();
		Controller.oeffneGuiModlist();
		Logger.logINFO("Controler", "oeffneGuiMenue", "start", "end");
	}
	
	//GuiMangement
	public static void oeffneGuiMenue(){
		Logger.logINFO("Controler", "oeffneGuiMenue", "start", "start");
		if(guiMenue == null){ guiMenue = new GuiMenue(); }
		guiMenue.setVisible(true);
	}
	
	public void schliesseGuiMenue(){
		Logger.logINFO("Controler", "schliesseGuiMenue", "start", "start");
		if(guiMenue != null){ guiMenue.setVisible(false); }
	}
	
	public static void oeffneGuiModlist(){
		Logger.logINFO("Controler", "oeffneGuiModlist", "start", "start");
		if(guiModlist == null){ guiModlist = new GuiModlist(); }
		guiModlist.setVisible(true);
		actuallisire_modordner();
	}
	
	public void schliesseGuiModInfo(){
		Logger.logINFO("Controler", "schliesseGuiModInfo", "start", "start");
		//TODO
	}
	
	public static void oeffneGuiModInfo(ModInfo modInfo){
		Logger.logINFO("Controler", "oeffneGuiModInfo", "start", "start");
		if(guiModInfoList.get(modInfo.pfad) == null){ guiModInfoList.put(modInfo.pfad, new GuiModInfo(modInfo)); modInfo.loadInfo();}
		guiModInfoList.get(modInfo.pfad).setVisible(true);
	}
	
	public void schliesseGuiModlist(ModInfo modInfo){
		Logger.logINFO("Controler", "schliesseGuiModlist", "start", "start");
		if(guiModInfoList.get(modInfo.pfad) != null){ guiModInfoList.get(modInfo.pfad).setVisible(false); }
	}
	
	public static void oeffneGuiEntityInfo(ModInfo modInfo){
		Logger.logINFO("Controler", "oeffneGuiEntityInfo", "start", "start");
		if(guiEntityInfoList.get(modInfo.pfad) == null){ guiEntityInfoList.put(modInfo.pfad, new GuiEntityInfo(modInfo)); modInfo.loadInfo();}
		guiEntityInfoList.get(modInfo.pfad).setVisible(true);
	}
	
	public void schliesseGuiEntitylist(ModInfo modInfo){
		Logger.logINFO("Controler", "schliesseGuiEntitylist", "start", "start");
		if(guiEntityInfoList.get(modInfo.pfad) != null){ guiEntityInfoList.get(modInfo.pfad).setVisible(false); }
	}
	
	//Close all
	public static void closeAll() {
		Logger.logINFO("Controler", "closeAll", "start", "start");
		
		if(guiMenue != null){ guiMenue.dispose(); }
		if(guiModlist != null){ guiModlist.dispose(); }
		
		for(String key : guiModInfoList.keySet()){
			guiModInfoList.get(key).dispose();
		}
		
		for(String key : guiEntityInfoList.keySet()){
			guiEntityInfoList.get(key).dispose();
		}
		
		Logger.logINFO("Controler", "closeAll", "start", "end");
		Logger.closeFile();
	}
	
	
	/**
	 * Öffnet die Gui für die Mods
	 */
	public static void actuallisire_modordner(){
		Logger.logINFO("Controler", "actuallisire_modordner", "start", "start");
		File file = new File(Controller.getPath());
		modList = file.listFiles();
		if(guiModlist != null){ guiModlist.aktuallisireListe(); }
	}

	/**
	 * Öffnet die Guis zu den Mod Ordnern
	 * @param selectedValuesList
	 */
	public static void oeffneGuiModInfo(List<File> selectedValuesList) {
		Logger.logINFO("Controler", "oeffneGuiModInfo", "start", "start");
		for(File file : selectedValuesList){
			Logger.log(Priority.DEBUG,"Controler", "oeffneGuiModInfo", "Test", "Constructor info next");
			ModInfo modInfo = new ModInfo(file.toString());
			Logger.log(Priority.DEBUG,"Controler", "oeffneGuiModInfo", "Test", modInfo.entityTypeList.keySet().toString() + " " + (modInfo == null) + " " + (modInfo.equals(null)));
			modInfoList.put(modInfo.pfad, modInfo);
			oeffneGuiModInfo(modInfo);
		}
	}

	/**
	 * Laed fur das Gegebene ModInfo Object die Entitys
	 * @param modInfo
	 */
	public static void openEntityList(ModInfo modInfo) {
		Logger.logINFO("Controler", "openEntityList", "start", "start");
		modInfo.loadEntityList();
		Controller.oeffneGuiEntityInfo(modInfo);
		Logger.logINFO("Controler", "openEntityList", "start", "end");
	}
	
}