package factorioObjects;

import java.util.ArrayList;
import java.util.HashMap;

import logger.Logger;

public class Module extends AbstractItem {

	public String sortorder = "Module";
	public String type_ = "\"module\"";
	public String effect = "effect";
	public String bonus = "bonus";
	public String productivity = "productivity";
	public String consumption = "consumption";
	public String speed = "speed";
	public String pollution = "pollution";
	public String limitation = "limitation";
	public String limitation_message_key = "limitation_message_key";
	
	public Module(String path) {
		super(path);
		this.needsAdvanceProcessing.put(effect, true);
		this.needsAdvanceProcessing.put(bonus, true);
		this.needsAdvanceProcessing.put(productivity, true);
		this.needsAdvanceProcessing.put(consumption, true);
		this.needsAdvanceProcessing.put(speed, true);
		this.needsAdvanceProcessing.put(pollution, true);
	}
	@Override
	public String getSortorder(){
		return this.sortorder;
	}
	@Override
	public String getType_(){
		return this.type_;
	}
	@Override
	public ArrayList<String> getNeededEntityInfo() {
		return neededEntityInfo;
	}
	@Override
	public HashMap<String, Boolean> getNeedsAdvanceProcessing() {
		return needsAdvanceProcessing;
	}
	@Override
	public HashMap<String, String> getEntityInfo(){
		return this.entityInfo;
	}
	@Override
	public void loadInfoFromMod(ModInfo modInfo, int startIndex, int endIndex,ArrayList<String> file, String pfad) {
		//Logger.logInfo("Module", "loadInfoFromMod", "FoundModule", startIndex + " - " + endIndex);
		Module modul = new Module(pfad);
		modul.loadInfo(modInfo, modul, startIndex, endIndex, file, pfad);
		modInfo.entityTypeList.get(type_).put(modul.entityInfo.get(name), modul);
		//Logger.logInfo("Module", "loadInfoFromMod", "LoadedModule", modInfo.entityTypeList.get(type_).keySet().toString());
	}
	public boolean advanceProcessingfromLine(ModInfo modInfo, Prototype entity, String atributArt, String atributValue){
		//Logger.logErrorMod("Module", "AdvancedProcessing", "try process", ":"+ atributValue);
		if(atributArt.equals(effect)){
			String[] spliteffect = atributValue.replace("{", "").replace("}", "").split(",");
			String[] splitbonus;
			for(int i = 0; i < spliteffect.length; i++){
				splitbonus = spliteffect[i].trim().split("=");
				if(splitbonus.length == 3 && needsAdvanceProcessing.containsKey(splitbonus[0].trim())){
					entity.entityInfo.put(splitbonus[0].trim(), splitbonus[2].trim());
				}else{
					//Logger.logErrorMod("Module", "AdvancedProcessing", "coud not split spliteffect", ":"+ atributValue);
				}
			}
			return true;
		}
			return super.advanceProcessingfromLine(modInfo, entity, atributArt, atributValue);
	}
}

