package com.gdx.chronoslime.ecs.system.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.assets.RegionNames;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class DebugRenderSystem extends IteratingSystem {
    private static final Family FAMILY = Family.one(
            BoundsComponent.class,
            VelocityComponent.class
    ).get();

    public static int PRIORITY = 3;
    private final SpriteBatch batch;

    private final ShapeDrawer shapeDrawer;
    private final TextureAtlas gamePlayAtlas;

    public DebugRenderSystem(SpriteBatch batch, AssetManager assetManager) {
        super(FAMILY, PRIORITY);

        this.batch = batch;
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        shapeDrawer = new ShapeDrawer(batch);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        shapeDrawer.setTextureRegion(gamePlayAtlas.findRegion(RegionNames.RENDER_PIXEL));
    }

    @Override
    public void update(float deltaTime) {
        batch.begin();

        super.update(deltaTime);

        batch.end();


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BoundsComponent boundsComponent = Mappers.BOUNDS.get(entity);
        VelocityComponent velocityComponent = Mappers.VELOCITY.get(entity);
        PositionComponent positionComponent = Mappers.POSITION.get(entity);
        if (boundsComponent != null) {
            shapeDrawer.polygon(boundsComponent.polygon);
        }
        if (velocityComponent != null && positionComponent != null) {
            shapeDrawer.line(positionComponent.getVector2(), velocityComponent.getVector2Velocity().scl(5f).add(positionComponent.getVector2()));
        }
    }
}
