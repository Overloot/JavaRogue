package project1;

import project1.BaseMonster;

public class Monsterset{

	public static final int MONSTER_PLAYER  = 0;


	/*>>>*/public static final int MAX_MONSTERS =10;

	private static BaseMonster[] MONSTERSET;

	static{
		MONSTERSET = new BaseMonster[MAX_MONSTERS];

//!!!
MONSTERSET[0] = new BaseMonster(1,0,"�������","res/monsters/player.png",100,10,10,10,10,5,10,0,0,0,0,0,0,0,0,0,0,0,0,0,10, 5);
MONSTERSET[1] = new BaseMonster(1,1,"����","res/monsters/mouse.png",10,1,12,1,1,1,3,0,0,0,-15,0,0,15,0,0,0,0,2,20,15, 7);
MONSTERSET[2] = new BaseMonster(1,2,"���","res/monsters/beetle.png",20,3,8,2,1,2,5,10,0,0,10,0,0,10,0,0,10,0,0,10,10, 4);
MONSTERSET[3] = new BaseMonster(2,3,"������","res/monsters/goblin.png",35,6,8,6,10,3,7,0,0,0,-20,0,0,0,0,0,0,0,2,20,10, 8);
MONSTERSET[4] = new BaseMonster(3,4,"����","res/monsters/snake.png",50,5,15,5,20,2,7,0,0,0,-30,0,0,0,0,0,-10,3,7,50,10, 6);
MONSTERSET[5] = new BaseMonster(4,5,"����","res/monsters/spider.png",30,3,13,5,30,1,5,0,0,0,-30,0,0,0,0,0,-10,2,5,40,10, 6);
MONSTERSET[6] = new BaseMonster(5,6,"���������","res/monsters/whisp.png",10,1,10,1,1,1,2,0,0,0,0,0,0,0,0,2,0,0,0,0,10, 6);
MONSTERSET[7] = new BaseMonster(6,7,"��� �����","res/monsters/warrior_ghost.png",70,8,15,10,30,10,20,0,0,0,-50,0,0,100,0,0,100,0,0,100,10, 8);
MONSTERSET[8] = new BaseMonster(8,8,"������ �������","res/monsters/rakovina.png",80,6,10,10,30,10,20,10,0,0,-30,0,0,-30,0,0,0,5,10,0,10, 8);
MONSTERSET[9] = new BaseMonster(9,9,"�������� ����","res/monsters/oblako.png",20,1,20,1,1,0,0,0,0,0,100,0,0,100,0,3,100,0,0,100,10, 8);
//!!!
	}

	public static BaseMonster getMonster(int id){
		return MONSTERSET[id];
	}

}
