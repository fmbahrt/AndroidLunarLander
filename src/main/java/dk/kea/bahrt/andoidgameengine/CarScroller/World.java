package dk.kea.bahrt.andoidgameengine.CarScroller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dk.kea.bahrt.andoidgameengine.GameEngine;
import dk.kea.bahrt.andoidgameengine.Sound;
import dk.kea.bahrt.andoidgameengine.TouchEvent;

//All game logic

public class World
{

    public static final float MIN_X = 0;
    public static final float MAX_X = 479;
    public static final float MIN_Y = 0;
    public static final float MAX_Y = 319;

    GameEngine game;
    Sound wallHit;
    Car car = new Car();
    List<Monster> monsterList = new ArrayList<>();
    ScrollingBackground scrollingBG = new ScrollingBackground();
    float gameSpeed = 100; //car velocity

    public World(GameEngine game)
    {
        this.game = game;
        this.wallHit = game.loadSound("blocksplosion.wav");
    }

    public void update(float deltaTime)
    {

        scrollingBG.scrollX = scrollingBG.scrollX + (deltaTime * gameSpeed);
        if (scrollingBG.scrollX > scrollingBG.WIDTH - 480)
        {
            scrollingBG.scrollX = 0;
        }

        /*
        float[] accel = game.getAccelerometer();
        if (accel[0] != 0)
        {
            Log.d("UPADTE HELO", "[0]" + accel[0] + " [1]" + accel[1] + " [2]" + accel[2]);
            car.y = car.y + accel[0] * 20 * deltaTime;
            game.clearAccelerometer();
        }
        */
        if (game.isTouchDown(0))
        {
            List<TouchEvent> touchList = game.getTouchEvents();
            int touchCount = touchList.size();
            if (touchCount > 0)
            {
                car.y = game.getTouchY(0) - car.HEIGHT / 2; //max?
            }
            //touchList.clear();
        }

        if (car.y < MIN_Y + 10)
        {
            car.y = MAX_Y + 10;
            wallHit.play(1);
        }

        if (car.y + car.HEIGHT > MAX_Y - 10)
        {
            car.y = MAX_Y - car.HEIGHT - 10;
            wallHit.play(1);
        }

        int rndm = (int)(Math.random() * 1000);
        if (rndm > 990)
        {
            Monster monster = new Monster();
            monster.y = 30 + (float) (250 * Math.random());
            monsterList.add(monster);
        }

        Monster monster;
        for (int i = 0; i < monsterList.size(); i++)
        {
            monster = monsterList.get(i);
            monster.x = monster.x - (deltaTime * gameSpeed);
            if (monster.x < -32)
            {
                monsterList.remove(i);
            }
        }

    }

}
