package com.gdx.chronoslime.ecs.common.types;

public class ProjectileType {
    public float speed;
    public float damage;
    public float size;
    public String spriteName;
    public String itemSpriteName;

    public ProjectileType(String spriteName, String itemSpriteName, float speed, float damage, float size) {
        this.spriteName = spriteName;
        this.itemSpriteName = itemSpriteName;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
    }
}
