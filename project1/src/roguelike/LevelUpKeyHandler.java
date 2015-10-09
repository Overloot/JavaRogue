package project1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class LevelUpKeyHandler implements KeyListener {

    LevelUpPanel lp;


    public LevelUpKeyHandler(LevelUpPanel lp) {
        super();
        this.lp = lp;
    }

    ;


    public synchronized void keyPressed(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.VK_UP) {
            lp.select--;
            if (lp.select == 0) lp.select = 3;
        } else if (keycode == KeyEvent.VK_DOWN) {
            lp.select++;
            if (lp.select == 4) lp.select = 1;
        } else if (keycode == KeyEvent.VK_ENTER) {
            lp.lwindow.game.statsfree--;

            switch (lp.select) {
                case 1: {
                    lp.lwindow.game.Player.getSTR().setCurrent(lp.lwindow.game.Player.getSTR().getCurrent() + 1);
                    lp.lwindow.game.Player.getSTR().setMax(lp.lwindow.game.Player.getSTR().getMax() + 1);

                    lp.x1++;
                    lp.lwindow.game.Player.getHP().setMax(lp.lwindow.game.Player.getHP().getMax() + lp.lwindow.game.HP_PER_STR);
                    lp.lwindow.game.Player.getHP().setCurrent(lp.lwindow.game.Player.getHP().getCurrent() + lp.lwindow.game.HP_PER_STR);
                    lp.lwindow.game.Player.getCW().setMax(lp.lwindow.game.Player.getCW().getMax() + lp.lwindow.game.CW_PER_STR);
                }
                break;
                case 2: {
                    lp.lwindow.game.Player.getAGI().setCurrent(lp.lwindow.game.Player.getAGI().getCurrent() + 1);
                    lp.lwindow.game.Player.getAGI().setMax(lp.lwindow.game.Player.getAGI().getMax() + 1);

                    lp.x2++;

                }
                break;
                case 3: {

                    lp.lwindow.game.Player.getEND().setCurrent(lp.lwindow.game.Player.getEND().getCurrent() + 1);
                    lp.lwindow.game.Player.getEND().setMax(lp.lwindow.game.Player.getEND().getMax() + 1);

                    lp.x3++;
                    lp.lwindow.game.Player.getHP().setMax(lp.lwindow.game.Player.getHP().getMax() + lp.lwindow.game.HP_PER_END);
                    lp.lwindow.game.Player.getHP().setCurrent(lp.lwindow.game.Player.getHP().getCurrent() + lp.lwindow.game.HP_PER_END);


                }
                break;
            }
            if (lp.lwindow.game.statsfree == 0) {
                lp.lwindow.stop();
                return;
            }
        }
        lp.repaint();

    }

    public void keyReleased(KeyEvent event) {
    }

    ;

    public void keyTyped(KeyEvent event) {
    }

    ;

}