package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.common.GravityComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;


public class GravitySystem extends IteratingSystem {

    private static final float force = -4.5f;
    private static final float terminalVelocity = -10f;
    private static final Family FAMILY = Family.all(
            GravityComponent.class,
            VelocityComponent.class
    ).get();

    public GravitySystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocity = Mappers.VELOCITY.get(entity);

        if (velocity.yVelocity > terminalVelocity) {
            velocity.yVelocity += force * deltaTime;
        }

    }
}
