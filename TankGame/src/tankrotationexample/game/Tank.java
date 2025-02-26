package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject{

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float screen_x, screen_y;
    private float angle;

    Bullet b;
    List<Bullet> ammo = new ArrayList<>();

    long timeSinceLastShot = 0L;
    long cooldown = 1000; // miliseconds
    Bullet currentChargeBullet = null;

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;
    private Rectangle hitbox;

    private int health;

    private int lives;

    private int shield;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img ) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitbox = (new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight()));
        this.health = 100;
        this.lives = 3;
        this.shield = 0;

    }

    public List<Bullet> getListOfBullets(){
        return ammo;
    }

    public void increaseHealth() {
        this.health = health + 25;
        if(health > 100) {
            health = 100;
        }

    }

    public void increaseShield() {
        this.shield = shield + 25;
    }

    public int getShield() {
        return this.shield;
    }

    public int getHealth() {
        return this.health;
    }

    public int getLives() {
        return this.lives;
    }

    public Rectangle getHitbox() {
        return this.hitbox.getBounds();
    }

    public float getScreen_x() {
        return screen_x;
    }

    public float getScreen_y() {
        return screen_y;
    }

    private void centerScreen() {
        int xLowerBound = GameConstants.GAME_SCREEN_WIDTH / 4;
        int xUpperBound = GameConstants.GAME_WORLD_WIDTH - xLowerBound;
        int yLowerBound = GameConstants.GAME_SCREEN_HEIGHT / 2;
        int yUpperBound = GameConstants.GAME_WORLD_HEIGHT - yLowerBound;


        if(getX() > xLowerBound && getX() < xUpperBound) {
            this.screen_x = Math.abs(getX() - GameConstants.GAME_SCREEN_WIDTH / 4);
        }

        if(getY() > yLowerBound && getY() < yUpperBound) {
            this.screen_y = Math.abs(getY() - GameConstants.GAME_SCREEN_HEIGHT / 2);

        }

    }

    float getX() {
        return this.x;
    }

    float getY() {
        return this.y;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if(this.shootPressed && ((this.timeSinceLastShot + this.cooldown) < System.currentTimeMillis())) {
//            b = new Bullet(x, y, Resources.getSprites("bullet"), angle);
            this.timeSinceLastShot = System.currentTimeMillis();

            Resources.getSound("fire").play();
            this.ammo.add(new Bullet(x, y, Resources.getSprites("bullet"), angle));


        }



        this.ammo.forEach(bullet->bullet.update());
        this.hitbox.setLocation((int)x, (int)y);

        for(int i = 0; i <ammo.size(); i ++) {
            if(ammo.get(i).isAlive() == false) {
                ammo.remove(i);
                i--;
            }
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        centerScreen();
        checkBorder(); // ensure we do not go off screen when using this key
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        centerScreen();
        checkBorder();
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
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
//        rotation.scale(6, 6); // scaling of how large the pictures are
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth()+5, this.img.getHeight()+5);

        this.ammo.forEach(bullet->bullet.drawImage(g2d));

        g2d.setColor(Color.GREEN);
        g2d.drawRect((int)x, (int)y-20, 100, 15); // shooting cooldown
        long currentWidth = 100 - ((this.timeSinceLastShot + this.cooldown) - System.currentTimeMillis())/10; // the divisor is what would gauge what the cooldown visually is
        if(currentWidth > 100) {
            currentWidth = 100;
        }
        g2d.fillRect((int)x, (int)y-20, (int)currentWidth, 15);
    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;
    }

    public void collides(GameObject with){
        if(with instanceof Wall) {
            this.moveBackwards();
        } else if (with instanceof Powerup) {
            Resources.getSound("pickup").play();
            ((Powerup) with).applyPower(this);
        } else if (with instanceof Tank) {
            this.moveBackwards();
        } else if (with instanceof Bullet) {
            this.inflictDamage();
        }

    }

    public void speedUp() {
        this.R = (float) (R * 1.2);
    }

    public void inflictDamage() {
        if(health == 0) {
            this.lives = lives - 1;
            health = 100; // reset health
        }



        if(shield > 0) {
            shield = shield - 25;

        } else {
            this.health = health - 25;
        }

        checkLives();


        System.out.println("Tank has taken damage.");
        System.out.println("Tank shield = " + shield);

        System.out.println("Tank health = " + health);
    }

    private void checkLives() {

        if(lives == 0 && health == 0) {
            System.out.println("Game Over");
        }
    }
}
