package com.gdx.chronoslime.ecs.component.lifetime;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.gdx.chronoslime.managers.GameManager;

public class LifetimeComponent implements Component, Pool.Poolable {

    public float lifeDuration = 0;
    public float lifeStartTime;

    LifetimeComponent() {
        lifeStartTime = GameManager.INSTANCE.elapsedTime;

    }

    @Override
    public void reset() {
        lifeStartTime = GameManager.INSTANCE.elapsedTime;
        lifeDuration = 0;
    }
}
