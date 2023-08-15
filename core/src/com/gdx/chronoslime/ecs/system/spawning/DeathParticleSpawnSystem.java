package com.gdx.chronoslime.ecs.system.spawning;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.lifetime.HealthComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ParticleFactorySystem;


public class DeathParticleSpawnSystem extends IteratingSystem {
    private static final Family FAMILY = Family.all(HealthComponent.class, PositionComponent.class).get();
    private ParticleFactorySystem particleFactory;

    public DeathParticleSpawnSystem() {
        super(FAMILY); // seconds
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        particleFactory = engine.getSystem(ParticleFactorySystem.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = Mappers.HEALTH.get(entity);
        PositionComponent positionComponent = Mappers.POSITION.get(entity);
        if (healthComponent.currentHealth < 1) {
            particleFactory.createParticle(positionComponent);
        }
    }


}
