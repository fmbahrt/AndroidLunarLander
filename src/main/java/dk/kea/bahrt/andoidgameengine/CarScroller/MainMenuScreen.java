package dk.kea.bahrt.andoidgameengine.CarScroller;

import android.graphics.Bitmap;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;

public class MainMenuScreen extends Screen
{

    public Bitmap car;
    public Bitmap startGame;
    public Bitmap background;

    public float startTime = System.nanoTime();
    public float passedTime = 0.0f;

    public MainMenuScreen(GameEngine game)
    {
        super(game);
        background = game.loadBitmap("xcarbackground.png");
        car = game.loadBitmap("xbluecar2.png");
        startGame = game.loadBitmap("xstartgame.png");
    }

    @Override
    public void update(float deltaTime)
    {

        if (game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game));
            return;
        }

        game.drawBitmap(background, 0, 0, 0, 0, 480, 320);
        game.drawBitmap(car, 75, 130);
        passedTime = passedTime + deltaTime;
        if (passedTime - (int) passedTime < 0.5)
        {
            game.drawBitmap(startGame, 240 - startGame.getWidth() / 2, 160 - startGame.getHeight());
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
