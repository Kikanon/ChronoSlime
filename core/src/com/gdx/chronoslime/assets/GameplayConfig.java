package com.gdx.chronoslime.assets;

import com.badlogic.gdx.utils.Array;
import com.gdx.chronoslime.ecs.common.types.ItemType;
import com.gdx.chronoslime.ecs.common.types.ProjectileType;

public class GameplayConfig {
    public static final float PLAYER_SIZE = 32f;
    public static final Array<ProjectileType> availableProjectiles = new Array<>(new ProjectileType[]{
            new ProjectileType(RegionNames.PROJECTILE, RegionNames.DEFAULT_ITEM, "Projectile 1", "Default projectile", 5f, 10, 8)
    });

    public static final Array<ItemType> availableItems = new Array<>(new ItemType[]{});
    public static float BOUNCE_SCALE = 0.02f;
}
