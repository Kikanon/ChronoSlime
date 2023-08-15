package com.gdx.chronoslime.ecs.component.interaction;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SizeComponent implements Component, Pool.Poolable {
    public float width = 1f;
    public float height = 1f;

    public void rectangle(float size) {
        width = size;
        height = size;
    }

    @Override
    public void reset() {
        width = 1f;
        height = 1f;
    }
}
