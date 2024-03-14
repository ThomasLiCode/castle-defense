import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;
import java.util.List;

public class SuperMonster {
    int x = 245;
    int y = 490;
    int w = 10;
    int h = 10;
    int maxMake = 100;
    int numMade;
    boolean TF = false;
    int col;
    int row;
    int radius = 70;
    int maxMoves;
    boolean right = true;
    boolean justSplit;
    boolean explode = false;
    PImage Explosion;

    //static List<SuperMonster> superMonsters = Collections.synchronizedList(new ArrayList<>());
    static List<SuperMonster> superMonsters = new CopyOnWriteArrayList<>();
    PApplet sketch;

    public SuperMonster(PApplet sketch, int row, int col, int maxMoves, int numMade, boolean justSplit, boolean right){
        this.row = row;
        this.col = col;
        this.justSplit = justSplit;
        this.right = right;
        this.sketch = sketch;
        this.maxMoves = maxMoves;
        this.numMade = numMade;
        Explosion = sketch.loadImage("Explosion.png");
        superMonsters.add(this);

        new Thread(() -> {findPath(row, col, this);}).start();
    }

    public void setup(){

//        findPath(49, 25);
    }

    public void draw(){
        if(!explode) {
            sketch.fill(0, 0, 255, 100f);
            sketch.strokeWeight(0.75f);
            sketch.rect(x, y, w, h);
        }else{
            sketch.image(Explosion, x, y, 60, 60);
        }
    }
    public boolean canMove(int r, int c){
        return r >= 0 && r <= 49 && c >= 0 && c <= 49 && Player.wallGrid[r][c]==0;
    }

    public static void drawAll() {
        for (SuperMonster m : superMonsters) {
            if (m != null) m.draw();
        }
    }

    public static void setupAll() {
        for (SuperMonster m : superMonsters) {
            m.setup();
        }
    }

    public void setZero(Wall w){
        if(w.getVert()) {
            for (int i = 0; i < 7; i++) {
                if (w.getY() / 10 + i > 49) {
                    break;
                }
                Player.wallGrid[w.getY() / 10 + i][w.getX() / 10] = 0;
            }
        }else {
            for (int i = 0; i < 7; i++) {
                if (w.getX() / 10 + i > 49) {
                    break;
                }
                Player.wallGrid[w.getY()  / 10][w.getX() / 10 + i] = 0;
            }
        }
    }

    public void setOne(Wall w){
        if(w.getVert()) {
            for (int i = 0; i < 7; i++) {
                if (w.getY() / 10 + i > 49) {
                    break;
                }
                Player.wallGrid[w.getY() / 10 + i][w.getX() / 10] = 1;
            }
        }else {
            for (int i = 0; i < 7; i++) {
                if (w.getX() / 10 + i > 49) {
                    break;
                }
                Player.wallGrid[w.getY()  / 10][w.getX() / 10 + i] = 1;
            }
        }
    }

//    public boolean getSplit(){
//        return justSplit;
//    }
//
//    public boolean getRight(){
//        return right;
//    }
//
//    public void setX(int n){
//        x = n;
//    }
//
//    public void setY(int n){
//        y = n;
//    }

    public void findPath(int r, int c, SuperMonster m){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        m.y = r * 10;
        m.x = c * 10 + 5;
        if (maxMoves <= 0){
            superMonsters.remove(this);
            for(int i = 0; i<Wall.walls.size(); i++){
                int xCenter = Wall.walls.get(i).getX() + Wall.walls.get(i).getW()/2;
                int yCenter = Wall.walls.get(i).getY() + Wall.walls.get(i).getH()/2;
                double distance = Math.sqrt(Math.pow(xCenter - m.x, 2) + Math.pow(yCenter - m.y, 2));

                if(distance <= radius){
                    System.out.println(x + " " + y + " " + xCenter + " " + yCenter);
                    System.out.println(distance);
                    System.out.println(Wall.walls.get(i));
                    setZero(Wall.walls.get(i));
                    Wall.removeWall(Wall.walls.get(i));
                    i--;
                }
                for(int a = 0; a<Wall.walls.size(); a++){
                    setOne(Wall.walls.get(a));
                }
            }
            return;
        }
        if (r <= 0) {
            superMonsters.remove(this);
            Player.lives--;
            return;
        }
        if(!canMove(r-1, c) && m.justSplit){
            if (canMove(r, c + 1) && m.right) {
                if(maxMoves == 1)
                    explode = true;
                maxMoves--;
                m.right = canMove(r, c + 2);
                findPath(r , c + 1, this);
            } else if(canMove(r, c - 1) && !m.right) {
                if(maxMoves == 1)
                    explode = true;
                maxMoves--;
                m.right = !(canMove(r, c - 2));
                findPath(r , c - 1, this);
            }else{
                m.right = !m.right;
                findPath(r, c, this);
            }
        } else if (canMove(r - 1, c)) {
            if(maxMoves == 1)
                explode = true;
            TF = false;
            m.justSplit = false;
            maxMoves--;
            findPath(r - 1, c, m);
        }else{
            if(!TF && m.numMade<=maxMake) {
                TF = true;
                int temp = this.maxMoves + 10;
                int numCreated = m.numMade += 2;
                superMonsters.remove(this);
                System.out.println("hello");
                SuperMonster m1 = new SuperMonster(sketch, r, c, temp, numCreated, true, false);
                SuperMonster m2 = new SuperMonster(sketch, r, c, temp, numCreated, true, true);
                findPath(r, c, m1);
                findPath(r, c, m2);
            }else{
                m.justSplit = true;
                findPath(r, c, m);
            }
        }
    }

}
