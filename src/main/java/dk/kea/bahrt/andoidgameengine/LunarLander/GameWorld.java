package dk.kea.bahrt.andoidgameengine.LunarLander;

import dk.kea.bahrt.andoidgameengine.GameEngine;

//All game logic
public class GameWorld
{

    GameEngine game;

    public GameUniverse gameUniverse;

    public GameWorld(GameEngine game)
    {
        this.game = game;

        gameUniverse = new GameUniverse();
    }

    public void update(float deltaTime)
    {
        gameUniverse.scrollX += 10 * deltaTime;
        gameUniverse.scrollY += 10 * deltaTime;
    }

}
