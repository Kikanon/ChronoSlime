package com.gdx.chronoslime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.screens.GameScreen;
import com.gdx.chronoslime.screens.MenuScreen;

public class ChronoSlimeGame extends Game {
    private SpriteBatch batch;
    private AssetManager assetManager;

    private GameScreen gameScreen;

    @Override
    public void create() {

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        batch = new SpriteBatch();
        assetManager.load(AssetDescriptors.FONT32);
        assetManager.load(AssetDescriptors.GAME_PLAY);
        assetManager.load(AssetDescriptors.UI);
        assetManager.load(AssetDescriptors.TILES);
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.COIN_EXPLOSION);
        assetManager.finishLoading();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }

    public void startGame() {
        Screen oldScreen = getScreen();
        if (gameScreen == null) {
            gameScreen = new GameScreen(this);
            setScreen(gameScreen);
        } else {
            setScreen(gameScreen);
            if (oldScreen != null)
                oldScreen.dispose();
        }
    }

    public void changeScreen(Screen newScreen) {
        Screen oldScreen = getScreen();
        if (oldScreen.getClass() == GameScreen.class) {
            setScreen(newScreen);
        } else {
            setScreen(newScreen);
            oldScreen.dispose();
        }
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
