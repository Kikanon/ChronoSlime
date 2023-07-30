package com.gdx.chronoslime.ecs.common;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

public class StartUpSystem extends EntitySystem {
  private EntityFactorySystem factory;
  public Entity player;

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
