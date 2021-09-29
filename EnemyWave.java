package sprites;

import game.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static game.Commons.*;

public class EnemyWave {

    private List<Enemy> enemies;
    private int numberOfEnemies;
    private int enemySpeed;
    public Boss boss;

    public List<Enemy> getEnemies() {
        return enemies;
    }

//    public Boss getBoss(){return boss;}

    public int getNumberOfEnemies() {
        return numberOfEnemies;
    }
    public boolean bossVisible(){ return boss.visible; }

    public void decreaseNumberOfEnemies() {
        numberOfEnemies--;
    }

    public EnemyWave() {            //creating the structure of the enemy

        enemies = new ArrayList<>();
        boss = new Boss(500,15);


        for(int i=0; i<5; i++) { //5
            for (int j = 0; j < 8; j++) { //8
                enemies.add(new Enemy(ENEMY_X + 55 * j, ENEMY_Y + 40 * i));
            }
        }

        numberOfEnemies=40; //40
        enemySpeed=1;
    }

    public void draw(Graphics g, Board board) {

        for (Enemy enemy : enemies) {
            if (enemy.visible)
                enemy.draw(g, board);
            if (enemy.bomb.visible)
                enemy.bomb.draw(g, board);
        }
    }


    public void drawBoss(Graphics g, Board board){ // new
        if (boss.visible)
            boss.draw(g, board);
        if (boss.bomb.visible)
            boss.bomb.draw(g, board);
    }

    public boolean reachedTheGround() {
        for(Enemy enemy: enemies) {                                 //enemies reaches ground
            if (enemy.visible && enemy.y + enemy.height > 550) {
                return true;
            }
        }

        if (boss.visible && boss.y + boss.height > 550) {           //boss reaches ground
            return true;
        }


        return false;
    }



    public void fixStatus() {  //1          //updates the entire enemy wave as its staying alive and getting hit
        for(Enemy enemy : enemies) {

            if(enemy.dying) { //The enemy got hit once //hit point one for the enemy // enemy was hit once //#2
                enemy.setAlmostDied(true); //changes enemy.almostdied to true
                enemy.setDying(false); //changes enemy.dying to false
            }
            else if(enemy.almostDied) { // #3 remove the enemy from the board
                enemy.die();  // enemy not visible anymore, changes enemy.visible to false
                enemy.setAlmostDied(false);
            }
            else if(enemy.visible) //initially enemy not hit by the player, so keeps moving, #1
                enemy.move();

            //
        }
    }


    public void fixStatusBoss() {  //new       //updates the boss wave as its staying alive and getting hit
        if(boss.dying) { //The enemy got hit once //hit point one for the enemy // enemy was hit once //#2
            boss.setAlmostDied(true); //changes enemy.almostdied to true
            boss.setDying(false); //changes enemy.dying to false
        }
        else if(boss.almostDied) { // #3 remove the enemy from the board
            boss.die();  // enemy not visible anymore, changes enemy.visible to false
            boss.setAlmostDied(false);
        }
        else if(boss.visible && numberOfEnemies == 0) //initially enemy not hit by the player, so keeps moving, #1
            boss.move();

    }



    public void bombMove() { //2
        for(Enemy enemy: enemies) {
            if(enemy.bomb.visible) {
                enemy.bomb.move();
            }
        }
    }


    public void bombMoveBoss(int PlayerX, int PlayerY) { //2
        if (boss.bomb.y > 575){
            boss.bomb.move();
        }
        else{

            if (boss.bomb.visible && boss.bomb.y > PlayerY ){

                boss.bomb.y -= 5;

            }
            else if (boss.bomb.visible && boss.bomb.y < PlayerY){

                boss.bomb.y += 5;

            }

            if (boss.bomb.visible && boss.bomb.x > PlayerX){


                boss.bomb.x -= 5;

            }
            else if (boss.bomb.visible && boss.bomb.x < PlayerX){

                boss.bomb.x += 5;


            }



        }





    }

    public void shooting() { //3

        for(Enemy enemy: enemies) {
            enemy.tryToShoot();
        }
    }

    public void shootingBoss() { //3


        boss.tryToShoot();

    }

    public void accelerateIfNeeded() { //4
        boolean b=false;



        if(numberOfEnemies==30) { //30
            enemySpeed = 2;
            b = true;
        }

        if(numberOfEnemies==20) { //20
            enemySpeed = 3;
            b = true;
        }
        if(numberOfEnemies==10) { //10
            enemySpeed = 4;
            b = true;
        }
        if(numberOfEnemies==0){
            boss.visible = true;

        }

        if(b) {

            for (Enemy enemy : enemies) {
                if (enemy.dx > 0) enemy.dx = enemySpeed;
                else enemy.dx = -enemySpeed;
            }
        }
    }

    public void turnAroundIfHitTheWall() { //5


        for(Enemy enemy: enemies) {

            if(enemy.x>BOARD_WIDTH-ENEMY_WIDTH) { // Hitting Right wall: then Enemies start moving right to left
                for(Enemy enemyReversed : enemies) {
                    enemyReversed.dx = -enemySpeed; //right2left
                    enemyReversed.y += 15; // Enemies go down by 15 units
                }
            }

            if(enemy.x<0) {  //Hitting Left wall: then Enemies move left to right
                for(Enemy enemyReversed : enemies) {
                    enemyReversed.dx = enemySpeed; //left2right
                    enemyReversed.y += 15;
                }
            }
        }
    }

    public void turnAroundIfHitTheWallBoss() {
        if (boss.x>BOARD_WIDTH-ENEMY_WIDTH){
            boss.dx = -20; //right2left //enemySpeed
            boss.y += 15; // Enemies go down by 15 units
        }

        if(boss.x<0) {  //Hitting Left wall: then Enemies move left to right

            boss.dx = 20; //left2right //enemySpeed
            boss.y += 15;

        }
    }
}

