package org.game.bangyouscreen.boss;

import java.util.Arrays;

import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.scene.ThemeScene;
import org.game.bangyouscreen.util.AnimatedButtonSprite;
import org.game.bangyouscreen.util.AnimatedButtonSprite.OnClickListener;
import org.game.bangyouscreen.util.EntityUtil;


public class ThemeBossForMXD extends ManagedScene implements IScrollDetectorListener{

	private static final ThemeBossForMXD INSTANCE = new ThemeBossForMXD();
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();

	private SurfaceScrollDetector mScrollDetector;
	private float directionPath;//判断手势方向，正-向右滑动，负-向左滑动
	private float themeRInitX;
	private int mCurrentTheme = 1;
	
	private Rectangle mBossSlider;
	private AnimatedButtonSprite[] bossPics;
	
	
	public static ThemeBossForMXD getInstance(){
		return INSTANCE;
	}
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		
	}

	public void onLoadScene() {
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
		mBossSlider = getScensSlider();
		attachChild(mBossSlider);
		
		//后退按钮
		ButtonSprite backBS = new ButtonSprite(0f,0f,ResourceManager.backTR,mVertexBufferObjectManager);
		backBS.setPosition(10f+backBS.getWidth()/2f, mCameraHeight-10f-backBS.getHeight()/2f);
		attachChild(backBS);
		backBS.setOnClickListener(new org.andengine.entity.sprite.ButtonSprite.OnClickListener(){
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SceneManager.getInstance().showScene(ThemeScene.getInstance());
				
			}
		});
		registerTouchArea(backBS);
		
		//主页按钮
		ButtonSprite homeBS = new ButtonSprite(0f,0f,ResourceManager.homeTR,mVertexBufferObjectManager);
		homeBS.setPosition(mCameraWidth-10f-homeBS.getWidth()/2f, backBS.getY());
		attachChild(homeBS);
		homeBS.setOnClickListener(new org.andengine.entity.sprite.ButtonSprite.OnClickListener(){
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SceneManager.getInstance().showScene(MainMenuScene.getInstance());
				
			}
		});
		registerTouchArea(homeBS);
		
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
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		
	}
	
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		directionPath = pDistanceX;
	}

	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		float f1 = mBossSlider.getX() + pDistanceX;
		mBossSlider.setPosition(f1, mBossSlider.getY());
	}

	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mBossSlider.clearEntityModifiers();
		if(directionPath > 0f){
			if(mCurrentTheme == 1){
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), mBossSlider.getWidth()/2f));
			}else{
				mCurrentTheme--;
				themeRInitX = themeRInitX + mCameraWidth;
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), themeRInitX));
			}
		}else{
			if(mCurrentTheme == bossPics.length){
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), mBossSlider.getWidth()/2f - mCameraWidth*(bossPics.length - 1)));
			}else{
				mCurrentTheme++;
				themeRInitX = themeRInitX - mCameraWidth;
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), themeRInitX));
			}
		}
	}
	
	public boolean onSceneTouchEvent(TouchEvent paramTouchEvent){
	    this.mScrollDetector.onTouchEvent(paramTouchEvent);
	    return super.onSceneTouchEvent(paramTouchEvent);
	}
	
	 /**
	  * BOSS滑块
	  */
	 private Rectangle getScensSlider(){
		 bossPics = new AnimatedButtonSprite[ResourceManager.themeSceneOneBossTotalTT.length];
		 Sprite[] bossInfo = new Sprite[bossPics.length];
		 float themeRWidth = mCameraWidth*bossPics.length;
		 themeRInitX = themeRWidth/2f;
		 Rectangle themeR = new Rectangle(themeRWidth/2f,mCameraHeight/2f,themeRWidth,
				 mCameraHeight*(2f/3f),mVertexBufferObjectManager);
		 themeR.setAlpha(0f);
		 long []frameDur;
		 
		 for(int i=0; i<ResourceManager.themeSceneOneBossTotalTT.length; i++){
			 if(ResourceManager.themeSceneOneBossTotalTT[i] != null){
				//BOSS动画
				bossPics[i] = new AnimatedButtonSprite(0f, 0f,ResourceManager.themeSceneOneBossTotalTT[i], mVertexBufferObjectManager);
				EntityUtil.setSize("height", 1f/2f, bossPics[i]);
				//BOSS简介
				bossInfo[i] = new Sprite(0f, 0f,ResourceManager.themeSceneOneBossInfoTR[i], mVertexBufferObjectManager);
				EntityUtil.setSize("height", 1f/2f, bossInfo[i]);
				
				switch (i) {
				case 0:
					bossPics[i].setPosition(mCameraWidth/4f, themeR.getHeight()/2f);
					frameDur = new long[6];
					Arrays.fill(frameDur, 100);
					bossPics[i].animate(frameDur,0,5,true);
					bossPics[i].setOnClickListener(new OnClickListener(){

						public void onClick(AnimatedButtonSprite pButtonSprite,
								float pTouchAreaLocalX, float pTouchAreaLocalY) {
							SceneManager.getInstance().showScene(GameLevel.getInstance());
						}});
					
					
					//bossInfo[i].setPosition(mCameraWidth-20f-bossInfo[i].getWidth()/2f, themeR.getHeight()/2f);
					bossInfo[i].setPosition(3f*mCameraWidth/4f, themeR.getHeight()/2f);
					
					break;

				case 1:
					bossPics[i].setPosition(mCameraWidth+bossPics[i-1].getX(), themeR.getHeight()/2f);
					frameDur = new long[5];
					Arrays.fill(frameDur, 100);
					bossPics[i].animate(frameDur,0,4,true);
					
					bossInfo[i].setPosition(mCameraWidth+bossInfo[i-1].getX(), themeR.getHeight()/2f);
					
					break;
				}
				themeR.attachChild(bossPics[i]);
				registerTouchArea(bossPics[i]);
				themeR.attachChild(bossInfo[i]);
			 }else{
				switch (i) {
				case 0:
					
					break;

				case 1:
					
					break;
				}
			 }
		 }
		 
		 
		 return themeR;
	 }

}
