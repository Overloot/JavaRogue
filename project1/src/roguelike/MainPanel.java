package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.util.*;
import project1.Tileset;
import project1.Map;
import project1.Tile;
import project1.BaseTile;
import project1.KeyHandler;
import project1.Monsterset;
import project1.BaseMonster;
import project1.ColorSet;


class MainPanel extends JPanel
{
	private static int SCREEN_TILE_SIZE_X;
	private static int SCREEN_TILE_SIZE_Y;
	private Map DrawingMap;
	private GameWindow window;
	public KeyHandler listener;
	private String LogString;
	private String LastString;
	private int countrepeat;
	private int linenumber;
	private int lastX;
	String laststr = "";
	public String descStr = "";

	public void setMap(Map map){
		DrawingMap = map;
		listener.map = map;
	}

	public boolean HasTileAtScreen(int y, int x){
		return (y>=DrawingMap.getCurrentY() && y<SCREEN_TILE_SIZE_Y+DrawingMap.getCurrentY() && x>=DrawingMap.getCurrentX() && x<SCREEN_TILE_SIZE_X+DrawingMap.getCurrentX());
	}

	MainPanel(GameWindow window, Map DrawingMap, int stsx, int stsy){
		super();
		this.window = window;
		LogString = "";
		linenumber = 1;
		this.DrawingMap = DrawingMap;
		this.SCREEN_TILE_SIZE_X = stsx;
		this.SCREEN_TILE_SIZE_Y = stsy;
	    listener = new KeyHandler(DrawingMap, this);
		addKeyListener(listener);
		setFocusable(true);
		lastX = 15;
	}

	public void LogMessage(String str){
		if (!str.endsWith("/#"))
		LogString += (str + "#/#");
		else
		LogString += (str);

	}

	public void drawLog(Graphics g){
		linenumber = 1;
		int leftX = 15;
		if (LogString == null || LogString.equals("")) return;
		StringTokenizer st = new StringTokenizer(LogString, "#");
		Graphics2D g2 = (Graphics2D)g;
		FontRenderContext context = g2.getFontRenderContext();
		Font f = new Font("Times New Roman", Font.PLAIN, 12);
		g2.setFont(f);
		Rectangle2D bounds;
		g2.setPaint(Color.WHITE);
		int topY = Tileset.TILE_SIZE*SCREEN_TILE_SIZE_Y+15;
		String str;
		while (st.hasMoreTokens()){
			str = st.nextToken();
			if (str.equals("/")){
					lastX = 15;
					linenumber++;
					continue;
			}
			else
			if (str.equals("^")){
				g2.setColor(Color.WHITE);
				continue;
			}
			else
			if (str.length() < 3){
				int col = 0;
				boolean skip = false;
				try{
				col = Integer.parseInt(str);
				} catch (NumberFormatException ex){col = 1; skip = true;}
				g2.setColor(ColorSet.COLORSET[col]);
				if (!skip)
				continue;
			}
			g2.drawString(str, lastX, linenumber * 15);
			bounds = f.getStringBounds(str, context);
			lastX += (bounds.getWidth());
		}
			if (linenumber > 15) LogString = "";

	}

	void drawColorString(Graphics g, String str, int lastX, int lastY){
		Graphics2D g2 = (Graphics2D)g;
		FontRenderContext context = g2.getFontRenderContext();
		Font f = new Font("Serif", Font.PLAIN, 12);
		g2.setFont(f);
		Rectangle2D bounds;
		StringTokenizer st = new StringTokenizer(str, "#");
		String token;
		while (st.hasMoreTokens()){
			token = st.nextToken();
			if (token.equals("^")){
				g2.setColor(Color.WHITE);
				continue;
			}
			else
			if (token.length() <= 2){
				int col = Integer.parseInt(token);
				g2.setColor(ColorSet.COLORSET[col]);
				continue;
			}
				g2.drawString(token, lastX, lastY);
				bounds = f.getStringBounds(token, context);
				lastX += (bounds.getWidth());
			}
	}


