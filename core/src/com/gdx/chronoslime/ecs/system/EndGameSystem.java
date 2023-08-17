package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.lifetime.HealthComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ParticleFactorySystem;
import com.gdx.chronoslime.ecs.passive.types.enums.GameState;
import com.gdx.chronoslime.managers.GameManager;


public class EndGameSystem extends IteratingSystem {

    private static final Family PLAYER_FAMILY = Family.all(
            PlayerComponent.class,
            HealthComponent.class
    ).get();

    private ParticleFactorySystem factory;

    public EndGameSystem() {
        super(PLAYER_FAMILY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.factory = engine.getSystem(ParticleFactorySystem.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = Mappers.HEALTH.get(entity);
        if (healthComponent.currentHealth < 1) {
            // game over
            GameManager.INSTANCE.gameState = GameState.PAUSED;
        }
    }
}
