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
    List<Floor> floors;

    List<BreakableWall> breakableWalls;

    List<Health> health;

    List<Shield> shield;

    List<Speed> speed;




    ArrayList<GameObject> worldObj;


    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
//                System.out.println(t1.toString());
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
        floors = new ArrayList<>();
        breakableWalls = new ArrayList<>();
        health = new ArrayList<>();
        shield = new ArrayList<>();
        speed = new ArrayList<>();

        InputStreamReader isr = new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("maps/map1.csv"));

        try (BufferedReader mapReader = new BufferedReader(isr)) {
                for (int i = 0; mapReader.ready(); i++) {
                    String[] items = mapReader.readLine().split(",");
//                    System.out.println(Arrays.toString(items));
                    for(int j = 0 ; j< items.length; j++) {
                        String objectType = items[j];
                        System.out.println(items[j]);
//                        GameObject.getNewInstance(objectType, j*30, i*30);
                        if("9".equals(objectType)) {
                            gameObjects.add(new Wall (j*30, i*30, Resources.getSprites("wall")));
                        }
//                        else if("0".equals(objectType)) {
//                            floors.add(new Floor(j*30,i*30,Resources.getSprites("floor")));
//
//                        }
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
        this.shield.forEach(s -> s.drawImage(buffer));
        this.health.forEach(h -> h.drawImage(buffer));
        this.speed.forEach(sp -> sp.drawImage(buffer));
        this.breakableWalls.forEach(b -> b.drawImage(buffer));
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
