import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x, y;
        Tile(int x, int y)
        {
            this.x= x;
            this.y=y;  
        }
    }
    int boardHeight;
    int boardWidth;
    int tileSize = 25;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody; 

    // food
    Tile food;

    Random random;

    final int wrapGapStart = 9;
    final int wrapGapEnd = 13;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

/***********************************************************************************************************************/

    // CONSTRUCTOR
    SnakeGame(int boardWidth, int boardHeight)
    {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);

        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;


        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

/***********************************************************************************************************************/

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

/***********************************************************************************************************************/

    public void draw(Graphics g)
    {
        // grid
        // for (int i=0; i< boardWidth/tileSize; i++)
        // {   // (x1, y1, x2, y2)
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        //     g.drawLine(0, i*tileSize , boardWidth, i*tileSize);
        // }

        // food
        g.setColor(Color.RED);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        // snake head
        g.setColor(Color.GRAY);
        //g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);

        // snake body
        g.setColor(Color.GREEN);
        for(int i=0; i<snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize );
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        // score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) 
        {
            g.setColor(Color.RED);
            g.drawString("Game Over!   Score:" + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }
        else
        {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }

        // drawing bolder edges
        g.setColor(Color.ORANGE);
        int thickness =10;
        // for left and right walls
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(thickness));
        // for top and bottom walls
        Graphics2D g3 = (Graphics2D) g;
        g3.setStroke(new BasicStroke(thickness));

        // // left wall draw
        // g2.drawLine(0, tileSize, 0, boardHeight - tileSize);
        // // right wall draw
        // g2.drawLine(boardWidth - 1, tileSize, boardWidth - 1, boardHeight - tileSize);
        
        // draw left wall with visible gap
        for (int i = 0; i < boardHeight / tileSize; i++) 
        {
            if (i < wrapGapStart || i > wrapGapEnd) 
            {
                int iPos = i * tileSize;
                g2.drawLine(0, iPos, 0, iPos + tileSize);
            }
        }

        // draw right wall with visible gap
        for (int i = 0; i < boardHeight / tileSize; i++) 
        {
            if (i < wrapGapStart || i > wrapGapEnd) 
            {
                int iPos = i * tileSize;
                g2.drawLine(boardWidth - 1, iPos, boardWidth - 1, iPos + tileSize);
            }
        }
        // top wall draw
        g3.drawLine(0, 0, boardWidth, 0);
        // bottom wall draw
        g3.drawLine(0, boardHeight-1, boardWidth, boardHeight-1);



        // left and right wall indicators
        int wallSegmentLength = tileSize*2;

        // left "top" amd "bottom" stub
        g2.drawLine(0,0,0,wallSegmentLength);
        g2.drawLine(0, boardHeight-wallSegmentLength, 0, boardHeight);
        // Right "top" amd "bottom" stub
        g2.drawLine(boardWidth - 1, 0, boardWidth - 1, wallSegmentLength);
        g2.drawLine(boardWidth - 1, boardHeight - wallSegmentLength, boardWidth - 1, boardHeight);
    
    }

/*****************************************************  ******************************************************************/

    public void placeFood()
    {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);

        // 600/25=24;  x and y would be random positions on the grid ranging between 0-24
    }

/***********************************************************************************************************************/

    public boolean collisions(Tile tile1, Tile tile2)
    {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

/***********************************************************************************************************************/
    
    public void move()
    {
        // eating food
        if(collisions(snakeHead, food))
        {
           snakeBody.add(new Tile(food.x, food.y));
           placeFood(); 
        }

        // snake body
        for(int i=snakeBody.size()-1; i>=0; i--)
        {
           Tile snakePart = snakeBody.get(i);
           if(i == 0)
           {
            snakePart.x = snakeHead.x;
            snakePart.y = snakeHead.y;
           } 
           else
           {
            Tile prev_snakePart = snakeBody.get(i-1);
            snakePart.x = prev_snakePart.x;
            snakePart.y = prev_snakePart.y;
           }
        }

        // snake head
        snakeHead.x += velocityX;
        snakeHead.y  += velocityY;


        // game over conditions
        for(int i=0; i<snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            if(collisions(snakeHead, snakePart))
            {
                System.out.println("Game Over! Can't eat yourself now, can you?");
                gameOver = true;
            }

            // vertical collision = game over
            else if (snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
                System.out.println("Game Over! Hit the top/bottom wall");
                gameOver = true;
            }
            
        }

        // if(snakeHead.x < 0) snakeHead.x = boardWidth/tileSize-1;
        // if(snakeHead.x >= boardWidth/tileSize) snakeHead.x = 0;
        // if(snakeHead.y < 0) snakeHead.y = boardHeight/tileSize-1;
        // if(snakeHead.y >= boardHeight/tileSize) snakeHead.y = 0;  
        // horizontal wrapping with gap condition
        if (snakeHead.x < 0) {
            if (snakeHead.y >= wrapGapStart && snakeHead.y <= wrapGapEnd) 
            {
                snakeHead.x = boardWidth / tileSize - 1;
            } else {
                System.out.println("Game Over! Hit the solid left wall.");
                gameOver = true;
            }
        } else if (snakeHead.x >= boardWidth / tileSize) {
            if (snakeHead.y >= wrapGapStart && snakeHead.y <= wrapGapEnd) 
            {
                snakeHead.x = 0;
            } else {
                System.out.println("Game Over! Hit the solid right wall.");
                gameOver = true;
            }
        }

        //vertical wall collision = instant death
        if (snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) 
        {
            System.out.println("Game Over! Hit the top/bottom wall.");
            gameOver = true;
        }
    }

/***********************************************************************************************************************/
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if (gameOver) gameLoop.stop();
    }

/***********************************************************************************************************************/

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1)
        {
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1)
        {
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1)
        {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1)
        {
            velocityX = 1;
            velocityY = 0;
        }
    }
    // not needed
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
