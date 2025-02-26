package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private static BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;

    List<GameObject> gameObjects;
    Sound bg = Resources.getSound("game_screen");



    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {

            bg.setLooping();
//            bg.setVolume(2.3f);
            bg.play();
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
                this.checkCollision();

                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }



    private void checkCollision() {

        // Check bullet collisions with tanks
        // try to refactor into current for loop
        List<Bullet> t1Bullets = t1.getListOfBullets();
        for (int k = 0; k < t1Bullets.size(); k++) {
            Bullet bullet = t1Bullets.get(k);
            if (bullet.getHitbox().intersects(t2.getHitbox())) {
                t2.collides(bullet);
                bullet.notAlive();
            }
        }

        List<Bullet> t2Bullets = t2.getListOfBullets();
        for (int l = 0; l < t2Bullets.size(); l++) {
            Bullet bullet = t2Bullets.get(l);
            if (bullet.getHitbox().intersects(t1.getHitbox())) {
                t1.collides(bullet);
                bullet.notAlive();
            }
        }

        // Check bullet collisions with overworld objects
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject obj1 = gameObjects.get(i);
            for (int j = 0; j < gameObjects.size(); j++) {
                if (i == j)
                    continue; // to avoid checking the same object
                GameObject obj2 = gameObjects.get(j);
                if (obj1.getHitbox().intersects(obj2.getHitbox())) {
                    System.out.println(obj1 + " has it hit " + obj2);
                    if (obj1 instanceof Tank) {
                        Tank tank = (Tank) obj1;
                        tank.collides(obj2);
                        if (obj2 instanceof Powerup) { // will remove the powerup object on collision
                            // Remove the image from the gameObjects list
                            gameObjects.remove(j);
                            j--; // Adjust the index after removing gameobject
                        }
                    } else if (obj1 instanceof Bullet && obj2 instanceof BreakableWall) { // why is it that when i put obj2 instanceof Tank it, walls are removed on collision but not bullets


                        System.out.println("Bullet hit wall");
                        Bullet bullet = (Bullet) obj1;
                        bullet.notAlive();
                        gameObjects.remove(i); // to remove breakable walls
                        i--; // Adjust the index after removing an element
                    }

                }
            }
        }

    }


    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        gameObjects = new ArrayList<>();


        InputStreamReader isr = new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("maps/map1.csv"));

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            for (int i = 0; mapReader.ready(); i++) {
                String[] items = mapReader.readLine().split(",");
                for(int j = 0 ; j< items.length; j++) {
                    String objectType = items[j];

                    if("9".equals(objectType)) {
                        gameObjects.add(new Wall (j*30, i*30, Resources.getSprites("wall")));
                    }

                    else if("2".equals(objectType)) {
                        gameObjects.add(new BreakableWall(j*30,i*30,Resources.getSprites("break1")));
                    }

                    else if("3".equals(objectType)) {
                        gameObjects.add(new BreakableWall(j*30,i*30,Resources.getSprites("break2")));
                    }

                    else if("4".equals(objectType)) {
                        gameObjects.add(new Speed(j*30,i*30,Resources.getSprites("speed")));
                    }

                    else if("5".equals(objectType)) {
                        gameObjects.add(new Health(j*30,i*30,Resources.getSprites("health")));
                    }

                    else if("6".equals(objectType)) {
                        gameObjects.add(new Shield(j*30,i*30,Resources.getSprites("shield")));
                    }



                }
//                    line = mapReader.readLine();
            }

//                walls.forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("Error reading map");
            throw new RuntimeException(e);
        }

        t1 = new Tank(300, 300, 0, 0, (short) 0, Resources.getSprites("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_F);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(800 , 800, 0, 0, (short) 180, Resources.getSprites("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_PERIOD);
        this.lf.getJf().addKeyListener(tc2);

        this.gameObjects.add(t1);
        this.gameObjects.add(t2);

    }


    private void drawFloor(Graphics2D buffer) {
        BufferedImage floor = Resources.getSprites("floor");
        for(int i = 0;i<GameConstants.GAME_WORLD_WIDTH; i+=320) {
            for(int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    private void renderMiniMap(Graphics2D g2, BufferedImage world) {
        BufferedImage mm = world.getSubimage(0,0,GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(.2,.2); // increases the range of the width and the height
        g2.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH*5/2) - (GameConstants.GAME_WORLD_WIDTH/2) ,
                (GameConstants.GAME_SCREEN_HEIGHT*5) - (GameConstants.GAME_WORLD_HEIGHT)-190,
                null); // minimap
    }

    private void renderSplitScreen(Graphics2D g2, BufferedImage world) {
        BufferedImage lh = world.getSubimage((int)this.t1.getScreen_x(),(int)this.t1.getScreen_y(),GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT); // left half
        BufferedImage rh = world.getSubimage((int)this.t2.getScreen_x(),(int)this.t2.getScreen_y(),GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT); // right half
        g2.drawImage(lh,0,0,null);
        g2.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH/2+4, 0, null);
    }

    private void renderMap(Graphics2D buffer) {
        this.gameObjects.forEach(w -> w.drawImage(buffer));

        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.drawFloor(buffer);

        renderMap(buffer);
        renderSplitScreen(g2, world);
        renderMiniMap(g2, world);


    }


}
