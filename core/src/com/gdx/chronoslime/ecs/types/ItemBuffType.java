package com.gdx.chronoslime.ecs.types;

import com.gdx.chronoslime.ecs.types.enums.ItemBuffId;

public class ItemBuffType extends ItemType {
    public ItemBuffId buffId;
    public float[] intensity;

    public ItemBuffType(String itemSpriteName, String itemName, String itemDescription, ItemBuffId id, float[] intensity) {
        super(itemSpriteName, itemName, itemDescription);
        this.buffId = id;
        this.intensity = intensity;
    }

    public ItemBuffType(String itemSpriteName, String itemName, String itemDescription, ItemBuffId id, float intensity) {
        super(itemSpriteName, itemName, itemDescription);
        this.buffId = id;
        this.intensity = new float[]{intensity};
        level = 0;
        maxLevel = 0;
    }

    public float getIntensity() {
        return intensity[level];
    }

    @Override
    public ItemBuffType clone() {
        return new ItemBuffType(this.itemSpriteName, this.itemName, this.itemDescription, this.buffId, this.intensity);
    }
}
