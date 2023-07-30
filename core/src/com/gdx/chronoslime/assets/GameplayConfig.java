package com.gdx.chronoslime.assets;

import com.gdx.chronoslime.ecs.common.types.ProjectileType;

public class GameplayConfig {
    public static final float PLAYER_SIZE = 32f;
    public static final ProjectileType[] projectileTypes = new ProjectileType[]{
            new ProjectileType(RegionNames.PROJECTILE, "", 5f, 10, 8)
    };
    public static float BOUNCE_SCALE = 0.02f;
}
