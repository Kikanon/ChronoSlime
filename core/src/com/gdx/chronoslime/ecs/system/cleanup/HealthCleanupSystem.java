package com.gdx.chronoslime.ecs.system.cleanup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.lifetime.HealthComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ParticleFactorySystem;
import com.gdx.chronoslime.managers.GameManager;


public class HealthCleanupSystem extends IteratingSystem {


    private static final Family FAMILY = Family.all(
            HealthComponent.class
    ).get();

    private ParticleFactorySystem factory;

    public HealthCleanupSystem() {
        super(FAMILY);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.factory = engine.getSystem(ParticleFactorySystem.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = Mappers.HEALTH.get(entity);
        if (healthComponent.currentHealth < 1) {
            EnemyComponent enemyComponent = Mappers.ENEMY.get(entity);
            if (enemyComponent != null) GameManager.INSTANCE.score += enemyComponent.score;
            getEngine().removeEntity(entity);
        }
    }
}
