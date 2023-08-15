package com.gdx.chronoslime.ecs.system.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.chronoslime.ecs.component.drawable.TextureComponent;
import com.gdx.chronoslime.ecs.component.drawable.ZOrderComparator;
import com.gdx.chronoslime.ecs.component.identification.ParticleComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.TiledSystem;
import com.gdx.chronoslime.managers.GameManager;


public class RenderSystem extends SortedIteratingSystem {
    private static final Family FAMILY = Family.one(
            ParticleComponent.class,
            TextureComponent.class
    ).get();

    public static int PRIORITY = 1;
    private final SpriteBatch batch;
    private final Viewport viewport;
    TiledSystem tiledSystem;

    public RenderSystem(SpriteBatch batch, Viewport viewport) {
        super(FAMILY, ZOrderComparator.INSTANCE, PRIORITY);

        this.batch = batch;

        this.viewport = viewport;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        tiledSystem = engine.getSystem(TiledSystem.class);
    }

    @Override
    public void update(float deltaTime) {

        tiledSystem.renderTiledView((OrthographicCamera) viewport.getCamera(), GameManager.INSTANCE.camera_x, GameManager.INSTANCE.camera_y);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        super.update(deltaTime);

        batch.end();


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent position = Mappers.POSITION.get(entity);
        SizeComponent size = Mappers.SIZE.get(entity);
        TextureComponent texture = Mappers.TEXTURE.get(entity);
        ParticleComponent particle = Mappers.PARTICLE.get(entity);
        VelocityComponent velocity = Mappers.VELOCITY.get(entity);

        if (velocity != null) {
            if (velocity.xVelocity > 0) {
                texture.xScale = 1;
            } else if (velocity.xVelocity < 0) {
                texture.xScale = -1;
            }
        }

        if (texture != null) {
            if (texture.texture != null) {
                batch.draw(texture.texture,
                        position.x, position.y,
                        size.width / 2, size.height / 2,
                        size.width, size.height,
                        texture.xScale, texture.yScale,
                        position.r);
            }
            if (texture.animation != null) {
                batch.draw(texture.animation.getKeyFrame(texture.animationTime, true),
                        position.x, position.y,
                        size.width / 2, size.height / 2,
                        size.width, size.height,
                        texture.xScale, texture.yScale,
                        position.r);
                texture.animationTime += deltaTime;
            }
        } else if (particle != null) {
            particle.effect.draw(batch, deltaTime);
        }

    }
}
