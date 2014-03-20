package org.game.bangyouscreen.boss;

import java.util.Arrays;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleAtModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.scene.ThemeScene;
import org.game.bangyouscreen.util.AnimatedButtonSprite;
import org.game.bangyouscreen.util.AnimatedButtonSprite.OnClickListenerABS;
import org.game.bangyouscreen.util.DataConstant;
import org.game.bangyouscreen.util.EntityUtil;

/**
 * 冒险岛主题场景 
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class ThemeBossForMXD extends ManagedScene implements IScrollDetectorListener{

	private static final ThemeBossForMXD INSTANCE = new ThemeBossForMXD();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();

	private SurfaceScrollDetector mScrollDetector;
	private float directionPath;//判断手势方向，正-向右滑动，负-向左滑动
	private float themeRInitX;
	private static int mCurrentBoss;
	private static boolean isKO;
	private Rectangle mBossSlider;
	private AnimatedButtonSprite[] bossPics;
	
	
	public static ThemeBossForMXD getInstance(boolean tempIsKO, int tempCurrentBoss){
		isKO = tempIsKO;
		mCurrentBoss = tempCurrentBoss;
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
		//mCurrentBoss = 1;
		//SFXManager.getInstance().loadSound("t_ko", ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity());
		System.out.println("mCurrentBoss --> "+mCurrentBoss);
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
		if(isKO){
			themeRInitX = themeRInitX - mCameraWidth*(mCurrentBoss-1);
			//mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), themeRInitX));
			mBossSlider.setX(themeRInitX);
		}
		
		//后退按钮
		ButtonSprite backBS = new ButtonSprite(0f,0f,ResourceManager.backTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 8f, backBS);
		backBS.setPosition(10f+backBS.getWidth()/2f, mCameraHeight-10f-backBS.getHeight()/2f);
		attachChild(backBS);
		backBS.setOnClickListener(new org.andengine.entity.sprite.ButtonSprite.OnClickListener(){
			
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(ThemeScene.getInstance());
			}
		});
		registerTouchArea(backBS);
		
		//主页按钮
		ButtonSprite homeBS = new ButtonSprite(0f,0f,ResourceManager.homeTR,mVertexBufferObjectManager);
		homeBS.setSize(backBS.getWidth(), backBS.getHeight());
		homeBS.setPosition(mCameraWidth-10f-homeBS.getWidth()/2f, backBS.getY());
		attachChild(homeBS);
		homeBS.setOnClickListener(new org.andengine.entity.sprite.ButtonSprite.OnClickListener(){
			
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			}
		});
		registerTouchArea(homeBS);
		
		//向左箭头
		ButtonSprite arrowLeftSprite = new ButtonSprite(0f,0f,ResourceManager.arrowLRTTR.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f/8f, arrowLeftSprite);
		arrowLeftSprite.setPosition(backBS.getX(), mCameraHeight/2f);
		attachChild(arrowLeftSprite);
		arrowLeftSprite.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				mBossSlider.clearEntityModifiers();
				if(mCurrentBoss == 1){
					mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), mBossSlider.getWidth()/2f));
				}else{
					mCurrentBoss--;
					themeRInitX = themeRInitX + mCameraWidth;
					mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), themeRInitX));
				}
				System.out.println("mCurrentBoss --> "+mCurrentBoss);
			}
		});
		registerTouchArea(arrowLeftSprite);
		//向右箭头
		ButtonSprite arrowRightSprite = new ButtonSprite(0f,0f,ResourceManager.arrowLRTTR.getTextureRegion(1),mVertexBufferObjectManager);
		arrowRightSprite.setSize(arrowLeftSprite.getWidth(), arrowLeftSprite.getHeight());
		arrowRightSprite.setPosition(homeBS.getX(), mCameraHeight/2f);
		attachChild(arrowRightSprite);
		arrowRightSprite.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				mBossSlider.clearEntityModifiers();
				if(mCurrentBoss == bossPics.length){
					mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), mBossSlider.getWidth()/2f - mCameraWidth*(bossPics.length - 1)));
				}else{
					mCurrentBoss++;
					themeRInitX = themeRInitX - mCameraWidth;
					mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), themeRInitX));
				}
				System.out.println("mCurrentBoss --> "+mCurrentBoss);
			}
		});
		registerTouchArea(arrowRightSprite);
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
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChildren();
				for(int i = 0; i < INSTANCE.getChildCount(); i++){
					INSTANCE.getChildByIndex(i).dispose();
					INSTANCE.getChildByIndex(i).clearEntityModifiers();
					//INSTANCE.getChildByIndex(i).clearTouchAreas();
					INSTANCE.getChildByIndex(i).clearUpdateHandlers();
				}
				INSTANCE.clearEntityModifiers();
				INSTANCE.clearTouchAreas();
				INSTANCE.clearUpdateHandlers();
				SFXManager.getInstance().unloadSound("t_ko");
			}});
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
			if(mCurrentBoss == 1){
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), mBossSlider.getWidth()/2f));
			}else{
				mCurrentBoss--;
				themeRInitX = themeRInitX + mCameraWidth;
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), themeRInitX));
			}
		}else{
			if(mCurrentBoss == bossPics.length){
				mBossSlider.registerEntityModifier(new MoveXModifier(0.3F, mBossSlider.getX(), mBossSlider.getWidth()/2f - mCameraWidth*(bossPics.length - 1)));
			}else{
				mCurrentBoss++;
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
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	 private Rectangle getScensSlider(){
		 bossPics = new AnimatedButtonSprite[ResourceManager.mxdBoss_TTRArray.length];
		 Sprite[] bossBlackBG = new Sprite[bossPics.length];
		 //Sprite[] bossInfo = new Sprite[bossPics.length];
		 float themeRWidth = mCameraWidth*bossPics.length;
		 themeRInitX = themeRWidth/2f;
		 Rectangle themeR = new Rectangle(themeRWidth/2f,mCameraHeight/2f,themeRWidth,
				 mCameraHeight*(2f/3f),mVertexBufferObjectManager);
		 themeR.setAlpha(0f);
		 int themeSceneOneBossTotal = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.SHARED_PREFS_THEME_MXD);
		 for(int i=0; i<ResourceManager.mxdBoss_TTRArray.length; i++){
			 if( i < themeSceneOneBossTotal){
				 if(isKO){
					 if(i != (mCurrentBoss-1)){
						//KO标志
						 Sprite s = new Sprite(0f,0f,ResourceManager.ko,mVertexBufferObjectManager);
						 EntityUtil.setSize("width", 1f/4f, s);
						 s.setPosition((i+1f/2f)*mCameraWidth, themeR.getHeight()/2f);
						 themeR.attachChild(s); 
					 }else{
						 //KO动画
						 Sprite s = new Sprite(0f,0f,ResourceManager.ko,mVertexBufferObjectManager);
						 EntityUtil.setSize("width", 1f/4f, s);
						 s.setPosition((i+1f/2f)*mCameraWidth, themeR.getHeight()/2f);
						 themeR.attachChild(s); 
						 ScaleAtModifier highestPicScale = new ScaleAtModifier(0.5f, 25f, 1f, 0.5f, 0.5f);//实体缩放
						 FadeInModifier highestPicFadeIn = new FadeInModifier(0.5f);//在0.5秒内改变透明度由0f变为1f
						 ParallelEntityModifier highestPicParalle = new ParallelEntityModifier(new IEntityModifierListener(){

							public void onModifierStarted(
									IModifier<IEntity> pModifier, IEntity pItem) {
								SFXManager.getInstance().playSound("t_ko");
								
							}

							public void onModifierFinished(
									IModifier<IEntity> pModifier, IEntity pItem) {
								// TODO Auto-generated method stub
								
							}},highestPicScale,highestPicFadeIn);//同时执行修饰符
						 s.registerEntityModifier(highestPicParalle);
						 
					 }
				 }else{
					//KO标志
					 Sprite s = new Sprite(0f,0f,ResourceManager.ko,mVertexBufferObjectManager);
					 EntityUtil.setSize("width", 1f/4f, s);
					 s.setPosition((i+1f/2f)*mCameraWidth, themeR.getHeight()/2f);
					 themeR.attachChild(s);
				 }
			 }
			 if(ResourceManager.mxdBoss_TTRArray[i] != null){
				 
				//BOSS动画
				bossPics[i] = new AnimatedButtonSprite(0f, 0f,ResourceManager.mxdBoss_TTRArray[i], mVertexBufferObjectManager);
				EntityUtil.setSize("height", 1f/2f, bossPics[i]);
				//BOSS简介
				//bossInfo[i] = new Sprite(0f, 0f,ResourceManager.mxdBoss_InfoTRArray[i], mVertexBufferObjectManager);
				//EntityUtil.setSize("height", 1f/2f, bossInfo[i]);
				if(i == 0){
					bossPics[i].setPosition(mCameraWidth/4f, themeR.getHeight()/2f);
					//bossInfo[i].setPosition(3f*mCameraWidth/4f, themeR.getHeight()/2f);
				}
				if(i == 7){
					long[] frameDur = new long[2];
					Arrays.fill(frameDur, 300);
					bossPics[i].animate(frameDur,0,1,true);
				}else{
					bossPics[i].animate(100,true);
				}
				
				bossPics[i].setOnClickListenerABS(new OnClickListenerABS(){
					public void onClick(AnimatedButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						SFXManager.getInstance().playSound("a_click");
						SFXManager.getInstance().stopMusic();
						SceneManager.getInstance().showScene(new GameLevel(DataConstant.getMXDBoss(mCurrentBoss),DataConstant.getPlayerModel()));
				}});
				//BOSS图片和简介的位置
				if(i > 0){
					bossPics[i].setPosition(mCameraWidth+bossPics[i-1].getX(), themeR.getHeight()/2f);
					//bossInfo[i].setPosition(mCameraWidth+bossInfo[i-1].getX(), themeR.getHeight()/2f);
				}
				themeR.attachChild(bossPics[i]);
				registerTouchArea(bossPics[i]);
				//themeR.attachChild(bossInfo[i]);
			 }else{
				bossBlackBG[i] = new Sprite(0f,0f,ResourceManager.bossBlackBG.getTextureRegion(i),mVertexBufferObjectManager);
				EntityUtil.setSize("height", 1f/2f, bossBlackBG[i]); 
				bossBlackBG[i].setPosition(mCameraWidth*(i+1f/4f), themeR.getHeight()/2f);
				
				switch (i) {
				case 0:
					
					break;

				case 1:
					
					break;
				}
				themeR.attachChild(bossBlackBG[i]);
			 }
		 }
		 
		 
		 return themeR;
	 }
	 
	 
	 
	 
}
