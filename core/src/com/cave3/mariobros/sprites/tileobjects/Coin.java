package com.cave3.mariobros.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cave3.mariobros.MarioBros;
import com.cave3.mariobros.scenes.Hud;
import com.cave3.mariobros.screens.PlayScreen;
import com.cave3.mariobros.sprites.Mario;
import com.cave3.mariobros.sprites.items.ItemDef;
import com.cave3.mariobros.sprites.items.Mushroom;
import com.cave3.mariobros.sprites.tileobjects.InteractiveTileObject;

/**
 * Created by wrk on 14/11/16.
 */

public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);


    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision");

        if (getCell().getTile().getId() == BLANK_COIN) {
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            if (object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM), Mushroom.class));
                MarioBros.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            } else {
                MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }
        }

        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
