package com.gdx.chronoslime.ecs.component.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class WorldWrapComponent implements Component, Pool.Poolable {
    private boolean canMoveX = true;
    private boolean canMoveY = true;

    public void setCanMoveX(boolean move) {
        this.canMoveX = move;
    }

    public void setCanMoveY(boolean move) {
        this.canMoveY = move;
    }

    public boolean canMoveX() {
        return canMoveX;
    }

    public boolean canMoveY() {
        return canMoveY;
    }

    @Override
    public void reset() {
        canMoveX = true;
        canMoveY = true;
    }
}
