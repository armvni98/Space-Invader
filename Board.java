package game;

import sprites.Enemy;
import sprites.EnemyWave;
import sprites.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import static game.Commons.*;

public class Board extends JPanel implements Runnable {

    private Player player;
    private EnemyWave enemyWave;

    private boolean inGame;     //
    private int lives;      // Player lives = 3
    private String message;     // message for the end of a game

    private Image image;

    Board() {

        ImageIcon ii = new ImageIcon("spac.png");
        image = ii.getImage();
        inGame=true;
        lives=3;

        player=new Player(START_X, START_Y);
        enemyWave = new EnemyWave();



        addKeyListener(new KAdapter());     //for Key events
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    @Override
    public void addNotify() {
        super.addNotify();              //Sets up with keyboardActionEvents

        Thread animator = new Thread(this);
        animator.start();               //two thread run concurrently, the run method and main method
    }

    @Override
    public void run() { // part of the runnable interface

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while(inGame) {
            repaint(); //calls the paintComponent as needed
            animationCycle();       //mechanics of a game

            timeDiff = System.currentTimeMillis() - beforeTime;
            //System.out.println("TimeDiff" + " " + timeDiff);
            sleep = DELAY - timeDiff; // If the repaint() and animationCycle() takes more time to run then make theread sleep less,to make the game faster

            if(sleep<0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep); //How often I want the graphics to move //sleep
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beforeTime=System.currentTimeMillis();
        }
        gameOver();
    }

    @Override
 public void paintComponent(Graphics g) {

                            /*helpful metaphor - The JPanel is the canvas,
                            - - JFrame is the holder of the canvas
                            - the Graphics object g is your paintbrush, and
                            - super.paintComponent(g) is your eraser.
                             */


        super.paintComponent(g); //erase and repaint
                                 //Bottom Layer
        g.drawImage(image, 0, 0,1000,700,this);     //background image painted
        Font font = new Font("Helvetica", Font.PLAIN, 15);
        g.setColor(Color.WHITE);
        g.setFont(font);

        g.drawString("Lives: " + "" + lives, BOARD_WIDTH - 90, 20);

        if (enemyWave.getNumberOfEnemies() != 0) {
            g.drawString("Enemies Left: " + "" + enemyWave.getNumberOfEnemies(), 28, 20);
        }

        player.draw(g, this);
        if (player.getM().isVisible())
            player.getM().draw(g, this);

        enemyWave.draw(g, this);

        if (enemyWave.getNumberOfEnemies() == 0) {
            enemyWave.drawBoss(g, this);
        }


    }

    private void animationCycle() {

        if(enemyWave.getNumberOfEnemies()==0){ //== 0

            bossMove();
        }

        if (enemyWave.bossVisible()==false){
            inGame=false;
            message = "You Won! Congrats! Press 'R' to play again";
        }

        if(player.isDying()) {
            lives--;
            if(lives!=0){
                player.revive();       //revives in original position, first checking lives count
            }
            else {
                inGame=false;
                message = "Game Over! Press 'R' to play again";
            }
        }

        if(enemyWave.reachedTheGround()) {  //if the enemy waves reaches ground
            inGame=false;
            message="Game Over! Press 'R' to play again";
        }

        player.move();
        player.missleMove();
        enemyWaveMove();
        collisionMissileEnemies();
        collisionBombPlayer();
        collisionBossBombPlayer();
    }
    public static void play(String filename)        //audio playing method
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }


    }


    private void enemyWaveMove() {
        enemyWave.fixStatus();
        enemyWave.bombMove();
        enemyWave.shooting();
        enemyWave.accelerateIfNeeded();
        enemyWave.turnAroundIfHitTheWall();
    }
    private void bossMove(){
        enemyWave.fixStatusBoss();
        enemyWave.bombMoveBoss(player.x,player.y);
        enemyWave.shootingBoss();
        enemyWave.accelerateIfNeeded();
        enemyWave.turnAroundIfHitTheWallBoss();

    }

    private void collisionMissileEnemies() {
        if(player.getM().isVisible()) {
            for (Enemy enemy : enemyWave.getEnemies())
                if(enemy.isVisible() && player.getM().collisionWith(enemy)) {
                    enemy.explosion();
                    enemyWave.decreaseNumberOfEnemies();
                    player.getM().die();
                    play("Explosion+1.wav");
                }

            if (enemyWave.boss.isVisible() && player.getM().collisionWith(enemyWave.boss) && enemyWave.getNumberOfEnemies() == 0){
                enemyWave.boss.explosion();
                player.getM().die();
                inGame = false; //testing
                message = "You won! Press 'R' to play again";
            }
        }


    }

    private void collisionBombPlayer() {
        for(Enemy enemy : enemyWave.getEnemies()) {
            if (enemy.getBomb().isVisible() && enemy.getBomb().collisionWith(player)) {
                player.explosion();
                enemy.getBomb().die();
                play("Explosion+1.wav");

            }
        }

        if (enemyWave.boss.isVisible() && enemyWave.boss.getBomb().collisionWith(player)){
            player.explosion();
            enemyWave.boss.getBomb().die();
            play("Explosion+1.wav");

        }
    }

    private void collisionBossBombPlayer() {
            if (enemyWave.boss.getBomb().isVisible() && enemyWave.boss.getBomb().collisionWith(player.getM())) {
                enemyWave.boss.getBomb().die();
                player.getM().die();
            }
        }




    private void gameOver() {
        Graphics g = this.getGraphics();
        super.paintComponent(g);

        Font font = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics ft = this.getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(message, (BOARD_WIDTH-ft.stringWidth(message))/2, BOARD_HEIGHT/2);
    }

    private class KAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

    }
}
