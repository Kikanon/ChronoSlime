package com.gdx.chronoslime.ecs.system.cleanup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gdx.chronoslime.ecs.component.lifetime.LifetimeComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ParticleFactorySystem;
import com.gdx.chronoslime.ecs.system.spawning.EnemySpawnSystem;
import com.gdx.chronoslime.managers.GameManager;


public class LifetimeCleanupSystem extends EntitySystem {

    private static final Family LIFETIME_FAMILY = Family.all(
            LifetimeComponent.class
    ).get();

    EnemySpawnSystem spawnSystem;
    private ParticleFactorySystem factory;

    public LifetimeCleanupSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.spawnSystem = engine.getSystem(EnemySpawnSystem.class);
        this.factory = engine.getSystem(ParticleFactorySystem.class);
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> limitedLifeEntities = getEngine().getEntitiesFor(LIFETIME_FAMILY);


        for (Entity entity : limitedLifeEntities
        ) {
            LifetimeComponent lifetimeComponent = Mappers.LIFETIME.get(entity);
            if (lifetimeComponent.lifeStartTime + lifetimeComponent.lifeDuration < GameManager.INSTANCE.elapsedTime) {
                getEngine().removeEntity(entity);
            }

        }
    }
}
