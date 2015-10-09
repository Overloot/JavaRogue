package project1;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;


public class KeyHandler implements KeyListener {

    public boolean ID_MODE = false;
    public ItemSelectMessage message = null;
    Map map;
    MainPanel mp;
    boolean OPEN_MODE = false;
    boolean CLOSE_MODE = false;
    boolean LOOK_MODE = false;
    boolean re;
    int Timer = -1;
    private int ly;
    private int lx;

    public KeyHandler(Map map, MainPanel mp) {
        super();
        this.map = map;
        this.mp = mp;
        map.getGame().keyhandler = this;
        re = false;
    }

    ;


    public void LookTo(int dy, int dx) {

        if (!map.HasTileAt(ly + dy, lx + dx)) return;
        if (!mp.HasTileAtScreen(ly + dy, lx + dx)) return;

        map.field[ly][lx].setCursor(false);
        ly += dy;
        lx += dx;
        String str = null;
        map.field[ly][lx].setCursor(true);
        if (map.field[ly][lx].getVisible()) {
            str = "Здесь находится " + Tileset.getTileName(map.field[ly][lx].getID()).toLowerCase() + ". ";
            if (map.field[ly][lx].getMonster() != null)
                str += "#^#Здесь стоит " + map.field[ly][lx].getMonster().getName().toLowerCase() + ".";
            LinkedList<Item> ilist = map.field[ly][lx].getItemList();
            if (ilist.size() != 0) {
                if (ilist.size() > 1)
                    str += "#^# Здесь лежит много вещей. ";
                else
                    str += "#^#" + ilist.getFirst().getName() + " лежит здесь. ";
            }
            mp.descStr = str;
        } else
            mp.descStr = "#^#Вы #2#не видите#^# этого! ";


    }


