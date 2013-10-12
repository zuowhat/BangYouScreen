package org.game.bangyouscreen.menus;


import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;

import android.graphics.PointF;

public class ThemeScene extends ManagedScene implements IScrollDetectorListener{

	private static final ThemeScene INSTANCE = new ThemeScene();
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private SurfaceScrollDetector mScrollDetector;
	private PointF mTouchPoint = new PointF();
	private PointF mScrollDown = new PointF();
	private long mScrollTime = 0L;
	
	public enum ThemeSceneScreens {
		ThemeSelector, LevelSelector
	}
	public ThemeSceneScreens mCurrentScreen = ThemeSceneScreens.ThemeSelector;
	
	public static ThemeScene getInstance(){
		return INSTANCE;
	}
	
	public Scene onLoadingScreenLoadAndShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadScene() {
		if(mScrollDetector == null){
			mScrollDetector = new SurfaceScrollDetector(this);
		}
		mScrollDetector.setTriggerScrollMinimumDistance(10f);
		
		
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		this.mScrollTime = System.currentTimeMillis();
	    this.mScrollDown.x = this.mTouchPoint.x;
	    this.mScrollDown.y = this.mTouchPoint.y;
		
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		if (System.currentTimeMillis() - this.mScrollTime > 150L){
			onScrollScenariosFinished();
		}
		
	}
	
	 public boolean onSceneTouchEvent(TouchEvent paramTouchEvent){
	    if (mCurrentScreen == ThemeSceneScreens.ThemeSelector){
	      this.mTouchPoint.x = paramTouchEvent.getX();
	      this.mTouchPoint.y = paramTouchEvent.getY();
	      this.mScrollDetector.onTouchEvent(paramTouchEvent);
	    }
	    return super.onSceneTouchEvent(paramTouchEvent);
	  }
	 
	 private void onScrollScenariosFinished(){
		 
	 }

}
