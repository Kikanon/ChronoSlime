package com.gdx.chronoslime.ecs.system.render;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.chronoslime.managers.GameManager;

public class HUDRenderSystem extends EntitySystem {

    private static final float PADDING = 20.0f;

    private final SpriteBatch batch;
    private final Viewport hudViewport;
    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    public HUDRenderSystem(SpriteBatch batch, Viewport hudViewport, BitmapFont font) {
        super(10);
        this.batch = batch;
        this.hudViewport = hudViewport;
        this.font = font;
    }

    @Override
    public void update(float deltaTime) {
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        layout.height = 10f;

        // middle
        float endX = (hudViewport.getWorldWidth() + layout.width) / 2 - layout.width;
        float endY = hudViewport.getWorldHeight() - 20f;

        // time
        layout.setText(font, String.format("%02d:%02d", GameManager.INSTANCE.minutes, GameManager.INSTANCE.seconds));
        font.draw(batch, layout, endX, endY);

        // debug data
        if (GameManager.INSTANCE.DEBUG) {
            layout.setText(font, String.format("Q size: %d", GameManager.INSTANCE.enemyQueue.size));
            font.draw(batch, layout, endX, endY - 40f);
            layout.setText(font, String.format("Size: %f %f", GameManager.INSTANCE.W_WIDTH, GameManager.INSTANCE.W_HEIGHT));
            font.draw(batch, layout, endX, endY - 80f);
        }


        if (GameManager.INSTANCE.gameOver()) {
            font.setColor(Color.RED);
            layout.setText(font, "The END");

            font.draw(batch, layout, endX, endY);
        }

        batch.end();
    }
}