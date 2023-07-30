package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.TimeUtils;
import com.gdx.chronoslime.ecs.component.InteractableComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;


public class InteractableRefreshSystem extends IteratingSystem {
    private static final Family FAMILY = Family.all(
            InteractableComponent.class
    ).get();

    private long currentTime;


    public InteractableRefreshSystem() {
        super(FAMILY);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        currentTime = TimeUtils.nanoTime();
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InteractableComponent interactableComponent = Mappers.INTERACTABLE.get(entity);

        if (interactableComponent.hit) {
            if (interactableComponent.lastHitTime + interactableComponent.hitDelay < currentTime) {
                interactableComponent.hit = false;
            }
        }

    }
}
