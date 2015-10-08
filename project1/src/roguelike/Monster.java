package project1;

import project1.Map;
import project1.BaseMonster;
import project1.ScriptObject;
import java.util.*;
import javax.swing.*;

public class Monster{
	private Game game;
	String name;
	private int Y;
	private int X;
	private int id;
	private Stat hp;
	private Stat AP;
	private Stat fovrad;

	private LinkedList<Item> Inventory;
	public Item[] Equipment;

	private Stat STR;
	private Stat AGI;
	private Stat END;
	private Stat LUCK;

	private Stat RFire;
	private Stat RCold;
	private Stat RElec;
	private Stat RPoison;
	private Stat RNormal;

	private Stat DFire;
	private Stat DCold;
	private Stat DElec;
	private Stat DPoison;
	private Stat DNormal;
	private Stat CW;
	private Stat SW;
	private int level;
	private int exp;
	private int paralyzecount;
	private int poisoncount;
	public LinkedList<Item> getInventory(){
		return Inventory;
	}

	public Map map;

	public void setGame(Game game){
		this.game = game;
	}

	public Map getMap(){return map;}
	public Stat getHP(){return hp;}
	public Game getGame(){return game;}
	public Stat getFOVRAD(){return fovrad;}
	public Stat getCW(){return CW;}
	public Stat getSW(){return SW;}
	public int getLevel(){return level;}
	public void setLevel(int x){level = x;};
	public void setY(int Y){
		this.Y = Y;
	}

	public void setCW(Stat CW)
	{
		this.CW = CW;
	}

	public void setSW(Stat SW){
		this.SW = SW;
	}

	public void setX(int X){
		this.X = X;
	}

	public int getX(){return X;}
	public int getY(){return Y;}
	public int getID(){return id;}
	public int getpoisoncount(){return poisoncount;}
	public int getparalyzecount(){return paralyzecount;}
	public void setparalyzecount(int pc){paralyzecount = pc;}
	public void setpoisoncount(int pc){poisoncount = pc;}
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	public Stat getRFire(){return RFire;};
	public Stat getRCold(){return RCold;};
	public Stat getRPoison(){return RPoison;};
	public Stat getRElec(){return RElec;};
	public Stat getRNormal(){return RNormal;};

	public Stat getDFire(){return DFire;};
	public Stat getDCold(){return DCold;};
	public Stat getDPoison(){return DPoison;};
	public Stat getDElec(){return DElec;};
	public Stat getDNormal(){return DNormal;};

	public Stat getSTR(){return STR;};
	public Stat getAGI(){return AGI;};
	public Stat getEND(){return END;};
	public Stat getLUCK(){return LUCK;};

	public Stat getAP(){return AP;};
	public void setXP(int amount){exp = amount;};
	public int getXP(){return exp;};


	public Monster(BaseMonster bm, int y, int x, Map map, Game game){
		this.name = bm.getName();
		this.id = bm.getID();
		this.level = bm.getLevel();
		this.map = map;
		this.exp = 0;
		this.map.PlaceMonsterAt(y,x,this);
		this.Y = y;
		this.X = x;
		this.hp = new Stat(bm.getHP().getCurrent(), bm.getHP().getMax());
		this.fovrad = new Stat(bm.getFOVRAD().getCurrent(), bm.getFOVRAD().getMax());
		this.STR = new Stat(bm.getSTR().getCurrent(), bm.getSTR().getMax());
		this.AGI = new Stat(bm.getAGI().getCurrent(), bm.getAGI().getMax());
		this.END = new Stat(bm.getEND().getCurrent(), bm.getEND().getMax());
		this.LUCK = new Stat(bm.getLUCK().getCurrent(), bm.getLUCK().getMax());

		this.RFire = new Stat(bm.getRFire().getCurrent(), bm.getRFire().getMax());
		this.RCold = new Stat(bm.getRCold().getCurrent(), bm.getRCold().getMax());
		this.RElec = new Stat(bm.getRElec().getCurrent(), bm.getRElec().getMax());
		this.RPoison = new Stat(bm.getRPoison().getCurrent(), bm.getRPoison().getMax());
		this.RNormal = new Stat(bm.getRNormal().getCurrent(), bm.getRNormal().getMax());

		this.DFire = new Stat(bm.getDFire().getCurrent(), bm.getDFire().getMax());
		this.DCold = new Stat(bm.getDCold().getCurrent(), bm.getDCold().getMax());
		this.DElec = new Stat(bm.getDElec().getCurrent(), bm.getDElec().getMax());
		this.DPoison = new Stat(bm.getDPoison().getCurrent(), bm.getDPoison().getMax());
		this.DNormal = new Stat(bm.getDNormal().getCurrent(), bm.getDNormal().getMax());
		this.AP = new Stat(bm.getAP().getCurrent(), bm.getAP().getMax());
		this.game = game;
		this.CW = new Stat(0, game.CW_PER_STR * STR.getCurrent());
		this.SW = new Stat(0, game.MIN_SIZE);

		Equipment = new Item[Itemset.MAX_SLOTS];
		Inventory = new LinkedList<Item>();
	}

