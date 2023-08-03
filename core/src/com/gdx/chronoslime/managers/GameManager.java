package com.gdx.chronoslime.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Queue;
import com.gdx.chronoslime.config.GameConfig;
import com.gdx.chronoslime.ecs.common.types.EnemyType;
import com.gdx.chronoslime.ecs.common.types.GameDataType;
import com.gdx.chronoslime.ecs.common.types.LevelType;
import com.gdx.chronoslime.ecs.common.types.Wave;

import java.util.concurrent.TimeUnit;

public class GameManager {

    // data
    public static final GameManager INSTANCE = new GameManager();
    public final Queue<Entity> enemyQueue = new Queue<Entity>();
    private final Json json = new Json();
    public GameDataType gameData = null;


    // variables

    public float camera_x = 0;
    public float camera_y = 0;
    public long elapsedTime = 0;
    public long minutes = 0;
    public long seconds = 0;

    public float score = 0;

    // game data variables
    public boolean DEBUG;
    public float W_HEIGHT;
    public float W_WIDTH;


    private GameManager() {
    }

    public void resizeWorld(float scale) {
        if (scale <= 0) return;
        W_WIDTH = W_WIDTH * scale;
        W_HEIGHT = W_HEIGHT * scale;
    }

    public boolean gameOver() {
        return false;
    }

    public float playerSpeed() {
        return 5.0f;
    }


    public void writeSettings() {
        FileHandle outFile = Gdx.files.absolute("J:\\Projects\\6.Semester - Diploma\\ChronoSlime\\assets\\data\\outData.json");
        GameDataType data = new GameDataType();
        EnemyType tempEnemy = new EnemyType();
        tempEnemy.damage = 20;
        tempEnemy.health = 20;
        tempEnemy.size = 20;

        tempEnemy.spriteName = "sprite name";
        LevelType tempLevel = new LevelType();
        tempLevel.startTimeMils = 1;
        Wave tempWave = new Wave();
        tempWave.levelTimeOffset = 1000;
        tempWave.enemyNum = 5;
        tempWave.enemyTypeNum = 1;
        tempLevel.waves = new Wave[]{tempWave, tempWave};
        data.enemyTypes = new EnemyType[]{tempEnemy, tempEnemy};
        data.levels = new LevelType[]{tempLevel, tempLevel};
        json.setOutputType(JsonWriter.OutputType.json);


        outFile.writeString(json.prettyPrint(data), false);
    }

    public void readSettings() {
        FileHandle file = Gdx.files.internal("data/data.json");
        String data = file.readString();
        gameData = json.fromJson(GameDataType.class, data);
    }

    public void update(float deltaTime) {
        elapsedTime += 1000 * deltaTime; // seconds to nanoseconds
        minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
    }

    public void reset() {
        enemyQueue.clear();
        elapsedTime = 0;
        score = 0;
        readSettings();

        W_HEIGHT = GameConfig.W_HEIGHT;
        W_WIDTH = GameConfig.W_WIDTH;
        DEBUG = GameConfig.DEBUG;


    }
}
