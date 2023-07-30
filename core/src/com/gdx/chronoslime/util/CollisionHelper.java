package com.gdx.chronoslime.util;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;

public class CollisionHelper {

    public static float getObstacleNormal(BoundsComponent collider, BoundsComponent obstacle) {

        Vector2 colliderLineStart = new Vector2();
        Vector2 colliderLineEnd = new Vector2();
        collider.polygon.getCentroid(colliderLineStart);
        obstacle.polygon.getCentroid(colliderLineEnd);


        float[] polygonVertices = obstacle.polygon.getTransformedVertices();
        Vector2[] polygonVertices2D = new Vector2[polygonVertices.length / 2];
        for (int i = 0; i < polygonVertices.length; i += 2) {
            polygonVertices2D[i / 2] = new Vector2(polygonVertices[i], polygonVertices[i + 1]);
        }
        Vector2 intersection = new Vector2();
        int edgeIndex = -1;
        for (int i = 0; i < polygonVertices2D.length; i++) {
            if (Intersector.intersectSegments(colliderLineStart, colliderLineEnd, polygonVertices2D[i], polygonVertices2D[i + 1 < polygonVertices2D.length ? i + 1 : 0], intersection)) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex == -1) {
            System.out.println("erorors");
        }

        Vector2 start = polygonVertices2D[edgeIndex];
        Vector2 end = polygonVertices2D[edgeIndex + 1 < polygonVertices2D.length ? edgeIndex + 1 : 0];
        Vector2 direction = start.cpy().sub(end).nor();
        Vector2 normal = new Vector2(-direction.y, direction.x);
        float edgeAngle = (float) Math.toDegrees(Math.atan2(normal.y, normal.x));
        //edgeAngle = (edgeAngle + 360) % 360;

        return edgeAngle;
    }
}
