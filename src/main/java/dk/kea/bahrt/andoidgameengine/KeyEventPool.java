package dk.kea.bahrt.andoidgameengine;

/**
 * Created by Bahrt on 26/09/16.
 */
public class KeyEventPool extends Pool<KeyEvent>
{

    @Override
    protected KeyEvent newItem() {
        return new KeyEvent();
    }
}
