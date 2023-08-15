package com.gdx.chronoslime.ecs.component.drawable;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent implements Component, Pool.Poolable {
    public TextureRegion texture;
    public Animation<TextureRegion> animation;

    public float xScale = 1;
    public float yScale = 1;
    public float animationTime = 0;


    @Override
    public void reset() {
        xScale = 1;
        yScale = 1;
        animationTime = 0;
        texture = null;
        animation = null;
    }
}
