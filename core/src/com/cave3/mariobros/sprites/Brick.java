package com.cave3.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.cave3.mariobros.MarioBros;
import com.cave3.mariobros.scenes.Hud;
import com.cave3.mariobros.screens.PlayScreen;

/**
 * Created by wrk on 14/11/16.
 */

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);

        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);


    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);

        MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}