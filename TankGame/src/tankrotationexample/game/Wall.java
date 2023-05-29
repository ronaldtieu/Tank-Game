package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;

public class Wall extends GameObject{
    float x, y;
    BufferedImage img;
    private Rectangle hitbox;

    Wall(){

    }
    Wall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = (new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight()));

    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, (int) x, (int) y, null);

    }

    public Rectangle getHitbox() {
        return this.hitbox.getBounds();
    }
}
