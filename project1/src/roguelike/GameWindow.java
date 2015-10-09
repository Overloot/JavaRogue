package project1;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public static int WINDOW_HEIGHT;
    public static int WINDOW_WIDTH;

    static {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        WINDOW_HEIGHT = screenSize.height;
        WINDOW_WIDTH = screenSize.width;
    }

    public MainPanel mainpanel;

    public GameWindow(Map DrawingMap) {
        setTitle("PROJECT1");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        mainpanel = new MainPanel(this, DrawingMap, getScreenTileSizeX(), getScreenTileSizeY());
        Container contentPane = getContentPane();
        contentPane.add(mainpanel);
    }

    public static int getScreenTileSizeX() {
        return (WINDOW_WIDTH / Tileset.TILE_SIZE - 1) - 10;
    }

    public static int getScreenTileSizeY() {
        return (WINDOW_HEIGHT / Tileset.TILE_SIZE - 1);
    }

    public void SwitchMap(Map map) {
        mainpanel.setMap(map);
    }


}
