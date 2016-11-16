package dk.kea.bahrt.andoidgameengine;

import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class MultiTouchHandler implements TouchHandler, View.OnTouchListener
{

    /*
    Gak gak design men det PERFORMER!
    Encapsulation :-((
     */
    private boolean [] isTouched = new boolean[20];
    private int     [] touchX    = new int    [20];
    private int     [] touchY    = new int    [20];

    private List<TouchEvent> touchEventBuffer;
    private TouchEventPool touchEventPool;

    public MultiTouchHandler(View view, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventPool)
    {
        view.setOnTouchListener(this); //Attach this code to the view as the touchlistener
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventPool;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        synchronized (touchEventPool) //Object used as 'key'
        {
            TouchEvent touchEvent = null;
            /*
            ALOT OF INFO IN ONE BYTES (OPTIMIZATION)
             */
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT; //SHIFT BYTES TO RIGHT
            int pointerID = event.getPointerId(pointerIndex);

            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.obtain();
                    touchEvent.type = TouchEvent.TouchEventType.DOWN;
                    touchEvent.pointer = pointerID;
                    touchX[pointerID] = (int) event.getX(pointerIndex);
                    touchEvent.x = touchX[pointerID];
                    touchY[pointerID] = (int) event.getY(pointerIndex);
                    touchEvent.y = touchY[pointerID];
                    isTouched[pointerID] = true;
                    touchEventBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.obtain();
                    touchEvent.type = TouchEvent.TouchEventType.UP;
                    touchEvent.pointer = pointerID;
                    touchX[pointerID] = (int) event.getX(pointerIndex);
                    touchEvent.x = touchX[pointerID];
                    touchY[pointerID] = (int) event.getY(pointerIndex);
                    touchEvent.y = touchY[pointerID];
                    isTouched[pointerID] = false;
                    touchEventBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for (int i = 0; i < pointerCount; i++)
                    {
                        touchEvent = touchEventPool.obtain();
                        touchEvent.type = TouchEvent.TouchEventType.DRAGGED;
                        pointerIndex = i;
                        pointerID = event.getPointerId(pointerIndex);
                        touchEvent.pointer = pointerID;
                        touchEvent.x = touchX[pointerID] = (int) event.getX(pointerIndex);
                        touchEvent.y = touchY[pointerID] = (int) event.getY(pointerIndex);
                        touchEventBuffer.add(touchEvent);
                    }
                    break;
            } //End Switch
        }
        return true;
    }

    @Override
    public synchronized boolean isTouchDown(int pointer)
    {
        if (pointer < 0 || pointer >= 20)
        {
            return false;
        }
        return isTouched[pointer];
    }

    @Override
    public synchronized int getTouchX(int pointer)
    {
        if (pointer < 0 || pointer >= 20)
        {
            return -1;
        }
        return touchX[pointer];
    }

    @Override
    public synchronized int getTouchY(int pointer)
    {
        if (pointer < 0 || pointer >= 20)
        {
            return -1;
        }
        return touchY[pointer];
    }


}
