package com.gdx.chronoslime.ecs.component.drawable;

import com.badlogic.ashley.core.Entity;
import com.gdx.chronoslime.ecs.component.util.Mappers;

import java.util.Comparator;

public class ZOrderComparator implements Comparator<Entity> {
    public static final ZOrderComparator INSTANCE = new ZOrderComparator();

    private ZOrderComparator() {
    }

    @Override
    public int compare(Entity entity1, Entity entity2) {
        int response = Float.compare(
                Mappers.Z_ORDER.get(entity1).z,
                Mappers.Z_ORDER.get(entity2).z
        );
        if (response == 0) {
            try {
                response = Float.compare(Mappers.POSITION.get(entity2).y, Mappers.POSITION.get(entity1).y);
            } catch (Exception e) {
                return 0;
            }

        }
        return response;
    }
}