	public boolean move(int y, int x){
		if (CW.getCurrent() > CW.getMax()){
			game.LogMessage("�� #2#�����������!#^#");
			return false;
		}
		if (SW.getCurrent() > SW.getMax()){
			game.LogMessage("�� ������ #2#������� ����� �����!#^#");
			return false;
		}
		int ny = (this.Y + y);
		int nx = (this.X + x);
		if (map.HasTileAt(ny, nx)){
			if (map.field[ny][nx].getMonster()!=null){
				AttackMonster(map.field[ny][nx].getMonster());
				}
			else
			if (!map.field[ny][nx].getPassable() && map.field[ny][nx].getOpenable() && !map.field[ny][nx].getOpened()){
				boolean m = false;
				if (this==game.Player) m = true;
				game.TryToOpenSomething(m, ny , nx, true);
			}
			else
			if (map.field[ny][nx].getPassable()){
				map.PlaceMonsterAt(this.Y,this.X,null);
				this.Y = ny;
				this.X = nx;
				map.PlaceMonsterAt(ny,nx,this);
				if (this == game.Player){
				if (map.field[ny][nx].getItemList().size()>0)
					if (map.field[ny][nx].getItemList().size()>=2)
					game.LogMessage("����� ����� ����� �����.");
					else
					game.LogMessage("����� ���� " + map.field[ny][nx].getItemList().get(0).getName().toLowerCase() + ".");
				}
				game.frame1.mainpanel.repaint();
				return true;
			}
	    }
		return false;
	}


