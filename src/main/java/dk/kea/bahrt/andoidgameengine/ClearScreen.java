package dk.kea.bahrt.andoidgameengine;

import java.util.Random;

/**
 * Created by Bahrt on 05/09/16.
 */
public class ClearScreen extends Screen
{

    Random rand = new Random();

    public ClearScreen(GameEngine game)
    {
        super(game);
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(rand.nextInt());
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
