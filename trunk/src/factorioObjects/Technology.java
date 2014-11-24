package factorioObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class Technology extends Prototype{

	public String sortorder = "Technology";
	public String type_ = "\"technology\"";
	public String effects = "effects";
	public String type_art = "type";
	public String prerequisites = "prerequisites";
	public String unit = "unit";
	public String count = "count";
	public String ingredients = "ingredients";
	public String time = "time";
	public String recipe = "recipe";
	public String upgrade = "upgrade";
	
	public static ArrayList<String> effectTypes = new ArrayList<String>();
	
	
	
	public Technology(String path) {
		super(path);
		
		this.needsAdvanceProcessing.put(effects, true);
		
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
	public void loadInfoFromMod(ModInfo modInfo, int startIndex, int endIndex,
			ArrayList<String> file, String pfad) {
		// TODO Auto-generated method stub
		
	}

}
