package com.gdx.chronoslime.ecs.common.types;

import com.gdx.chronoslime.ecs.common.types.enums.ItemBuffId;

public class ItemBuffType extends ItemType {
    public ItemBuffId[] buffId;
    public float[] intensity;

    public ItemBuffType(String itemSpriteName, String itemName, String itemDescription, ItemBuffId[] ids, float[] intensity) {
        super(itemSpriteName, itemName, itemDescription);
        this.buffId = ids;
        this.intensity = intensity;
    }

    public ItemBuffType(String itemSpriteName, String itemName, String itemDescription, ItemBuffId id, float[] intensity) {
        super(itemSpriteName, itemName, itemDescription);
        this.buffId = new ItemBuffId[]{id};
        this.intensity = intensity;
    }

    public ItemBuffType(String itemSpriteName, String itemName, String itemDescription, ItemBuffId[] ids, float intensity) {
        super(itemSpriteName, itemName, itemDescription);
        this.buffId = ids;
        this.intensity = new float[]{intensity};
    }

    public ItemBuffType(String itemSpriteName, String itemName, String itemDescription, ItemBuffId id, float intensity) {
        super(itemSpriteName, itemName, itemDescription);
        this.buffId = new ItemBuffId[]{id};
        this.intensity = new float[]{intensity};
    }

    public float getIntensity() {
        return intensity[level];
    }


}
