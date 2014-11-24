package factorioObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logger.Logger;

public class ModInfo {

	public String pfad;
	public String filename = "info.json";
	private int nummber;

	public String prototyps_ = "prototypes";
	public String dummykey_ = "dummykey";
	
	public String name_ = "\"name\"";
	public String version_ = "\"version\"";
	public String title_ = "\"title\"";
	public String author_ = "\"author\"";
	public String contact_ = "\"contact\"";
	public String homepage_ = "\"homepage\"";
	public String description_ = "\"description\"";
	public String dependencies_ = "\"dependencies\"";
	
	public Map<String, HashMap<String, IPrototype>> entityTypeList;
	public HashMap<String, String> infoParts;
	public ArrayList<String> posibleEntityInfo;
	
	public ModInfo(String pfad) {
		Logger.logJustSo("ModInfo", "ModInfo", "Constructor", "Start");
		this.pfad = pfad;
		this.nummber = -1;
		this.infoParts = new HashMap<String, String>();
		this.posibleEntityInfo = new ArrayList<String>();
		this.posibleEntityInfo.add(name_);
		this.posibleEntityInfo.add(version_);
		this.posibleEntityInfo.add(title_);
		this.posibleEntityInfo.add(author_);
		this.posibleEntityInfo.add(contact_);
		this.posibleEntityInfo.add(homepage_);
		this.posibleEntityInfo.add(description_);
		this.posibleEntityInfo.add(dependencies_);
		Logger.logJustSo("ModInfo", "ModInfo", "Register Types", "Start");
		this.entityTypeList = new HashMap<String, HashMap<String, IPrototype>>();
		registerType(entityTypeList, new Module(null));
		registerType(entityTypeList, new Item(null));
		Logger.logJustSo("ModInfo", "ModInfo", "Register Types End", entityTypeList.keySet().toString());
	}
	
	public void loadInfo() {
		ModInfoLoad.loadInfo(this, pfad, filename);
	}

	public void saveInfo() {
		//TODO
	}

	public String[] listInfo() {
		ArrayList<String> listData = new ArrayList<String>();
		
		listData.add(name_ + ": \"" + this.infoParts.get(name_) + "\"");
		listData.add(version_ + ": \"" + this.infoParts.get(version_) + "\"");
		listData.add(title_ + ": \"" + this.infoParts.get(title_) + "\"");
		listData.add(author_ + ": \"" + this.infoParts.get(author_) + "\"");
		listData.add(contact_ + ": \"" + this.infoParts.get(contact_) + "\"");
		listData.add(homepage_ + ": \"" + this.infoParts.get(homepage_) + "\"");
		listData.add(description_ + ": \"" + this.infoParts.get(description_) + "\"");
		listData.add(dependencies_ + ": \"" + this.infoParts.get(dependencies_) + "\"");
		
		return listData.toArray(new String[0]);
	}

	public void loadEntityList() {
		ModInfoLoadModEntitys.loadModEntitys(this);
	}

	public String requestNumber() {
		nummber++;
		return new Integer(nummber).toString();
	}

	public String[] getDiffrintModTypes() {
		ArrayList<String> diffrentModTypes = new ArrayList<String>(entityTypeList.keySet());
		ArrayList<IPrototype> diffrentModTypeDummys = new ArrayList<IPrototype>();
		for(String key : diffrentModTypes){
			diffrentModTypeDummys.add(entityTypeList.get(key).get(dummykey_));
		}
		/*for(IEntity dummyType : diffrentModTypeDummys){
			
			System.out.println(dummyType == null);
			System.out.println(dummyType.sortorder);
			System.out.println(dummyType.entityInfo.get(Entity.type));
			System.out.println(dummyType.entityInfo.get(Entity.name));
			Logger.logAll("ModInfo", "getDiffrintModTypes", "Sort", dummyType.sortorder + ":" + dummyType.entityInfo.get(Entity.type) + ":" +  dummyType.entityInfo.get(Entity.name));
			
		}*/
		
		//Collections.sort(diffrentModTypeDummys); TODO
		
		diffrentModTypes = new ArrayList<String>();
		for(int i = 0; i < diffrentModTypeDummys.size(); i++){
			diffrentModTypes.add(diffrentModTypeDummys.get(i).getType_());
		}
		
		return diffrentModTypes.toArray(new String[0]);
	}

	public String[] getEntitysFromType(List<String> list) {
		ArrayList<String> selectedModEntitys = new ArrayList<String>();
		ArrayList<IPrototype> modEntitys = new ArrayList<IPrototype>();
		for(int index = 0; index < list.size(); index++){
			
			modEntitys.clear();
			modEntitys.addAll(this.entityTypeList.get(list.get(index)).values());
			modEntitys.remove(this.entityTypeList.get(list.get(index)).get(dummykey_));
			for(IPrototype iEntity : modEntitys){
				Logger.logAll("ModInfo", "getEntitysFromType", "Load from Module", iEntity.toString());
				selectedModEntitys.add(iEntity.toString());
			}
		}
		Collections.sort(selectedModEntitys);
		return selectedModEntitys.toArray(new String[0]);
	}

	public void registerType(Map<String, HashMap<String, IPrototype>> entityTypeList_, IPrototype dummy){
		Logger.logAll("ModInfo", "registerType", "Usabal Data", dummy.getType_() + " : " + (dummy == null) + " : " + dummy.getType() + " : " + dummy.getName());
		entityTypeList_.put(dummy.getType_(), new HashMap<String, IPrototype>());
		entityTypeList_.get(dummy.getType_()).put(dummykey_, dummy);
		dummy.getEntityInfo().put(dummy.getType(), dummy.getType_());
		dummy.getEntityInfo().put(dummy.getName(), dummykey_);
	}
	
	
}
