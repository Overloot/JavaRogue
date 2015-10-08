package project1;

import project1.BaseTile;

public class Tileset{

	public static final int TILE_EMPTY  = 0;
	public static final int TILE_GRASS = 1;
	public static final int TILE_TREE   = 2;
	public static final int TILE_CLOSED_DOOR   = 3;
	public static final int TILE_OPENED_DOOR = 4;
	public static final int TILE_STAIR_UP = 5;
	public static final int TILE_STAIR_DOWN = 6;
	public static final int TILE_DESTROYED_TOWER_WALL = 7;
	public static final int TILE_TOWER_WALL = 8;
	public static final int TILE_TOWER_FLOOR = 9;
	public static final int TILE_WATER = 10;
	public static final int TILE_DUNGEON_FLOOR = 11;
	public static final int TILE_DUNGEON_WALL = 12;





	public static final int MAX_TILES = 13;

    public static final int TILE_SIZE = 32;

	private static BaseTile[] TILESET;


	public static String getTileName(int id){
		return TILESET[id].getName();
	}
	static{
		TILESET = new BaseTile[MAX_TILES];
		TILESET[TILE_EMPTY] = new BaseTile(TILE_EMPTY, "������ ����", "res/dungeons/empty.png", false, false, false);
		TILESET[TILE_GRASS] = new BaseTile(TILE_GRASS, "�����", "res/dungeons/grass.png", true, true, false);
		TILESET[TILE_TREE] = new BaseTile(TILE_TREE, "������", "res/dungeons/tree.png", false, false, false);
		TILESET[TILE_CLOSED_DOOR] = new BaseTile(TILE_CLOSED_DOOR, "�������� �����", "res/dungeons/closed_door.png", false, false, true);
		TILESET[TILE_OPENED_DOOR] = new BaseTile(TILE_OPENED_DOOR, "�������� �����", "res/dungeons/opened_door.png", true, true, true);
		TILESET[TILE_STAIR_UP] = new BaseTile(TILE_STAIR_UP, "�������� �����", "res/dungeons/stair_up.png", true, true, false);
		TILESET[TILE_STAIR_DOWN] = new BaseTile(TILE_STAIR_DOWN, "�������� ����", "res/dungeons/stair_down.png", true, true, false);
		TILESET[TILE_DESTROYED_TOWER_WALL] = new BaseTile(TILE_DESTROYED_TOWER_WALL, "����������� ����� �����", "res/dungeons/destroyed_tower_wall.png", false, false, false);
		TILESET[TILE_TOWER_WALL] = new BaseTile(TILE_TOWER_WALL,"����� �����", "res/dungeons/tower_wall.png", false, false, false);
		TILESET[TILE_WATER] = new BaseTile(TILE_WATER, "����", "res/dungeons/water.png", true, true, false);
		TILESET[TILE_TOWER_FLOOR] = new BaseTile(TILE_TOWER_FLOOR, "��� �����", "res/dungeons/tower_floor.png", true, true, false);
		TILESET[TILE_DUNGEON_FLOOR] = new BaseTile(TILE_DUNGEON_FLOOR, "��� ����������", "res/dungeons/dungeon_floor.png", true, true, false);
		TILESET[TILE_DUNGEON_WALL] = new BaseTile(TILE_DUNGEON_WALL, "����� ����������", "res/dungeons/dungeon_wall.png", false, false, false);


	}

	public static BaseTile getTile(int id){
		return TILESET[id];
	}

}