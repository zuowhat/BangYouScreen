package org.game.bangyouscreen.Managers;


import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.SmoothCamera;
import org.game.bangyouscreen.BangYouScreenActivity;

import android.content.Context;

public class ResourceManager extends Object{
	
	private static final ResourceManager INSTANCE = new ResourceManager();
	public FixedStepEngine engine;
	public Context context;
	public BangYouScreenActivity activity;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;
	
	public ResourceManager(){
	}

	public static ResourceManager getInstance(){
		return INSTANCE;
	}
	
	public static Context getContext(){
		return getInstance().context;
	}
	
	public static FixedStepEngine getEngine(){
		return getInstance().engine;
	}
	
	public static SmoothCamera getCamera(){
		return (SmoothCamera) getInstance().engine.getCamera();
	}

	public static void setup(BangYouScreenActivity pActivity, FixedStepEngine pEngine, Context pContext, float pCameraWidth, float pCameraHeight, float pCameraScaleX, float pCameraScaleY){
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().context = pContext;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		getInstance().cameraScaleFactorX = pCameraScaleX;
		getInstance().cameraScaleFactorY = pCameraScaleY;
	}
}
