package factorioObjects;

import java.util.ArrayList;
import java.util.HashMap;

public interface IPrototype {// extends  Comparable<IEntity>{
	
	public String getType_();
	public String getType();
	public String getSortorder();
	public String getName();
	public ArrayList<String> getNeededEntityInfo();
	public HashMap<String, Boolean> getNeedsAdvanceProcessing();
	public HashMap<String, String> getEntityInfo();
	
	public void loadInfoFromMod(ModInfo modInfo, int startIndex, int endIndex, ArrayList<String> file, String pfad);
}