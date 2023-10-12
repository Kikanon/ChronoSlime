package com.gdx.chronoslime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.chronoslime.ChronoSlimeGame;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.util.GdxUtils;

public class StartingItemScreen extends ScreenAdapter {

    private final Stage stage;
    private ChronoSlimeGame game;
    private Skin uiSkin;

    public StartingItemScreen(final ChronoSlimeGame game) {

        AssetManager assetManager = game.getAssetManager();

        stage = new Stage();
        this.game = game;
        this.uiSkin = assetManager.get(AssetDescriptors.UI_SKIN);

        Gdx.input.setInputProcessor(stage);

        stage.addActor(createUi());
    }

    private Actor createUi() {
        Table screen = new Table();
        screen.defaults().pad(20);

        // Create a button
        TextButton button = new TextButton("Play", uiSkin);
        TextButton button2 = new TextButton("Exit", uiSkin);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.changeScreen(new GameScreen(game));
                return true;
            }
        });

        button2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.exit(0);
                return true;
            }
        });


        screen.add(button).row();
        screen.add(button2).row();
        screen.setFillParent(true);

        return screen;
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
