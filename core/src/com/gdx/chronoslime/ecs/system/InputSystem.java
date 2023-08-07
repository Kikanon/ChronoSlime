package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.gdx.chronoslime.ecs.common.EntityFactorySystem;
import com.gdx.chronoslime.ecs.component.PlayerComponent;
import com.gdx.chronoslime.ecs.component.common.Mappers;
import com.gdx.chronoslime.ecs.component.common.PositionComponent;
import com.gdx.chronoslime.ecs.component.common.TextureComponent;
import com.gdx.chronoslime.ecs.component.common.VelocityComponent;
import com.gdx.chronoslime.ecs.component.common.WorldWrapComponent;
import com.gdx.chronoslime.managers.GameManager;


public class InputSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PlayerComponent.class,
            VelocityComponent.class,
            WorldWrapComponent.class,
            PositionComponent.class,
            TextureComponent.class
    ).get();
    private EntityFactorySystem factory;

    public InputSystem() {
        super(FAMILY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.factory = engine.getSystem(EntityFactorySystem.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocity = Mappers.VELOCITY.get(entity);
        PositionComponent position = Mappers.POSITION.get(entity);
        TextureComponent texture = Mappers.TEXTURE.get(entity);
        WorldWrapComponent wrap = Mappers.WRAP.get(entity);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.xVelocity = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.xVelocity = MathUtils.clamp(velocity.xVelocity + (wrap.canMoveX() ? -GameManager.INSTANCE.playerSpeed() : GameManager.INSTANCE.playerSpeed()) / 5f, -GameManager.INSTANCE.playerSpeed(), GameManager.INSTANCE.playerSpeed());
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.xVelocity = MathUtils.clamp(velocity.xVelocity + (wrap.canMoveX() ? GameManager.INSTANCE.playerSpeed() : -GameManager.INSTANCE.playerSpeed()) / 5f, -GameManager.INSTANCE.playerSpeed(), GameManager.INSTANCE.playerSpeed());
        } else velocity.xVelocity = 0;


        if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.yVelocity = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.yVelocity = MathUtils.clamp(velocity.yVelocity + (wrap.canMoveY() ? GameManager.INSTANCE.playerSpeed() : -GameManager.INSTANCE.playerSpeed()) / 5f, -GameManager.INSTANCE.playerSpeed(), GameManager.INSTANCE.playerSpeed());
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.yVelocity = MathUtils.clamp(velocity.yVelocity + (wrap.canMoveY() ? -GameManager.INSTANCE.playerSpeed() : GameManager.INSTANCE.playerSpeed()) / 5f, -GameManager.INSTANCE.playerSpeed(), GameManager.INSTANCE.playerSpeed());
        } else velocity.yVelocity = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            factory.createProjectile(position, velocity, 0, 0);
//            factory.createProjectile(position, velocity, 90, 0);
//            factory.createProjectile(position, velocity, 180, 0);
//            factory.createProjectile(position, velocity, 270, 0);

            factory.createRelativeProjectile(0);


//            factory.createParticle(position);

//            EnemyType type = new EnemyType();
//            type.spriteName = "enemy";
//            type.size = GameConfig.PLAYER_SIZE;
//            type.health = 100;
//            type.damage = 20;
//
//            factory.createEnemy(type);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) GameManager.INSTANCE.resizeWorld(1.1f);
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) GameManager.INSTANCE.resizeWorld(0.9f);

        if (velocity.yVelocity == 0 && velocity.xVelocity == 0) {
            texture.animationTime = 0f;
        }
        if (velocity.xVelocity > 0) {
            texture.xScale = 1;
        } else if (velocity.xVelocity < 0) {
            texture.xScale = -1;
        }
    }
}