    public synchronized void keyPressed(KeyEvent event) {
        int keycode = 0;


        if (Timer == 0) System.exit(0);
        if (map.getGame().Player.getHP().getCurrent() <= 0) {
            Timer = 0;
            map.getGame().LogMessage("Вы умерли!... Нажмите любую клавишу");
            mp.repaint();
            return;
        }
        boolean flag = true;


        if (event != null) keycode = event.getKeyCode();

        if (keycode == KeyEvent.VK_ESCAPE && !LOOK_MODE) System.exit(0);
        else if (map.getGame().Player.getparalyzecount() > 0 && !LOOK_MODE) {
            map.getGame().LogMessage("ВЫ #5#ПАРАЛИЗОВАНЫ!!!#^#/#");
            flag = true;
        } else if (message != null) {
            flag = true;

            if (message.command == 'g') {
                map.getGame().TryToPickupItem(map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getItemList(), message.number);
                flag = true;
            } else if (message.command == 'd') {
                map.getGame().TryToDropItem(map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getItemList(), message.number);
                flag = true;
            } else if (message.command == 'q') {
                map.getGame().TryToQuaffItem(map.getGame().Player.getInventory(), message.number);
                flag = true;
            } else if (message.command == 'r') {
                map.getGame().TryToReadItem(map.getGame().Player.getInventory(), message.number);
                flag = true;
            } else if (message.command == 'i') {
                map.getGame().TryToExamineItem(map.getGame().Player.getInventory(), message.number);
                flag = false;
            } else if (message.command == 'w') {
                map.getGame().TryToEquipItem(map.getGame().Player.getInventory(), message.number);
                flag = true;
            } else if (message.command == 'b') {
                map.getGame().TryToIdentifyItem(message.number);
                flag = false;
            } else {
                flag = false;
            }


            message = null;
            mp.repaint();
        } else if (LOOK_MODE) {
            if (keycode == KeyEvent.VK_RIGHT) LookTo(0, +1);
            else if (keycode == KeyEvent.VK_LEFT) LookTo(0, -1);
            else if (keycode == KeyEvent.VK_UP) LookTo(-1, 0);
            else if (keycode == KeyEvent.VK_DOWN) LookTo(+1, 0);
            else if (keycode == KeyEvent.VK_Q) LookTo(-1, -1);
            else if (keycode == KeyEvent.VK_Z) LookTo(+1, -1);
            else if (keycode == KeyEvent.VK_E) LookTo(-1, +1);
            else if (keycode == KeyEvent.VK_C) LookTo(+1, +1);
            else if (keycode == KeyEvent.VK_ENTER) {
                map.getGame().TryToLookAtMonster(ly, lx);
                flag = false;
            } else if (keycode == KeyEvent.VK_ESCAPE) {
                map.field[ly][lx].setCursor(false);
                LOOK_MODE = false;
                mp.descStr = "";
            }
            flag = false;
        } else if (keycode == KeyEvent.VK_G && !event.isShiftDown()) {
            if (map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getItemList().size() == 0) {
                map.getGame().LogMessage("На земле пусто, нечего взять!");
                flag = false;
                mp.repaint();
                return;
            }
            if (map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getItemList().size() == 1) {
                message = new ItemSelectMessage();
                message.command = 'g';
                message.number = 0;
                keyPressed(null);
                return;

            } else {

                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'g';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_ANY, map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getItemList(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите поднять?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                flag = false;
            }
        } else if (keycode == KeyEvent.VK_D) {
            if (map.getGame().Player.getInventory().size() == 0) {
                map.getGame().LogMessage("У вас пусто в инвентаре, нечего бросить!");
                flag = false;
                mp.repaint();
                return;
            }
            if (map.getGame().Player.getInventory().size() == 1) {
                message = new ItemSelectMessage();
                message.command = 'd';
                message.number = 0;
                keyPressed(null);
                return;

            } else {
                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'd';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_ANY, map.getGame().Player.getInventory(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите бросить?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                flag = false;
            }
        } else if (keycode == KeyEvent.VK_Q && event.isShiftDown()) {
            if (map.getGame().Player.getInventory().size() == 0) {
                map.getGame().LogMessage("У вас пусто в инвентаре, нечего выпить!");
                flag = false;
                mp.repaint();
                return;
            } else {
                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'q';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_POTION, map.getGame().Player.getInventory(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите выпить?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                flag = false;
            }
        } else if (keycode == KeyEvent.VK_W && event.isShiftDown()) {
            if (map.getGame().Player.getInventory().size() == 0) {
                map.getGame().LogMessage("У вас пусто в инвентаре, нечего надеть!");
                flag = false;
                mp.repaint();
                return;
            } else {
                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'w';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_ANY, map.getGame().Player.getInventory(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите надеть?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                flag = false;
            }
        } else if (keycode == KeyEvent.VK_E && event.isShiftDown()) {
            map.getGame().frame1.setFocusable(false);
            map.getGame().frame1.setFocusableWindowState(false);
            EqWindow frame2 = new EqWindow(map.getGame(), map.getGame().Player);
            frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
            frame2.toFront();
            frame2.setTitle("Экипировка игрока");
            frame2.setVisible(true);
            flag = false;
        } else if (keycode == KeyEvent.VK_I) {
            if (map.getGame().Player.getInventory().size() == 0) {
                map.getGame().LogMessage("У вас пусто в инвентаре, нечего осмотреть!");
                flag = false;
                mp.repaint();
                return;
            } else {
                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'i';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_ANY, map.getGame().Player.getInventory(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите осмотреть?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                flag = false;
            }
        } else if (keycode == KeyEvent.VK_R) {
            if (map.getGame().Player.getInventory().size() == 0) {
                map.getGame().LogMessage("У вас пусто в инвентаре, нечего прочитать!");
                flag = false;
                mp.repaint();
                return;
            } else {
                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'r';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_SCROLL, map.getGame().Player.getInventory(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите прочитать?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                flag = false;
            }
        } else if (keycode == KeyEvent.VK_C && event.isShiftDown()) {
            map.getGame().frame1.setFocusable(false);
            map.getGame().frame1.setFocusableWindowState(false);
            DescWindow frame2 = new DescWindow(map.getGame(), map.getGame().Player);
            frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
            frame2.toFront();
            frame2.setTitle("Информация об игроке");
            frame2.setVisible(true);
            flag = false;
        } else if (OPEN_MODE) {
            if (keycode == KeyEvent.VK_RIGHT)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 0, map.getGame().Player.getX() + 1, true);
            else if (keycode == KeyEvent.VK_LEFT)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 0, map.getGame().Player.getX() - 1, true);
            else if (keycode == KeyEvent.VK_UP)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() - 1, map.getGame().Player.getX() + 0, true);
            else if (keycode == KeyEvent.VK_DOWN)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 1, map.getGame().Player.getX() + 0, true);
            else if (keycode == KeyEvent.VK_Q)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() - 1, map.getGame().Player.getX() - 1, true);
            else if (keycode == KeyEvent.VK_Z)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 1, map.getGame().Player.getX() - 1, true);
            else if (keycode == KeyEvent.VK_E)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() - 1, map.getGame().Player.getX() + 1, true);
            else if (keycode == KeyEvent.VK_C)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 1, map.getGame().Player.getX() + 1, true);
            else {
                map.getGame().LogMessage("#2#НЕВЕРНОЕ#^# НАПРАВЛЕНИЕ! #/#");
                flag = false;
            }

