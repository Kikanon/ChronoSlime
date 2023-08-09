package com.gdx.chronoslime.ecs.common.types;

public class ProjectileType extends ItemType {
    public float speed;
    public float damage;
    public float size;
    public String spriteName;

    public ProjectileType(String spriteName, String itemSpriteName, String itemName, String itemDescription, float speed, float damage, float size) {
        super(itemSpriteName, itemName, itemDescription);

        this.spriteName = spriteName;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
    }
}
