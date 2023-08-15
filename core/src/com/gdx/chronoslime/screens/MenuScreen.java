package com.gdx.chronoslime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.chronoslime.ChronoSlimeGame;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.util.GdxUtils;

public class MenuScreen extends ScreenAdapter {

    private final Stage stage;

    public MenuScreen(final ChronoSlimeGame game) {

        AssetManager assetManager = game.getAssetManager();

        stage = new Stage();
        Skin skin = assetManager.get(AssetDescriptors.UI_SKIN);

        Gdx.input.setInputProcessor(stage);

        // Create a button
        TextButton button = new TextButton("Play", skin);
        TextButton button2 = new TextButton("Exit", skin);

        final float gap = 10f;


        button.setPosition(Gdx.graphics.getWidth() / 2f - button.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        button2.setPosition(Gdx.graphics.getWidth() / 2f - button.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - button.getHeight() - gap);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Button action
                game.changeScreen(new GameScreen(game));
                return true;
            }
        });

        button2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Button action
                System.exit(0);
                return true;
            }
        });

        stage.addActor(button);
        stage.addActor(button2);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
