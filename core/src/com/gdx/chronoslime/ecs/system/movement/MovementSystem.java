package com.gdx.chronoslime.ecs.system.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.RelativePositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.movement.WorldWrapComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;


public class MovementSystem extends EntitySystem {
    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            VelocityComponent.class
    ).get();

    private static final Family RELATIVE_FAMILY = Family.all(
            PositionComponent.class,
            SizeComponent.class,
            RelativePositionComponent.class
    ).get();

    private static final Family PLAYER_FAMILY = Family.all(
            PositionComponent.class,
            SizeComponent.class,
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
        SizeComponent playerSize = Mappers.SIZE.get(player);

        Vector2 playerSizeOffset = new Vector2(playerSize.width / 2f, playerSize.height / 2f);

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
            SizeComponent size = Mappers.SIZE.get(entity);
            VelocityComponent velocity = Mappers.VELOCITY.get(entity);

            Vector2 entitySizeOffset = new Vector2(size.width / 2f, size.height / 2f);


            position.set(playerPosition.getVector2().sub(entitySizeOffset).add(playerSizeOffset).add(relative.getRelativePosition(deltaTime)));

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
