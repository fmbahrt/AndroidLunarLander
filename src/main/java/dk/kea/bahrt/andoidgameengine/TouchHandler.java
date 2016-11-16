package dk.kea.bahrt.andoidgameengine;

/**
 * Created by Bahrt on 19/09/16.
 */
public interface TouchHandler
{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
}
