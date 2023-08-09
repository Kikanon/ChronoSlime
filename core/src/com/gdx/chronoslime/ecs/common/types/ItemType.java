package com.gdx.chronoslime.ecs.common.types;

public class ItemType {
    public String itemSpriteName;
    public String itemName;
    public String itemDescription;

    public int maxLevel = 1;
    public int level = 0;

    public ItemType(String itemSpriteName, String itemName, String itemDescription) {
        this.itemSpriteName = itemSpriteName;
        this.itemName = itemName;
        this.itemDescription = itemDescription;

    }
}
