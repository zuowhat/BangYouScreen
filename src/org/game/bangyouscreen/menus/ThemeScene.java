package org.game.bangyouscreen.menus;


import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
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
	private Rectangle mScensSlider;
	private Sprite[] themePics = new Sprite[4];
	private float themeRInitX;
	private int mCurrentTheme = 0;
	private float directionPath;//判断手势方向，正-向右滑动，负-向左滑动
	
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
		ResourceManager.loadGameResources();
		Sprite themeBGSprite = new Sprite(0f,0f,ResourceManager.themeBG,mVertexBufferObjectManager);
		//themeBGSprite.setScale(ResourceManager.getInstance().cameraHeight / ResourceManager.themeBG.getHeight());
		themeBGSprite.setSize(mCameraWidth, mCameraHeight);
		themeBGSprite.setPosition(mCameraWidth/2f,mCameraHeight/2f);
		themeBGSprite.setZIndex(-90);
		attachChild(themeBGSprite);
		
		if(mScrollDetector == null){
			mScrollDetector = new SurfaceScrollDetector(this);
		}
		mScrollDetector.setTriggerScrollMinimumDistance(10f);
		mScensSlider = getScensSlider();
		attachChild(mScensSlider);
		
		
		
		
		
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
		System.out.println("onScrollStarted_X-->"+pDistanceX);
		System.out.println("onScrollStarted_Y-->"+pDistanceY);
		this.mScrollTime = System.currentTimeMillis();
	    this.mScrollDown.x = this.mTouchPoint.x;
	    this.mScrollDown.y = this.mTouchPoint.y;
	    directionPath = pDistanceX;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		System.out.println("onScroll_X-->"+pDistanceX);
		System.out.println("onScroll_Y-->"+pDistanceY);
		if (mCurrentScreen == ThemeSceneScreens.ThemeSelector){
//		    float f1 = paramFloat + mScensSlider.getX();
//		    if (f1 > mScensSlider.getInitialX())
//		      f1 = this.mScensSlider.getInitialX();
//		    float f2 = ResourceManager.theme1.getWidth();
//		    if (f1 < f2 + (this.mScensSlider.getInitialX() - this.mScensSlider.getWidth()))
//		      f1 = f2 + (this.mScensSlider.getInitialX() - this.mScensSlider.getWidth());
//		    this.mScensSlider.setPosition(f1, this.mScensSlider.getY());
			float f1 = mScensSlider.getX() + pDistanceX;
			mScensSlider.setPosition(f1, mScensSlider.getY());
		}
		
	}
	


	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		System.out.println("onScrollFinished_X-->"+pDistanceX);
		System.out.println("onScrollFinished_Y-->"+pDistanceY);
		mScensSlider.clearEntityModifiers();
		if(directionPath > 0f){
			if(mCurrentTheme == 0){
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), mScensSlider.getWidth()/2f));
			}else{
				mCurrentTheme--;
				themeRInitX = themeRInitX + mCameraWidth;
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), themeRInitX));
			}
		}else{
			if(mCurrentTheme == (themePics.length - 1)){
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), mScensSlider.getWidth()/2f - mCameraWidth*(themePics.length - 1)));
			}else{
				mCurrentTheme++;
				themeRInitX = themeRInitX - mCameraWidth;
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), themeRInitX));
			}
		}
		
	}
	
	 public boolean onSceneTouchEvent(TouchEvent paramTouchEvent){
		 System.out.println("onSceneTouchEvent_X-->"+paramTouchEvent.getX());
			System.out.println("onSceneTouchEvent_Y-->"+paramTouchEvent.getY());
	    if (mCurrentScreen == ThemeSceneScreens.ThemeSelector){
	      this.mTouchPoint.x = paramTouchEvent.getX();
	      this.mTouchPoint.y = paramTouchEvent.getY();
	      this.mScrollDetector.onTouchEvent(paramTouchEvent);
	    }
	    return super.onSceneTouchEvent(paramTouchEvent);
	  }
	 

	 
	 /**
	  * 主题滑块
	  */
	 private Rectangle getScensSlider(){
		 float themeRWidth = mCameraWidth*themePics.length;
		 themeRInitX = themeRWidth/2f;
		 Rectangle themeR = new Rectangle(themeRWidth/2f,mCameraHeight/2f,themeRWidth,
				 ResourceManager.theme1.getHeight(),mVertexBufferObjectManager);
		 //主题1
		 themePics[0] = new Sprite(0f,0f,ResourceManager.theme1,mVertexBufferObjectManager);
		 themePics[0].setPosition(mCameraWidth/2f, themeR.getHeight()/2f);
		 themeR.attachChild(themePics[0]);
		 
		 //主题2
		 themePics[1] = new Sprite(0f,0f,ResourceManager.theme2,mVertexBufferObjectManager);
		 themePics[1].setPosition(themePics[0].getX()+mCameraWidth, themeR.getHeight()/2f);
		 themeR.attachChild(themePics[1]);
		 
		 themePics[2] = new Sprite(0f,0f,ResourceManager.theme1,mVertexBufferObjectManager);
		 themePics[2].setPosition(themePics[1].getX()+mCameraWidth, themeR.getHeight()/2f);
		 themeR.attachChild(themePics[2]);
		 
		 themePics[3] = new Sprite(0f,0f,ResourceManager.theme2,mVertexBufferObjectManager);
		 themePics[3].setPosition(themePics[2].getX()+mCameraWidth, themeR.getHeight()/2f);
		 themeR.attachChild(themePics[3]);
		 
		 return themeR;
	 }

}
