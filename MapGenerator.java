import java.awt.*;


public class MapGenerator {
    public int[][] bricks;//array of bricks

    //size of bricks
    public int brickWidth;
    public int brickHeight;

    //size of array of bricks
    public int row;
    public int col;

    //borders of map
    public int borderX = 20;
    public int borderY = 40;


    interface MyLevel {//interface for levels
        boolean setLevel(int i, int j);
    }

    boolean  setLevel1(int i, int j) {
        return (i%3==0||(j)%2==0);
    }//level 1 formula

    boolean  setLevel2(int i, int j) {
        return i == 5 || (j + 1) % 3 == 0 || (i * j + i + j) % 3 == 0;
    }//level 2 formula
    boolean  setLevel3(int i, int j) {
        return (i + 1) % 4 == 0 || (j + 1) % 5 == 0 || i == 0 || j == 0 || j == 23 || i == 14 || ((i + j) % 4 == 0 || (i + j + 2) % 4 == 0);//level 3 formula
    }


    //constructor of MapGenerator
    public MapGenerator(int level) {
        MyLevel p = null;
        //select level
        switch (level) {
            case 1 -> {
                row = 7;
                col = 15;
                p = this:: setLevel1;
            }
            case 2 -> {
                row = 10;
                col = 20;
                p = this:: setLevel2;
            }
            case 3 -> {
                row = 15;
                col = 24;
                p = this:: setLevel3;
            }
        }
        //create bricks
        bricks = new int[row][col];
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if(p.setLevel(i,j)) {
                    bricks[i][j] = 1;
                }
            }
        }
        //calculate size of bricks
        brickWidth = (691-borderX*2) / col;
        brickHeight = (350-borderY*2) / row;
    }



    public void draw(Graphics2D g) {//draw bricks
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + borderX, i * brickHeight + borderY, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + borderX, i * brickHeight + borderY, brickWidth, brickHeight);

                }
            }
        }
    }
    public void setBricksValue(int value , int row , int col){//set value of brick
        bricks[row] [col] = value;
    }
}