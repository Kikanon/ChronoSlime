package com.gdx.chronoslime.ecs.system.movement;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.gdx.chronoslime.ecs.component.drawable.TextureComponent;
import com.gdx.chronoslime.ecs.component.identification.PlayerComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.movement.WorldWrapComponent;
import com.gdx.chronoslime.ecs.component.util.Mappers;
import com.gdx.chronoslime.managers.GameManager;


public class InputSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PlayerComponent.class,
            VelocityComponent.class,
            WorldWrapComponent.class,
            PositionComponent.class,
            TextureComponent.class
    ).get();

    public InputSystem() {
        super(FAMILY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocity = Mappers.VELOCITY.get(entity);
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


        if (velocity.yVelocity == 0 && velocity.xVelocity == 0) {
            texture.animationTime = 0f;
        }
    }
}
