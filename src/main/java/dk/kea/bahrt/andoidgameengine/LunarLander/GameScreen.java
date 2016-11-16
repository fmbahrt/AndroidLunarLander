package dk.kea.bahrt.andoidgameengine.LunarLander;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;

public class GameScreen extends Screen
{

    GameWorld gameWorld;
    GameWorldRenderer gameWorldRenderer;

    public GameScreen(GameEngine game)
    {
        super(game);
        gameWorld         = new GameWorld(game);
        gameWorldRenderer = new GameWorldRenderer(game, gameWorld);
    }

    @Override
    public void update(float deltaTime)
    {
        gameWorld.update(deltaTime); //Update the 'state' of the game
        gameWorldRenderer.render();  //Draw the 'state' of the game
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
