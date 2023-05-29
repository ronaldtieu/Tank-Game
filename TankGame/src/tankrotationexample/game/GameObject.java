package tankrotationexample.game;

import tankrotationexample.Resources;

import java.awt.*;

public abstract class GameObject {

    // 0 - empty space
    // 2 - breakable walls
    // 3 - unbreakable wall
    // 4 - speed
    // 5 - health
    // 6 - shield
    // 9 - outer walls

    public static GameObject getNewInstance(String type, float x, float y) {
        return switch (type) {
            case "0" ->  new Floor(x, y, Resources.getSprites("floor"));
            case "2" -> new BreakableWall(x, y, Resources.getSprites("break1"));
//            case "3" -> new Wall(x, y, Resources.getSprites("break2"));
            case "4" -> new Speed(x, y, Resources.getSprites("speed"));
            case "5" -> new Health(x, y, Resources.getSprites("health"));
            case "6" -> new Shield(x, y, Resources.getSprites("shield"));
            case "9", "3" -> new Wall(x, y, Resources.getSprites("wall"));



            default -> throw new IllegalArgumentException("type not supported");


        };
    }

    public abstract void drawImage(Graphics g);

    public abstract Rectangle getHitbox();
}
