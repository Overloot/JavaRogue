package project1;

import java.util.LinkedList;

public class Tile {
    public int lastseenID;
    private int id;
    private boolean isVisible;
    private boolean isPassable;
    private boolean isTransparent;
    private boolean isSeen;
    private boolean isBlood;
    private boolean isOpenable;
    private boolean isOpened;
    private boolean isSelected;

    private Monster monster;
    private LinkedList<Item> itemlist;

    public Tile(int id, boolean isPassable, boolean isTransparent, boolean isOpenable) {
        this.id = id;
        this.isPassable = isPassable;
        this.isTransparent = isTransparent;
        this.isVisible = false;
        this.isSeen = false;
        if (id == Tileset.TILE_OPENED_DOOR)
            this.isOpened = true;
        else
            this.isOpened = false;
        itemlist = new LinkedList<Item>();
        this.isOpenable = isOpenable;
        monster = null;
    }

    public void setCursor(boolean b) {
        isSelected = b;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean getBlood() {
        return isBlood;
    }

    public void setBlood(boolean isBlood) {
        this.isBlood = isBlood;
    }

    public boolean getOpened() {
        return isOpened;
    }

    public void setOpened(boolean op) {
        isOpened = op;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public boolean getVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean getPassable() {
        return isPassable;
    }

    public void setPassable(boolean isPassable) {
        this.isPassable = isPassable;
    }

    public boolean getOpenable() {
        return isOpenable;
    }

    public void setOpenable(boolean op) {
        isOpenable = op;
    }

    public boolean getSeen() {
        return isSeen;
    }

    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void AddItem(Item x) {
        itemlist.add(x);
    }

    public int getItemsQty() {
        return itemlist.size();
    }

    public LinkedList<Item> getItemList() {
        return itemlist;
    }

    public boolean getTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }
}