package com.gdx.chronoslime.ecs.component.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.gdx.chronoslime.ecs.component.drawable.TextureComponent;
import com.gdx.chronoslime.ecs.component.drawable.ZOrderComponent;
import com.gdx.chronoslime.ecs.component.identification.EnemyComponent;
import com.gdx.chronoslime.ecs.component.identification.ObstacleComponent;
import com.gdx.chronoslime.ecs.component.identification.ParticleComponent;
import com.gdx.chronoslime.ecs.component.identification.ProjectileComponent;
import com.gdx.chronoslime.ecs.component.interaction.BoundsComponent;
import com.gdx.chronoslime.ecs.component.interaction.InteractableComponent;
import com.gdx.chronoslime.ecs.component.interaction.SizeComponent;
import com.gdx.chronoslime.ecs.component.lifetime.LifetimeComponent;
import com.gdx.chronoslime.ecs.component.movement.PositionComponent;
import com.gdx.chronoslime.ecs.component.movement.RelativePositionComponent;
import com.gdx.chronoslime.ecs.component.movement.VelocityComponent;
import com.gdx.chronoslime.ecs.component.movement.WorldWrapComponent;

public final class Mappers {

    public static final ComponentMapper<BoundsComponent> BOUNDS =
            ComponentMapper.getFor(BoundsComponent.class);

    public static final ComponentMapper<VelocityComponent> VELOCITY =
            ComponentMapper.getFor(VelocityComponent.class);

    public static final ComponentMapper<PositionComponent> POSITION =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<SizeComponent> SIZE =
            ComponentMapper.getFor(SizeComponent.class);

    public static final ComponentMapper<WorldWrapComponent> WRAP =
            ComponentMapper.getFor(WorldWrapComponent.class);
    public static final ComponentMapper<TextureComponent> TEXTURE =
            ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<ParticleComponent> PARTICLE =
            ComponentMapper.getFor(ParticleComponent.class);

    public static final ComponentMapper<ObstacleComponent> OBSTACLE =
            ComponentMapper.getFor(ObstacleComponent.class);
    public static final ComponentMapper<ZOrderComponent> Z_ORDER =
            ComponentMapper.getFor(ZOrderComponent.class);
    public static final ComponentMapper<EnemyComponent> ENEMY =
            ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<InteractableComponent> INTERACTABLE =
            ComponentMapper.getFor(InteractableComponent.class);

    public static final ComponentMapper<ProjectileComponent> PROJECTILE =
            ComponentMapper.getFor(ProjectileComponent.class);

    public static final ComponentMapper<LifetimeComponent> LIFETIME =
            ComponentMapper.getFor(LifetimeComponent.class);
    public static final ComponentMapper<RelativePositionComponent> RELATIVE_POSITION =
            ComponentMapper.getFor(RelativePositionComponent.class);

    private Mappers() {
    }
}