	public void setEffectFrom(ScriptObject so, boolean b){
		STR.add(so.STR_UP);

		if (so.IDENTIFY){
			game.frame1.mainpanel.listener.ID_MODE = true;

		}

		getHP().setMax(getHP().getMax() + game.HP_PER_END * so.END_UP.getCurrent());
		getHP().setCurrent(getHP().getCurrent() + game.HP_PER_END * so.END_UP.getCurrent());
		getHP().setMax(getHP().getMax() + game.HP_PER_STR * so.STR_UP.getCurrent());
		getHP().setCurrent(getHP().getCurrent() + game.HP_PER_STR * so.STR_UP.getCurrent());
		getCW().setMax(getCW().getMax() + game.CW_PER_STR * so.STR_UP.getCurrent());

		if (b)
		if (so.STR_UP.getCurrent()>0) game.LogMessage("�� ������������� ���� #3#�������!#^#");
		else
		if (so.STR_UP.getCurrent()<0) game.LogMessage("�� ������������� ���� #2#������!#^#");


		AGI.add(so.AGI_UP);

		if (b)
		if (so.AGI_UP.getCurrent()>0) game.LogMessage("�� ����� #3#����� ������!#^#");
		else
		if (so.AGI_UP.getCurrent()<0) game.LogMessage("�� ������������� ���� #2#����� ��������!#^#");

		END.add(so.END_UP);

		if (b)
		if (so.END_UP.getCurrent()>0) game.LogMessage("�� �������������, ��� ����� #3#����� ����������!#^#");
		else
		if (so.END_UP.getCurrent()<0) game.LogMessage("�� �������������, ��� ����� #2#����� ����������!#^#");

		LUCK.add(so.LUCK_UP);
		if (b)
		if (so.LUCK_UP.getCurrent()>0) game.LogMessage("�� ������������� ���� #3#���������!#^#");
		else
		if (so.LUCK_UP.getCurrent()<0) game.LogMessage("�� ������������� ���� #2#����� ���������!#^#");

		if (b)
		if (so.DFIRE.getCurrent()>0) game.LogMessage("�� ������������� ���� #2#����!#^#");
		else
		if (so.DFIRE.getCurrent()<0) game.LogMessage("�� ����� ���� ����������� ���� #2#����!#^#");

		DFire.setCurrent(DFire.getCurrent() + so.DFIRE.getCurrent());
		DFire.setMax(DFire.getMax() + so.DFIRE.getMax());
		if (b)
		if (so.DCOLD.getCurrent()>0) game.LogMessage("�� ������������� ���� #4#������!#^#");
		else
		if (so.DCOLD.getCurrent()<0) game.LogMessage("�� ����� ���� ����������� ���� #4#������!#^#");

		DCold.setCurrent(DCold.getCurrent() + so.DCOLD.getCurrent());
		DCold.setMax(DCold.getMax() + so.DCOLD.getMax());
		if (b)
		if (so.DPOISON.getCurrent()>0) game.LogMessage("�� ������������� ���� #3#���!#^#");
		else
		if (so.DPOISON.getCurrent()<0) game.LogMessage("�� ����� ���� ����������� ���� #3#���!#^#");

		DPoison.setCurrent(DPoison.getCurrent() + so.DPOISON.getCurrent());
		DPoison.setMax(DPoison.getMax() + so.DPOISON.getMax());
		if (b)
		if (so.DELEC.getCurrent()>0) game.LogMessage("�� ������������� ���� #5#�������������!#^#");
		else
		if (so.DELEC.getCurrent()<0) game.LogMessage("�� ����� ���� ����������� ���� #5#�������������!#^#");

		DElec.setCurrent(DElec.getCurrent() + so.DELEC.getCurrent());
		DElec.setMax(DElec.getMax() + so.DELEC.getMax());
		DNormal.setCurrent(DNormal.getCurrent() + so.DNORMAL.getCurrent());
		DNormal.setMax(DNormal.getMax() + so.DNORMAL.getMax());
		if (b)
		if (so.RFIRE.getCurrent()>0) game.LogMessage("�� ����� #8#�����#^# ������������ � #2#����!#^#");
		else
		if (so.RFIRE.getCurrent()<0) game.LogMessage("�� ����� #2#�����#^# ������������ � #2#����!#^#");

		RFire.add(so.RFIRE);
		if (b)
		if (so.RCOLD.getCurrent()>0) game.LogMessage("�� ����� #8#�����#^# ������������ � #4#������!#^#");
		else
		if (so.RCOLD.getCurrent()<0) game.LogMessage("�� ����� #2#�����#^# ������������ � #4#������!#^#");

		RCold.add(so.RCOLD);
		if (b)
		if (so.RPOISON.getCurrent()>0) game.LogMessage("�� ����� #8#�����#^# ������������ � #3#���!#^#");
		else
		if (so.RPOISON.getCurrent()<0) game.LogMessage("�� ����� #2#�����#^# ������������ � #3#���!#^#");

		RPoison.add(so.RPOISON);
		if (b)
		if (so.RNORMAL.getCurrent()>0) game.LogMessage("�� ����� #8#�����#^# ������������ � #8#������!#^#");
		else
		if (so.RNORMAL.getCurrent()<0) game.LogMessage("�� ����� #2#�����#^# ������������ � #8#������!#^#");

		RNormal.add(so.RNORMAL);
		if (b)
		if (so.RELEC.getCurrent()>0) game.LogMessage("�� ����� #8#�����#^# ������������ � #5#�������������!#^#");
		else
		if (so.RELEC.getCurrent()<0) game.LogMessage("�� ����� #2#�����#^# ������������ � #5#�������������!#^#");

		RElec.add(so.RELEC);
		if (b)
		if (so.HEALSELF.getCurrent()>0) game.LogMessage("�� #3#�����������!#^#");
		else
		if (so.HEALSELF.getCurrent()<0) game.LogMessage("�� #2#��������� �� ����!#^#");

		hp.setCurrent(hp.getCurrent() + so.HEALSELF.getCurrent());
		if (b)
		if (so.FOVRAD.getCurrent()>0) game.LogMessage("�� ����� #8#�����#^# ������!");
		else
		if (so.FOVRAD.getCurrent()<0) game.LogMessage("�� ����� #2#����#^# ������!");

		fovrad.add(so.FOVRAD);

		if (b)
		if (so.SW_UP.getCurrent()>0) game.LogMessage("������ �� ������ ����� #3#������#^# �����!");
		else
		if (so.SW_UP.getCurrent()<0) game.LogMessage("������ �� ������ ����� #2#������#^# �����!");
		SW.setMax(SW.getMax() + so.SW_UP.getCurrent());
		if (b)
		if (so.HEALPOISON.getCurrent()>0) game.LogMessage("�� ����������� �� #3#���!#^#");

		if (so.TELEPORT) {
			Random random = new Random();
			game.LogMessage("�� �������� �����������������!");
			int ny = random.nextInt(map.getHeight());
			int nx = random.nextInt(map.getWidth());
			while (map.field[ny][nx].getMonster()!=null || !map.field[ny][nx].getPassable()){
			 ny = random.nextInt(map.getHeight());
			 nx = random.nextInt(map.getWidth());
			}
			map.field[Y][X].setMonster(null);
			this.Y = ny;
			this.X = nx;
			map.field[Y][X].setMonster(this);
			game.map.setCurrentX(getX() - ((GameWindow.getScreenTileSizeX() * Tileset.TILE_SIZE)/ (Tileset.TILE_SIZE * 2)));
			game.map.setCurrentY(getY() - ((GameWindow.getScreenTileSizeY() * Tileset.TILE_SIZE)/ (Tileset.TILE_SIZE * 2)));

		}

		if (so.HEALPOISON.getCurrent() > 0)
			setpoisoncount(getpoisoncount() - so.HEALPOISON.getCurrent());
		if (so.POISONCOUNT.getCurrent() != 0)
		setpoisoncount(getpoisoncount() + so.POISONCOUNT.getCurrent()  + 1);
		if (so.PARALYZECOUNT.getCurrent() != 0)

		setparalyzecount(getparalyzecount() + so.PARALYZECOUNT.getCurrent() + 1);
		if (getpoisoncount()<0) setpoisoncount(0);
	}

