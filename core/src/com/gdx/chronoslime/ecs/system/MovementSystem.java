package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.assets.GameplayConfig;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.ecs.component.common.RelativePositionComponent;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;
import com.gdx.chronoslime.ecs.component.common.WorldWrapComponent;


public class MovementSystem extends EntitySystem {
    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            VelocityComponent.class
    ).get();

    private static final Family RELATIVE_FAMILY = Family.all(
            PositionComponent.class,
            RelativePositionComponent.class
    ).get();

    private static final Family PLAYER_FAMILY = Family.all(
            PositionComponent.class,
            PlayerComponent.class
    ).get();

    public MovementSystem() {
        super();
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> objects = getEngine().getEntitiesFor(FAMILY);
        ImmutableArray<Entity> relativeObjects = getEngine().getEntitiesFor(RELATIVE_FAMILY);
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        PositionComponent playerPosition = Mappers.POSITION.get(player);

        Vector2 playerSizeOffset = new Vector2(GameplayConfig.PLAYER_SIZE / 2f, GameplayConfig.PLAYER_SIZE / 2f);

        for (Entity entity : objects) {

            PositionComponent position = Mappers.POSITION.get(entity);
            VelocityComponent velocity = Mappers.VELOCITY.get(entity);
            BoundsComponent bounds = Mappers.BOUNDS.get(entity);
            WorldWrapComponent wrapComponent = Mappers.WRAP.get(entity);


            position.r += velocity.rVelocity + velocity.rVelocityPersistent;
            if (position.r < 0) position.r = position.r + 360;
            if (position.r > 360) position.r = position.r - 360;

            if (wrapComponent == null || wrapComponent.canMoveX())
                position.x += velocity.xVelocity + velocity.xVelocityPersistent;
            if (wrapComponent == null || wrapComponent.canMoveY())
                position.y += velocity.yVelocity + velocity.yVelocityPersistent;

            if (bounds != null) {
                bounds.setPosition(position.x, position.y);
            }
        }

        for (Entity entity : relativeObjects) {

            PositionComponent position = Mappers.POSITION.get(entity);
            RelativePositionComponent relative = Mappers.RELATIVE_POSITION.get(entity);
            BoundsComponent bounds = Mappers.BOUNDS.get(entity);
            VelocityComponent velocity = Mappers.VELOCITY.get(entity);

            position.set(playerPosition.getVector2().add(playerSizeOffset).add(relative.getRelativePosition(deltaTime)));

            if (velocity != null) {
                position.r += velocity.rVelocity + velocity.rVelocityPersistent;
            }
            if (position.r < 0) position.r = position.r + 360;
            if (position.r > 360) position.r = position.r - 360;

            if (bounds != null) {
                bounds.setPosition(position.x, position.y);
            }
        }

    }
}
