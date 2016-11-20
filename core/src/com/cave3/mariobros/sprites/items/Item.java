package com.cave3.mariobros.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.cave3.mariobros.MarioBros;
import com.cave3.mariobros.screens.PlayScreen;

/**
 * Created by wrk on 19/11/16.
 */

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void use();

    public void update(float dt) {
        if(toDestroy && !destroyed) {

        }
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy() {
        toDestroy = true;
    }



}
