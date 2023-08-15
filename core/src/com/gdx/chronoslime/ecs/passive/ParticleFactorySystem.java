package com.gdx.chronoslime.ecs.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gdx.chronoslime.assets.AssetDescriptors;
import com.gdx.chronoslime.config.ZOrder;
import com.gdx.chronoslime.ecs.component.drawable.ZOrderComponent;
import com.gdx.chronoslime.ecs.component.identification.ParticleComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;

public class ParticleFactorySystem extends EntitySystem {
    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;

    public ParticleFactorySystem(AssetManager assetManager) {
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

}
