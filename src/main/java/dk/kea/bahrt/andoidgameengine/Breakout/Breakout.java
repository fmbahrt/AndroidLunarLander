package dk.kea.bahrt.andoidgameengine.Breakout;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Music;
import dk.kea.bahrt.andoidgameengine.Screen;

public class Breakout extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        music = this.loadMusic("music.ogg");
        return new MainMenuScreen(this);
    }

    public void onPause()
    {
        super.onPause();
        music.pause();
    }

    public void onResume()
    {
        super.onResume();
        music.play();
    }
}
