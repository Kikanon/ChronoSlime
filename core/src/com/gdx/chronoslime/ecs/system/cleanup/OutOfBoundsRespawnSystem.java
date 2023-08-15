package com.gdx.chronoslime.ecs.system.cleanup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.system.spawning.EnemySpawnSystem;
import com.gdx.chronoslime.managers.GameManager;


public class OutOfBoundsRespawnSystem extends IteratingSystem {

    private static final Family ENEMY_FAMILY = Family.all(
            EnemyComponent.class,
            PositionComponent.class
    ).get();
    private static final Family PLAYER_FAMILY = Family.all(
            PlayerComponent.class,
            PositionComponent.class
    ).get();


    private EnemySpawnSystem spawnSystem;
    private PositionComponent playerPosition;

    public OutOfBoundsRespawnSystem() {
        super(ENEMY_FAMILY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.spawnSystem = engine.getSystem(EnemySpawnSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        playerPosition = Mappers.POSITION.get(player);
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = Mappers.POSITION.get(entity);

        Vector2 posDiff = playerPosition.getVector2().sub(positionComponent.getVector2());

        if (posDiff.x > GameManager.INSTANCE.W_WIDTH / 1.5f ||
                -posDiff.x > GameManager.INSTANCE.W_WIDTH / 1.5f ||
                posDiff.y > GameManager.INSTANCE.W_HEIGHT / 1.5f ||
                -posDiff.y > GameManager.INSTANCE.W_HEIGHT / 1.5f) {
            spawnSystem.assignRandomPos(entity);
        }
    }
}
