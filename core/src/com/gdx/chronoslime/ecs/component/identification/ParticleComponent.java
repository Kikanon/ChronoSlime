package com.gdx.chronoslime.ecs.component.identification;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent implements Component, Pool.Poolable {
    public ParticleEffect effect;


    @Override
    public void reset() {
        effect = null;
    }
}
