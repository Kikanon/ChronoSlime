package com.gdx.chronoslime.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.chronoslime.ChronoSlimeGame;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.assets.AssetPaths;
import com.gdx.chronoslime.assets.RegionNames;
import com.gdx.chronoslime.ecs.common.EntityFactorySystem;
import com.gdx.chronoslime.ecs.common.SoundSystem;
import com.gdx.chronoslime.ecs.common.StartUpSystem;
import com.gdx.chronoslime.ecs.common.TiledSystem;
import com.gdx.chronoslime.ecs.common.types.ItemType;
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
    public final Stage pauseStage;
    public final Stage lvlUpStage;
    private final ChronoSlimeGame game;
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final Skin skin;
    private final TextureAtlas uiAtlas;
    TiledMap map;
    private PooledEngine engine;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;

    public GameScreen(final ChronoSlimeGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        uiAtlas = assetManager.get(AssetDescriptors.UI);
        batch = game.getBatch();
        GameManager.INSTANCE.reset(this);

        pauseStage = new Stage();
        lvlUpStage = new Stage();
        pauseStage.setViewport(new FitViewport(GameManager.INSTANCE.W_WIDTH, GameManager.INSTANCE.W_HEIGHT, new OrthographicCamera()));
        lvlUpStage.setViewport(new FitViewport(GameManager.INSTANCE.W_WIDTH, GameManager.INSTANCE.W_HEIGHT, new OrthographicCamera()));

        skin = assetManager.get(AssetDescriptors.UI_SKIN);

        Gdx.input.setInputProcessor(null);

        TextButton button = new TextButton("Resume", skin);
        TextButton button2 = new TextButton("Quit", skin);

        final float gap = 10f;

        button.setPosition(Gdx.graphics.getWidth() / 2f - button.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        button2.setPosition(Gdx.graphics.getWidth() / 2f - button.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - button.getHeight() - gap);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameManager.INSTANCE.gameState = GameState.PLAY;
                Gdx.input.setInputProcessor(null);
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

        pauseStage.addActor(button);
        pauseStage.addActor(button2);
    }

    public void displayOptions(Array<ItemType> options) {
        lvlUpStage.clear();

        Image uiBackground = new Image(uiAtlas.findRegion(RegionNames.UI_FRAME));
        uiBackground.setSize(lvlUpStage.getWidth() * 0.8f, lvlUpStage.getHeight() * 0.7f);
        uiBackground.setPosition(lvlUpStage.getWidth() / 2f - uiBackground.getWidth() / 2f, lvlUpStage.getHeight() / 2f - uiBackground.getHeight() / 2f);
        lvlUpStage.addActor(uiBackground);

        for (final ItemType item : options) {
            ImageButton option = new ImageButton(new TextureRegionDrawable(uiAtlas.findRegion(item.itemSpriteName)));
            option.setSize(50f, 50f);
            option.setPosition(uiBackground.getX() + uiBackground.getWidth() * 0.3f, uiBackground.getY() + uiBackground.getHeight() * 0.8f);
            option.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    GameManager.INSTANCE.addItem(item);
                    Gdx.input.setInputProcessor(null);
                    return true;
                }
            });
            lvlUpStage.addActor(option);
        }

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
        engine.addSystem(new HUDRenderSystem(batch, hudViewport, font, skin));

        engine.addSystem(new ParticleCleanupSystem());
        engine.addSystem(new InteractableRefreshSystem());
        engine.addSystem(new PersistentVelocityRefreshSystem());
        engine.addSystem(new EnemyCleanupSystem());
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameManager.INSTANCE.gameState = GameState.PAUSED;
            Gdx.input.setInputProcessor(pauseStage);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            GameManager.INSTANCE.DEBUG = !GameManager.INSTANCE.DEBUG;
            engine.getSystem(DebugRenderSystem.class).setProcessing(GameManager.INSTANCE.DEBUG);
        }

        switch (GameManager.INSTANCE.gameState) {
            case PLAY: {
                GdxUtils.clearScreen();
                GameManager.INSTANCE.update(delta);
                engine.update(delta);
                break;
            }
            case LVL_UP: {
                engine.getSystem(RenderSystem.class).update(0);
                lvlUpStage.act();
                lvlUpStage.draw();
                break;
            }
            case PAUSED: {
                engine.getSystem(RenderSystem.class).update(0);
                pauseStage.act();
                pauseStage.draw();
                break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        pauseStage.getViewport().update(width, height, true);
        lvlUpStage.getViewport().update(width, height, true);


        viewport.apply();
        hudViewport.apply();
        pauseStage.getViewport().apply();
        lvlUpStage.getViewport().apply();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        engine.removeAllEntities();
    }
}
