package com.gdx.chronoslime.ecs.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.config.GameplayConfig;
import com.gdx.chronoslime.config.ZOrder;
import com.gdx.chronoslime.ecs.component.drawable.TextureComponent;
import com.gdx.chronoslime.ecs.component.drawable.ZOrderComponent;
import com.gdx.chronoslime.ecs.component.identification.ProjectileComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.lifetime.LifetimeComponent;
import com.gdx.chronoslime.ecs.component.movement.GravityComponent;
import com.gdx.chronoslime.ecs.component.movement.OrbitalPositionComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.types.ProjectileType;

public class ProjectileFactorySystem extends EntitySystem {
    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;

    public ProjectileFactorySystem(AssetManager assetManager) {
        this.assetManager = assetManager;
        setProcessing(false); //passive
        init();
    }

    private void init() {
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
    }


    public Entity createProjectileForward(PositionComponent startPosition, VelocityComponent parentVelocity, ProjectileType type, float dir) {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.set(startPosition);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        ProjectileComponent projectileComponent = engine.createComponent(ProjectileComponent.class);
        GravityComponent gravityComponent = engine.createComponent(GravityComponent.class);

        projectileComponent.damage = type.damage;
        size.rectangle(type.size);

        // offset
        position.x += GameplayConfig.PLAYER_SIZE / 2f - size.width / 2f;
        position.y += GameplayConfig.PLAYER_SIZE / 2f - size.height / 2f;

        bounds.setPosition(position.x, position.y);
        bounds.rectangle(size.width, size.height);

        texture.texture = gamePlayAtlas.findRegion(type.spriteName);

        float direction = (float) Math.toDegrees(Math.atan2(parentVelocity.getTotalYVelocity(), parentVelocity.getTotalXVelocity()));
        if (direction == 0 && dir < 0) {
            direction += 180;
        }

        velocity.xVelocity = MathUtils.cosDeg(direction) * type.speed;
        velocity.yVelocity = MathUtils.sinDeg(direction) * type.speed;
        velocity.addFromVector2(parentVelocity.getVector2Velocity());
        zOrder.z = ZOrder.PROJECTILE.getZ();


        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(size);
        entity.add(bounds);
        entity.add(velocity);
        entity.add(zOrder);
        entity.add(texture);
        entity.add(projectileComponent);
        entity.add(gravityComponent);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createRelativeProjectile(PositionComponent parentPosition, ProjectileType type) {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.set(parentPosition);
        OrbitalPositionComponent relative = engine.createComponent(OrbitalPositionComponent.class);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        ProjectileComponent projectileComponent = engine.createComponent(ProjectileComponent.class);
        GravityComponent gravityComponent = engine.createComponent(GravityComponent.class);
        LifetimeComponent lifetimeComponent = engine.createComponent(LifetimeComponent.class);

        lifetimeComponent.lifeDuration = 2000f; // 2s

        projectileComponent.damage = type.damage;
        size.rectangle(type.size);

        relative.rDistance = 50f;
        relative.speed = 3f;
        relative.direction = false;

        bounds.setPosition(position.x, position.y);
        bounds.rectangle(size.width, size.height);

        texture.texture = gamePlayAtlas.findRegion(type.spriteName);
        zOrder.z = ZOrder.PROJECTILE.getZ();


        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(relative);
        entity.add(size);
        entity.add(bounds);
        entity.add(zOrder);
        entity.add(texture);
        entity.add(projectileComponent);
        entity.add(gravityComponent);
        entity.add(lifetimeComponent);

        engine.addEntity(entity);

        return entity;
    }

}
