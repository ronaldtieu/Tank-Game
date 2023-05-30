package tankrotationexample;

import tankrotationexample.game.GameWorld;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.dnd.Autoscroll;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Resources {
    // resource manager class that will be used in launcher
    // using private maps to map keys to images

    private static final Map<String, BufferedImage> sprites = new HashMap<>();
    private static final Map<String, List<BufferedImage>> animations = new HashMap<>();
    private static final Map<String, Sound> sounds = new HashMap<>();
    private static void initSounds(){
        try {
            Resources.sounds.put("pickup", Resources.loadSound("sounds/pickup.wav"));
            Resources.sounds.put("hit", Resources.loadSound("sounds/shotexplosion.wav"));
            Resources.sounds.put("fire", Resources.loadSound("sounds/shotfiring.wav"));
            Resources.sounds.put("main_screen", Resources.loadSound("sounds/main_screen.wav"));
            Resources.sounds.put("game_screen", Resources.loadSound("sounds/game_screen.wav"));
            Resources.sounds.put("bullet", Resources.loadSound("sounds/bullet.wav"));
            Resources.sounds.put("game_screen", Resources.loadSound("sounds/game_screen.wav"));
            Resources.sounds.put("main_screen", Resources.loadSound("sounds/main_screen.wav"));



        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    private static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(
                                Resources.class.getClassLoader().getResource(path)
        ));
        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        s.setVolume(2f);
        return s;
    }

    public static Sound getSound(String type) {
        if(!Resources.sounds.containsKey(type)) {
            throw new RuntimeException("%s is missing from resource map.".formatted(type));
        }
        return Resources.sounds.get(type);
    }

    private static void initSprites() {
        try {
            Resources.sprites.put("tank1", loadSprites("tank/tank1.png"));
            Resources.sprites.put("tank2", loadSprites("tank/tank2.png"));
            Resources.sprites.put("menu", loadSprites("menu/title.png"));
            Resources.sprites.put("wall", loadSprites("walls/unbreak.jpg"));
            Resources.sprites.put("break1", loadSprites("walls/break1.jpg"));
            Resources.sprites.put("break2", loadSprites("walls/break2.jpg"));
            Resources.sprites.put("health", loadSprites("powerups/health.png"));
            Resources.sprites.put("shield", loadSprites("powerups/shield.png"));
            Resources.sprites.put("speed", loadSprites("powerups/speed.png"));
            Resources.sprites.put("bullet", loadSprites("bullet/bullet.jpg"));
            Resources.sprites.put("rocket1", loadSprites("bullet/rocket1.png"));
            Resources.sprites.put("rocket2", loadSprites("bullet/rocket2.png"));
            Resources.sprites.put("floor", loadSprites("floor/bg.bmp"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void loadAssets() {
        initSprites();
        initSounds();
    }

    public static BufferedImage loadSprites (String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        Resources.
                                class.
                                getClassLoader().getResource(path),
                        "Resource %s is not found".formatted(path))
                );
    }


    public static BufferedImage getSprites(String type) {
//        if(Resources.sprites.containsKey(type)) {
//            return Resources.sprites.get(type);
//        }
//            throw new RuntimeException("Resource %s is missing".formatted(type));
//    }
        if(!Resources.sprites.containsKey(type)) {
            throw new RuntimeException("%s is missing from resource map.".formatted(type));
        }
            return Resources.sprites.get(type);
    }


    public static void main(String[] args) throws InterruptedException {
        initSounds();
        Sound bg = Resources.getSound("game_screen");
        bg.setLooping();
        bg.setVolume(.1f);
        bg.play();
        for(;;){}

    }
}
