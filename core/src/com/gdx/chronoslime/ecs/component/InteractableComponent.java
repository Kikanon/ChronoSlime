package com.gdx.chronoslime.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InteractableComponent implements Component, Pool.Poolable {

    public float hitDelay = 1f;
    public long lastHitTime = 0;
    public boolean hit = false;

    public void setHit(long time) {
        lastHitTime = time;
        hit = true;
    }

    @Override
    public void reset() {
        hit = false;
        hitDelay = 1f;
        lastHitTime = 0;
    }
}
