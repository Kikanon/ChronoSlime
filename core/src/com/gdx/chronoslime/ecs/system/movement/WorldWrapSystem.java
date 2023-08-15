package com.gdx.chronoslime.ecs.system.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Polygon;
import com.gdx.chronoslime.ecs.component.identification.ObstacleComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.movement.WorldWrapComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;


public class WorldWrapSystem extends EntitySystem {
    private static final Family FAMILY = Family.all(
            VelocityComponent.class,
            BoundsComponent.class,
            WorldWrapComponent.class
    ).get();

    private static final Family FAMILY_OBSTACLE = Family.all(ObstacleComponent.class, BoundsComponent.class).get();

    public WorldWrapSystem() {
        super();
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> objects = getEngine().getEntitiesFor(FAMILY);
        ImmutableArray<Entity> obstacles = getEngine().getEntitiesFor(FAMILY_OBSTACLE);

        for (Entity object : objects) {
            VelocityComponent velocity = Mappers.VELOCITY.get(object);
            WorldWrapComponent wrap = Mappers.WRAP.get(object);
            wrap.reset();
            BoundsComponent bounds = Mappers.BOUNDS.get(object);

            Polygon xBounds = bounds.getPolygon();
            Polygon yBounds = bounds.getPolygon();
            Polygon xyBounds = bounds.getPolygon();
            xBounds.setPosition(xBounds.getX() + velocity.xVelocity + velocity.xVelocityPersistent, xyBounds.getY());
            yBounds.setPosition(yBounds.getX(), yBounds.getY() + velocity.yVelocity + velocity.yVelocityPersistent);
            xyBounds.setPosition(xyBounds.getX() + velocity.xVelocity + velocity.xVelocityPersistent, xyBounds.getY() + velocity.yVelocity + velocity.yVelocityPersistent);

            for (Entity obstacle : obstacles) {
                BoundsComponent bounds2 = Mappers.BOUNDS.get(obstacle);

                if (bounds2.collides(xBounds))
                    wrap.setCanMoveX(false);
                if (bounds2.collides(yBounds))
                    wrap.setCanMoveY(false);

                //corner
                if (bounds2.collides(xyBounds) && wrap.canMoveX() && wrap.canMoveY()) {
                    wrap.setCanMoveY(false);
                }
            }
        }
    }
}