	public void deleteEffectFrom(ScriptObject so, boolean b){
		STR.sub(so.STR_UP);
		if (b)
		if (so.STR_UP.getCurrent()<0) game.LogMessage("�� ������������� ���� #3#�������!#^#");
		else
		if (so.STR_UP.getCurrent()>0) game.LogMessage("�� ������������� ���� #2#������!#^#");

		getHP().setMax(getHP().getMax() - game.HP_PER_END * so.END_UP.getCurrent());
		getHP().setCurrent(getHP().getCurrent() - game.HP_PER_END * so.END_UP.getCurrent());
		getHP().setMax(getHP().getMax() - game.HP_PER_STR * so.STR_UP.getCurrent());
		getHP().setCurrent(getHP().getCurrent() - game.HP_PER_STR * so.STR_UP.getCurrent());
		getCW().setMax(getCW().getMax() - game.CW_PER_STR * so.STR_UP.getCurrent());


		AGI.sub(so.AGI_UP);
		if (b)
		if (so.AGI_UP.getCurrent()<0) game.LogMessage("�� ����� #3#����� ������!#^#");
		else
		if (so.AGI_UP.getCurrent()>0) game.LogMessage("�� ������������� ���� #2#����� ��������!#^#");

		END.sub(so.END_UP);
		if (b)
		if (so.END_UP.getCurrent()<0) game.LogMessage("�� �������������, ��� ����� #3#����� ����������!#^#");
		else
		if (so.END_UP.getCurrent()>0) game.LogMessage("�� �������������, ��� ����� #2#����� ����������!#^#");

		LUCK.sub(so.LUCK_UP);
		if (b)
		if (so.LUCK_UP.getCurrent()<0) game.LogMessage("�� ������������� ���� #3#���������!#^#");
		else
		if (so.LUCK_UP.getCurrent()>0) game.LogMessage("�� ������������� ���� #2#����� ���������!#^#");

		if (b)
		if (so.DFIRE.getCurrent()<0) game.LogMessage("�� ������������� ���� #2#����!#^#");
		else
		if (so.DFIRE.getCurrent()>0) game.LogMessage("�� ����� ���� ����������� ���� #2#����!#^#");

		DFire.setCurrent(DFire.getCurrent() - so.DFIRE.getCurrent());
		DFire.setMax(DFire.getMax() - so.DFIRE.getMax());
		if (b)
		if (so.DCOLD.getCurrent()<0) game.LogMessage("�� ������������� ���� #4#������!#^#");
		else
		if (so.DCOLD.getCurrent()>0) game.LogMessage("�� ����� ���� ����������� ���� #4#������!#^#");

		DCold.setCurrent(DCold.getCurrent() - so.DCOLD.getCurrent());
		DCold.setMax(DCold.getMax() - so.DCOLD.getMax());
		if (b)
		if (so.DCOLD.getCurrent()<0) game.LogMessage("�� ������������� ���� #3#���!#^#");
		else
		if (so.DCOLD.getCurrent()>0) game.LogMessage("�� ����� ���� ����������� ���� #3#���!#^#");

		DPoison.setCurrent(DPoison.getCurrent() - so.DPOISON.getCurrent());
		DPoison.setMax(DPoison.getMax() - so.DPOISON.getMax());
		if (b)
		if (so.DELEC.getCurrent()<0) game.LogMessage("�� ������������� ���� #5#�������������!#^#");
		else
		if (so.DELEC.getCurrent()>0) game.LogMessage("�� ����� ���� ����������� ���� #5#�������������!#^#");

		DElec.setCurrent(DElec.getCurrent() - so.DELEC.getCurrent());
		DElec.setMax(DElec.getMax() - so.DELEC.getMax());
		DNormal.setCurrent(DNormal.getCurrent() - so.DNORMAL.getCurrent());
		DNormal.setMax(DNormal.getMax() - so.DNORMAL.getMax());
		if (b)
		if (so.RFIRE.getCurrent()<0) game.LogMessage("�� ����� #8#�����#^# ������������ � #2#����!#^#");
		else
		if (so.RFIRE.getCurrent()>0) game.LogMessage("�� ����� #2#�����#^# ������������ � #2#����!#^#");

		RFire.sub(so.RFIRE);
		if (b)
		if (so.RCOLD.getCurrent()<0) game.LogMessage("�� ����� #8#�����#^# ������������ � #4#������!#^#");
		else
		if (so.RCOLD.getCurrent()>0) game.LogMessage("�� ����� #2#�����#^# ������������ � #4#������!#^#");

		RCold.sub(so.RCOLD);
		if (b)
		if (so.RPOISON.getCurrent()<0) game.LogMessage("�� ����� #8#�����#^# ������������ � #3#���!#^#");
		else
		if (so.RPOISON.getCurrent()>0) game.LogMessage("�� ����� #2#�����#^# ������������ � #3#���!#^#");

		RPoison.sub(so.RPOISON);
		if (b)
		if (so.RNORMAL.getCurrent()<0) game.LogMessage("�� ����� #8#�����#^# ������������ � #8#������!#^#");
		else
		if (so.RNORMAL.getCurrent()>0) game.LogMessage("�� ����� #2#�����#^# ������������ � #8#������!#^#");

		RNormal.sub(so.RNORMAL);
		if (b)
		if (so.RELEC.getCurrent()<0) game.LogMessage("�� ����� #8#�����#^# ������������ � #5#�������������!#^#");
		else
		if (so.RELEC.getCurrent()>0) game.LogMessage("�� ����� #2#�����#^# ������������ � #5#�������������!#^#");

		RElec.sub(so.RELEC);
		if (b)
		if (so.HEALSELF.getCurrent()<0) game.LogMessage("�� #3#�����������!#^#");
		else
		if (so.HEALSELF.getCurrent()>0) game.LogMessage("�� #2#��������� �� ����!#^#");

		hp.setCurrent(hp.getCurrent() - so.HEALSELF.getCurrent());
		if (b)
		if (so.FOVRAD.getCurrent()<0) game.LogMessage("�� ����� #8#�����#^# ������!");
		else
		if (so.FOVRAD.getCurrent()>0) game.LogMessage("�� ����� #2#����#^# ������!");

		fovrad.sub(so.FOVRAD);
		if (b)
		if (so.SW_UP.getCurrent()<0) game.LogMessage("������ �� ������ ����� #3#������#^# �����!");
		else
		if (so.SW_UP.getCurrent()>0) game.LogMessage("������ �� ������ ����� #2#������#^# �����!");
		SW.setMax(SW.getMax() - so.SW_UP.getCurrent());

		if (getpoisoncount()<0) setpoisoncount(0);
		}




