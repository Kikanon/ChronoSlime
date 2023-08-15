package com.gdx.chronoslime.ecs.system.cleanup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;


public class PersistentVelocityRefreshSystem extends IteratingSystem {
    private static final Family FAMILY = Family.all(
            VelocityComponent.class
    ).get();


    public PersistentVelocityRefreshSystem() {
        super(FAMILY);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocityComponent = Mappers.VELOCITY.get(entity);

        if (!velocityComponent.keepPersistent) {
            velocityComponent.resetPersistent();
        }

        velocityComponent.keepPersistent = false;

    }
}
