package com.gdx.chronoslime.ecs.passive.types;

import com.gdx.chronoslime.ecs.passive.types.enums.SpawnFuncId;

public class ProjectileType extends ItemType {
    public SpawnFuncId spawnFuncId;
    public float speed;
    public float damage;
    public float size;
    public String spriteName;
    public float lastSpawnTime;
    public float spawnSpeed;

    public ProjectileType(SpawnFuncId funcId, String spriteName, String itemSpriteName, String itemName, String itemDescription, float speed, float damage, float size, float spawnSpeed) {
        super(itemSpriteName, itemName, itemDescription);
        this.spawnSpeed = spawnSpeed;
        this.spriteName = spriteName;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.spawnFuncId = funcId;
        lastSpawnTime = 0;
    }

    @Override
    public ProjectileType clone() {
        return new ProjectileType(this.spawnFuncId, this.spriteName, this.itemSpriteName, this.itemName, this.itemDescription, this.speed, this.damage, this.size, this.spawnSpeed);
    }
}