	private void AttackMonster(Monster enemy){
		if (this==enemy) return;
		if (this!= game.Player && enemy!=game.Player) return;
		Random rand = new Random();

				int ndamage = ( (100 - enemy.RNormal.getCurrent()) * (game.getValueFrom(DNormal.getCurrent(), DNormal.getMax()) + game.rand(STR.getCurrent())) / 100 ) ;
				int fdamage = ((100 - enemy.RFire.getCurrent()) * game.getValueFrom(DFire.getCurrent(), DFire.getMax()) / 100) ;
				int cdamage = ((100 - enemy.RCold.getCurrent() ) * game.getValueFrom(DCold.getCurrent(), DCold.getMax()) / 100 );
				int edamage = (  (100 - enemy.RElec.getCurrent() ) * game.getValueFrom(DElec.getCurrent(), DElec.getMax())/ 100 );
				int pdamage = ((100 - enemy.RPoison.getCurrent() ) * game.getValueFrom(DPoison.getCurrent(), DPoison.getMax()) / 100);
				int damage = (ndamage + fdamage + cdamage);
				int pd = enemy.hp.getCurrent();
				//String dlog = "�������� ���������� " + AGI.getCurrent() + "| �������� ������" + enemy.AGI.getCurrent();
				int min = rand.nextInt(AGI.getCurrent());
				int max = rand.nextInt(enemy.AGI.getCurrent());;
				//dlog+=" ������" + min + " ������ " + max;
				//game.LogMessage(dlog);

				if (min>=max){
					if (game.dice(LUCK.getCurrent() * 100,100000) ){
						damage += (rand.nextInt(ndamage) + 1);
						if (enemy == game.Player)
							game.LogMessage(this.getName().toLowerCase() + "  #8#����������#^# ���� ���! ");
						else
						if (this==game.Player)
							game.LogMessage("�� �������� #3#����������#^# ����! ");
					}
					enemy.hp.setCurrent(pd-damage);
					if (pdamage>0) enemy.poisoncount += pdamage;
					if (edamage>0) enemy.paralyzecount += edamage + 1;
					if (pdamage<0) enemy.getHP().setCurrent(enemy.getHP().getCurrent() - pdamage);
					if (edamage<0) enemy.getHP().setCurrent(enemy.getHP().getCurrent() - edamage);
					if (enemy==game.Player){
					    if (ndamage>0)
						game.LogMessage("��� #8#����#^# " + this.getName().toLowerCase() + "!#^# ("+Integer.toString(ndamage)+") #/#");
						if (ndamage==0)
						game.LogMessage("��� #8#����#^# " + this.getName().toLowerCase() + ", �� #8#�� ������� ��� ������-���� �����.#^#/#");
						if (ndamage<0)
						game.LogMessage("��� #8#����#^# " + this.getName().toLowerCase() + "! ���� #3#��������#^# ���! ("+Integer.toString(-ndamage)+") #/#");
					}
					else
					if (this==game.Player){
						if (ndamage>0)
						game.LogMessage(enemy.getName() + " #8#�������� ���� �� ���!#^# (" + Integer.toString(ndamage) + ") #/#");
						if (ndamage==0)
						game.LogMessage(enemy.getName() + " �������� ���� �� ���, �� #8#��������� ��������� ����!#^#/#");
						if (ndamage<0)
						game.LogMessage(enemy.getName() + " �������� ���� �� ��� � �������� #3#����������!#^# (" + Integer.toString(-ndamage) + ") #/#");
					}


				if (DFire.getMax()>0)
				if (enemy==game.Player){
					    if (fdamage>0)
						game.LogMessage("��� #2#��������#^# " + this.getName().toLowerCase() + "! ("+Integer.toString(fdamage)+") #/#");
						if (fdamage==0)
						game.LogMessage("��� #2#��������#^# " + this.getName().toLowerCase() + ", �� #8#�� ������� ��� ������-���� �����.#^#/#");
						if (fdamage<0)
						game.LogMessage("��� #2#��������#^# " + this.getName().toLowerCase() + "! ����� #3#��������#^# ���! ("+Integer.toString(-fdamage)+") #/#");
					}
					else
					if (this==game.Player){
						if (fdamage>0)
						game.LogMessage(enemy.getName() + " #2#�����#^# � ����� ����! (" + Integer.toString(fdamage) + ") #/#");
						if (fdamage==0)
						game.LogMessage(enemy.getName() + " #2#����������#^# �� ����� �����, �� #8#��������� ��������� ����! #^#/#");
						if (fdamage<0)
						game.LogMessage(enemy.getName() + " #2#�������� ����#^# �� ��� � �������� #3#����������!#^# (" + Integer.toString(-fdamage) + ") #/#");
					}


				if (DCold.getMax()>0)
				if (enemy==game.Player){
					    if (cdamage>0)
						game.LogMessage("��� #4#������������#^# " + this.getName().toLowerCase() + "! ("+Integer.toString(cdamage)+") #/#");
						if (cdamage==0)
						game.LogMessage("��� #4#������������#^# " + this.getName().toLowerCase() + ", �� #8#�� ������� ��� ������-���� �����. #^#/#");
						if (cdamage<0)
						game.LogMessage("��� #4#������������#^# " + this.getName().toLowerCase() + "! ����� #3#��������#^# ���! ("+Integer.toString(-cdamage)+") #/#");
					}
					else
					if (this==game.Player){
						if (cdamage>0)
						game.LogMessage(enemy.getName() + " #4#������ �� ������!#^# (" + Integer.toString(cdamage) + ") #/#");
						if (cdamage==0)
						game.LogMessage(enemy.getName() + " #4#������ �� ������,#^# �� #8#��������� ��������� ����!#^#/#");
						if (cdamage<0)
						game.LogMessage(enemy.getName() + " #4#�������#^# � �������� #3#����������!#^# (" + Integer.toString(-cdamage) + ") #/#");
					}

			if (DElec.getMax()>0)
			if (enemy==game.Player){
					    if (edamage>0)
						game.LogMessage("��� #5#���� �����#^# " + this.getName().toLowerCase() + "! �� #5#������������!#^# ("+Integer.toString(edamage)+") #/#");
						if (edamage==0)
						game.LogMessage("��� #5#���� �����#^# " + this.getName().toLowerCase() + ", �� #8#�� ������� ��� ������-���� �����.#^#/#");
						if (edamage<0)
						game.LogMessage("��� #5#���� �����#^# " + this.getName().toLowerCase() + "! ������������� #3#��������#^# ���! ("+Integer.toString(-edamage)+") #/#");
					}
					else
					if (this==game.Player){
						if (edamage>0)
						game.LogMessage(enemy.getName() + " #5#������ � �����������#^# � #5#�� ����� ���������!#^# (" + Integer.toString(edamage) + ") #/#");
						if (edamage==0)
						game.LogMessage(enemy.getName() + " #5#�������� ���� �����,#^# �� #8#��������� ��������� ����!#^#/#");
						if (edamage<0)
						game.LogMessage(enemy.getName() + " #5#�������� ���� �����#^# � �������� #3#����������!#^# (" + Integer.toString(-edamage) + ") #/#");
					}
			if (DPoison.getMax()>0)
		    if (enemy==game.Player){
					    if (pdamage>0)
						game.LogMessage("��� #3#���������#^# " + this.getName().toLowerCase() + "! ("+Integer.toString(pdamage)+") #/#");
						if (pdamage==0)
						game.LogMessage("��� #3#�������� ��������#^# " + this.getName().toLowerCase() + ", �� #8#�� ������� ��� ������-���� �����.#^#/#");
						if (pdamage<0)
						game.LogMessage("��� #3#���������#^# " + this.getName().toLowerCase() + "! �� #3#��������#^# ���! ("+Integer.toString(-pdamage)+") #/#");
					}
					else
					if (this==game.Player){
						if (pdamage>0)
						game.LogMessage(enemy.getName() + " #3#������ �� ������� �������� �� ������ ���!#^# (" + Integer.toString(pdamage) + ") #/#");
						if (pdamage==0)
						game.LogMessage(enemy.getName() + " #3#��������� ��� ��,#^# �� #8#��������� ��������� ����!#^#/#");
						if (pdamage<0)
						game.LogMessage(enemy.getName() + " #3#��������� ��� ��#^# � �������� #3#����������!#^# (" + Integer.toString(-pdamage) + ") #/#");
					}



		}
				else
				if (this==game.Player) game.LogMessage("�� ������������! #/#");
				else
				if (enemy == game.Player) game.LogMessage(this.getName() + " #8#������������� �� ���!#^#/#");

	}



	}