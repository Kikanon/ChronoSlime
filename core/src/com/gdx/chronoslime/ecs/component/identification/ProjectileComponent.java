package com.gdx.chronoslime.ecs.component.identification;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ProjectileComponent implements Component, Pool.Poolable {

    public int health = 1;
    public float damage = 1;

    @Override
    public void reset() {
        health = 1;
        damage = 1;

    }
}
