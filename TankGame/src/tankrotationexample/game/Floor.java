package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Floor extends GameObject{

    private float x, y;
    private BufferedImage img;


    Floor(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, (int) x, (int) y, null);

    }
}
