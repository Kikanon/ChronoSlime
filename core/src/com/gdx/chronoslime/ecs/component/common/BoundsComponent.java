package com.gdx.chronoslime.ecs.component.common;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Pool;

public class BoundsComponent implements Component, Pool.Poolable {
    public final Polygon polygon = new Polygon(new float[]{0, 0, 0, 1, 1, 0, 1, 1});

    public boolean collides(BoundsComponent other) {
        return Intersector.overlapConvexPolygons(polygon, other.polygon);
    }

    public boolean collides(Polygon other) {
        return Intersector.overlapConvexPolygons(polygon, other);
    }

    public void rectangle(float width, float height) {
        polygon.setVertices(new float[]{0, 0, width, 0, width,
                height, 0, height});
    }

    public Polygon getPolygon() {
        Polygon copy = new Polygon(polygon.getVertices());
        copy.setPosition(polygon.getX(), polygon.getY());
        return copy;
    }

    public void setPolygon(float[] vertices) {
        polygon.setVertices(vertices);
    }

    public void setPosition(float x, float y) {
        polygon.setPosition(x, y);
    }

    @Override
    public void reset() {
        polygon.setVertices(new float[]{0, 0, 0, 1, 1, 0, 1, 1});
    }
}
