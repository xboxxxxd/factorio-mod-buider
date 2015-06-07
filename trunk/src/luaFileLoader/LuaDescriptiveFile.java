package luaFileLoader;

import java.util.ArrayList;

public class LuaDescriptiveFile {
	
	private int index;
	private String key;
	private String value;
	private ArrayList<LuaDescriptiveFile> keyValue;
	
	public LuaDescriptiveFile(String key) {
		this.index = 0;
		this.key = key;
		this.value = null;
		this.keyValue = new ArrayList<LuaDescriptiveFile>();
	}
	
	public LuaDescriptiveFile(String key, String value) {
		this.index = -1;
		this.key = key;
		this.value = value;
		this.keyValue = null;
	}
	
	public boolean isLeaf(){
		return (value != null);
	}
	
	public String getKey(){
		return key;
	}
	
	public String getValue(){
		return value;
	}
	
	public void reset(){
		if(index > 0){
			index = 0;
		}
	}
	
	public boolean hasNext(){
		return (keyValue.size() < index);
	}
	
	public void next(){
		index++;
	}
	
	public LuaDescriptiveFile get(){
		return keyValue.get(index);
	}
	
	public boolean hasKey(String key){
		return (this.get(key) != null);
	}
	
	public LuaDescriptiveFile get(String key){
		for(LuaDescriptiveFile item : keyValue){
			if(item.getKey().equals(key)){
				return item;
			}
		}
		return null;
	}
	
	public void add(LuaDescriptiveFile item){
		keyValue.add(item);
	}
	
	public String toString(){
		if(isLeaf()){
			return key + " = " + value;
		} else {
			return key + " = " + "Not implemented jet";
		}
	}
}
