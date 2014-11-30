package startup;

import factorioObjects.ModInfo;
import gui.GuiEntityInfo;
import gui.GuiMenue;
import gui.GuiModInfo;
import gui.GuiModlist;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import logger.Logger;
import logger.Priority;

public class RunProgramm {

	public final long FIVE_S_IN_MS = 5000;
	public Logger logger;
	
	private String path;
	private File[] modList;
	
	private GuiMenue guiMenue;
	private GuiModlist guiModlist;
	private HashMap<String, ModInfo> modInfoList;
	private HashMap<String, GuiModInfo> guiModInfoList;
	private HashMap<String, GuiEntityInfo> guiEntityInfoList;
	
	public RunProgramm(){
		guiModInfoList = new HashMap<String, GuiModInfo>();
		modInfoList = new HashMap<String, ModInfo>();
		guiEntityInfoList = new HashMap<String, GuiEntityInfo>();
	}

	public String getPath() { return path; }
	public void setPath(String path) { this.path = path; }
	
	public File[] getModArray() { return modList; }
	public void setModArray(File[] modArray) { this.modList = modArray; }
	
	public void run() {
		setPath(getPath().replace("//", "////"));
		setPath(pfade.Pfade.MEINPFAD);
		Logger.init(path + "\\Factorio_Mod_Builder", Priority.JUSTSO, Priority.JUSTSO);
		oeffneGuiMenue();
		oeffneGuiModlist();
	}
	
	//GuiMangement
	public void oeffneGuiMenue(){
		if(guiMenue == null){ guiMenue = new GuiMenue(this); }
		guiMenue.setVisible(true);
	}
	
	public void schliesseGuiMenue(){
		if(guiMenue != null){ guiMenue.setVisible(false); }
	}
	
	public void oeffneGuiModlist(){
		if(guiModlist == null){ guiModlist = new GuiModlist(this); }
		guiModlist.setVisible(true);
		actuallisire_modordner();
	}
	
	public void schliesseGuiModInfo(){
		//TODO
	}
	
	public void oeffneGuiModInfo(ModInfo modInfo){
		if(guiModInfoList.get(modInfo.pfad) == null){ guiModInfoList.put(modInfo.pfad, new GuiModInfo(this, modInfo)); modInfo.loadInfo();}
		guiModInfoList.get(modInfo.pfad).setVisible(true);
	}
	
	public void schliesseGuiModlist(ModInfo modInfo){
		if(guiModInfoList.get(modInfo.pfad) != null){ guiModInfoList.get(modInfo.pfad).setVisible(false); }
	}
	
	public void oeffneGuiEntityInfo(ModInfo modInfo){
		if(guiEntityInfoList.get(modInfo.pfad) == null){ guiEntityInfoList.put(modInfo.pfad, new GuiEntityInfo(this, modInfo)); modInfo.loadInfo();}
		guiEntityInfoList.get(modInfo.pfad).setVisible(true);
	}
	
	public void schliesseGuiEntitylist(ModInfo modInfo){
		if(guiEntityInfoList.get(modInfo.pfad) != null){ guiEntityInfoList.get(modInfo.pfad).setVisible(false); }
	}
	
	//Close all
	public void closeAll() {
		if(guiMenue != null){ guiMenue.dispose(); }
		if(guiModlist != null){ guiModlist.dispose(); }
		
		for(String key : guiModInfoList.keySet()){
			guiModInfoList.get(key).dispose();
		}
		for(String key : guiEntityInfoList.keySet()){
			guiEntityInfoList.get(key).dispose();
		}
		Logger.closeFile();
	}
	
	
	/**
	 * Öffnet die Gui für die Mods
	 */
	public void actuallisire_modordner(){
		File file = new File(this.getPath());
		modList = file.listFiles();
		if(guiModlist != null){ guiModlist.aktuallisireListe(); }
	}

	/**
	 * Öffnet die Guis zu den Mod Ordnern
	 * @param selectedValuesList
	 */
	public void oeffneGuiModInfo(List<File> selectedValuesList) {
		for(File file : selectedValuesList){
			Logger.log(Priority.DEBUG,"RunProgramm", "oeffneGuiModInfo", "Test", "Constructor info next");
			ModInfo modInfo = new ModInfo(file.toString());
			Logger.log(Priority.DEBUG,"RunProgramm", "oeffneGuiModInfo", "Test", modInfo.entityTypeList.keySet().toString() + " " + (modInfo == null) + " " + (modInfo.equals(null)));
			modInfoList.put(modInfo.pfad, modInfo);
			oeffneGuiModInfo(modInfo);
		}
	}

	/**
	 * Laed fur das Gegebene ModInfo Object die Entitys
	 * @param modInfo
	 */
	public void openEntityList(ModInfo modInfo) {
		modInfo.loadEntityList();
		oeffneGuiEntityInfo(modInfo);
	}
	
	/**
	 * Laed fur das Gegebene ModInfo Object die Infos
	 * @param modInfo
	 */
	public void loadModInfo(ModInfo modInfo) {
		modInfo.loadInfo();
	}


}