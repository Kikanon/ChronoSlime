package com.gdx.chronoslime.ecs.component.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class RelativePositionComponent implements Component, Pool.Poolable {

    public float rDistance = 10f;

    public float innerTime = 0f;

    public float speed = 1f;

    // true -> counter clockwise, false -> clockwise
    public boolean direction = true;

    public Vector2 getRelativePosition(float deltaTime) {
        if (direction) {
            innerTime += deltaTime * speed;
        } else {
            innerTime -= deltaTime * speed;
        }
        return new Vector2((float) Math.cos(innerTime) * rDistance, (float) Math.sin(innerTime) * rDistance);
    }

    @Override
    public void reset() {
        innerTime = 0f;
        rDistance = 10f;
    }
}
