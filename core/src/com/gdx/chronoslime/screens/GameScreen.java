package com.gdx.chronoslime.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.chronoslime.ChronoSlimeGame;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.assets.AssetPaths;
import com.gdx.chronoslime.ecs.common.EntityFactorySystem;
import com.gdx.chronoslime.ecs.common.SoundSystem;
import com.gdx.chronoslime.ecs.common.StartUpSystem;
import com.gdx.chronoslime.ecs.common.TiledSystem;
import com.gdx.chronoslime.ecs.system.CameraMovementSystem;
import com.gdx.chronoslime.ecs.system.CollisionSystem;
import com.gdx.chronoslime.ecs.system.EnemyCleanupSystem;
import com.gdx.chronoslime.ecs.system.EnemyMovementSystem;
import com.gdx.chronoslime.ecs.system.EnemySpawnSystem;
import com.gdx.chronoslime.ecs.system.GravitySystem;
import com.gdx.chronoslime.ecs.system.InputSystem;
import com.gdx.chronoslime.ecs.system.InteractableRefreshSystem;
import com.gdx.chronoslime.ecs.system.LevelSystem;
import com.gdx.chronoslime.ecs.system.MovementSystem;
import com.gdx.chronoslime.ecs.system.PersistentVelocityRefreshSystem;
import com.gdx.chronoslime.ecs.system.WorldWrapSystem;
import com.gdx.chronoslime.ecs.system.render.DebugRenderSystem;
import com.gdx.chronoslime.ecs.system.render.HUDRenderSystem;
import com.gdx.chronoslime.ecs.system.render.ParticleCleanupSystem;
import com.gdx.chronoslime.ecs.system.render.RenderSystem;
import com.gdx.chronoslime.managers.GameManager;
import com.gdx.chronoslime.util.GdxUtils;

public class GameScreen extends ScreenAdapter {
    private final ChronoSlimeGame game;
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final Stage stage;
    TiledMap map;
    private PooledEngine engine;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;
    private boolean paused = false;

    public GameScreen(final ChronoSlimeGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        GameManager.INSTANCE.reset();

        stage = new Stage();
        Skin skin = assetManager.get(AssetDescriptors.UI_SKIN);

        Gdx.input.setInputProcessor(stage);

        TextButton button = new TextButton("Resume", skin);
        TextButton button2 = new TextButton("Quit", skin);

        final float gap = 10f;

        button.setPosition(Gdx.graphics.getWidth() / 2f - button.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        button2.setPosition(Gdx.graphics.getWidth() / 2f - button.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - button.getHeight() - gap);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                paused = false;
                return true;
            }
        });

        button2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.changeScreen(new MenuScreen(game));
                return true;
            }
        });

        stage.addActor(button);
        stage.addActor(button2);

    }

    @Override
    public void show() {
        map = assetManager.get(AssetPaths.TILES);
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(GameManager.INSTANCE.W_WIDTH, GameManager.INSTANCE.W_HEIGHT, camera);
        hudViewport = new FitViewport(GameManager.INSTANCE.W_WIDTH, GameManager.INSTANCE.W_HEIGHT);
        renderer = new ShapeRenderer();
        BitmapFont font = assetManager.get(AssetDescriptors.FONT32);
        engine = new PooledEngine();

        // order important
        engine.addSystem(new EntityFactorySystem(assetManager));
        engine.addSystem(new SoundSystem(assetManager));
        engine.addSystem(new TiledSystem(map));
        engine.addSystem(new StartUpSystem());
        engine.addSystem(new LevelSystem());
        engine.addSystem(new EnemySpawnSystem());

        engine.addSystem(new EnemyMovementSystem());

        engine.addSystem(new CollisionSystem());


        engine.addSystem(new InputSystem());
        engine.addSystem(new GravitySystem());
        engine.addSystem(new WorldWrapSystem());
        engine.addSystem(new MovementSystem());


        engine.addSystem(new CameraMovementSystem());

        engine.addSystem(new RenderSystem(batch, viewport));
        engine.addSystem(new DebugRenderSystem(batch, assetManager)); // debug only
        engine.addSystem(new HUDRenderSystem(batch, hudViewport, font));

        engine.addSystem(new ParticleCleanupSystem());
        engine.addSystem(new InteractableRefreshSystem());
        engine.addSystem(new PersistentVelocityRefreshSystem());
        engine.addSystem(new EnemyCleanupSystem());
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            GameManager.INSTANCE.DEBUG = !GameManager.INSTANCE.DEBUG;
            engine.getSystem(DebugRenderSystem.class).setProcessing(GameManager.INSTANCE.DEBUG);
        }


        if (paused) {
            engine.getSystem(RenderSystem.class).update(0);
            stage.act();
            stage.draw();
        } else {
            GdxUtils.clearScreen();
            GameManager.INSTANCE.update(delta);
            engine.update(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
        viewport.apply();
        hudViewport.apply();
        stage.getViewport().apply();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        engine.removeAllEntities();
    }
}
