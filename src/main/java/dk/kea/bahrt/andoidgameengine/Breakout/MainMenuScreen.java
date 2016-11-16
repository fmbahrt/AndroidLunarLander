package dk.kea.bahrt.andoidgameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;

public class MainMenuScreen extends Screen
{

    Bitmap mainmenu;
    Bitmap insertcoin;
    Bitmap ball;
    float startTime = System.nanoTime();
    float passedTime = 0.0f;

    public MainMenuScreen(GameEngine game)
    {
        super(game);
        mainmenu   = game.loadBitmap("mainmenu.png");
        insertcoin = game.loadBitmap("insertcoin.png");
        ball       = game.loadBitmap("ball.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if (game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game));
            return;
        }
        passedTime = passedTime + deltaTime;
        game.drawBitmap(mainmenu, 0, 0);
        game.drawBitmap(ball, 100, 230);
        if (passedTime - (int)passedTime > 0.5) //mod
        {
            game.drawBitmap(insertcoin, 160 - insertcoin.getWidth() / 2, 333);
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }


}
