import java.awt.event.*;
import java.util.Timer;
import javax.swing.*;
import java.awt.*;
public class GamePlay extends JPanel implements KeyListener{

    private int score = 0; //gamescore
    private GameStatus status ;//status of game(running, paused, not started, game over)

    private int totalBricks; //total number of bricks on screen

    private java.util.Timer timer;

    private int playerX = 310;

    private int ballposX = 120; // starting x of the ball
    private int ballposY = 350;// starting y of the ball
    private int ballXdir = -1;   // starting directionX of the ball
    private int ballYdir = -2;   // starting directionY of the ball

    private MapGenerator map;// map of bricks
    private int level = 1;//chosen level(default - easy)

    //fonts used
    private static final Font FONT_M_BOLD = new Font("Serif", Font.BOLD, 24);
    private static final Font FONT_S_ITALIC = new Font("Serif", Font.ITALIC, 20);
    private static final Font FONT_L = new Font("Serif", Font.PLAIN, 84);

    //constructor
    public GamePlay() {
        map = new MapGenerator(level);//creating map

        //calculating totalBricks in map
        totalBricks=0;
        for (int i = 0; i < map.bricks.length; i++) {
            for (int j = 0; j < map.bricks[0].length; j++) {
                if (map.bricks[i][j] > 0) {
                    totalBricks ++;
                }
            }}
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        status = GameStatus.NOT_STARTED;//setting status
        timer = new Timer();//creating timer
    }
    private void reset() {//reseting map
        map = new MapGenerator(level);
        totalBricks=0;
        for (int i = 0; i < map.bricks.length; i++) {
            for (int j = 0; j < map.bricks[0].length; j++) {
                if (map.bricks[i][j] > 0) {
                    totalBricks ++;
                }
            }}
        //setting default position of ball and player
        ballposX = 120; //
        ballposY = 350;//
        ballXdir = -1;   //
        ballYdir = -2;   //
        playerX = 310;


        setStatus(GameStatus.RUNNING);//start game after resetting

    }

    //method for drawing centered String
    public void drawCenteredString(Graphics g, String text, Font font, int y) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (700 - metrics.stringWidth(text)) / 2;
        g.setFont(font);
        g.drawString(text, x, y);
    }
    public void paint(Graphics g) {//creating visuals


        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        //drawing map
        map.draw((Graphics2D) g);
        g.setColor(Color.red);
        //creating borders
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(683, 0, 3, 592);
        g.fillRect(0, 560, 692, 3);

        //displaying text
        g.setColor(Color.white);
        g.setFont(FONT_M_BOLD);
        g.drawString("Score " + score, 575, 30);
        g.drawString("Level " + level, 20, 30);
        String msg="";
        g.setFont(FONT_M_BOLD);
        if(status==GameStatus.NOT_STARTED||status==GameStatus.GAME_OVER) {
            msg = "Press 1,2,3 to change level";

        }else if(status==GameStatus.RUNNING){
            msg = "Press SPACE to pause";

        } else if (status==GameStatus.PAUSED) {
            msg = "Press SPACE to unpause";

        }drawCenteredString(g,msg, FONT_S_ITALIC, 30);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        if(status==GameStatus.GAME_OVER&&totalBricks==0){
            g.setColor(Color.green);
            drawCenteredString(g,"VICTORY!", FONT_L, 350);
            drawCenteredString(g,"Press Enter to Restart", FONT_S_ITALIC, 390);

        }

        //drawing ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 18, 18);
        //check if ball out of game
        if (ballposY > 558) {
            status = GameStatus.GAME_OVER;//stop game
            //stop ball movement
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            //display game over
            drawCenteredString(g,"Game Over", FONT_L, 380);
            drawCenteredString(g,"Press Enter to Restart", FONT_S_ITALIC, 400);

        }
        g.dispose();
    }


    private class GameLoop extends java.util.TimerTask {
        public void run() {
            //check if ball hit player
            if (status==GameStatus.RUNNING) {
                if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                    ballYdir = -ballYdir;//change direction
                }

                //check if ball hit bricks
                A:
                for (int i = 0; i < map.bricks.length; i++) {
                    for (int j = 0; j < map.bricks[0].length; j++) {
                        if (map.bricks[i][j] > 0) {
                            int brickX = j * map.brickWidth + 20;
                            int brickY = i * map.brickHeight + 40;
                            int brickWidth = map.brickWidth;
                            int brickHeight = map.brickHeight;

                            Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                            Rectangle ballRect = new Rectangle(ballposX, ballposY, 18, 18);


                            if (ballRect.intersects(rect)) {
                                map.setBricksValue(0, i, j);//delete brick
                                totalBricks--;//change totalBricks
                                score += 5;//change score

                                //check if win
                                if(totalBricks==0)
                                {
                                    setStatus(GameStatus.GAME_OVER);
                                }

                                //check if ball hit borders
                                if (ballposX + 18 <= rect.x || ballposX + 1 >= rect.x + rect.width) {
                                    ballXdir = -ballXdir;

                                } else {
                                    ballYdir = -ballYdir;
                                }
                                break A;
                            }
                        }
                    }

                }
                //move ball
                ballposX += ballXdir;
                ballposY += ballYdir;
                if (ballposX < 0) {
                    ballXdir = -ballXdir;
                }
                if (ballposY < 0) {
                    ballYdir = -ballYdir;
                }
                if (ballposX > 670) {
                    ballXdir = -ballXdir;

                }
            }
            repaint();
        }
    }

    //handling status of game
    private void setStatus(GameStatus newStatus) {
        int delay = 8;//speed of timer
        switch (newStatus) {
            case RUNNING -> {
                //if game started then begin the timer with set speed according to difficulty
                timer.cancel();
                timer = new Timer();
                timer.schedule(new GameLoop(), 0, delay);
            }
            case PAUSED ->
                //pause the timer
                    timer.cancel();
            case GAME_OVER -> {
                //stop timer
                timer.cancel();
                score = 0;
            }
        }
        //update status of the game
        status = newStatus;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {//handling key controls
        if(status==GameStatus.GAME_OVER||status==GameStatus.NOT_STARTED){
            //choose level
            switch (e.getKeyCode()) {
                case '1' -> {
                    level = 1;
                    map = new MapGenerator(level);
                    repaint();
                }
                case '2' -> {
                    level = 2;
                    map = new MapGenerator(level);
                    repaint();
                }
                case '3' -> {
                    level = 3;
                    map = new MapGenerator(level);
                    repaint();
                }
                default -> {
                    setStatus(GameStatus.RUNNING);
                    reset();
                }
            }
        }else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {//pause on SPACE
                if (status == GameStatus.RUNNING || status == GameStatus.PAUSED) {
                    togglePause();
                }
            }
            if(status== GameStatus.RUNNING) {

                //movement of player
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (playerX >= 561) {
                        playerX = 561;
                    }
                } else {
                    moveRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (playerX <= 3) {
                        playerX = 3;
                    }
                } else {
                    moveLeft();
                }
        }
            }
          }

    private void togglePause() {//set pause/unpause
        setStatus(status == GameStatus.PAUSED ? GameStatus.RUNNING : GameStatus.PAUSED);
        repaint();
}
    //sol
    public void moveLeft() {//move player left
       setStatus(GameStatus.RUNNING);
        playerX += 18;
    }

    //sağ
    public void moveRight() {//move player right
        setStatus(GameStatus.RUNNING);
        playerX -= 18;
    }

}
