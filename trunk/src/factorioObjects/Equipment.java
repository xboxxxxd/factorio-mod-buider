package factorioObjects;

public abstract class Equipment extends Prototype{
	
	public String shape = "shape";
	public String shape_width = "width";
	public String shape_height = "height";

	public Equipment(String pfad){
		super(pfad);
	}
}
