package factorioObjects;

import java.util.ArrayList;
import java.util.HashMap;

import logger.Logger;

public class Item extends Prototype{

	public String sortorder = "Item";
	public String type_ = "\"item\"";
	public Item(String path) {
		super(path);
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
	public void loadInfoFromMod(ModInfo modInfo, int startIndex, int endIndex, ArrayList<String> file, String pfad) {
		Logger.logInfo("Item", "loadInfoFromMod", "FoundItem", startIndex + " - " + endIndex);
		Item modul = new Item(pfad);
		modul.loadInfo(modInfo, modul, startIndex, endIndex, file, pfad);
		modInfo.entityTypeList.get(type_).put(modul.entityInfo.get(name), modul);
		Logger.logInfo("Item", "loadInfoFromMod", "LoadedItem", modInfo.entityTypeList.get(type_).keySet().toString());
	}
}