            OPEN_MODE = false;
        } else if (CLOSE_MODE) {
            if (keycode == KeyEvent.VK_RIGHT)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 0, map.getGame().Player.getX() + 1, false);
            else if (keycode == KeyEvent.VK_LEFT)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 0, map.getGame().Player.getX() - 1, false);
            else if (keycode == KeyEvent.VK_UP)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() - 1, map.getGame().Player.getX() + 0, false);
            else if (keycode == KeyEvent.VK_DOWN)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 1, map.getGame().Player.getX() + 0, false);
            else if (keycode == KeyEvent.VK_Q)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() - 1, map.getGame().Player.getX() - 1, false);
            else if (keycode == KeyEvent.VK_Z)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 1, map.getGame().Player.getX() - 1, false);
            else if (keycode == KeyEvent.VK_E)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() - 1, map.getGame().Player.getX() + 1, false);
            else if (keycode == KeyEvent.VK_C)
                map.getGame().TryToOpenSomething(true, map.getGame().Player.getY() + 1, map.getGame().Player.getX() + 1, false);
            else {
                map.getGame().LogMessage("#2#НЕВЕРНОЕ#^# НАПРАВЛЕНИЕ! #/#");
                flag = false;
            }

            CLOSE_MODE = false;
        } else if (keycode == KeyEvent.VK_RIGHT) {
            if (map.getGame().Player.move(0, +1))
                map.setCurrentX(map.getCurrentX() + 1);
        } else if (keycode == KeyEvent.VK_L) {
            ly = map.getGame().Player.getY();
            lx = map.getGame().Player.getX();
            map.field[ly][lx].setCursor(true);
            LOOK_MODE = true;
            LookTo(0, 0);
            flag = false;
        } else if (keycode == KeyEvent.VK_C && event.isShiftDown()) {
            CLOSE_MODE = true;
            map.getGame().LogMessage("В каком направлении вы хотите #8#закрыть#^# что-то?#/#");
            flag = false;
        } else if (keycode == KeyEvent.VK_LEFT) {
            if (map.getGame().Player.move(0, -1))
                map.setCurrentX(map.getCurrentX() - 1);
        } else if (keycode == KeyEvent.VK_UP) {
            if (map.getGame().Player.move(-1, 0))
                map.setCurrentY(map.getCurrentY() - 1);
        } else if (keycode == KeyEvent.VK_DOWN) {
            if (map.getGame().Player.move(+1, 0))
                map.setCurrentY(map.getCurrentY() + 1);
        } else if (keycode == KeyEvent.VK_S) {
            map.getGame().Player.move(0, 0);
        } else if (keycode == KeyEvent.VK_O) {
            OPEN_MODE = true;
            map.getGame().LogMessage("В каком направлении вы хотите #8#открыть#^# что-то?#/#");
            flag = false;
        } else if (keycode == KeyEvent.VK_B && event.isShiftDown()) {
            if (map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getID() != Tileset.TILE_STAIR_DOWN)
                map.getGame().LogMessage("Вы #2#не можете#^# спуститься #8#вниз#^# здесь!#/#");
            else
                map.getGame().SwitchMap(+1);
        } else if (keycode == KeyEvent.VK_G && event.isShiftDown()) {
            if (map.field[map.getGame().Player.getY()][map.getGame().Player.getX()].getID() != Tileset.TILE_STAIR_UP)
                map.getGame().LogMessage("Вы #2#не можете#^# подняться #8#вверх#^# здесь!#/#");
            else

                map.getGame().SwitchMap(-1);
        } else if (keycode == KeyEvent.VK_Q && !event.isShiftDown()) {
            if (map.getGame().Player.move(-1, -1)) {
                map.setCurrentY(map.getCurrentY() - 1);
                map.setCurrentX(map.getCurrentX() - 1);
            }
        } else if (keycode == KeyEvent.VK_E && !event.isShiftDown()) {
            if (map.getGame().Player.move(-1, +1)) {
                map.setCurrentY(map.getCurrentY() - 1);
                map.setCurrentX(map.getCurrentX() + 1);
            }
        } else if (keycode == KeyEvent.VK_Z) {
            if (map.getGame().Player.move(+1, -1)) {
                map.setCurrentY(map.getCurrentY() + 1);
                map.setCurrentX(map.getCurrentX() - 1);
            }
        } else if (keycode == KeyEvent.VK_C && !event.isShiftDown()) {
            if (map.getGame().Player.move(+1, +1)) {
                map.setCurrentY(map.getCurrentY() + 1);
                map.setCurrentX(map.getCurrentX() + 1);
            }
        } else
            flag = false;


        mp.repaint();

        if (ID_MODE) {
            if (map.getGame().Player.getInventory().size() < 1) {
                map.getGame().LogMessage("У вас пусто в инвентаре, нечего идентифицировать!");
                ID_MODE = false;
            } else {
                map.getGame().frame1.setFocusable(false);
                map.getGame().frame1.setFocusableWindowState(false);
                message = new ItemSelectMessage();
                message.command = 'b';
                ItemSelectWindow frame2 = new ItemSelectWindow(map.getGame(), Itemset.TYPE_ANY, map.getGame().Player.getInventory(), message);
                frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame2.setTitle("Что вы хотите идентифицировать?");
                frame2.setLocation(map.getGame().frame1.WINDOW_WIDTH / 2 - frame2.WINDOW_WIDTH / 2, map.getGame().frame1.WINDOW_HEIGHT / 2 - frame2.WINDOW_HEIGHT / 2);
                frame2.toFront();
                frame2.setVisible(true);
                ID_MODE = false;
                flag = false;
            }
        }


        if (flag) {
            map.getGame().MonstersAI();
            map.FOV(map.getGame().Player.getY(), map.getGame().Player.getX(), map.getGame().Player.getFOVRAD().getCurrent());
            mp.repaint();
        }


    }

    public void keyReleased(KeyEvent event) {
    }

    ;

    public void keyTyped(KeyEvent event) {
    }

    ;

}