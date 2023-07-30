package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.EnemyComponent;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.ecs.component.common.SizeComponent;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;


public class EnemyMovementSystem extends EntitySystem {

    private static final float force = -4.5f;
    private static final float terminalVelocity = -10f;
    private static final Family ENEMY_FAMILY = Family.all(
            EnemyComponent.class,
            VelocityComponent.class,
            PositionComponent.class
    ).get();
    private static final Family PLAYER_FAMILY = Family.all(
            PlayerComponent.class,
            PositionComponent.class
    ).get();

    public EnemyMovementSystem() {

    }

    @Override
    public void update(float deltaTime) {
        if (getEngine().getEntitiesFor(PLAYER_FAMILY).size() == 0) return;
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        ImmutableArray<Entity> enemies = getEngine().getEntitiesFor(ENEMY_FAMILY);

        PositionComponent playerPosition = Mappers.POSITION.get(player);
        SizeComponent playerSize = Mappers.SIZE.get(player);

        for (Entity enemy : enemies) {
            VelocityComponent velocityComponent = Mappers.VELOCITY.get(enemy);
            PositionComponent positionComponent = Mappers.POSITION.get(enemy);
            EnemyComponent enemyComponent = Mappers.ENEMY.get(enemy);

            // Calculate the vector C
            Vector2 vectorC = new Vector2(playerPosition.getVector2().add(playerSize.width / 2f, playerSize.height / 2f)).sub(positionComponent.getVector2()).nor().scl(enemyComponent.speed);

            velocityComponent.setFromVector2(vectorC);
        }

    }
}
