package com.gdx.chronoslime.ecs.common;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.assets.GameplayConfig;
import com.gdx.chronoslime.assets.RegionNames;
import com.gdx.chronoslime.config.ZOrder;
import com.gdx.chronoslime.ecs.common.types.EnemyType;
import com.gdx.chronoslime.ecs.common.types.ProjectileType;
import com.gdx.chronoslime.ecs.component.EnemyComponent;
import com.gdx.chronoslime.ecs.component.InteractableComponent;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.ProjectileComponent;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;
import com.gdx.chronoslime.ecs.component.common.GravityComponent;
import com.gdx.chronoslime.ecs.component.common.ParticleComponent;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.ecs.component.common.RelativePositionComponent;
import com.gdx.chronoslime.ecs.component.common.SizeComponent;
import com.gdx.chronoslime.ecs.component.common.TextureComponent;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;
import com.gdx.chronoslime.ecs.component.common.WorldWrapComponent;
import com.gdx.chronoslime.ecs.component.common.ZOrderComponent;
import com.gdx.chronoslime.managers.GameManager;

public class EntityFactorySystem extends EntitySystem {
    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;

    public EntityFactorySystem(AssetManager assetManager) {
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


    public Entity createPlayer() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = 400f; //GameConfig.W_WIDTH/2f;
        position.y = 400f; //GameConfig.W_HEIGHT/2f;
        SizeComponent size = engine.createComponent(SizeComponent.class);
        size.width = GameplayConfig.PLAYER_SIZE;
        size.height = GameplayConfig.PLAYER_SIZE;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.setPosition(position.x, position.y);
        bounds.rectangle(size.width, size.height);

        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);

        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.animation = new Animation<TextureRegion>(0.44f, gamePlayAtlas.findRegions(RegionNames.PLAYER), Animation.PlayMode.LOOP);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = ZOrder.PLAYER.getZ();

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(size);
        entity.add(bounds);
        entity.add(velocity);
        entity.add(playerComponent);
        entity.add(worldWrap);
        entity.add(zOrder);
        entity.add(texture);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createProjectile(PositionComponent startPosition, VelocityComponent parentVelocity, float direction, int projectileId) {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.set(startPosition);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        ProjectileComponent projectileComponent = engine.createComponent(ProjectileComponent.class);
        GravityComponent gravityComponent = engine.createComponent(GravityComponent.class);

        ProjectileType type = GameplayConfig.availableProjectiles.get(projectileId);
        projectileComponent.damage = type.damage;
        size.rectangle(type.size);

        // offset
        position.x += GameplayConfig.PLAYER_SIZE / 2f - size.width / 2f;
        position.y += GameplayConfig.PLAYER_SIZE / 2f - size.height / 2f;

        bounds.setPosition(position.x, position.y);
        bounds.rectangle(size.width, size.height);

        texture.texture = gamePlayAtlas.findRegion(type.spriteName);
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

    public Entity createParticle(PositionComponent startPosition) {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        ParticleComponent particleComponent = engine.createComponent(ParticleComponent.class);
        ZOrderComponent zOrderComponent = engine.createComponent(ZOrderComponent.class);

        position.set(startPosition);

        particleComponent.effect = new ParticleEffect(assetManager.get(AssetDescriptors.COIN_EXPLOSION));
        particleComponent.effect.setPosition(position.x, position.y);
        particleComponent.effect.start();

        zOrderComponent.z = ZOrder.PARTICLE.getZ();

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(particleComponent);
        entity.add(zOrderComponent);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createEnemy(EnemyType type) {

        PositionComponent position = engine.createComponent(PositionComponent.class);
        SizeComponent size = engine.createComponent(SizeComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        WorldWrapComponent wrapComponent = engine.createComponent(WorldWrapComponent.class);
        InteractableComponent interactableComponent = engine.createComponent(InteractableComponent.class);

        size.height = type.size;
        size.width = type.size;

        bounds.setPolygon(new float[]{0, 0, 1 * type.size, 0, 0.5f * type.size, 1 * type.size});

        texture.texture = gamePlayAtlas.findRegion(type.spriteName);

        zOrder.z = ZOrder.ENEMY.getZ();

        enemyComponent.init(type);

        interactableComponent.hitDelay = 2000;

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(size);
        entity.add(bounds);
        entity.add(velocity);
        entity.add(zOrder);
        entity.add(texture);
        entity.add(wrapComponent);
        entity.add(enemyComponent);
        entity.add(interactableComponent);

        GameManager.INSTANCE.enemyQueue.addLast(entity);

        return entity;
    }

    public Entity createRelativeProjectile(int projectileId) {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        RelativePositionComponent relative = engine.createComponent(RelativePositionComponent.class);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        ProjectileComponent projectileComponent = engine.createComponent(ProjectileComponent.class);
        GravityComponent gravityComponent = engine.createComponent(GravityComponent.class);

        ProjectileType type = GameplayConfig.availableProjectiles.get(projectileId);
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

        engine.addEntity(entity);

        return entity;
    }

}
