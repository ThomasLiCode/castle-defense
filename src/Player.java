import processing.core.PApplet;
import processing.event.KeyEvent;

public class Player {
    public static int[][] wallGrid = new int[50][50];
    public boolean isVert = false;
    int x = 215, y = 0, w = 70, h = 10;
    int delay = 60, lastPress = 0;
    int gold = 35;
    public static int lives = 3;
    PApplet sketch;
    public Player(PApplet sketch) {
        this.sketch = sketch;
    }


    public void draw() {
        sketch.fill(255, 0, 0,100f);
        sketch.strokeWeight(0.75f);
        sketch.rect(x,y,w,h);
        if (sketch.keyPressed){
            if (sketch.key == 'w' && y>0){
                y-=10;
            } else if (sketch.key == 's' && y<500-h){
                y+=10;
            }else if (sketch.key == 'a'&& x>0){
                x-=10;
            }else if (sketch.key == 'd' && x<500-w){
                x+=10;
            }
            else if (sketch.key == ' ' && gold - 5 >= 0  && sketch.millis() - lastPress >= delay) {
                gold -= 5;
                new Wall(sketch, x, y, w, h, isVert);
                if(isVert) {
                    for (int i = 0; i < 7; i++){
                        if (y/10 + i > 49) {
                            break;
                        }
                        wallGrid[y / 10 + i][x / 10] = 1;
                    }
                }else{
                    for(int i = 0; i < 7; i++) {
                        if (x/10 + i > 49) {
                            break;
                        }
                        wallGrid[y / 10][x / 10 + i] = 1;
                    }
             }
                for(int i = 0; i<50; i++){
                    for(int j = 0; j<50; j++){
                        System.out.print(wallGrid[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
                System.out.println();
                System.out.println();
            }
            lastPress = sketch.millis();
        }
    }





    public int getGold(){
        return gold;
    }

    public void increaseGold(int n){
        gold += n;
    }

    public void keyTyped(KeyEvent event) {
        if (event.getKey() == 'r') {
            w = isVert ? 70 : 10;
            h = isVert ? 10 : 70;
            isVert = !isVert;
        }
        if (event.getKey() == 32){
            sketch.rect(x, y, w, h);
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
//                    wallGrid[y / 10 + a][x / 10] = 1;
//                }
//            }else{
//                for(int b = 0; b < 7; b++) {
//                    if (x/10 + b > 49) {
//                        break;
//                    }
//                    wallGrid[y / 10][x / 10 + b] = 1;
//                }
//            }
//        }
//    }
}
