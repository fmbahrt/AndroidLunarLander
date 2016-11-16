package dk.kea.bahrt.andoidgameengine;

public class TouchEventPool extends Pool<TouchEvent>
{

    @Override
    protected TouchEvent newItem()
    {
        return new TouchEvent();
    }
}
