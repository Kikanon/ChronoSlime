package com.gdx.chronoslime.ecs.component.lifetime;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Component, Pool.Poolable {

    public float currentHealth;
    public float maxHealth;

    public void hurt(float damage) {
        currentHealth -= damage;
    }

    public void init(float hp) {
        maxHealth = hp;
        currentHealth = hp;
    }

    @Override
    public void reset() {
        currentHealth = 0;
        maxHealth = 1;
    }
}
