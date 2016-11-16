package dk.kea.bahrt.andoidgameengine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable, View.OnKeyListener, SensorEventListener
{

    private Thread mainLoopThread;
    private State state = State.PAUSED;
    private List<State> stateChanges = new LinkedList<>();

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Screen screen;
    private Canvas canvas;
    private Bitmap offscreenSurface;
    private Rect src = new Rect();
    private Rect dst = new Rect();

    public Music music;

    private boolean pressedKeys[] = new boolean[256];

    private KeyEventPool keyEventPool = new KeyEventPool();
    private List<KeyEvent> keyEvents = new ArrayList<>();
    private List<KeyEvent> keyEventBuffer = new ArrayList<>();

    private TouchHandler     touchHandler;
    private TouchEventPool   touchEventPool   = new TouchEventPool();
    private List<TouchEvent> touchEvents      = new ArrayList<>(); //working list
    private List<TouchEvent> touchEventBuffer = new ArrayList<>(); //buffer list

    //Accel
    private float[] accelerometer = new float[3];

    private SoundPool soundPool;

    private int framesPerSecond = 0;

    private Paint paint = new Paint();

    //************* variabes above, and methods below

    public abstract Screen createStartScreen();

    public void onCreate(Bundle instanceBunde)
    {
        super.onCreate(instanceBunde);
        //My Stuff
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();

        //moved the screen check to onResume
        if (this.getResources().getConfiguration().orientation == 2)
        {
            setOffScreenSurface(480, 320); //Ligger
        }
        else
        {
            setOffScreenSurface(320, 480); //Står
        }

        //setOffScreenSurface(400, 320);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.requestFocus();
        surfaceView.setOnKeyListener(this);
        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) //!= is faster, half the instructions (>0)
        {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); //Dette skal ændres
        screen = createStartScreen();
    }//end of onCreate

    public void setOffScreenSurface(int width, int height)
    {
        if (offscreenSurface != null)
        {
            offscreenSurface.recycle();
        }
        offscreenSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); //COnfig = how many bits for each color
        canvas = new Canvas(offscreenSurface);
    }

    public void setScreen(Screen screen)
    {
        if (this.screen != null)
        {
            this.screen.dispose();
        }
        this.screen = screen;
    }

    public SurfaceView getSurfaceView()
    {
        return this.surfaceView;
    }

    //Resource loading
    public Bitmap loadBitmap(String fileName)
    {
        InputStream inFromFile = null;
        Bitmap bitmap = null;

        try
        {
            inFromFile = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(inFromFile);

            if (bitmap == null) //Can leave this part out
            {
                throw new RuntimeException("1: Couldn't load bitmap from asset " + fileName + "!");
            }

        }
        catch (IOException e)
        {
            throw new RuntimeException("2: Couldn't load bitmap from asset " + fileName + "!");
        }
        finally
        {
            if (inFromFile != null)
            {
                try
                {
                    inFromFile.close();
                }
                catch (IOException e1)
                {
                    //EMPTY
                }
            }
        }

        return bitmap;
    }

    public Typeface loadFont(String fileName)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), fileName);
        if (font == null)
        {
            throw new RuntimeException("Oh no! I couldnt load font from file: " + fileName);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int color, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text, x, y, paint);
    }

    public Sound loadSound(String fileName)
    {
        try
        {
            AssetFileDescriptor assDescriptor = getAssets().openFd(fileName);
            int soundID = soundPool.load(assDescriptor, 0);
            return new Sound(soundPool, soundID);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load sound " + fileName + "!");
        }
    }

    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        }
        catch (IOException e)
        {
            throw new RuntimeException("GameEngine: Damn, Could not load music!");
        }
    }

    public void clearFramebuffer(int color)
    {
        if (canvas != null)
        {
            canvas.drawColor(color);
        }
    }
    public int getFramebufferWidth()
    {
        return offscreenSurface.getWidth();
    }

    public int getFramebufferHeight()
    {
        return offscreenSurface.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if (canvas != null)
        {
            canvas.drawBitmap(bitmap, x, y, null); //Using default Paint object
        }
    }
    public void drawBitmap(Bitmap bitmap, int x, int y,
                           int srcX, int srcY,
                           int srcWidth, int srcHeight)
    {
        if (canvas == null)
        {
            return;
        }

        Rect src = new Rect();
        Rect dst = new Rect();

        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null); //Default paint object
    }


    //***** TESTING SPACE :-) **********

    public void drawBitmap(Bitmap bitmap, Matrix m, Paint paint)
    {
        if(canvas != null)
        {
            canvas.drawBitmap(bitmap, m, paint);
        }
    }

    //**** TESTING SPACE :-) *****

    //From interface
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) //Import keyevent
    {
        if (event.getAction() == android.view.KeyEvent.ACTION_DOWN)
        {
            pressedKeys[keyCode] = true;
            synchronized (keyEventBuffer)
            {

                KeyEvent keyEvent = keyEventPool.obtain();
                keyEvent.type = KeyEvent.KeyEventType.DOWN;
                keyEvent.keyCode = keyCode;
                keyEvent.character = (char) event.getUnicodeChar();
                keyEventBuffer.add(keyEvent);

            }
        }
        if (event.getAction() == android.view.KeyEvent.ACTION_UP)
        {
            pressedKeys[keyCode] = false;
            synchronized (keyEventBuffer)
            {

                KeyEvent keyEvent = keyEventPool.obtain();
                keyEvent.type = KeyEvent.KeyEventType.UP;
                keyEvent.keyCode = keyCode;
                keyEvent.character = (char) event.getUnicodeChar();
                keyEventBuffer.add(keyEvent);

            }
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode)
    {
        return pressedKeys[keyCode];
    }

    public boolean isTouchDown(int pointer) //Pointer to arrayOdtouchevents
    {
        return touchHandler.isTouchDown(pointer);
    }

    //Where on the Screen
    public int getTouchX(int pointer)
    {
        return (int) ((float) touchHandler.getTouchX(pointer) / (float) surfaceView.getWidth() * offscreenSurface.getWidth());
    }

    public int getTouchY(int pointer)
    {
        return (int) ((float) touchHandler.getTouchY(pointer) / (float) surfaceView.getHeight() * offscreenSurface.getHeight());
    }

    public List<KeyEvent> getKeyEvents()
    {
        return keyEvents;
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEvents;
    }

    private void fillEvents()
    {

        synchronized (keyEventBuffer)
        {

            int size = keyEventBuffer.size();
            for (int i = 0; i < size; i++)
            {
                keyEvents.add(keyEventBuffer.get(i));
            }
            keyEventBuffer.clear();

        }

        synchronized (touchEventBuffer)
        {

            int size = touchEventBuffer.size();
            for (int i = 0; i < size; i++)
            {
                touchEvents.add(touchEventBuffer.get(i));
            }
            touchEventBuffer.clear();

        }

    }

    private void freeEvents()
    {

        synchronized (keyEvents)
        {

            int size = keyEvents.size();
            for (int i = 0; i < size; i++)
            {
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();

        }

        synchronized (touchEvents)
        {

            int size = touchEvents.size();
            for (int i = 0; i < size; i++)
            {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();

        }

    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    public void clearAccelerometer()
    {
        accelerometer[0] = 0;
        accelerometer[1] = 0;
        accelerometer[2] = 0;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //Empty so far
    }

    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
    }

    public int getFramesPerSecond()
    {
        return framesPerSecond;
    }

    public void run()
    {
        int frames = 0;
        long startTime = System.nanoTime();
        long currentTime;
        long lastTime= System.nanoTime();
        while (true)
        {
            synchronized (stateChanges)
            {

                for (int i = 0; i < stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if (state == State.DISPOSED)
                    {
                        Log.d("GameEngine", "State is changed to DISPOSED");
                        return;
                    }
                    if (state == State.PAUSED)
                    {
                        Log.d("GameEngine", "State is changed to PAUSED");
                        return;
                    }
                    if (state == State.RESUMED)
                    {
                        state = State.RUNNING;
                        Log.d("GameEngine", "Game is Resumed, State is changed to RUNNING");
                    }//end of if

                } //end of for loop
                stateChanges.clear();

            } //end of synchronized

            if (state == State.RUNNING)
            {
                if (!surfaceHolder.getSurface().isValid())
                {
                    continue;
                }
                Canvas canvas = surfaceHolder.lockCanvas(); //THis is physical screen canvas
                // Drawing happens here!

                fillEvents();
                currentTime  = System.nanoTime();
                if (screen != null)
                {
                    screen.update((currentTime - lastTime) / 1000000000.0f);
                }
                lastTime = currentTime;
                freeEvents();

                src.left = 0;
                src.top = 0;
                src.right = offscreenSurface.getWidth() - 1;
                src.bottom = offscreenSurface.getHeight() - 1;

                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth();
                dst.bottom = surfaceView.getHeight();

                canvas.drawBitmap(offscreenSurface, src, dst, null);

                surfaceHolder.unlockCanvasAndPost(canvas);
                canvas = null;
            }
            frames = frames + 1;
            if (System.nanoTime() - startTime > 1000000000)
            {
                framesPerSecond = frames;
                frames = 0;
                startTime = System.nanoTime();
            }

        } //end of while loop
    } //end of run() method

    public void onPause()
    {
        super.onPause();
        //My stuff
        synchronized (stateChanges)
        {
            if(isFinishing())
            {
                stateChanges.add(stateChanges.size(), State.DISPOSED);
            }
            else
            {
                stateChanges.add(stateChanges.size(), State.PAUSED);
            }
        }
        try
        {
            mainLoopThread.join();
        }
        catch (InterruptedException e)
        {
            //do nothing
        }
        if (isFinishing())
        {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            soundPool.release();
        }
    }

    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges)
        {
            stateChanges.add(stateChanges.size(), State.RESUMED);
        }
        /* Just delete
        if (surfaceView.getWidth() > surfaceView.getHeight())
        {
            setOffScreenSurface(480, 320); //Ligger
        }
        else
        {
            setOffScreenSurface(320, 480); //Står
        }
        */
    }
}
