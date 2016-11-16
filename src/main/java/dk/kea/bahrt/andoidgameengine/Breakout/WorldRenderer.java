package dk.kea.bahrt.andoidgameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea.bahrt.andoidgameengine.GameEngine;

/**
 *
 */
public class WorldRenderer
{

    GameEngine game;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;
    Bitmap blocksImage;

    public WorldRenderer(GameEngine game, World world)
    {
        this.game        = game;
        this.world       = world;
        this.ballImage   = game.loadBitmap("ball.png");
        this.paddleImage = game.loadBitmap("paddle.png");
        this.blocksImage = game.loadBitmap("blocks.png");
    }

    public void render()
    {
        game.drawBitmap(ballImage, (int) world.ball.x, (int) world.ball.y);
        game.drawBitmap(paddleImage, (int) world.paddle.x, (int) world.paddle.y);

        int stop = world.blocks.size();
        Block tempBlock;
        for (int i = 0; i < stop; i++)
        {
            tempBlock = world.blocks.get(i);
            game.drawBitmap(blocksImage, (int) tempBlock.x, (int) tempBlock.y,
                    0, (int) (tempBlock.type * Block.HEIGHT), (int) Block.WIDTH, (int) Block.HEIGHT);
        }

    }

}
