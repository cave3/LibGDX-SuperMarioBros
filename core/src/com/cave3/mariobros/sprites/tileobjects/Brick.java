package com.cave3.mariobros.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.cave3.mariobros.MarioBros;
import com.cave3.mariobros.scenes.Hud;
import com.cave3.mariobros.screens.PlayScreen;
import com.cave3.mariobros.sprites.Mario;
import com.cave3.mariobros.sprites.tileobjects.InteractiveTileObject;

/**
 * Created by wrk on 14/11/16.
 */

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);

        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);

    }

    @Override
    public void onHeadHit(Mario mario) {
        if (mario.isBig()) {
            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);

            MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }
}
