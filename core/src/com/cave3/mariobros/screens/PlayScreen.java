package com.cave3.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cave3.mariobros.MarioBros;
import com.cave3.mariobros.scenes.Hud;
import com.cave3.mariobros.sprites.enemies.Enemy;
import com.cave3.mariobros.sprites.Mario;
import com.cave3.mariobros.sprites.items.Item;
import com.cave3.mariobros.sprites.items.ItemDef;
import com.cave3.mariobros.sprites.items.Mushroom;
import com.cave3.mariobros.tools.B2WorldCreator;
import com.cave3.mariobros.tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wrk on 12/11/16.
 */

public class PlayScreen implements Screen {

    private MarioBros game;
    private TextureAtlas atlas;

    // basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    public Hud hud;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // sprites
    private Mario player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        // create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();
        // create a FitViewport to maintain virtual aspect ratio on different devices
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);
        // create our game HUD for score/timers/level info
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        //TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        //music.setLooping(true);
        //music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

    }


    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if(!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1/60f, 6, 2);

        player.update(dt);
        for (Enemy enemy : creator.getGoombas()) {
            enemy.update(dt);
            // 224 (12 x 16) is the distance from Mario to the screen right side
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for (Item item : items) {
            item.update(dt);
        }

        hud.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        //clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render the game map
        renderer.render();

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getGoombas())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        game.batch.end();

        //render ourBox2DDebugLines
        b2dr.render(world, gamecam.combined);

        //set our batch to now draw what the Hud cam sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
