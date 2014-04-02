package org.game.bangyouscreen.scene;

/**
 * 主题选择画面
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.boss.ThemeBossForMXD;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.Constants;
import org.game.bangyouscreen.util.EntityUtil;


public class ThemeScene extends ManagedScene implements IScrollDetectorListener{

	private static final ThemeScene INSTANCE = new ThemeScene();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private SurfaceScrollDetector mScrollDetector;
	private Rectangle mScensSlider;
	private ButtonSprite[] themePics = new ButtonSprite[Constants.THEME_NUM];
	private float themeRInitX;
	private int mCurrentTheme = 1;
	private float directionPath;//判断手势方向，正-向右滑动，负-向左滑动
	
	public static ThemeScene getInstance(){
		return INSTANCE;
	}
	
//	public ThemeScene(){
//		super(0.1f);
//	}
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		//LoadingScene.getInstance().onLoadScene();
		//return LoadingScene.getInstance();
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		//LoadingScene.getInstance().unloadScene();
	}

	@Override
	public void onLoadScene() {
		//AppConnect.getInstance(ResourceManager.getInstance().activity).initPopAd(ResourceManager.getInstance().activity); 
		mCurrentTheme = 1;
		//ResourceManager.loadGameResources();
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
		
		//后退按钮
		ButtonSprite backBS = new ButtonSprite(0f,0f,ResourceManager.backTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 8f, backBS);
		backBS.setPosition(10f+backBS.getWidth()/2f, mCameraHeight-10f-backBS.getHeight()/2f);
		attachChild(backBS);
		backBS.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			}
		});
		registerTouchArea(backBS);
		
		//主页按钮
		ButtonSprite homeBS = new ButtonSprite(0f,0f,ResourceManager.homeTR,mVertexBufferObjectManager);
		homeBS.setSize(backBS.getWidth(), backBS.getHeight());
		homeBS.setPosition(mCameraWidth-10f-homeBS.getWidth()/2f, backBS.getY());
		attachChild(homeBS);
		homeBS.setOnClickListener(new OnClickListener(){
			
			@Override
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
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				mScensSlider.clearEntityModifiers();
				if(mCurrentTheme == 1){
					mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), mScensSlider.getWidth()/2f));
				}else{
					mCurrentTheme--;
					themeRInitX = themeRInitX + mCameraWidth;
					mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), themeRInitX));
				}
			}
		});
		registerTouchArea(arrowLeftSprite);
		//向右箭头
		ButtonSprite arrowRightSprite = new ButtonSprite(0f,0f,ResourceManager.arrowLRTTR.getTextureRegion(1),mVertexBufferObjectManager);
		arrowRightSprite.setSize(arrowLeftSprite.getWidth(), arrowLeftSprite.getHeight());
		arrowRightSprite.setPosition(homeBS.getX(), mCameraHeight/2f);
		attachChild(arrowRightSprite);
		arrowRightSprite.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				mScensSlider.clearEntityModifiers();
				if(mCurrentTheme == themePics.length){
					mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), mScensSlider.getWidth()/2f - mCameraWidth*(themePics.length - 1)));
				}else{
					mCurrentTheme++;
					themeRInitX = themeRInitX - mCameraWidth;
					mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), themeRInitX));
				}
			}
		});
		registerTouchArea(arrowRightSprite);
		
	}

	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
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
			}});
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
	    directionPath = pDistanceX;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
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
	


	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mScensSlider.clearEntityModifiers();
		if(directionPath > 0f){
			if(mCurrentTheme == 1){
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), mScensSlider.getWidth()/2f));
			}else{
				mCurrentTheme--;
				themeRInitX = themeRInitX + mCameraWidth;
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), themeRInitX));
			}
		}else{
			if(mCurrentTheme == themePics.length){
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), mScensSlider.getWidth()/2f - mCameraWidth*(themePics.length - 1)));
			}else{
				mCurrentTheme++;
				themeRInitX = themeRInitX - mCameraWidth;
				mScensSlider.registerEntityModifier(new MoveXModifier(0.3F, mScensSlider.getX(), themeRInitX));
			}
		}
	}
	
	 @Override
	public boolean onSceneTouchEvent(TouchEvent paramTouchEvent){
	    this.mScrollDetector.onTouchEvent(paramTouchEvent);
	    return super.onSceneTouchEvent(paramTouchEvent);
	  }
	 
	/**
	 * 主题滑块
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	 private Rectangle getScensSlider(){
		 float themeRWidth = mCameraWidth*themePics.length;
		 themeRInitX = themeRWidth/2f;
		 Rectangle themeR = new Rectangle(themeRWidth/2f,mCameraHeight/2f,themeRWidth,
				 ResourceManager.theme1.getHeight(),mVertexBufferObjectManager);
		 themeR.setAlpha(0f);
		 //冒险岛主题
		 themePics[0] = new ButtonSprite(0f,0f,ResourceManager.theme1,mVertexBufferObjectManager);
		 themePics[0].setPosition(mCameraWidth/2f, themeR.getHeight()/2f);
		 //themePics[0].setSize(mCameraWidth/2f, mCameraHeight*(2f/3f));
		 EntityUtil.setSize("height", 2f/3f, themePics[0]);
		 themeR.attachChild(themePics[0]);
		 themePics[0].setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					SFXManager.getInstance().playSound("a_click");
					SceneManager.getInstance().showScene(ThemeBossForMXD.getInstance(false,1));
				}
			});
			registerTouchArea(themePics[0]);
		 
		 //主题2
		 themePics[1] = new ButtonSprite(0f,0f,ResourceManager.theme2,mVertexBufferObjectManager);
		 themePics[1].setPosition(themePics[0].getX()+mCameraWidth, themeR.getHeight()/2f);
		 //themePics[1].setSize(mCameraWidth/2f, mCameraHeight*(2f/3f));
		 EntityUtil.setSize("height", 2f/3f, themePics[1]);
		 themeR.attachChild(themePics[1]);
		 themePics[1].setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					//AppConnect.getInstance(ResourceManager.getInstance().activity).showPopAd(ResourceManager.getInstance().activity); 
					//mCurrentScreen = ThemeSceneScreens.LevelSelector;
					//SceneManager.getInstance().showLayer(GamePauseLayer.getInstance(), false, false, false);
				}
			});
			registerTouchArea(themePics[1]);
		 
//		 themePics[2] = new ButtonSprite(0f,0f,ResourceManager.theme1Temp,mVertexBufferObjectManager);
//		 themePics[2].setPosition(themePics[1].getX()+mCameraWidth, themeR.getHeight()/2f);
//		 themeR.attachChild(themePics[2]);
//		 themePics[3] = new ButtonSprite(0f,0f,ResourceManager.theme2Temp,mVertexBufferObjectManager);
//		 themePics[3].setPosition(themePics[2].getX()+mCameraWidth, themeR.getHeight()/2f);
//		 themeR.attachChild(themePics[3]);
		 
		 return themeR;
	 }
	 
}
