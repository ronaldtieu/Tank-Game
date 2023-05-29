package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferDouble;

public class BreakableWall extends Wall {
    float x,y;
    BufferedImage img;

    BreakableWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;

    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, (int) x, (int) y, null);

    }
}
