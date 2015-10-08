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
import project1.LevelUpKeyHandler;
import project1.Monsterset;
import project1.BaseMonster;
import project1.ColorSet;


class LevelUpPanel extends JPanel
{
	public LevelUpWindow lwindow;
	public LevelUpKeyHandler listener;
	public int select =  1;
	public int x1 = 0;
	public int x2 = 0;
	public int x3 = 0;



	LevelUpPanel(LevelUpWindow lwindow){
		super();
		this.lwindow = lwindow;
		listener = new LevelUpKeyHandler(this);
		addKeyListener(listener);
		setFocusable(true);
	}





	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Image im =  Toolkit.getDefaultToolkit().getImage("res/icons/texture_menu.png");
				g.drawImage(im,0,0, this);
		Graphics2D g2 = (Graphics2D)g;
		Image image = Toolkit.getDefaultToolkit().getImage("res/icons/str_icon.png");
		int y = (0);
		int x = (0) ;
		g.drawImage(image,x,y,this);
		image = Toolkit.getDefaultToolkit().getImage("res/icons/agi_icon.png");
		y = (Tileset.TILE_SIZE - 1);
		x = (0) ;
		g.drawImage(image,x, y,this);
		image = Toolkit.getDefaultToolkit().getImage("res/icons/end_icon.png");
		y = (Tileset.TILE_SIZE  * 2 - 1);
		x = (0) ;
		g.drawImage(image,x,y,this);
		image = Toolkit.getDefaultToolkit().getImage("res/icons/icon_plus.png");
		y = ((select - 1) * Tileset.TILE_SIZE);
		x = (lwindow.WINDOW_WIDTH - Tileset.TILE_SIZE - 5);
		g.drawImage(image,x,y,this);
     	g2.setPaint(Color.YELLOW);
		g.drawString("—ила: вли€ет на величину наносимого урона в рукопашном бою ",Tileset.TILE_SIZE + 5, (int)Tileset.TILE_SIZE/2);
		g.drawString("Ћовкость: вли€ет на шанс уворота и попадани€ в бою ",Tileset.TILE_SIZE + 5, (int)(Tileset.TILE_SIZE * 1.5));
		g.drawString("¬ыносливость: вли€ет на величину здоровь€ ",Tileset.TILE_SIZE + 5, (int)(Tileset.TILE_SIZE * 2.5));
		g2.setPaint(Color.WHITE);
		g.drawString("ќчков осталось : " + Integer.toString(lwindow.game.statsfree), 5, Tileset.TILE_SIZE * 3 + 10);

		x = (lwindow.WINDOW_WIDTH - (Tileset.TILE_SIZE*2) - 5);

		g2.setPaint(Color.GREEN);
		if (x1!=0)
		g.drawString("+ " + Integer.toString(x1), x,  (int)(Tileset.TILE_SIZE * 0.5));
		if (x2!=0)
		g.drawString("+ " + Integer.toString(x2), x,  (int)(Tileset.TILE_SIZE * 1.5));
        if (x3!=0)
		g.drawString("+ " + Integer.toString(x3), x, (int)(Tileset.TILE_SIZE * 2.5));


	}
}
