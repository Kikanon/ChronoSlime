package com.gdx.chronoslime.ecs.system.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.movement.FollowPlayerComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;


public class FollowPlayerSystem extends IteratingSystem {
    private static final Family FOLLOWER_FAMILY = Family.all(
            PositionComponent.class,
            VelocityComponent.class,
            FollowPlayerComponent.class
    ).get();
    private static final Family PLAYER_FAMILY = Family.all(
            PlayerComponent.class,
            PositionComponent.class
    ).get();

    private PositionComponent playerPosition;
    private SizeComponent playerSize;

    public FollowPlayerSystem() {
        super(FOLLOWER_FAMILY);
    }

    @Override
    public void update(float deltaTime) {
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();

        playerPosition = Mappers.POSITION.get(player);
        playerSize = Mappers.SIZE.get(player);
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocityComponent = Mappers.VELOCITY.get(entity);
        PositionComponent positionComponent = Mappers.POSITION.get(entity);
        EnemyComponent enemyComponent = Mappers.ENEMY.get(entity);

        // Calculate the vector C
        Vector2 vectorC = new Vector2(playerPosition.getVector2().add(playerSize.width / 2f, playerSize.height / 2f)).sub(positionComponent.getVector2()).nor().scl(enemyComponent.speed);

        velocityComponent.setFromVector2(vectorC);
    }
}
