package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.gdx.chronoslime.config.GameConfig;
import com.gdx.chronoslime.ecs.component.ObstacleComponent;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.managers.GameManager;


public class EnemySpawnSystem extends IntervalSystem {
    private static final Family FAMILY_OBSTACLE = Family.all(ObstacleComponent.class, BoundsComponent.class).get();
    public static int PRIORITY = 1;
    private final Family PLAYER_FAMILY = Family.all(PlayerComponent.class, PositionComponent.class).get();

    public EnemySpawnSystem() {
        super(0.2f, PRIORITY); // seconds
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void updateInterval() {
        for (int i = 0; i < (int) Math.ceil(GameManager.INSTANCE.enemyQueue.size / 100.0) * GameConfig.SPAWN_BATCH_SIZE; i++) {
            if (GameManager.INSTANCE.enemyQueue.isEmpty()) return;
            Entity enemy = GameManager.INSTANCE.enemyQueue.removeFirst();
            getEngine().addEntity(enemy);
        }
    }

    public Entity assignRandomPos(Entity entity) {
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        ImmutableArray<Entity> obstacles = getEngine().getEntitiesFor(FAMILY_OBSTACLE);
        return assignRandomPos(entity, player, obstacles);
    }

    public Entity assignRandomPos(Entity entity, Entity player, ImmutableArray<Entity> obstacles) {
        PositionComponent playerPosition = Mappers.POSITION.get(player);
        PositionComponent enemyPosition = entity.getComponent(PositionComponent.class);
        BoundsComponent enemyBounds = entity.getComponent(BoundsComponent.class);
        boolean validPosition;
        do {
            validPosition = true;
            // top side rand
            if (MathUtils.randomBoolean()) {// vertical edges
                enemyPosition.y = MathUtils.random(playerPosition.y - GameManager.INSTANCE.W_HEIGHT / 2f, playerPosition.y + GameManager.INSTANCE.W_HEIGHT / 2f);
                if (MathUtils.randomBoolean()) { // left
                    enemyPosition.x = playerPosition.x - GameManager.INSTANCE.W_WIDTH / 2f;
                } else { // right
                    enemyPosition.x = playerPosition.x + GameManager.INSTANCE.W_WIDTH / 2f;
                }
            } else { // horizontal edges
                enemyPosition.x = MathUtils.random(playerPosition.x - GameManager.INSTANCE.W_WIDTH / 2f, playerPosition.x + GameManager.INSTANCE.W_WIDTH / 2f);
                if (MathUtils.randomBoolean()) { // top
                    enemyPosition.y = playerPosition.y - GameManager.INSTANCE.W_HEIGHT / 2f;
                } else { // bottom
                    enemyPosition.y = playerPosition.y + GameManager.INSTANCE.W_HEIGHT / 2f;
                }
            }
            enemyBounds.setPosition(enemyPosition.x, enemyPosition.y);
            for (Entity obstacle : obstacles) {
                BoundsComponent obstacleBounds = Mappers.BOUNDS.get(obstacle);
                if (enemyBounds.collides(obstacleBounds)) {
                    validPosition = false;
                }
            }
        } while (!validPosition);
        return entity;
    }

}
