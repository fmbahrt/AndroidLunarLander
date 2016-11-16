package dk.kea.bahrt.andoidgameengine.Breakout;

/**
 * Created by Bahrt on 10/10/16.
 */
public class Block
{

    public static float WIDTH  = 40;
    public static float HEIGHT = 18;
    public int   type;
    public float x;
    public float y;

    public Block(float x, float y, int type)
    {
        this.x    = x;
        this.y    = y;
        this.type = type;
    }

}
