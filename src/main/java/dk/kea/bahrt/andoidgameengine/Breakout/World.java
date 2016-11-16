package dk.kea.bahrt.andoidgameengine.Breakout;

import java.util.ArrayList;
import java.util.List;

/**
 * Logical game world HANDLES LOGIC ;-)
 */
public class World {

    public static float MIN_X = 0;
    public static float MIN_Y = 32;
    public static float MAX_X = 319;
    public static float MAX_Y = 479;

    Ball ball = new Ball();
    Paddle paddle = new Paddle();

    List<Block> blocks = new ArrayList<>();

    public boolean gameOver = false;

    public int points = 0;
    public int paddleHits = 0;
    public int advance = 0;

    public int lives= 3;
    public boolean lostLife = false;

    public CollisionListener listener;

    public World(CollisionListener listener)
    {
        this.listener = listener;
        generateBlocks();
    }

    private void generateBlocks()
    {
        blocks.clear();
        for (int y = 60, type = 0; y < 60 + 8 * Block.HEIGHT; y = y + (int) Block.HEIGHT, type++) //row for loop
        {
            for (int x = 20; x < 320 - Block.WIDTH; x = x + (int) Block.WIDTH) //col for loop
            {
                blocks.add(new Block(x, y, type));
            }
        }
    }

    public void update(float deltaTime, float accelX, int touchX)
    {

        if (ball.y > MAX_Y - Ball.HEIGHT) //ball touches bottom screen
        {
            lives--;
            lostLife = true;
            ball = new Ball(); //'resetting' ball, trusting the garbage collector - the lazy solution
            // take a break and show the user something happened?
            if (lives == 0)
            {
                gameOver = true;
            }
            return;
        }

        if (blocks.size() == 0)
        {
            generateBlocks();
        }

        //AI for ball
        ball.x = ball.x + ball.vx * deltaTime;
        ball.y = ball.y + ball.vy * deltaTime;
        collideBallWalls();
        //AI for paddle
        paddle.x = paddle.x - accelX * 40 * deltaTime; // *a* scaling constant, sensitivity
        paddle.x = touchX - paddle.WIDTH / 2; //for test only
        collidePaddleWalls();

        collideBallPaddle();
        collideBallBlocks();

    }

    private void collidePaddleWalls() {
        if (paddle.x < MIN_X) {
            paddle.x = MIN_X;
        }
        if (paddle.x > MAX_X - paddle.WIDTH) {
            paddle.x = MAX_X - paddle.WIDTH;
        }
    }

    private void collideBallWalls()
    {
        if (ball.x < MIN_X) {
            ball.x = MIN_X;
            ball.vx = -1 * ball.vx;
            listener.collisionWall(); //asking system to react to collision in the game!
        }
        if (ball.x > MAX_X - ball.WIDTH) // Make operation in different scope
        {
            ball.x = MAX_X - ball.WIDTH;
            ball.vx = -1 * ball.vx;
            listener.collisionWall();
        }
        if (ball.y < MIN_Y) {
            ball.y = MIN_Y;
            ball.vy = -1 * ball.vy;
            listener.collisionWall();
        }
    }

    private void collideBallPaddle() {
        if (ball.y > paddle.y)
        {
            return;
        }
        if (ball.x >= paddle.x &&
                ball.x + Ball.WIDTH <= paddle.x + Paddle.WIDTH &&
                ball.y + Ball.HEIGHT >= paddle.y) //PERFORMANCE
        {
            ball.y = paddle.y - Ball.HEIGHT - 1; // prone to failure without -1
            ball.vy = -1 * ball.vy;
            //advanceBlocks();
            listener.collisionPaddle();
            return;
        }
        if (ball.x + Ball.WIDTH >= paddle.x &&
                ball.x <= paddle.x + Paddle.WIDTH &&
                ball.y + Ball.HEIGHT >= paddle.y) //needs test
        {
            ball.y = paddle.y - Ball.HEIGHT - 1;
            ball.vy = -1 * ball.vy;
            ball.vx = -1 * ball.vx;
            //advanceBlocks();
            listener.collisionPaddle();
        }
    }

    private void advanceBlocks()
    {
        paddleHits++;
        if (paddleHits == 3)
        {
            paddleHits = 0;
            //if (advance > 200) return; //Limit
            //Advance Blocks
            int stop = blocks.size();
            Block block = blocks.get(0); //temp block
            if (block.y > 250) return;
            for (int i = 0; i < stop; i++)
            {
                block = blocks.get(i);
                block.y = block.y + 10; //advance by 10
            }
        }
    }

    private void collideBallBlocks()
    {
        int stop = blocks.size();
        Block block = null;
        for (int i = 0; i < stop; i++) {
            block = blocks.get(i);
            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                    block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                points = points + (10 - block.type);
                blocks.remove(i);
                i--;
                stop--;
                deflectBall(ball, block);
                advanceBlocks();
                listener.collisionBlock();
            }
        }
    }

    private boolean collideRects(float x1, float y1, float width1, float height1,
                                 float x2, float y2, float width2, float height2)
    {

        if (x1 < x2 + width2 &&  //Left edge object1 one is to the left of right edge object2
                x1 + width1 > x2 &&
                y1 + height1 > y2 && //Bottom edge object1 is below top egde object2
                y1 < y2 + height2) //Top egde object1 above bottom edge object2
        {
            return true;
        }
        return false;
    }

    private void deflectBall(Ball ball, Block block)
    {
        //check the top left corner of teh block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y, 1, 1))
        {
            if (ball.vx > 0) ball.vx = -1 * ball.vx;
            if (ball.vy > 0) ball.vy = -1 * ball.vy;
            return;
        }
        //Check the top right corner of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y, 1, 1))
        {
            if (ball.vx < 0) ball.vx = -1 * ball.vx;
            if (ball.vy > 0) ball.vy = -1 * ball.vy;
            return;
        }
        //check the bottom left corner of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y + Block.HEIGHT, 1, 1))
        {
            if (ball.vx > 0) ball.vx = -1 * ball.vx;
            if (ball.vy < 0) ball.vy = -1 * ball.vy;
            return;
        }
        //check the bottom right corner of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y + Block.HEIGHT, 1, 1))
        {
            if (ball.vx < 0) ball.vx = -1 * ball.vx;
            if (ball.vy < 0) ball.vy = -1 * ball.vy;
            return;
        }
        //check the top edge of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y, Block.WIDTH, 1))
        {
            if (ball.vy > 0) ball.vy = -1 * ball.vy;
            return;
        }
        //check the bottom edge of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y + Block.HEIGHT, Block.WIDTH, 1))
        {
            if (ball.vy < 0) ball.vy = -1 * ball.vy;
            return;
        }
        //check the left edge of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y, 1, Block.HEIGHT)) //block.y + block.Height?
        {
            if (ball.vx > 0) ball.vx = -1 * ball.vx;
            return;
        }
        //check the right edge of the block for impact
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y, 1, Block.HEIGHT)) //block.y + block.Height?
        {
            if (ball.vx < 0) ball.vx = -1 * ball.vx;
            return;
        }
    }

}
