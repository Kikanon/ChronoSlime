package com.gdx.chronoslime.ecs.system.cleanup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.lifetime.LifetimeComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ParticleFactorySystem;
import com.gdx.chronoslime.ecs.system.spawning.EnemySpawnSystem;
import com.gdx.chronoslime.managers.GameManager;


public class OutOfBoundsCleanupSystem extends EntitySystem {

    private static final float force = -4.5f;
    private static final float terminalVelocity = -10f;
    private static final Family ENEMY_FAMILY = Family.all(
            EnemyComponent.class,
            PositionComponent.class
    ).get();
    private static final Family PLAYER_FAMILY = Family.all(
            PlayerComponent.class,
            PositionComponent.class
    ).get();
    private static final Family LIFETIME_FAMILY = Family.all(
            LifetimeComponent.class
    ).get();

    EnemySpawnSystem spawnSystem;
    private ParticleFactorySystem factory;

    public OutOfBoundsCleanupSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.spawnSystem = engine.getSystem(EnemySpawnSystem.class);
        this.factory = engine.getSystem(ParticleFactorySystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (getEngine().getEntitiesFor(PLAYER_FAMILY).size() == 0) return;
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        ImmutableArray<Entity> enemies = getEngine().getEntitiesFor(ENEMY_FAMILY);
        ImmutableArray<Entity> limitedLifeEntities = getEngine().getEntitiesFor(LIFETIME_FAMILY);

        PositionComponent playerPosition = Mappers.POSITION.get(player);

        for (Entity enemy : enemies) {
            PositionComponent positionComponent = Mappers.POSITION.get(enemy);
            EnemyComponent enemyComponent = Mappers.ENEMY.get(enemy);

            if (enemyComponent.health < 1) {
                factory.createParticle(positionComponent);
                GameManager.INSTANCE.score += enemyComponent.score;
                getEngine().removeEntity(enemy);
                continue;
            }

            Vector2 posDiff = playerPosition.getVector2().sub(positionComponent.getVector2());

            if (posDiff.x > GameManager.INSTANCE.W_WIDTH / 1.5f ||
                    -posDiff.x > GameManager.INSTANCE.W_WIDTH / 1.5f ||
                    posDiff.y > GameManager.INSTANCE.W_HEIGHT / 1.5f ||
                    -posDiff.y > GameManager.INSTANCE.W_HEIGHT / 1.5f) {
                spawnSystem.assignRandomPos(enemy);
            }

        }

        for (Entity entity : limitedLifeEntities
        ) {
            LifetimeComponent lifetimeComponent = Mappers.LIFETIME.get(entity);
            if (lifetimeComponent.lifeStartTime + lifetimeComponent.lifeDuration < GameManager.INSTANCE.elapsedTime) {
                getEngine().removeEntity(entity);
            }

        }
    }
}
