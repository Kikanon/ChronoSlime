package com.gdx.chronoslime.ecs.types.enums;

public enum PreferenceKey {
    START_ITEM("startItem");
    private final String text;

    PreferenceKey(String value) {
        this.text = value;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
