package com.gdx.chronoslime.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.utils.TimeUtils;
import com.gdx.chronoslime.ecs.common.EntityFactorySystem;
import com.gdx.chronoslime.ecs.common.types.LevelType;
import com.gdx.chronoslime.managers.GameManager;


public class LevelSystem extends IntervalSystem {
    public static int PRIORITY = 1;
    EntityFactorySystem factory;
    private int currentLevel = -1;
    private int currentWave = 0;
    private long startLevelTime;

    public LevelSystem() {
        super(0.1f, PRIORITY); // seconds
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.factory = engine.getSystem(EntityFactorySystem.class);
        startLevelTime = TimeUtils.millis();
    }

    @Override
    protected void updateInterval() {
        LevelType[] levels = GameManager.INSTANCE.gameData.levels;

        if (currentLevel != -1 && currentWave < levels[currentLevel].waves.length) {
            if (TimeUtils.timeSinceMillis(startLevelTime) > levels[currentLevel].waves[currentWave].levelTimeOffset) {
                for (int i = 0; i < levels[currentLevel].waves[currentWave].enemyNum; i++) {

                    factory.createEnemy(GameManager.INSTANCE.gameData.enemyTypes[levels[currentLevel].waves[currentWave].enemyTypeNum]);
                }

                currentWave++;
            }
        }

        if (currentLevel + 1 != levels.length && GameManager.INSTANCE.elapsedTime > levels[currentLevel + 1].startTimeMils) {
            currentLevel++;
            currentWave = 0;
            startLevelTime = TimeUtils.millis();
        }
    }

}
