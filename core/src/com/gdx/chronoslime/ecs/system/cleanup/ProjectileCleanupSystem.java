package com.gdx.chronoslime.ecs.system.cleanup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.identification.ProjectileComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.managers.GameManager;


public class ProjectileCleanupSystem extends IteratingSystem {

    private static final Family PROJECTILE_FAMILY = Family.all(
            ProjectileComponent.class,
            PositionComponent.class
    ).get();
    private static final Family PLAYER_FAMILY = Family.all(
            PlayerComponent.class,
            PositionComponent.class
    ).get();


    private PositionComponent playerPosition;

    public ProjectileCleanupSystem() {
        super(PROJECTILE_FAMILY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
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
            getEngine().removeEntity(entity);
        }
    }
}
