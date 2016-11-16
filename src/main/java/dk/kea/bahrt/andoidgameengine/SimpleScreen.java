package dk.kea.bahrt.andoidgameengine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.List;

/**
 * Created by Bahrt on 05/09/16.
 */
public class SimpleScreen extends Screen
{
    /*
    int x = 0;
    int y = 0;
    */
    Sound sound;
    Music music;
    boolean isPlaying = false;
    Bitmap bitmap;
    int clearColor = Color.BLUE;

    float x = 0;
    float y = 0;
    float vX = 100;
    float vY = 100;

    public SimpleScreen(GameEngine game)
    {
        super(game);
        sound = game.loadSound("bounce.wav");
        bitmap = game.loadBitmap("bob.png");
        music = game.loadMusic("music.ogg");
    }

    @Override
    public void update(float deltaTime)
    {
        Log.d("SimpleScreen", "fps: " + game.getFramesPerSecond() + "*************************");

        game.clearFramebuffer(Color.BLUE);

        x = x + vX * deltaTime;
        y = y + vY * deltaTime;

        game.drawBitmap(bitmap, (int) x, (int) y);

        if (x > game.getFramebufferWidth() - bitmap.getWidth() || x < 0)
        {
            vX = vX * -1;
        }

        if (y > game.getFramebufferHeight() - bitmap.getHeight() || y < 0)
        {
            vY = vY * -1;
        }

        if (true)
        {
            if (music.isPlaying())
            {
                music.pause();
                isPlaying = false;
            }
            else
            {
                music.play();
                isPlaying = true;
            }
        }

        /*
        List<KeyEvent> keyEvents = game.getKeyEvents();

        for (int i = 0; i < keyEvents.size(); i++)
        {

            KeyEvent event = keyEvents.get(i);
            Log.d("KeyEvent TEST", "key: " + event.type + ", " + event.keyCode + ", " + event.character);

        }
        */

        /*
        float x = -1 * game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];

        x = (x / 10) * game.getFramebufferWidth()  / 2 + game.getFramebufferWidth()  / 2;
        y = (y / 10) * game.getFramebufferHeight() / 2 + game.getFramebufferHeight() / 2;

        game.drawBitmap(bitmap, (int) (x - 64), (int) (y - 64));
        */

        /*
        if (game.isTouchDown(0))
        {
            x = game.getTouchX(0);
            y = game.getTouchY(0);
        }
        */
        //game.drawBitmap(bitmap, 10, 10);
        //game.drawBitmap(bitmap, 100, 150, 0, 0, 64, 64);

    }

    @Override
    public void pause()
    {
        music.pause();
    }

    @Override
    public void resume()
    {
        if (!isPlaying)
        {
            music.play();
        }
    }

    @Override
    public void dispose()
    {
        music.dispose();
        sound.dispose();
    }
}
