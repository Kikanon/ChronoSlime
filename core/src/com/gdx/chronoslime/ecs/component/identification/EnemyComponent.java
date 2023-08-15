package com.gdx.chronoslime.ecs.component.identification;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.gdx.chronoslime.ecs.passive.types.EnemyType;

public class EnemyComponent implements Component, Pool.Poolable {
    public float damage;
    public float size;
    public float speed;

    public float score;

    public void init(EnemyType type) {
        damage = type.damage;
        size = type.size;
        speed = type.speed;
        score = type.score;
    }


    @Override
    public void reset() {
        damage = 0f;
        size = 0f;
        speed = 0f;
    }
}
