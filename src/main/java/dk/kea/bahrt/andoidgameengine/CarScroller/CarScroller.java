package dk.kea.bahrt.andoidgameengine.CarScroller;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Screen;

public class CarScroller extends GameEngine
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
