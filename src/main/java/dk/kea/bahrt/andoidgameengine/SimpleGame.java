package dk.kea.bahrt.andoidgameengine;

public class SimpleGame extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        return new SimpleScreen(this);
    }
}
