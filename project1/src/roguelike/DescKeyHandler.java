package project1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class DescKeyHandler implements KeyListener {

    DescPanel dp;


    public DescKeyHandler(DescPanel dp) {
        super();
        this.dp = dp;
    }

    ;


    public synchronized void keyPressed(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.VK_ESCAPE) {
            dp.dwindow.stop();
            return;
        }

    }

    public void keyReleased(KeyEvent event) {
    }

    ;

    public void keyTyped(KeyEvent event) {
    }

    ;

}