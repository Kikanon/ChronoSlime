package com.gdx.chronoslime.ecs.passive.types;

public class ItemType {
    public String itemSpriteName;
    public String itemName;
    public String itemDescription;

    public int maxLevel = 0;
    public int level = 0;

    public ItemType(String itemSpriteName, String itemName, String itemDescription) {
        this.itemSpriteName = itemSpriteName;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    @Override
    public ItemType clone() {
        return new ItemType(this.itemSpriteName, this.itemName, this.itemDescription);
    }

}
