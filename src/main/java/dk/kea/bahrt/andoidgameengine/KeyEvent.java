package dk.kea.bahrt.andoidgameengine;

public class KeyEvent
{
    public enum KeyEventType
    {
        DOWN,
        UP,
    }

    public KeyEventType type;
    public int keyCode;
    public char character;

}
