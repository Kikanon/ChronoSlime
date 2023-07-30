package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdx.chronoslime.assets.GameplayConfig;
import com.gdx.chronoslime.ecs.common.TiledSystem;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.managers.GameManager;

public class CameraMovementSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            PlayerComponent.class
    ).get();
    TiledSystem tiledSystem;

    public CameraMovementSystem() {
        super(FAMILY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        tiledSystem = engine.getSystem(TiledSystem.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);

        GameManager.INSTANCE.camera_x = position.x + GameplayConfig.PLAYER_SIZE / 2f;
        GameManager.INSTANCE.camera_y = position.y + GameplayConfig.PLAYER_SIZE / 2f;
    }
}
