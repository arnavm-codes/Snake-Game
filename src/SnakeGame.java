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

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

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
        //g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        // snake head
        g.setColor(Color.GREEN);
        //g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);

        // snake body
        for(int i=0; i<snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize );
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }
    }

/***********************************************************************************************************************/

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
    }

/***********************************************************************************************************************/
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
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
