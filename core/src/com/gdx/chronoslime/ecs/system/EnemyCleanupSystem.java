package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.common.EntityFactorySystem;
import com.gdx.chronoslime.ecs.component.EnemyComponent;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.managers.GameManager;


public class EnemyCleanupSystem extends EntitySystem {

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

    EnemySpawnSystem spawnSystem;
    private EntityFactorySystem factory;

    public EnemyCleanupSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.spawnSystem = engine.getSystem(EnemySpawnSystem.class);
        this.factory = engine.getSystem(EntityFactorySystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (getEngine().getEntitiesFor(PLAYER_FAMILY).size() == 0) return;
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        ImmutableArray<Entity> enemies = getEngine().getEntitiesFor(ENEMY_FAMILY);

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

    }
}
