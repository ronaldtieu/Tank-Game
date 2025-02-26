package tankrotationexample.game;

import org.w3c.dom.css.Rect;
import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{



    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;

    private float R = 8;
    private BufferedImage img;

    private float charge = 3f;

    private Rectangle hitbox;

    private int damage;

    private boolean alive = true;

    Bullet(float x, float y, BufferedImage img, float angle ) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitbox = (new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight()));
        this.damage = 25;


    }



    public boolean isAlive() {
        return alive;
    }
    float getX() {
        return this.x;
    }

    float getY() {
        return this.y;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}


    public Rectangle getHitbox() {
        return this.hitbox.getBounds();
    }

    public int getDamage(){
        return this.damage;
    }

    public boolean notAlive(){
        return alive = false;
    }




    void update() {
        // move forward logic
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int)x, (int)y);

    }



    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
//        if (x < 0 || x >= GameConstants.GAME_WORLD_WIDTH  || y < 0 || y >= GameConstants.GAME_WORLD_HEIGHT - 100) {
//            alive = false;
//        }
    }

    public void increaseCharge() {
        this.charge = this.charge + 0.5f;
    }



    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
//        rotation.scale(6, 6); // scaling of how large the pictures are
        rotation.scale(this.charge, this.charge);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth()+5, this.img.getHeight()+5);

    }


    public void setHeading(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }


}
