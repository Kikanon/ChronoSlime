package com.gdx.chronoslime.ecs.component.common;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent implements Component, Pool.Poolable {
    public float x;
    public float y;
    public float r;

    public void set(PositionComponent pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.r = pos.r;
    }

    public void set(Vector2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2 getVector2() {
        return new Vector2(x, y);
    }

    @Override
    public void reset() {
        x = 0f;
        y = 0f;
        r = 0f;
    }

    @Override
    public String toString() {
        return "PositionComponent{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                '}';
    }
}
