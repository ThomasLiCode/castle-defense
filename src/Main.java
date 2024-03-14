import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.sound.*;






public class Main extends PApplet {
    final int START_SCREEN = 0;

    final int INSTRUCTIONS = 1;
    final int LEVEL_1 = 2;
    final int LEVEL_2 = 3;
    final int LEVEL_3 = 4;
    final int LEVEL_4 = 5;
    final int LEVEL_5 = 6;
    final int END_SCREEN = 6;



    int gameState = START_SCREEN;
//    int gameState = LEVEL_3;
    PImage img;

    PImage menu;
    Player player;

    SoundFile background;


    public void settings() {
        size(500, 500);
        pixelDensity(displayDensity());
    }

    public void setup(){
        player = new Player(this);
        new Monster(this, 49, 25);
        background = new SoundFile(this, "Background music.mp3");
        background.play();
//        new Monster(this, 49, 10);
//        new Monster(this, 49, 40);

//        Monster.setupAll();
    }

    public void keyTyped(KeyEvent event) {
        player.keyTyped(event);
    }

    public void mousePressed() {
        if (gameState == START_SCREEN) {
            if (mouseX > 175 && mouseX < 325 && mouseY > 225 && mouseY < 275) {
                gameState = INSTRUCTIONS;
                //Monster.setupAll();
            }
        }else if(gameState == INSTRUCTIONS) {
            if (mouseX > 175 && mouseX < 325 && mouseY > 225 && mouseY < 275) {
                gameState = LEVEL_1;
                Monster.setupAll();
            }
        }else if(gameState == LEVEL_1){
            if (mouseX > 175 && mouseX < 325 && mouseY > 225 && mouseY < 275 && newGame()) {
                gameState = LEVEL_2;
                player.increaseGold(15);
                new Monster(this, 49, 10);
                new Monster(this, 49, 40);
                Monster.setupAll();
            }
        }else if (gameState == LEVEL_2){
            if (mouseX > 175 && mouseX < 325 && mouseY > 225 && mouseY < 275 && newGame()) {
                gameState = LEVEL_3;
                player.increaseGold(15);
                new Monster(this, 49, 10);
                new Monster(this, 49, 40);
                new Monster(this, 49, 25);
                Monster.setupAll();
            }
        }else if (gameState == LEVEL_3) {
            if (mouseX > 175 && mouseX < 325 && mouseY > 225 && mouseY < 275 && newGame()) {
                gameState = LEVEL_4;
                player.increaseGold(15);
                new Monster(this, 49, 16);
                new SuperMonster(this, 49, 32, 65, 0,false, true);
                Monster.setupAll();
            }
        }else if (gameState == LEVEL_4) {
            if (mouseX > 175 && mouseX < 325 && mouseY > 225 && mouseY < 275 && newGame()) {
                gameState = LEVEL_5;
                player.increaseGold(15);
                new SuperMonster(this, 49, 16, 65, 0, false, true);
                new SuperMonster(this, 49, 32, 65, 0, false, true);
                new Monster(this, 49, 25);
                Monster.setupAll();
            }
        }
    }

    public boolean newGame(){
        return Monster.monsters.isEmpty() && SuperMonster.superMonsters.isEmpty();
    }

    public void drawStartScreen(){
        menu =  loadImage("Menu.png");
        image(menu, 0, 0);
        fill(255);
        rect(0, 175, 500,125, 100f);
        textAlign(CENTER, CENTER);
        textSize(40);
        fill(0);
        text("Castle Defense", width/2, height/2 -50);
        fill(255);
        rect(175, 225, 150,50);
        fill(0);
        textSize(32);
        text("Start", width / 2, height / 2 );

//        image(imageVar, imageXPos, imageYPos);

    }

    public void drawGamePlay(){
        img = loadImage("GrassPlain.jpg");
        image(img, 0, 0);
        player.draw();
        fill(255, 215, 0);
        textSize(25);
        text("Gold: " + player.getGold(), width/2 + 100, height/2 -200);
        text("Lives: " + Player.lives, width/2 -100, height/2 -200);
        Monster.drawAll();
        SuperMonster.drawAll();
        Wall.drawAll();
    }

    public void drawEndScreen(){
        background(0,0,0);
        fill(255, 0, 0);
        textAlign(CENTER, CENTER);
        textSize(40);
        text("Game Over!", width/2, height/2);
    }


    public void draw() {
        background(220);
        if (gameState == START_SCREEN) {
            drawStartScreen();
        }
        if(gameState == INSTRUCTIONS){
            fill(255);
            rect(175, 225, 150,50);
            fill(0);
            textSize(32);
            text("To Level 1", width / 2, height / 2 );
            textSize(20);
            text("Defend the top portion of the screen from red and blue", 250, 100);
            text("Monsters by placing walls to delay their approach", 250, 120);
            text("Use W A S D to move around and R to rotate the player's", 250, 140);
            text("orientation. Press Space to place a wall down at the player's", 250, 160);
            text("Current location. Blue monsters will create 2 more blue", 250, 180);
            text("monsters if they are blocked by a horizontal wall. Good Luck!", 250, 200);
        }
        if (gameState == LEVEL_1) {
            drawGamePlay();
        }
        if (Player.lives <= 0) {
            drawEndScreen();
            return;
        }
        if(gameState == LEVEL_1 && newGame()){
            fill(255);
            rect(175, 225, 150,50);
            fill(0);
            textSize(32);
            text("To Level 2", width / 2, height / 2 );
        }
        if(gameState == LEVEL_2){
            drawGamePlay();
        }
        if(gameState == LEVEL_2 && newGame()) {
            fill(255);
            rect(175, 225, 150, 50);
            fill(0);
            textSize(32);
            text("To Level 3", width / 2, height / 2);
        }
        if(gameState == LEVEL_3){
            drawGamePlay();
        }
        if(gameState == LEVEL_3 && newGame()) {
            fill(255);
            rect(175, 225, 150, 50);
            fill(0);
            textSize(32);
            text("To Level 4", width / 2, height / 2);
        }
        if(gameState == LEVEL_4) {
            drawGamePlay();
        }
        if(gameState == LEVEL_4 && newGame()) {
            fill(255);
            rect(175, 225, 150, 50);
            fill(0);
            textSize(32);
            text("To Level 5", width / 2, height / 2);
        }
        if(gameState == LEVEL_5){
            drawGamePlay();
        }
        if(gameState == LEVEL_5 && newGame()){
            background(0,0,0);
            fill(255, 215, 0);
            textAlign(CENTER, CENTER);
            textSize(40);
            text("Congratulations!", width/2, height/2);
        }
    }

    public static void main(String[] args) {PApplet.main("Main");}

}