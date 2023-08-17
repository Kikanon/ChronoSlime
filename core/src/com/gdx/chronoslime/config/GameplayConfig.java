package com.gdx.chronoslime.config;

import com.badlogic.gdx.utils.Array;
import com.gdx.chronoslime.assets.RegionNames;
import com.gdx.chronoslime.ecs.passive.types.ItemBuffType;
import com.gdx.chronoslime.ecs.passive.types.ProjectileType;
import com.gdx.chronoslime.ecs.passive.types.enums.SpawnFuncId;

public class GameplayConfig {
    public static final float PLAYER_SIZE = 32f;
    public static final Array<ProjectileType> availableProjectiles = new Array<>(new ProjectileType[]{
            new ProjectileType(SpawnFuncId.ORBIT, RegionNames.PROJECTILE, RegionNames.DEFAULT_ITEM, "orbit projectile", "Orbiting projectiles", 5f, 10, 8, 20f),
            new ProjectileType(SpawnFuncId.FORWARD, RegionNames.PROJECTILE, RegionNames.DEFAULT_ITEM, "straight projectile", "Go straight", 5f, 10, 8, 20f)
    });

    public static final Array<ItemBuffType> availableItems = new Array<>(new ItemBuffType[]{});
    public static float BOUNCE_SCALE = 0.02f;
}
