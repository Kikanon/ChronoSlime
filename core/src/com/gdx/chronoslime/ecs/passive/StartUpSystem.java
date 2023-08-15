package com.gdx.chronoslime.ecs.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

public class StartUpSystem extends EntitySystem {
    public Entity player;
    private EntityFactorySystem factory;

    public StartUpSystem() {
        setProcessing(false);
    }

    @Override
    public void addedToEngine(Engine engine) {
        factory = engine.getSystem(EntityFactorySystem.class);
        startUp();
    }

    private void startUp() {
        player = factory.createPlayer();
    }
}
