package com.gdx.chronoslime.ecs.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.assets.RegionNames;
import com.gdx.chronoslime.config.GameplayConfig;
import com.gdx.chronoslime.config.ZOrder;
import com.gdx.chronoslime.ecs.component.drawable.TextureComponent;
import com.gdx.chronoslime.ecs.component.drawable.ZOrderComponent;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.interaction.InteractableComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.movement.WorldWrapComponent;
import com.gdx.chronoslime.ecs.passive.types.EnemyType;
import com.gdx.chronoslime.managers.GameManager;

import java.util.MissingResourceException;

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

        float[] ary = type.shape.clone();
        for (int i = 0; i < ary.length; i++) {
            ary[i] = ary[i] * type.size;
        }
        bounds.setPolygon(ary);

        int numFrames = gamePlayAtlas.findRegions(type.spriteName).size;
        if (numFrames > 1) {
            texture.animation = new Animation<TextureRegion>(0.44f, gamePlayAtlas.findRegions(type.spriteName), Animation.PlayMode.LOOP);
        } else if (numFrames == 1) {
            texture.texture = gamePlayAtlas.findRegion(type.spriteName);
        } else {
            throw new MissingResourceException("Missing texture region", null, null);
        }


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

}
