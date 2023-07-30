package com.gdx.chronoslime.ecs.common;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.gdx.chronoslime.assets.AssetDescriptors;


public class SoundSystem extends EntitySystem {

  private final AssetManager assetManager;

  private Sound pickSound;
  private Sound obstacleSound;
  boolean obstacleSoundPlaying;
  float obtsacleTimePlaying;


  public SoundSystem(AssetManager assetManager) {
    this.assetManager = assetManager;
    //setProcessing(false); //passive
    init();
  }


  private void init() {
    obstacleSoundPlaying = false;
//    pickSound = assetManager.get(AssetDescriptors.PICK_SOUND);
//    obstacleSound = assetManager.get(AssetDescriptors.OBSTACLE_SOUND);
  }

  public void pick() {
    pickSound.play(0);
  }

  public void obstacle() {
    if (obtsacleTimePlaying < 0) {
      obstacleSound.play();
      obtsacleTimePlaying = 2000; //2s
    }

  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    obtsacleTimePlaying-=deltaTime;
  }
}