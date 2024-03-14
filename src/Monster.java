import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import processing.core.PImage;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class Monster {
    int x = 245;
    int y = 490;
    int w = 10;
    int h = 10;
    int rol;
    int col;
    int radius = 80;
    int maxMoves = 57;
    boolean right;
    boolean explode = false;
    PImage Explosion;


    //static List<Monster> monsters = Collections.synchronizedList(new ArrayList<>());
    static List<Monster> monsters =new CopyOnWriteArrayList<>();


    PApplet sketch;

    public Monster(PApplet sketch, int rol, int col){
        Random r = new Random();
        this.sketch = sketch;
        this.rol = rol;
        this.col = col;
        right = r.nextBoolean();
        monsters.add(this);
        Explosion = sketch.loadImage("Explosion.png");
    }

    public void remove() {
        monsters.remove(this);
    }

    public static void drawAll() {
        for (Monster m : monsters) {
            if (m != null) m.draw();
        }
    }

    public static void setupAll() {
        for (Monster m : monsters) {
            m.setup();
        }
    }
    public void setup(){
        new Thread(() -> {findPath(rol, col);}).start();
//        findPath(49, 25);
    }

    public void draw(){
        if(!explode) {
            sketch.fill(255, 0, 0, 100f);
            sketch.strokeWeight(0.75f);
            sketch.rect(x, y, w, h);
        }else{
                sketch.image(Explosion, x, y, 60, 60);
        }

    }

    public boolean getExplode(){
        return explode;
    }

    public boolean canMove(int r, int c){
        return r >= 0 && r <= 49 && c >= 0 && c <= 49 && Player.wallGrid[r][c]==0;
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

    public void findPath(int r, int c)  {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        y = 10 * r;
        x = 10 * c + 5;
        if (maxMoves <= 0){
            this.remove();
            for(int i = 0; i<Wall.walls.size(); i++){
                int xCenter = Wall.walls.get(i).getX() + Wall.walls.get(i).getW()/2;
                int yCenter = Wall.walls.get(i).getY() + Wall.walls.get(i).getH()/2;
                double distance = Math.sqrt(Math.pow(xCenter - x, 2) + Math.pow(yCenter - y, 2));

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
            this.remove();
            Player.lives--;
            return;
        }

        if (canMove(r - 1, c)) {
            if(maxMoves == 1)
                explode = true;
            maxMoves--;
            findPath(r - 1, c);
        } else if ((canMove(r, c-1) || canMove(r, c+1))){
            if (canMove(r, c + 1) && right) {
                if(maxMoves == 1)
                    explode = true;
                maxMoves--;
                right = canMove(r, c + 2);
                findPath(r , c + 1);
            } else if(canMove(r, c - 1) && !right) {
                if(maxMoves == 1)
                    explode = true;
                maxMoves--;
                right = !(canMove(r, c - 2));
                findPath(r , c - 1);
            }else{
                right = !right;
                findPath(r, c);
            }
        }else{
            maxMoves = 0;
            findPath(r, c);
        }
    }
//    public void updateGrid(){
//        for(int i = 0; i<Wall.walls.size(); i++){
//            Wall curr = Wall.walls.get(i);
//            if(curr.getVert()){
//                for (int a = 0; a < 7; a++){
//                    if (y/10 + a > 49) {
//                        break;
//                    }
//                    Player.wallGrid[y / 10 + a][x / 10] = 1;
//                }
//            }else{
//                System.out.println("Here1: ");
//                for(int b = 0; b < 7; b++) {
//                    System.out.print(b + ": ");
//                    if (x/10 + b > 49) {
//                        System.out.println("BREAK");
//                        break;
//                    }
//                    Player.wallGrid[y / 10][x / 10 + b] = 1;
//                    System.out.println("Inserted");
//                }
//            }
//        }
//    }


//    public void findPath(int r, int c)  {
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        y = 10 * r;
//        x = 10 * c + 5;
//        if (maxMoves <= 0){
//            this.remove();
//            return;
//        }
//        if (r <= 0) {
//            this.remove();
//            return;
//        }
//        int[] dx = {0, 1, 0, -1};
//        int[] dy = {-1, 0, 1, 0};
//        if (canMove(r + dy[0], c + dx[0])) {
//            int row = r + dy[0];
//            int col = c + dx[0];
//            maxMoves--;
//            findPath(row, col);
//        } else if (!canMove(r + dy[0], c + dx[0]) && (canMove(r, c-1) || canMove(r, c+1))){
//            for (int i = 1; i < 4; i++) {
//                int newRow = r + dy[i];
//                int newCol = c + dx[i];
//                if (canMove(newRow, newCol) && right) {
//                    // lastRun = sketch.millis();
//                    maxMoves--;
//                    findPath(newRow, newCol);
//                    return;
//                } else {
//                    right = !right;
//                }
//            }
//        }else{
//            return;
//        }
//    }


//    private void pause(){
//        try{
//            Thread.sleep(100);
//        }catch(InterruptedException e){
//            System.out.println(e);
//        }
//    }

//    public void findPath(int yG, int xG){
//        int currentTime = sketch.millis();
//        if(currentTime - lastMove >= delay) {
//            if (yG <= 0) {
//                return;
//            }
//            if (xG >= 0 && xG < 50 && yG < 50 && Player.wallGrid[yG][xG] == 0) {
//                y = yG * 10;
//                x = xG * 10;
//                System.out.println(y + " " + x);
//                if (Player.wallGrid[yG - 1][xG] == 0) {
////                    y -= 10;
//                    findPath(yG - 1, xG);
//
//                }else if (Player.wallGrid[yG][xG - 1]==0&&Player.wallGrid[yG][xG]==0){
//                    Random r = new Random();
//                    int direction = r.nextInt(2);
//                    if(direction==0){
//                        findPath(yG, xG - 1);
////                        x -= 10;
//                    }else{
//                        findPath(yG, xG + 1);
////                        x += 10;
//                    }
//                }else if (Player.wallGrid[yG][xG -1]==0){
//                    findPath(yG, xG - 1);
////                    x -= 10;
//                }else if (Player.wallGrid[yG][xG + 1]==0){
//                    findPath(yG, xG + 1);
////                    x +=10;
//                }else if (Player.wallGrid[yG + 1][xG]==0){
//                    findPath(yG + 1, xG);
////                    y += 10;
//                }else{
//                    System.out.println("Trapped");
//                    return;
//                }
//            } else {
//                return;
//            }
//            lastMove = currentTime;
//        }
//    }
}
