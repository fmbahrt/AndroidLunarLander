package dk.kea.bahrt.andoidgameengine;

public class TouchEvent
{
    public enum TouchEventType
    {
        DOWN,
        UP,
        DRAGGED
    }

    public TouchEventType type;
    public int x;
    public int y;
    public int pointer; //index for array

}
