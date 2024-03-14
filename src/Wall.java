import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Wall {
    //public static List<Wall> walls = Collections.synchronizedList(new ArrayList<>());
    static List<Wall> walls = new CopyOnWriteArrayList<>();
    PApplet sketch;
    int x, y, w, h;

    boolean isVert;
    PImage wallImage;

    public Wall(PApplet sketch, int x, int y, int w, int h, boolean isVert) {
        this.sketch = sketch;
        this.x  = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isVert = isVert;
        wallImage = sketch.loadImage("WallGraphic.png");
        walls.add(this);
    }

    public void draw() {
        //sketch.rect(x, y, w, h);

        sketch.fill(255, 0, 0);

        sketch.image(wallImage, x, y, w, h, 0, 0, wallImage.width, wallImage.height);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getW(){
        return w;
    }

    public int getH(){
        return h;
    }

    public boolean getVert(){
        return isVert;
    }

    public static void drawAll() {
        for (Wall w : walls) {
            if (w != null) w.draw();
        }
    }

    public static void removeWall(Wall w) {
        walls.remove(w);
    }
}
