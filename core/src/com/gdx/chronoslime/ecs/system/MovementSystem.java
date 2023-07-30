package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;
import com.gdx.chronoslime.ecs.component.common.WorldWrapComponent;


public class MovementSystem extends IteratingSystem {
    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            VelocityComponent.class
    ).get();

    public MovementSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
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
}
