package factorioObjects;

import java.util.ArrayList;
import java.util.HashMap;

import logger.Logger;

public abstract class Prototype implements IPrototype{

	public String sortorder = "Shoud never be displaed";
	public String type_ = "Shoud never be displaed";
	public String type = "type";
	public String name = "name";
	public String icon = "icon";
	public String order = "order";
	
	public String pfad;
	
	
	protected ArrayList<String> neededEntityInfo;
	protected HashMap<String, Boolean> needsAdvanceProcessing;
	public HashMap<String, String> entityInfo;
	
	public Prototype(String path){
		this.pfad = path;
		this.entityInfo = new HashMap<String, String>();
		this.neededEntityInfo = new ArrayList<String>();
		this.neededEntityInfo.add(type);
		this.neededEntityInfo.add(name);
		this.needsAdvanceProcessing = new HashMap<String, Boolean>();
		this.needsAdvanceProcessing.put(type, false);
		this.needsAdvanceProcessing.put(name, false);
		this.needsAdvanceProcessing.put(icon, false);
		this.needsAdvanceProcessing.put(order, false);
	}
	
	@Override
	public String getType(){
		return this.type;
	}
	@Override
	public String getName(){
		return this.name;
	}
	public boolean controlEntitySetting(ModInfo modInfo){
		if(this.entityInfo.get(name) == null){
			this.entityInfo.put(name, modInfo.requestNumber());
		}
		Logger.logJustSo("Entity", "controlEntitySetting", "Needed List", neededEntityInfo.toString());
		boolean isOK = true;
		for(String val : neededEntityInfo){
			if(this.entityInfo.get(val) == null){
				Logger.logErrorMod("Entity", "controlEntitySetting", "Missing Info", this.entityInfo.get(name) + " : "+ val);
				isOK = false;
			}
		}
		return isOK;
	}

	protected void loadInfo(ModInfo modInfo, IPrototype modul, int startIndex, int endIndex, ArrayList<String> file, String pfad) {
		Logger.logAll("Entity", "SearchRange", "This Range", startIndex + " - " + endIndex);
		Logger.logInfo("Entity", "loadInfo", "I can Load", modul.getNeedsAdvanceProcessing().keySet().toString());
		for (int index = startIndex; index < endIndex; index++) {
			String[] line = file.get(index).split("=", 2);
			if (line.length < 2) {
				Logger.logAll("Entity", "Splitting for Search", "ERROR to short", file.get(index));
			} else {
				String atributArt = line[0].trim();
				String atributValue = line[1].trim();
				Logger.logAll("Entity", "Splitting for Search", "Found Good Line " + atributArt, file.get(index));
				
				//Atribut Exestiert und ist Nicht Avanced
				
				if(modul.getNeedsAdvanceProcessing().containsKey(atributArt) && modul.getNeedsAdvanceProcessing().get(atributArt) == false){
					Logger.logAll("Entity", "Splitting for Search", "Found ease Line", ":"+ atributArt + ":" );
					
					if(atributValue.endsWith(",")){ atributValue = (String)atributValue.subSequence(0, atributValue.length()-2); }				
					modul.getEntityInfo().put(atributArt, atributValue);
					//modInfo.entityTypeList.get(Module.type).put(name, Module.dummy);
					
					//Atribut Exestiert und ist Avanced	
				} else if(modul.getNeedsAdvanceProcessing().containsKey(atributArt) && modul.getNeedsAdvanceProcessing().get(atributArt) == true){
					Logger.logAll("Entity", "Line Type for Search", "Line Type is Complex", ":"+ atributArt + ":" + atributValue + ":");
					advanceProcessingfromLine(modInfo, modul, atributArt, atributValue);
					
					//Atribut Exestiert nicht
				} else {
					Logger.logJustSo("Entity", "Line Type for Search", "Line Type not found", ":"+ atributArt + ":");
				}
			}
		}

		Logger.logAll("Entity", "Serch Finished", "Evrifing i can", modul.getNeedsAdvanceProcessing().toString());
		Logger.logAll("Entity", "Serch Finished", "Evrifing i got", modul.getEntityInfo().toString());
		this.controlEntitySetting(modInfo);
	}
	
	public boolean advanceProcessingfromLine(ModInfo modInfo, IPrototype modul, String atributArt, String atributValue) {
		Logger.logErrorCode("Entity", "AdvancedProcessing", "Type not availabal", ":"+ atributArt + ":");
		return false;
	}
	
	//@Override
	public int compareTo(IPrototype entity) {
		Logger.logInfo("Entity", "compareTo", "Was is da", this.toString() + " : " + entity.toString());
		if(this.sortorder.compareTo(entity.getSortorder()) == 0 && this.entityInfo != null && entity.getEntityInfo() != null && this.entityInfo.get(name) != null && entity.getEntityInfo().get(name) != null){
			return this.entityInfo.get(name).compareTo(entity.getEntityInfo().get(name));
		} else {
			return this.sortorder.compareTo(entity.getSortorder());
		}
	}
	
	public String toString(){
		return this.entityInfo.get(type) + " : " + this.entityInfo.get(name);
	}
}