	public void paintGUI(Graphics g){
		int leftX = Tileset.TILE_SIZE*SCREEN_TILE_SIZE_X+10;
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(Color.WHITE);
		String str =  DrawingMap.getGame().Player.getName() + "#8#  " + Integer.toString(DrawingMap.getGame().Player.getLevel()) + "  #^#ÛÓ‚Ìˇ";
	    drawColorString(g, str ,leftX, 50);
		g2.setPaint(Color.WHITE);
		str = "#2#∆»«Õ‹#^# : " + Integer.toString(DrawingMap.getGame().Player.getHP().getCurrent()) + "/" + Integer.toString(DrawingMap.getGame().Player.getHP().getMax());
		drawColorString(g,str,leftX, 100);
		str = "#3#Œœ€“ : #^#" + DrawingMap.getGame().Player.getXP() + "/" + DrawingMap.getGame().MAX_EXP;
		drawColorString(g,str,leftX, 115);
		str = "#8#—»À¿#^# : " + Integer.toString(DrawingMap.getGame().Player.getSTR().getCurrent());
		drawColorString(g,str,leftX, 130);
		str = "#8#ÀŒ¬ Œ—“‹#^# : " + Integer.toString(DrawingMap.getGame().Player.getAGI().getCurrent());
		drawColorString(g,str,leftX, 145);
		str = "#8#¬€ÕŒ—À»¬Œ—“‹#^# : " + Integer.toString(DrawingMap.getGame().Player.getEND().getCurrent());
		drawColorString(g,str,leftX, 160);
		str = "#8#”ƒ¿◊¿#^# :" + Integer.toString(DrawingMap.getGame().Player.getLUCK().getCurrent());
		drawColorString(g,str,leftX, 175);
		if (DrawingMap.getGame().Player.getparalyzecount() > 0){
			str = "#8#¬€ œ¿–¿À»«Œ¬¿Õ€!!!#^# : #5#  " + DrawingMap.getGame().Player.getparalyzecount();
			drawColorString(g,str,leftX, 400);
		}
		if (DrawingMap.getGame().Player.getpoisoncount() > 0){
			str = "#8#¬€ Œ“–¿¬À≈Õ€!!!#^# : #3#  " + DrawingMap.getGame().Player.getpoisoncount();
			drawColorString(g,str,leftX, 420);
		}

		str = "#8#√À”¡»Õ¿#^# : " + Integer.toString(DrawingMap.getGame().CurrentMapNumber + 1);
		drawColorString(g,str,leftX, 435);
		str = "’Œƒ π " + Integer.toString(DrawingMap.getGame().turn);
		drawColorString(g,str,leftX, 450);
		str = "¬≈— »Õ¬≈Õ“¿–ﬂ: " + Integer.toString(DrawingMap.getGame().Player.getCW().getCurrent()) + "/" + Integer.toString(DrawingMap.getGame().Player.getCW().getMax());
		drawColorString(g,str,leftX, 465);
		str = "–¿«Ã≈– »Õ¬≈Õ“¿–ﬂ: " + Integer.toString(DrawingMap.getGame().Player.getSW().getCurrent()) + "/" + Integer.toString(DrawingMap.getGame().Player.getSW().getMax());
		drawColorString(g,str,leftX, 480);
		str = Integer.toString(DrawingMap.getGame().Player.getY()) + "/" + Integer.toString(DrawingMap.getGame().Player.getX());
		drawColorString(g,str,leftX, 495);

		if (descStr!=""){
			drawColorString(g,descStr,15, GameWindow.WINDOW_HEIGHT - (2 * Tileset.TILE_SIZE));
		}



		str = DrawingMap.getName();
				drawColorString(g,str,leftX, 520);


}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		Rectangle2D canvas = new Rectangle2D.Double(0, 0 , window.WINDOW_WIDTH, window.WINDOW_WIDTH);
		g2.setPaint(Color.BLACK);
		g2.fill(canvas);
		g2.draw(canvas);
		for (int i=0; i<SCREEN_TILE_SIZE_Y; i++)
		for (int j=0; j<SCREEN_TILE_SIZE_X; j++){
			if (!DrawingMap.HasTileAt(i+DrawingMap.getCurrentY(),j+DrawingMap.getCurrentX()) || (!DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getVisible() && !DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getSeen()) ){
			Image image = Toolkit.getDefaultToolkit().getImage("res/dungeons/empty.png");
				int y = (j*Tileset.TILE_SIZE);
				int x = (i*Tileset.TILE_SIZE) ;
				g.drawImage(image,y,x,this);

			}
			else
			if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getSeen() &&  !DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getVisible())
			{
				Image image = Toolkit.getDefaultToolkit().getImage(Tileset.getTile(DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].lastseenID).getPath());
				int y = (j*Tileset.TILE_SIZE);
				int x = (i*Tileset.TILE_SIZE) ;
				g.drawImage(image,y,x,this);
				image = Toolkit.getDefaultToolkit().getImage("res/icons/transparent.png");
				g.drawImage(image,y,x,this);
			}
			else
			{
				Image image = Toolkit.getDefaultToolkit().getImage(Tileset.getTile(DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getID()).getPath());
				int y = (j*Tileset.TILE_SIZE);
				int x = (i*Tileset.TILE_SIZE) ;
				g.drawImage(image,y,x,this);
				image = Toolkit.getDefaultToolkit().getImage("res/icons/blood.png");
				if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getBlood())
					g.drawImage(image,y,x,this);

				if (DrawingMap.HasTileAt(i+DrawingMap.getCurrentY(),j+DrawingMap.getCurrentX())){
								if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getItemsQty()!=0)
								{
								if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getItemsQty() > 1)
								   image = Toolkit.getDefaultToolkit().getImage("res/icons/manyitems.png");
								else
								{
									LinkedList<Item> itemlist = DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getItemList();
								    image = Toolkit.getDefaultToolkit().getImage(Itemset.getItem(itemlist.getFirst().getID()).getPath());
								}
													    g.drawImage(image,y,x,this);

								}




							if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster()!=null){
								image = Toolkit.getDefaultToolkit().getImage(Monsterset.getMonster(DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getID()).getPath());
								int px = DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getX();
								int py = DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getY();
								y = (j*Tileset.TILE_SIZE);
								x = (i*Tileset.TILE_SIZE);
								double leftX = (double)y;
								double topY = (double)x;
								double curHP = (double)(DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getHP().getCurrent());
								double maxHP = (double)(DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getHP().getMax());
								double width = (double)(curHP/maxHP*32-1);
								double height = 2;
								Rectangle2D rect = new Rectangle2D.Double(leftX, topY, width, height);
								Rectangle2D br = new Rectangle2D.Double(leftX, topY, 31D, height);
								g.drawImage(image,y,x,this);
								g2.setPaint(Color.BLACK);
								g2.fill(br);
								g2.draw(br);
								if (curHP/maxHP>0.75)
								g2.setPaint(Color.GREEN);
								else
								if (curHP/maxHP>0.50)
								g2.setPaint(Color.YELLOW);
								else
								g2.setPaint(Color.RED);
								g2.fill(rect);
								g2.draw(rect);
								if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getparalyzecount()!=0){
									image = Toolkit.getDefaultToolkit().getImage("res/icons/paralyzed.png");
									g.drawImage(image,y,x,this);
								}
								if (DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].getMonster().getpoisoncount()!=0){
									image = Toolkit.getDefaultToolkit().getImage("res/icons/poisoned.png");
									g.drawImage(image,y,x,this);
								}
							}
						}
				}
	if (DrawingMap.HasTileAt(i+DrawingMap.getCurrentY(),j+DrawingMap.getCurrentX()) && DrawingMap.field[i+DrawingMap.getCurrentY()][j+DrawingMap.getCurrentX()].isSelected())
	{
		Image image = Toolkit.getDefaultToolkit().getImage("res/icons/selected.png");
		int y = (j*Tileset.TILE_SIZE);
		int x = (i*Tileset.TILE_SIZE) ;
		g.drawImage(image,y,x,this);
	}
	}
	paintGUI(g);
	drawLog(g);
	}
}
