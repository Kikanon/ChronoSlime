package com.gdx.chronoslime.ecs.component.common;

import com.badlogic.ashley.core.ComponentMapper;
import com.gdx.chronoslime.ecs.component.EnemyComponent;
import com.gdx.chronoslime.ecs.component.InteractableComponent;
import com.gdx.chronoslime.ecs.component.ObstacleComponent;
import com.gdx.chronoslime.ecs.component.ProjectileComponent;

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

    private Mappers() {
    }
}