package project1;

public class BaseTile{

	private int id;
	private boolean isPassable;
	private boolean isTransparent;
	private boolean isOpenable;
	private String path;
	private String name;


	public BaseTile(int id, String name, String path, boolean isPassable, boolean isTransparent, boolean isOpenable){
		this.id = id;
		this.path = path;
		this.isPassable = isPassable;
		this.isTransparent = isTransparent;
		this.isOpenable = isOpenable;
		this.name = name;
	}

	public String getName(){return name;}
	public int getID(){return id;}

	public boolean getPassable(){return isPassable;}

	public boolean getTransparent(){return isTransparent;}

	public String getPath(){return path;}

	public boolean getOpenable(){return isOpenable;}

}
