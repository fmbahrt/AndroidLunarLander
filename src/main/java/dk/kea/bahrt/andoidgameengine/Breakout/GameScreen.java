package dk.kea.bahrt.andoidgameengine.Breakout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import java.util.List;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;
import dk.kea.bahrt.andoidgameengine.Sound;
import dk.kea.bahrt.andoidgameengine.TouchEvent;

public class GameScreen extends Screen
{

    enum State
    {
        PAUSED,
        RUNNING,
        GAMEOVER
    }

    State state = State.RUNNING;

    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;

    Typeface font;

    Sound bounceSound;
    Sound blockSound;

    World world;
    WorldRenderer renderer;

    public GameScreen(GameEngine game)
    {
        super(game);
        background  = game.loadBitmap("background.png");
        resume      = game.loadBitmap("resume.png");
        gameOver    = game.loadBitmap("gameover.png");
        font        = game.loadFont("font.ttf");
        bounceSound = game.loadSound("bounce.wav");
        blockSound  = game.loadSound("blocksplosion.wav");
        world       = new World(new CollisionListener()
        {
            @Override
            public void collisionWall()
            {
                bounceSound.play(1);
            }

            @Override
            public void collisionPaddle()
            {
                bounceSound.play(1);
            }

            @Override
            public void collisionBlock()
            {
                blockSound.play(1);
            }
        });
        renderer    = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime)
    {

        if (world.gameOver)
        {
            state = State.GAMEOVER;
        }

        if (state == State.PAUSED && game.getTouchEvents().size() > 0)
        {
            state = State.RUNNING;
            resume(); //??
        }
        if (state == State.GAMEOVER)
        {
            List<TouchEvent> events = game.getTouchEvents();
            for (int i = 0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.UP)
                {
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
        if (state == State.RUNNING && game.getTouchY(0) < 32 && game.getTouchX(0) > 320 - 32)
        {
            state = State.PAUSED;
            pause();
            return;
        }
        if (state == State.RUNNING && world.lostLife && game.getTouchY(0) > 32) //Terrible code :-)))
        {
            List<TouchEvent> events = game.getTouchEvents();
            for (int i = 0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.UP)
                {
                    world.lostLife = false;
                    return;
                }
            }
        }

        if (state == State.RUNNING && !world.lostLife)
        {
            world.update(deltaTime, game.getAccelerometer()[0], game.getTouchX(0));
        }
        game.drawBitmap(background, 0, 0);
        if (world.lostLife && state == State.RUNNING)
        {
            game.drawText(font, "You lost a life!", 40, 300, Color.RED, 24);
        }
        game.drawText(font, "Points: " + Integer.toString(world.points), 24, 22, Color.GREEN, 12);
        game.drawText(font, "Lives: " + Integer.toString(world.lives), 118, 22, Color.GREEN, 12);
        renderer.render();

        if (state == State.PAUSED)
        {
            game.drawBitmap(resume, 160 - resume.getWidth() / 2, 240 - resume.getHeight() / 2);
        }

        if (state == State.GAMEOVER)
        {
            game.drawBitmap(gameOver, 160 - gameOver.getWidth() / 2, 240 - gameOver.getHeight() / 2);
        }
    }

    @Override
    public void pause()
    {
        if (state == State.RUNNING)
        {
            state = State.PAUSED;
        }
        game.music.pause();
    }

    @Override
    public void resume()
    {
        game.music.play();
    }

    @Override
    public void dispose()
    {

    }
}
