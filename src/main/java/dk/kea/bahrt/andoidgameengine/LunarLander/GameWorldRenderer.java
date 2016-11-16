package dk.kea.bahrt.andoidgameengine.LunarLander;

import android.graphics.Bitmap;

import dk.kea.bahrt.andoidgameengine.GameEngine;

//This class draws the current game 'state' on the screen.
public class GameWorldRenderer
{

    GameEngine game;
    GameWorld gameWorld;

    //Bitmaps for various game objects
    private Bitmap bitmapGameUniverse;

    //Additional field, can / should be moved
    //'Camera' frame height and width
    //Will be the size of the logical screen, offScreenSurface.
    private int frameWidth;
    private int frameHeight;

    public GameWorldRenderer(GameEngine game, GameWorld gameWorld)
    {
        this.game      = game;
        this.gameWorld = gameWorld;

        //Instantiate various bitmaps
        bitmapGameUniverse = game.loadBitmap("pixelspacebg.png");

        //Adds
        frameWidth = game.getFramebufferWidth();
        frameHeight = game.getFramebufferHeight();
    }

    public void render()
    {
        game.drawBitmap(bitmapGameUniverse, 0, 0,
                (int) gameWorld.gameUniverse.scrollX, (int) gameWorld.gameUniverse.scrollY,
                frameWidth, frameHeight);
    }

}
