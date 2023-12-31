package com.gdx.chronoslime.ecs.system.spawning;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.gdx.chronoslime.ecs.component.drawable.TextureComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ProjectileFactorySystem;
import com.gdx.chronoslime.ecs.types.ProjectileType;
import com.gdx.chronoslime.managers.GameManager;


public class ProjectileSpawnSystem extends IntervalSystem {
    private static final Family FAMILY = Family.all(
            PlayerComponent.class,
            PositionComponent.class,
            VelocityComponent.class,
            TextureComponent.class
    ).get();
    private ProjectileFactorySystem projectileFactory;

    public ProjectileSpawnSystem() {
        super(0.1f);
    }


    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        projectileFactory = engine.getSystem(ProjectileFactorySystem.class);
    }

    @Override
    public void updateInterval() {
        Entity player = getEngine().getEntitiesFor(FAMILY).first();
        if (player == null) return;

        PositionComponent position = Mappers.POSITION.get(player);
        VelocityComponent velocity = Mappers.VELOCITY.get(player);
        TextureComponent texture = Mappers.TEXTURE.get(player);


        for (ProjectileType item : GameManager.INSTANCE.playerWeapons) {
            if (item.lastSpawnTime + item.spawnSpeed < GameManager.INSTANCE.elapsedTime) {
                switch (item.spawnFuncId) {
                    case FORWARD: {
                        projectileFactory.createProjectileForward(position, velocity, item, texture.xScale);
                        break;
                    }
                    case ORBIT: {
                        projectileFactory.createRelativeProjectile(position, item);
                        break;
                    }
                }

                item.lastSpawnTime = GameManager.INSTANCE.elapsedTime;

            }
        }

    }
}
