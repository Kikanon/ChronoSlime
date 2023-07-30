package com.gdx.chronoslime.ecs.system.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.ParticleComponent;


public class ParticleCleanupSystem extends IteratingSystem {
    private static final Family FAMILY = Family.all(
            ParticleComponent.class
    ).get();


    public ParticleCleanupSystem() {
        super(FAMILY);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleComponent particle = Mappers.PARTICLE.get(entity);

        if (particle.effect.isComplete()) {
            getEngine().removeEntity(entity);
        }

    }
}
