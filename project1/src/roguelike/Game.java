package project1;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Game {

    public static final int MAX_MONSTER_PER_LEVEL = 50;
    public static final int MAX_ITEM_PER_LEVEL = 150;
    public static final int MAX_LEVEL = 30;
    public static int MAX_EXP = 200;
    public final int MAP_SIZE_Y = 100;
    public final int MAP_SIZE_X = 100;
    public final int HP_PER_STR = 5;
    public final int CW_PER_STR = 5;
    public final int MIN_SIZE = 100;
    public final int HP_PER_END = 10;
    final int minfovrad = 4;
    final int MOVE_COST_AP = 10;
    final int MAX_FLOORS = 12;
    final int MAX_MONSTERS = MAX_FLOORS * MAX_MONSTER_PER_LEVEL + 1;
    final int MAX_ITEMS = MAX_FLOORS * MAX_ITEM_PER_LEVEL + 1;
    public Monster Player;
    public Map map;
    public KeyHandler keyhandler;
    public Monster[] Monsterlist;
    public Item[] Itemlist;
    public Map[] Maplist;
    public GameWindow frame1;
    public int CurrentMapNumber;
    public int turn = 0;
    public int statsfree = 0;
    public int reghp = 0;
    private int MonstersQty;
    private int ItemsQty;

    public int rand(int value) {
        Random random = new Random();
        return random.nextInt(value) + 1;
    }

    public boolean dice(int value, int max) {
        Random random = new Random();
        int res = (random.nextInt(max) + 1);
        return (res <= value);
    }

    public int getValueFrom(int min, int max) {
        Random random = new Random();
        return (min + random.nextInt(max - min + 1));
    }

    public void LogMessage(String s) {
        frame1.mainpanel.LogMessage(s);
    }

    public void init() {
        MonstersQty = 0;
        ItemsQty = 0;
        Monsterlist = new Monster[MAX_MONSTERS];
        Itemlist = new Item[MAX_ITEMS];
        Maplist = new Map[MAX_FLOORS];
    }

    private int sign(int x) {
        if (x > 0) return 1;
        if (x < 0) return -1;
        return 0;
    }


    public void TryToOpenSomething(boolean isPlayer, int ny, int nx, boolean mode) {
        if (!map.HasTileAt(ny, nx) || !map.field[ny][nx].getOpenable()) {
            if (isPlayer)
                if (mode)
                    map.getGame().LogMessage("Там нечего открыть!#/#");
                else
                    map.getGame().LogMessage("Там нечего закрыть!#/#");
            return;
        } else if (map.field[ny][nx].getOpened() == mode) {
            if (isPlayer)
                if (mode)
                    map.getGame().LogMessage("Это уже открыто!#/#");
                else
                    map.getGame().LogMessage("Это уже закрыто!#/#");
            return;
        }


        if (mode)
            map.setTileAt(ny, nx, Tileset.TILE_OPENED_DOOR);
        else
            map.setTileAt(ny, nx, Tileset.TILE_CLOSED_DOOR);

        if (isPlayer)
            if (mode)
                map.getGame().LogMessage("Вы открыли это. #/#");
            else
                map.getGame().LogMessage("Вы закрыли это. #/#");

    }


    public int dist(int y1, int x1, int y2, int x2) {
        int d = (int) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
        return d;
    }


    public void MonstersAI() {
        turn++;
        CheckMonsters();
        for (int i = 1; i < MonstersQty; i++) {
            Random random = new Random();
            int dy = random.nextInt(3) - 1;
            int dx = random.nextInt(3) - 1;

            if (Monsterlist[i] == null || Monsterlist[i].getparalyzecount() > 0) continue;

            while (Monsterlist[i].getAP().getCurrent() > 0) {

                if (dist(Player.getY(), Player.getX(), Monsterlist[i].getY(), Monsterlist[i].getX()) <= Monsterlist[i].getFOVRAD().getCurrent())
                    Monsterlist[i].move(sign(Player.getY() - Monsterlist[i].getY()), sign(Player.getX() - Monsterlist[i].getX()));
                else
                    Monsterlist[i].move(dy, dx);
                Monsterlist[i].getAP().setCurrent(Monsterlist[i].getAP().getCurrent() - MOVE_COST_AP);

            }

            if (Monsterlist[i].getAP().getCurrent() <= 0) {
                Monsterlist[i].getAP().setCurrent(Monsterlist[i].getAP().getCurrent() + Monsterlist[i].getAP().getMax());
                if (Monsterlist[i].getAP().getCurrent() > Monsterlist[i].getAP().getMax())
                    Monsterlist[i].getAP().setCurrent(Monsterlist[i].getAP().getMax());
                continue;
            }

        }

        CheckMonsters();
        CheckTimeEffects();

    }

    void CheckStat(Stat s, String s1, String s2) {
        if (s.getTimer() > 0) {
            s.subTimer();
            if (s.getTimer() == 0) {
                if (s.getCurrent() < s.getMax())
                    LogMessage(s1);
                else
                    LogMessage(s2);
                s.setCurrent(s.getMax());
            }
        }


    }

    void CheckTimers(Monster m) {
        CheckStat(m.getSTR(), "Вы снова стали #3#сильнее!#^#", "Вы снова стали #2#слабее!#^#");

        if (m.getEND().getTimer() == 1) {
            m.getHP().setMax(m.getHP().getMax() + HP_PER_END * (m.getEND().getMax() - m.getEND().getCurrent()));
            m.getHP().setCurrent(m.getHP().getCurrent() + HP_PER_END * (m.getEND().getMax() - m.getEND().getCurrent()));
        }

        if (m.getSTR().getTimer() == 1) {
            m.getHP().setMax(m.getHP().getMax() + HP_PER_STR * (m.getSTR().getMax() - m.getSTR().getCurrent()));
            m.getHP().setCurrent(m.getHP().getCurrent() + HP_PER_STR * (m.getSTR().getMax() - m.getSTR().getCurrent()));
            m.getCW().setMax(m.getCW().getMax() + CW_PER_STR * (m.getSTR().getMax() - m.getSTR().getCurrent()));
        }

        CheckStat(m.getAGI(), "Вы снова стали более #3#ловким!#^#", "Вы снова стали менее #2#ловким!#^#");
        CheckStat(m.getEND(), "Вы снова стали #3#выносливее!#^#", "Вы снова стали менее #2#выносливее!#^#");
        CheckStat(m.getLUCK(), "Вы снова стали #3#удачливее!#^#", "Вы снова стали менее #2#удачливее!#^#");
        CheckStat(m.getRFire(), "Вы снова стали #3#сильнее#^# сопротивляться #2#огню!#^#", "Вы снова стали #2#слабее#^# сопротивляться #2#огню!#^#");
        CheckStat(m.getRCold(), "Вы снова стали #3#сильнее#^# сопротивляться #4#холоду!#^#", "Вы снова стали #2#слабее#^# сопротивляться #4#холоду!#^#");
        CheckStat(m.getRElec(), "Вы снова стали #3#сильнее#^# сопротивляться #5#электричеству!#^#", "Вы снова стали #2#слабее#^# сопротивляться #5#электричеству!#^#");
        CheckStat(m.getRNormal(), "Вы снова стали #3#сильнее#^# сопротивляться #8#ударам!#^#", "Вы снова стали #2#слабее#^# сопротивляться #8#ударам!#^#");
        CheckStat(m.getRPoison(), "Вы снова стали #3#сильнее#^# сопротивляться #3#яду!#^#", "Вы снова стали #2#слабее#^# сопротивляться #3#яду!#^#");
        CheckStat(m.getFOVRAD(), "Вы снова стали #3#лучше#^# видеть!#^#", "Вы снова стали #2#хуже#^# видеть!#^#");
    }

    void CheckTimeEffects() {
        boolean x = false;
        while (Player.getXP() >= Game.MAX_EXP) {
            x = true;
            LevelUp();
        }
        if (x) {

            frame1.setFocusable(false);
            frame1.setFocusableWindowState(false);
            LevelUpWindow frame1 = new LevelUpWindow(map.getGame());
            frame1.setUndecorated(true);
            frame1.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame1.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame1.WINDOW_HEIGHT / 2);
            frame1.toFront();
            frame1.setVisible(true);

        }


        for (int i = 0; i < MonstersQty; i++) {
            if (Monsterlist[i] != null) {
                CheckTimers(Monsterlist[i]);
            }
            if (Monsterlist[i] != null)
                if (reghp < Monsterlist[i].getEND().getCurrent())
                    Monsterlist[i].getHP().setCurrent(Monsterlist[i].getHP().getCurrent() + 1);
            if (Monsterlist[i] != null && Monsterlist[i].getHP().getCurrent() > Monsterlist[i].getHP().getMax())
                Monsterlist[i].getHP().setCurrent(Monsterlist[i].getHP().getMax());
            if (Monsterlist[i] != null && Monsterlist[i].getpoisoncount() > 0) {
                if (i == 0) LogMessage("Вы очень #3#страдаете от яда! (1)#^#/#");
                Monsterlist[i].setpoisoncount(Monsterlist[i].getpoisoncount() - 1);
                Monsterlist[i].getHP().setCurrent((Monsterlist[i].getHP().getCurrent() - 1));
            }
            if (Monsterlist[i] != null && Monsterlist[i].getparalyzecount() > 0) {
                Monsterlist[i].setparalyzecount(Monsterlist[i].getparalyzecount() - 1);
            }

        }
        reghp += 10;
        if (reghp > 100) reghp = 0;
    }


    public void SwitchMap(int dmap) {
        int newnum = dmap + CurrentMapNumber;
        if (newnum < 0 || newnum >= MAX_FLOORS) return;

        if (Maplist[newnum] == null) {
            Maplist[newnum] = new Map(MAP_SIZE_Y, MAP_SIZE_X);
            map.field[Player.getY()][Player.getX()].setMonster(null);
            map = Maplist[newnum];
            map.setGame(this);
            frame1.SwitchMap(map);
            CurrentMapNumber = newnum;
            map.PlaceMonsterAt(Player.getY(), Player.getX(), Player);
            Player.map = map;
            while (!map.field[Player.getY()][Player.getX()].getPassable())
                map.generate();
            map.setCurrentX(Player.getX() - ((GameWindow.getScreenTileSizeX() * Tileset.TILE_SIZE) / (Tileset.TILE_SIZE * 2)));
            map.setCurrentY(Player.getY() - ((GameWindow.getScreenTileSizeY() * Tileset.TILE_SIZE) / (Tileset.TILE_SIZE * 2)));
            FillLevelByMonsters();
            FillLevelByItems();
            map.FOV(Player.getY(), Player.getX(), Player.getFOVRAD().getCurrent());
            if (CurrentMapNumber == MAX_FLOORS - 1)
                LogMessage("ВЫ ДОБРАЛИСЬ ДО ПОСЛЕДНЕГО УРОВНЯ И ВЫИГРАЛИ ИГРУ! СПАСИБО ЗА ТЕСТИРОВАНИЕ!");

        } else {
            map.field[Player.getY()][Player.getX()].setMonster(null);
            map = Maplist[newnum];
            frame1.SwitchMap(map);
            CurrentMapNumber = newnum;
            map.PlaceMonsterAt(Player.getY(), Player.getX(), Player);
            Player.map = map;
            map.setCurrentX(Player.getX() - ((GameWindow.getScreenTileSizeX() * Tileset.TILE_SIZE) / (Tileset.TILE_SIZE * 2)));
            map.setCurrentY(Player.getY() - ((GameWindow.getScreenTileSizeY() * Tileset.TILE_SIZE) / (Tileset.TILE_SIZE * 2)));
            map.FOV(Player.getY(), Player.getX(), Player.getFOVRAD().getCurrent());
        }

    }


    public void run() {
        Maplist[0] = new Map(MAP_SIZE_Y, MAP_SIZE_X);
        map = Maplist[0];
        CurrentMapNumber = 0;
        map.setGame(this);
        map.generate();
        frame1 = new GameWindow(map);
        frame1.setVisible(true);
        frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        int y = 10;
        int x = 10;
        Random random = new Random();
        while (!map.field[y][x].getPassable() || map.field[y][x].getMonster() != null) {
            y = random.nextInt(map.getHeight());
            x = random.nextInt(map.getWidth());
        }
        AddMonster(Monsterset.MONSTER_PLAYER, y, x, map);
        map.setCurrentX(x - ((GameWindow.getScreenTileSizeX() * Tileset.TILE_SIZE) / (Tileset.TILE_SIZE * 2)));
        map.setCurrentY(y - ((GameWindow.getScreenTileSizeY() * Tileset.TILE_SIZE) / (Tileset.TILE_SIZE * 2)));
        Player = Monsterlist[0];
        FillLevelByMonsters();
        FillLevelByItems();
        map.FOV(y, x, Player.getFOVRAD().getCurrent());
        frame1.mainpanel.repaint();
    }

    public void FillLevelByMonsters() {
        for (int i = 0; i < MAX_MONSTER_PER_LEVEL; i++)
            AddRandomMonster();
    }

    public void FillLevelByItems() {
        for (int i = 0; i < MAX_ITEM_PER_LEVEL; i++)
            AddRandomItem();
    }


    public void AddMonster(int id, int y, int x, Map map) {
        BaseMonster bm = Monsterset.getMonster(id);
        Monsterlist[MonstersQty] = new Monster(bm, y, x, map, this);
        MonstersQty++;
    }


    public void AddRandomMonster() {
        Random random = new Random();
        int newID = 0;

        while (true) {
            newID = random.nextInt(Monsterset.MAX_MONSTERS);
            if (newID == Monsterset.MONSTER_PLAYER) continue;
            if (Monsterset.getMonster(newID).getLevel() < CurrentMapNumber - 4 || Monsterset.getMonster(newID).getLevel() > CurrentMapNumber + 4)
                continue;
            int chanse = 90 - Math.abs(Monsterset.getMonster(newID).getLevel() - CurrentMapNumber) * 20;
            if (!dice(chanse, 100)) continue;
            break;
        }

        BaseMonster bm = Monsterset.getMonster(newID);
        int y = 0;
        int x = 0;
        while (map.field[y][x].getPassable() == false || map.field[y][x].getMonster() != null) {
            y = random.nextInt(map.getHeight());
            x = random.nextInt(map.getWidth());
        }
        Monsterlist[MonstersQty] = new Monster(bm, y, x, map, this);
        MonstersQty++;
    }


    public void TryToPickupItem(LinkedList<Item> list, int number) {
        if (number == -1) return;
        Player.getInventory().addLast(list.get(number));
        LogMessage("#8#Взято!#^# (" + list.get(number).getName().toLowerCase() + "#1#)");
        Player.getCW().setCurrent(Player.getCW().getCurrent() + list.get(number).getMass());
        Player.getSW().setCurrent(Player.getSW().getCurrent() + list.get(number).getSize());
        list.remove(number);
    }

    public void TryToDropItem(LinkedList<Item> list, int number) {
        if (number == -1) return;
        list.addLast(Player.getInventory().get(number));
        LogMessage("#8#Выброшено!#^# (" + Player.getInventory().get(number).getName().toLowerCase() + "#1#)");
        Player.getCW().setCurrent(Player.getCW().getCurrent() - Player.getInventory().get(number).getMass());
        Player.getSW().setCurrent(Player.getSW().getCurrent() - Player.getInventory().get(number).getSize());
        Player.getInventory().remove(number);
    }

    public void TryToLookAtMonster(int y, int x) {
        if (map.field[y][x].getMonster() == null && map.field[y][x].getItemList().size() == 0) {
            LogMessage("Здесь ничего нет!");
            return;
        }
        if (map.field[y][x].getMonster() != null) {
            frame1.setFocusable(false);
            frame1.setFocusableWindowState(false);
            DescWindow frame2 = new DescWindow(this, map.field[y][x].getMonster());
            frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame2.setLocation(frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
            frame2.toFront();
            frame2.setTitle("Информация о монстре");
            frame2.setVisible(true);
        } else if (map.field[y][x].getItemList().size() != 0) {
            TryToExamineItem(map.field[y][x].getItemList(), 0);

        }


    }

    public void TryToQuaffItem(LinkedList<Item> list, int number) {
        if (number == -1) return;

        if (Player.getInventory().get(number).getType() != Itemset.TYPE_POTION) {
            LogMessage("#8#" + Player.getInventory().get(number).getName() + "#^# - это нельзя выпить!");
            return;
        }
        LogMessage("#8#Выпито!#^# (" + Player.getInventory().get(number).getName().toLowerCase() + "#^#)");
        TryToIdentifyItem(Player.getInventory().get(number));
        Player.getCW().setCurrent(Player.getCW().getCurrent() - Player.getInventory().get(number).getMass());
        Player.getSW().setCurrent(Player.getSW().getCurrent() - Player.getInventory().get(number).getSize());
        Player.setEffectFrom(ScriptParser.parseString(Player.getInventory().get(number).getScript()), Player.getInventory().get(number).isIdentify());
        Player.getInventory().remove(number);
    }

    public void TryToReadItem(LinkedList<Item> list, int number) {
        if (number == -1) return;

        if (Player.getInventory().get(number).getType() != Itemset.TYPE_SCROLL) {
            LogMessage(Player.getInventory().get(number).getName() + "#^# - это нельзя прочитать!");
            return;
        }

        LogMessage("#8#Прочитано!#^# (" + Player.getInventory().get(number).getName().toLowerCase() + "#^#)");
        TryToIdentifyItem(Player.getInventory().get(number));
        Player.setEffectFrom(ScriptParser.parseString(Player.getInventory().get(number).getScript()), Player.getInventory().get(number).isIdentify());
        Player.getCW().setCurrent(Player.getCW().getCurrent() - Player.getInventory().get(number).getMass());
        Player.getSW().setCurrent(Player.getSW().getCurrent() - Player.getInventory().get(number).getSize());
        Player.getInventory().remove(number);
    }

    public void TryToIdentifyItem(int number) {
        if (number == -1) return;

        if (Player.getInventory().get(number).isIdentify()) {
            LogMessage(Player.getInventory().get(number).getName() + "#^# - это уже опознано!");
            return;
        }
        LogMessage("#8#Идентифицировано!#^# (" + Player.getInventory().get(number).getName().toLowerCase() + "#^#)");
        for (int i = 0; i < ItemsQty; i++)
            if (Itemlist[i] != null && Itemlist[i] != Player.getInventory().get(number)) {
                if (Itemlist[i].getID() == Player.getInventory().get(number).getID() && !Itemlist[i].isIdentify()) {
                    Itemlist[i].swap_names();
                    Itemlist[i].setIdentify(true);
                }

            }


        Itemset.ID_ITEMS[Player.getInventory().get(number).getID()] = 1;
        String str;
        str = "Вы поняли, что " + Player.getInventory().get(number).getName().toLowerCase();
        Player.getInventory().get(number).swap_names();
        str += " - на самом деле: " + Player.getInventory().get(number).getName().toLowerCase();
        LogMessage(str);
        Player.getInventory().get(number).setIdentify(true);
    }

    public void TryToIdentifyItem(Item item) {

        if (item.isIdentify()) {
            return;
        }

        Itemset.ID_ITEMS[item.getID()] = 1;
        for (int i = 0; i < ItemsQty; i++)
            if (Itemlist[i] != null) {
                if (Itemlist[i].getID() == item.getID() && !Itemlist[i].isIdentify()) {
                    Itemlist[i].swap_names();
                    Itemlist[i].setIdentify(true);
                }

            }

        String str;
        item.swap_names();
        str = "Вы поняли, что это на самом деле " + new String(item.getName().toLowerCase()) + "!";
        LogMessage(str);
        item.setIdentify(true);
    }


    public void TryToExamineItem(LinkedList<Item> list, int number) {
        if (number == -1) return;
        LogMessage("Вы осмотрели предмет (" + list.get(number).getName().toLowerCase() + "#^#)");
        frame1.setFocusable(false);
        frame1.setFocusableWindowState(false);
        DescWindow frame2 = new DescWindow(this, list.get(number));
        frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame2.setLocation(frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
        frame2.toFront();
        frame2.setTitle("Информация об предмете");
        frame2.setVisible(true);
    }

    public void TryToExamineItem(Item item) {
        LogMessage("Вы осмотрели предмет (" + item.getName().toLowerCase() + "#^#)");
        frame1.setFocusable(false);
        frame1.setFocusableWindowState(false);
        DescWindow frame2 = new DescWindow(this, item);
        frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame2.setLocation(frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
        frame2.toFront();
        frame2.setTitle("Информация об предмете");
        frame2.setVisible(true);
    }


    public void TryToEquipItem(LinkedList<Item> list, int number) {
        if (number == -1) return;

        if (Player.getInventory().get(number).getSlot() == Itemset.SLOT_ANY) {
            LogMessage("#8#" + Player.getInventory().get(number).getName() + "#^# - это нельзя надеть!");
            return;
        }
        Item li = null;

        if (Player.Equipment[Player.getInventory().get(number).getSlot()] != null)
            li = Player.Equipment[Player.getInventory().get(number).getSlot()];
        if (li != null) {
            LogMessage("Вы #8#сняли#^# это (" + li.getName().toLowerCase() + "#^#)");
            Player.deleteEffectFrom(ScriptParser.parseString(li.getScript()), li.isIdentify());
            Player.getCW().setCurrent(Player.getCW().getCurrent() + li.getMass());
            Player.getSW().setCurrent(Player.getSW().getCurrent() + li.getSize());
        }
        LogMessage("Вы #8#надели#^# это (" + Player.getInventory().get(number).getName().toLowerCase() + "#^#)");
        Player.setEffectFrom(ScriptParser.parseString(Player.getInventory().get(number).getScript()), Player.getInventory().get(number).isIdentify());
        TryToIdentifyItem(Player.getInventory().get(number));
        Player.getCW().setCurrent(Player.getCW().getCurrent() - Player.getInventory().get(number).getMass());
        Player.getSW().setCurrent(Player.getSW().getCurrent() - Player.getInventory().get(number).getSize());
        if (li != null)
            Player.getInventory().add(li);
        Player.Equipment[Player.getInventory().get(number).getSlot()] = Player.getInventory().get(number);
        Player.getInventory().remove(number);

    }

    public void TryToTakeOffItem(int num) {
        LogMessage("Вы #8#сняли#^# это (" + Player.Equipment[num].getName().toLowerCase() + "#^#)");
        Player.deleteEffectFrom(ScriptParser.parseString(Player.Equipment[num].getScript()), Player.Equipment[num].isIdentify());
        Player.getCW().setCurrent(Player.getCW().getCurrent() + Player.Equipment[num].getMass());
        Player.getSW().setCurrent(Player.getSW().getCurrent() + Player.Equipment[num].getSize());
        Player.getInventory().add(Player.Equipment[num]);
        Player.Equipment[num] = null;
    }


    public void AddRandomItem() {
        Random random = new Random();
        int newID = 0;

        while (true) {
            newID = random.nextInt(Itemset.MAX_ITEMS);
            if (Itemset.getItem(newID).getLevel() > CurrentMapNumber + 4) continue;
            int chanse = 90 - Math.abs(Itemset.getItem(newID).getLevel() - CurrentMapNumber) * 20;
            if (chanse > 100) chanse = 80;
            if (!dice(chanse, 100)) continue;
            if (!dice(Itemset.getItem(newID).getChanse(), 100)) continue;
            break;
        }

        BaseItem bm = Itemset.getItem(newID);
        int y = random.nextInt(map.getHeight());
        ;
        int x = random.nextInt(map.getWidth());
        ;
        while (map.field[y][x].getPassable() == false || map.field[y][x].getMonster() != null) {
            y = random.nextInt(map.getHeight());
            x = random.nextInt(map.getWidth());
        }
        Itemlist[ItemsQty] = new Item(bm, y, x, map, this);
        ItemsQty++;
    }


    public void CheckMonsters() {
        for (int i = 0; i < MonstersQty; i++) {
            if (Monsterlist[i] != null && Monsterlist[i].getHP().getCurrent() > Monsterlist[i].getHP().getMax())
                Monsterlist[i].getHP().setCurrent(Monsterlist[i].getHP().getMax());

        }
        for (int i = 1; i < MonstersQty; i++) {
            if (Monsterlist[i] != null)
                if (Monsterlist[i].getHP().getCurrent() <= 0)
                    KillMonster(i);
        }


    }

    public void LevelUp() {
        if (Player.getLevel() == Game.MAX_LEVEL) {
            Player.setXP(0);
            return;
        }
        Player.setLevel(Player.getLevel() + 1);
        Player.setXP(Player.getXP() - Game.MAX_EXP);
        Game.MAX_EXP *= 2;
        LogMessage("Вы достигли следующего уровня! ");
        statsfree += 5;


    }


    public void KillMonster(int i) {
        LogMessage(Monsterlist[i].getName() + " #2#УМИРАЕТ В МУКАХ#^#!!!#/#");
        Map m = Monsterlist[i].getMap();
        m.field[Monsterlist[i].getY()][Monsterlist[i].getX()].setBlood(true);
        m.DeleteMonsterAt(Monsterlist[i].getY(), Monsterlist[i].getX());
        int mod = 0;

        if (Player.getLevel() <= Monsterlist[i].getLevel()) mod = Monsterlist[i].getLevel();
        else
            mod = 1;

        int nexp = getValueFrom((int) (0.75 * (mod * mod) * 10), (int) (1.5 * (mod * mod) * 10));
        Player.setXP(Player.getXP() + nexp);
        LogMessage("Вы получаете " + Integer.toString(nexp) + " опыта! ");
        Monsterlist[i] = null;
    }


    public void done() {
    }

}