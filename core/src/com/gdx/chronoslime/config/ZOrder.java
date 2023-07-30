package com.gdx.chronoslime.config;

public enum ZOrder {
    // 0 == ozadje
    PLAYER(5),
    ENEMY(4),
    PROJECTILE(3),
    PARTICLE(6);

    private final int z;

    ZOrder(int i) {
        this.z = i;
    }

    public int getZ() {
        return z;
    }
}
