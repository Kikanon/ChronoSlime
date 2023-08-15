package com.gdx.chronoslime.ecs.component.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.gdx.chronoslime.config.GameplayConfig;

public class VelocityComponent implements Component, Pool.Poolable {
    public float xVelocity;
    public float yVelocity;
    public float rVelocity;

    public float xVelocityPersistent;
    public float yVelocityPersistent;
    public float rVelocityPersistent;

    public boolean keepPersistent = false;

    public float getTotalVelocity() {
        return (float) Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
    }

    public float getTotalXVelocity() {
        return xVelocity + xVelocityPersistent;
    }

    public float getTotalYVelocity() {
        return yVelocity + yVelocityPersistent;
    }

    public float getTotalRVelocity() {
        return rVelocity + rVelocityPersistent;
    }

    public void setFromVector2(Vector2 vector2) {
        this.xVelocity = vector2.x;
        this.yVelocity = vector2.y;
    }

    public void addFromVector2(Vector2 vector2) {
        this.xVelocity += vector2.x;
        this.yVelocity += vector2.y;
    }


    public void addPersistentVelocity(Vector2 vector2) {
        this.xVelocityPersistent += vector2.x * GameplayConfig.BOUNCE_SCALE;
        this.yVelocityPersistent += vector2.y * GameplayConfig.BOUNCE_SCALE;
    }

    public Vector2 getVector2Velocity() {
        return new Vector2(xVelocity, yVelocity);
    }

    public void bounce(float angle, float bounciness) {
        float angleDiff = (float) ((Math.atan2(xVelocity, yVelocity) * 180 / Math.PI) - angle);
        float bounceVelocity = (float) (getTotalVelocity() * Math.cos(angleDiff) * (1 + bounciness));
        this.xVelocity += bounceVelocity * Math.cos(angle);
        this.yVelocity += bounceVelocity * Math.sin(angle);
    }

    @Override
    public void reset() {
        keepPersistent = false;
        xVelocity = 0f;
        yVelocity = 0f;
        rVelocity = 0f;
        xVelocityPersistent = 0f;
        yVelocityPersistent = 0f;
        rVelocityPersistent = 0f;
    }

    public void resetPersistent() {
        xVelocityPersistent = 0f;
        yVelocityPersistent = 0f;
        rVelocityPersistent = 0f;
    }
}
