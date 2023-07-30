package com.gdx.chronoslime.ecs.common;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.gdx.chronoslime.ecs.component.ObstacleComponent;
import com.gdx.chronoslime.ecs.component.common.BoundsComponent;
import com.gdx.chronoslime.util.OrthogonalTiledMapRendererStopStartAnimated;

public class TiledSystem extends EntitySystem {
    public static float UNIT_SCALE = 1f;
    private final TiledMap tiledMap;
    float tileWidth;
    float tileHeight;
    int widthInt;
    int heightInt;
    float widthMapInPx;
    float heightMapInPx;
    //  TiledMapTileLayer colideTileLayer;
    MapLayer collideObjectsLayer;
    Rectangle tmp;
    Array<BoundsComponent> debug;


    OrthogonalTiledMapRendererStopStartAnimated mapRenderer;

    public TiledSystem(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        setProcessing(false); //passive
        init();
        tmp = new Rectangle();
    }

    private void init() {
        mapRenderer = new OrthogonalTiledMapRendererStopStartAnimated(tiledMap, UNIT_SCALE);
        mapRenderer.setAnimate(true);
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
        collideObjectsLayer = tiledMap.getLayers().get("Obstacles_objects");
        widthInt = tiledLayer.getWidth();
        heightInt = tiledLayer.getHeight();
        tileWidth = tiledLayer.getTileWidth();
        tileHeight = tiledLayer.getTileHeight();
        widthMapInPx = tileWidth * widthInt;
        heightMapInPx = tileHeight * heightInt;
//        GameConfig.W_WIDTH = widthMapInPx;
//        GameConfig.W_HEIGHT = heightMapInPx;
    }

    private void addObstacles(MapLayer layer, Engine engine) {
        MapObjects objects = layer.getObjects();
        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Entity entity = engine.createEntity();
            BoundsComponent bc = engine.createComponent(BoundsComponent.class);
            bc.rectangle(rectangle.width, rectangle.height);
            bc.setPosition(rectangle.getX(), rectangle.getY());
            ObstacleComponent oc = engine.createComponent(ObstacleComponent.class);
            entity.add(bc).add(oc);
            engine.addEntity(entity);
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        addObstacles(collideObjectsLayer, engine);

    }

    public void renderTiledView(OrthographicCamera camera, float x, float y) {
        camera.position.x = MathUtils.clamp(x, camera.viewportWidth / 2, widthMapInPx - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(y, camera.viewportHeight / 2, heightMapInPx - camera.viewportHeight / 2);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

}
