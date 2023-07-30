package com.gdx.chronoslime.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.gdx.chronoslime.ecs.common.types.EnemyType;

public class EnemyComponent implements Component, Pool.Poolable {
    public float health;
    public float damage;
    public float size;
    public float speed;
    public String spriteName;

    public void init(EnemyType type) {
        health = type.health;
        damage = type.damage;
        size = type.size;
        speed = type.speed;
        spriteName = type.spriteName;
    }

    public void hurt(float damage) {
        health -= damage;
    }

    @Override
    public void reset() {
        spriteName = null;
        health = 0f;
        damage = 0f;
        size = 0f;
        speed = 0f;
    }
}
