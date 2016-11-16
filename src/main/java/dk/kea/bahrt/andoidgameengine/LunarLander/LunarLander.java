package dk.kea.bahrt.andoidgameengine.LunarLander;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;

/**
 * Created by Bahrt on 12/11/16.
 */
public class LunarLander extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new GameScreen(this);
    }
}
