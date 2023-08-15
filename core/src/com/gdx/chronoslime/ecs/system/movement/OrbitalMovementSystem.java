package com.gdx.chronoslime.ecs.system.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.movement.OrbitalPositionComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;


public class OrbitalMovementSystem extends IteratingSystem {
    private static final Family ORBITAL_FAMILY = Family.all(
            PositionComponent.class,
            SizeComponent.class,
            OrbitalPositionComponent.class
    ).get();

    private static final Family PLAYER_FAMILY = Family.all(
            PositionComponent.class,
            SizeComponent.class,
            PlayerComponent.class
    ).get();

    private Vector2 playerSizeOffset;
    private PositionComponent playerPosition;

    public OrbitalMovementSystem() {
        super(ORBITAL_FAMILY);
    }

    @Override
    public void update(float deltaTime) {
        Entity player = getEngine().getEntitiesFor(PLAYER_FAMILY).first();
        playerPosition = Mappers.POSITION.get(player);
        SizeComponent playerSize = Mappers.SIZE.get(player);

        playerSizeOffset = new Vector2(playerSize.width / 2f, playerSize.height / 2f);
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        OrbitalPositionComponent relative = Mappers.RELATIVE_POSITION.get(entity);
        BoundsComponent bounds = Mappers.BOUNDS.get(entity);
        SizeComponent size = Mappers.SIZE.get(entity);
        VelocityComponent velocity = Mappers.VELOCITY.get(entity);

        Vector2 entitySizeOffset = new Vector2(size.width / 2f, size.height / 2f);

        position.set(playerPosition.getVector2().sub(entitySizeOffset).add(playerSizeOffset).add(relative.getRelativePosition(deltaTime)));

        if (velocity != null) {
            position.r += velocity.rVelocity + velocity.rVelocityPersistent;
        }
        if (position.r < 0) position.r = position.r + 360;
        if (position.r > 360) position.r = position.r - 360;

        if (bounds != null) {
            bounds.setPosition(position.x, position.y);
        }

    }
}
