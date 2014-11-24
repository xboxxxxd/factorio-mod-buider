package factorioObjects;

import java.util.ArrayList;

public abstract class AbstractItem extends Prototype{

	
	public String sortorder = "Shoud never be displaed";
	public String type_ = "Shoud never be displaed";
	public String flags = "flags";
	public String subgroup = "subgroup";
	public String stack_size = "stack_size";
	
	public AbstractItem(String path) {
		super(path);
		this.needsAdvanceProcessing.put(flags, true);
		this.needsAdvanceProcessing.put(subgroup, false);
		this.needsAdvanceProcessing.put(stack_size, false);
	}

	@Override
	public void loadInfoFromMod(ModInfo modInfo, int startIndex, int endIndex,
			ArrayList<String> file, String pfad) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean advanceProcessingfromLine(ModInfo modInfo, Prototype entity, String atributArt, String atributValue) {
		if(atributArt.equals(flags)){
			entity.entityInfo.put(atributArt, atributValue.replace("{", "").replace("}", ""));
			return true;
		} else {
			return super.advanceProcessingfromLine(modInfo, entity, atributArt, atributValue);
		}
	}
}
