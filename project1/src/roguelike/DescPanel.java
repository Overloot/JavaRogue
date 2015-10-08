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


class DescPanel extends JPanel
{
	public DescWindow dwindow;
	public DescKeyHandler listener;
	public int strY = 0;



	DescPanel(DescWindow dwindow){
		super();
		this.dwindow = dwindow;
		listener = new DescKeyHandler(this);
		addKeyListener(listener);
		setFocusable(true);
	}

	void drawColorString(Graphics g, String str, int lastX, int lastY){
		strY += 15;
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


	public void DrawItemDesc(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(Color.WHITE);
		int fontsize = 15;
		Image im = Toolkit.getDefaultToolkit().getImage("res/icons/texture_menu.png");
		g.drawImage(im,0,0,this);
		ScriptObject so = ScriptParser.parseString(dwindow.item.getScript());
		int leftX = 10;
		strY = 15;
		String str = dwindow.item.getName() + "(������� " + dwindow.item.getLevel() + " ������)";
		drawColorString(g, str, leftX, strY);
		if (dwindow.item.getSlot()!=Itemset.SLOT_ANY){
			str = Itemset.getSlotName(dwindow.item.getSlot());
			drawColorString(g, str, leftX, strY);
		}
		str = "���� ������� ����������� ���������:#8# " + Itemset.getNameOfType(dwindow.item.getType()).toLowerCase() + "#^#";
		drawColorString(g, str, leftX, strY);
		str = "���:" + dwindow.item.getMass() + ", ������:" + dwindow.item.getSize();
		drawColorString(g, str, leftX, strY);

		if (!dwindow.item.isIdentify())
		{
			drawColorString(g, "��� ���� ������ ���������� � ���� ��������.", leftX, strY);
			return;
		}

 		if (so.STR_UP.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� #8#����.#^#", leftX, strY);
		else
		if (so.STR_UP.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� #8#����.#^#", leftX, strY);

		if (so.AGI_UP.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� #8#��������!#^#", leftX, strY);
		else
		if (so.AGI_UP.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� #8#��������.#^#", leftX, strY);

		if (so.END_UP.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� #8#������������!#^#", leftX, strY);
		else
		if (so.END_UP.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� #8#������������.#^#", leftX, strY);

		if (so.LUCK_UP.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� #8#�����!#^#", leftX, strY);
		else
		if (so.LUCK_UP.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� #8#�����.#^#", leftX, strY);

		if (so.DFIRE.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ��� ���� #2#�����.#^#", leftX, strY);
		else
		if (so.DFIRE.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ��� ���� #2#�����.#^#", leftX, strY);

		if (so.DCOLD.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ��� ���� #4#�������.#^#", leftX, strY);
		else
		if (so.DCOLD.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ��� ���� #4#�������.#^#", leftX, strY);

		if (so.DELEC.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ��� ���� #5#��������������.#^#", leftX, strY);
		else
		if (so.DELEC.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ��� ���� #5#��������������.#^#", leftX, strY);

		if (so.DPOISON.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ��� ���� #3#����.#^#", leftX, strY);
		else
		if (so.DPOISON.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ��� ���� #3#����.#^#", leftX, strY);

		if (so.DNORMAL.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ��� #8#����.#^#", leftX, strY);
		else
		if (so.DNORMAL.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ��� #8#����.#^#", leftX, strY);

		if (so.RFIRE.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� ������������� � #2#����.#^#", leftX, strY);
		else
		if (so.RFIRE.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� ������������� � #2#����.#^#", leftX, strY);

		if (so.RCOLD.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� ������������� � #4#������.#^#", leftX, strY);
		else
		if (so.RCOLD.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� ������������� � #4#������.#^#", leftX, strY);

		if (so.RPOISON.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� ������������� � #3#���.#^#", leftX, strY);
		else
		if (so.RPOISON.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� ������������� � #3#���.#^#", leftX, strY);

		if (so.RELEC.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� ������������� � #5#�������������.#^#", leftX, strY);
		else
		if (so.RELEC.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� ������������� � #5#�������������.#^#", leftX, strY);

		if (so.RNORMAL.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ���� ������������� � #8#�����.#^#", leftX, strY);
		else
		if (so.RNORMAL.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ���� ������������� � #8#�����.#^#", leftX, strY);

		if (so.SW_UP.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#���������#^# ������ ������ ���������.", leftX, strY);
		else
		if (so.SW_UP.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#���������#^# ������ ������ ���������.", leftX, strY);


		if (so.BLINK) drawColorString(g, "���� ������� ����� #8#��������������� ��� �� �������� ����������.#^#", leftX, strY);
		if (so.TELEPORT) drawColorString(g, "���� ������� ����� #8#��������������� ���.#^#", leftX, strY);
		if (so.IDENTIFY) drawColorString(g, "���� ������� ����� #8#��������#^# ������ �������.", leftX, strY);
		if (so.POISONCOUNT.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#��������#^# ���!", leftX, strY);

		if (so.FOVRAD.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#��������#^# ���� #8#������!#^#", leftX, strY);
		else
		if (so.FOVRAD.getCurrent()<0) drawColorString(g, "���� ������� ����� #2#��������#^# ���� #8#������!#^#", leftX, strY);

		if (so.PARALYZECOUNT.getCurrent()>0) drawColorString(g, "���� ������� ����� #5#������������#^# ���!", leftX, strY);

		if (so.HEALPOISON.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#��������#^# ��� �� #3#���!#^#", leftX, strY);
		if (so.HEALSELF.getCurrent()>0) drawColorString(g, "���� ������� ����� #3#��������#^# ���!#^#", leftX, strY);
	}

	public void DrawMonsterDesc(Graphics g){
		Image im = Toolkit.getDefaultToolkit().getImage("res/icons/texture_menu.png");
		g.drawImage(im,0,0,this);
		int leftX = 10;
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(Color.YELLOW);
		g.drawString(dwindow.monster.getName(),leftX, 50);
		g2.setPaint(Color.WHITE);
		String str = "#2#�����#^# : " + Integer.toString(dwindow.monster.getHP().getCurrent()) + "/" + Integer.toString(dwindow.monster.getHP().getMax());
		drawColorString(g,str,leftX, 100);
		str = "#8#����#^# : " + Integer.toString(dwindow.monster.getSTR().getCurrent());
		drawColorString(g,str,leftX, 120);
		str = "#8#��������#^# : " + Integer.toString(dwindow.monster.getAGI().getCurrent());
		drawColorString(g,str,leftX, 135);
		str = "#8#������������#^# : " + Integer.toString(dwindow.monster.getEND().getCurrent());
		drawColorString(g,str,leftX, 150);
		str = "#8#�����#^# :" + Integer.toString(dwindow.monster.getLUCK().getCurrent());
		drawColorString(g,str,leftX, 165);
		str = "#8#������ ������#^# : " + dwindow.monster.getFOVRAD().getCurrent();
		drawColorString(g,str,leftX, 180);
		str = "#1#������������� ������#^# : #8#  " + dwindow.monster.getRNormal().getCurrent();
		drawColorString(g,str,leftX, 195);
		str = "#1#���������� ����#^# : #8#  " + dwindow.monster.getDNormal().getCurrent() + " - " + dwindow.monster.getDNormal().getMax();
		drawColorString(g,str,leftX, 210);
		str = "#1#������������� ����#^# : #2#  " + dwindow.monster.getRFire().getCurrent();
		drawColorString(g,str,leftX, 225);
		str = "#1#���� �����#^# : #2#  " + dwindow.monster.getDFire().getCurrent() + " - " + dwindow.monster.getDFire().getMax();
		drawColorString(g,str,leftX, 240);
		str = "#1#������������� ������#^# : #4#  " + dwindow.monster.getRCold().getCurrent();
		drawColorString(g,str,leftX, 255);
		str = "#1#���� �������#^# : #4#  " + dwindow.monster.getDCold().getCurrent() + " - " + dwindow.monster.getDCold().getMax();
		drawColorString(g,str,leftX, 270);
		str = "#1#������������� ���#^# : #3#  " + dwindow.monster.getRPoison().getCurrent();
		drawColorString(g,str,leftX, 285);
		str = "#1#���� ����#^# : #3#  " + dwindow.monster.getDPoison().getCurrent() + " - " + dwindow.monster.getDPoison().getMax();
		drawColorString(g,str,leftX, 300);
		str = "#1#������������� �������������#^# : #5#  " + dwindow.monster.getRElec().getCurrent();
		drawColorString(g,str,leftX, 315);
		str = "#1#���� ��������������#^# : #5#  " + dwindow.monster.getDElec().getCurrent() + " - " + dwindow.monster.getDElec().getMax();
		drawColorString(g,str,leftX, 330);
		if (dwindow.monster.getparalyzecount() > 0){
			str = "#8#�����������! #^# : #5#  " + dwindow.monster.getparalyzecount();
			drawColorString(g,str,leftX, 400);
		}
		if (dwindow.monster.getpoisoncount() > 0){
			str = "#8#��������! #^# : #3#  " + dwindow.monster.getpoisoncount();
			drawColorString(g,str,leftX, 420);
		}
		str = "#1#�������#^# : #8#  " + dwindow.monster.getLevel() + "#^#";
		drawColorString(g,str,leftX, 435);
		if (dwindow.monster == dwindow.game.Player){
		str = "�����: " + dwindow.monster.getXP() + "/" + dwindow.game.MAX_EXP;
			drawColorString(g,str,leftX, 450);

		}



	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (dwindow.monster != null)
		   DrawMonsterDesc(g);
		else
		    DrawItemDesc(g);

	}


}
