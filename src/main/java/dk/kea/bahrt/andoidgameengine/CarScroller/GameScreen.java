package dk.kea.bahrt.andoidgameengine.CarScroller;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;

public class GameScreen extends Screen
{

    enum State
    {
        PAUSED,
        RUNNING,
        GAMEOVER
    }

    State state = State.RUNNING;
    World world;
    WorldRenderer renderer;

    public GameScreen(GameEngine game)
    {
        super(game);
        world = new World(game);
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime)
    {
        if (state == State.RUNNING)
        {
            world.update(deltaTime);
        }
        renderer.render();
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
