package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.identification.ObstacleComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.identification.ProjectileComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.interaction.InteractableComponent;
import com.gdx.chronoslime.ecs.component.lifetime.HealthComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.ecs.passive.ParticleFactorySystem;
import com.gdx.chronoslime.ecs.passive.SoundSystem;
import com.gdx.chronoslime.ecs.passive.TiledSystem;
import com.gdx.chronoslime.managers.GameManager;

import java.util.Vector;

public class CollisionSystem extends EntitySystem {

    private static final Family FAMILY = Family.all(PlayerComponent.class, BoundsComponent.class).get();
    private static final Family FAMILY_OBSTACLE = Family.all(ObstacleComponent.class, BoundsComponent.class).get();
    private static final Family FAMILY_ENEMY = Family.all(EnemyComponent.class, BoundsComponent.class, VelocityComponent.class, PositionComponent.class, InteractableComponent.class, HealthComponent.class).get();
    private static final Family FAMILY_PROJECTILE = Family.all(ProjectileComponent.class, BoundsComponent.class).get();

    private ParticleFactorySystem particleFactory;
    private SoundSystem soundSystem;
    private TiledSystem tiledSystem;

    public CollisionSystem() {
    }

    @Override
    public void addedToEngine(Engine engine) {
        particleFactory = engine.getSystem(ParticleFactorySystem.class);
        soundSystem = engine.getSystem(SoundSystem.class);
        tiledSystem = engine.getSystem(TiledSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (GameManager.INSTANCE.gameOver()) return;
        Entity player = getEngine().getEntitiesFor(FAMILY).first();
        ImmutableArray<Entity> enemies = getEngine().getEntitiesFor(FAMILY_ENEMY);
        ImmutableArray<Entity> projectiles = getEngine().getEntitiesFor(FAMILY_PROJECTILE);

        BoundsComponent playerBounds = Mappers.BOUNDS.get(player);
        PositionComponent playerPosition = Mappers.POSITION.get(player);

        Vector<Integer> enemyVisited = new Vector<>();
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            BoundsComponent enemyBounds = Mappers.BOUNDS.get(enemy);
            VelocityComponent enemyVelocity = Mappers.VELOCITY.get(enemy);
            PositionComponent enemyPosition = Mappers.POSITION.get(enemy);
            enemyVisited.add(i);

            if (enemyBounds.collides(playerBounds)) {
                enemyVelocity.addPersistentVelocity(enemyPosition.getVector2().sub(playerPosition.getVector2()).scl(2f));
                enemyVelocity.keepPersistent = true;
            }

            for (int j = 0; j < enemies.size(); j++) {
                if (i == j || enemyVisited.contains(j)) continue;
                Entity enemy2 = enemies.get(j);
                BoundsComponent enemy2Bounds = Mappers.BOUNDS.get(enemy2);
                VelocityComponent enemy2Velocity = Mappers.VELOCITY.get(enemy2);
                PositionComponent enemy2Position = Mappers.POSITION.get(enemy2);
                if (enemyBounds.collides(enemy2Bounds)) {

                    enemyVelocity.addPersistentVelocity(enemyPosition.getVector2().sub(enemy2Position.getVector2()));
                    enemy2Velocity.addPersistentVelocity(enemy2Position.getVector2().sub(enemyPosition.getVector2()));
                    enemyVelocity.keepPersistent = true;
                    enemy2Velocity.keepPersistent = true;


                }

            }
        }

        for (int i = 0; i < projectiles.size(); i++) {
            Entity projectile = projectiles.get(i);
            BoundsComponent projectileBounds = Mappers.BOUNDS.get(projectile);
            ProjectileComponent projectileComponent = Mappers.PROJECTILE.get(projectile);
            for (int j = 0; j < enemies.size(); j++) {
                Entity enemy = enemies.get(j);
                BoundsComponent enemyBounds = Mappers.BOUNDS.get(enemy);
                EnemyComponent enemyComponent = Mappers.ENEMY.get(enemy);
                HealthComponent healthComponent = Mappers.HEALTH.get(enemy);
                InteractableComponent interactableComponent = Mappers.INTERACTABLE.get(enemy);
                if (interactableComponent == null) continue;

                if (projectileBounds.collides(enemyBounds) && !interactableComponent.hit) {
                    healthComponent.hurt(projectileComponent.damage);
                    interactableComponent.setHit(TimeUtils.nanoTime());
                    getEngine().removeEntity(projectile);
                    break;
                }
            }
        }
    }

}
