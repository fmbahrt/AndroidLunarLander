package dk.kea.bahrt.andoidgameengine.CarScroller;

import android.graphics.Bitmap;

import dk.kea.bahrt.andoidgameengine.GameEngine;

public class WorldRenderer
{

    GameEngine game;
    World world;
    Bitmap backgroundImage;
    Bitmap carImage;
    Bitmap monsterImage;
    int backgroundWidth = 0;
    int backgroundHeight = 0;

    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        this.backgroundImage = game.loadBitmap("xcarbackground.png");
        this.carImage = game.loadBitmap("xbluecar2.png");
        this.monsterImage = game.loadBitmap("xyellowmonster.png");
        this.backgroundWidth = backgroundImage.getWidth();
        this.backgroundHeight = backgroundImage.getHeight();
    }

    public void render()
    {
        game.drawBitmap(backgroundImage, 0, 0, (int) world.scrollingBG.scrollX, 0, 480, 320);
        game.drawBitmap(carImage, (int) world.car.x, (int) world.car.y);
        Monster monster;
        for (int i = 0; i < world.monsterList.size(); i++)
        {
            monster = world.monsterList.get(i);
            game.drawBitmap(monsterImage, (int) monster.x, (int) monster.y);
        }
    }

}
