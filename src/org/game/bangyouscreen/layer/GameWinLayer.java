package org.game.bangyouscreen.layer;


import net.youmi.android.offers.PointsManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.boss.ThemeBossForMXD;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.util.DataConstant;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameNumberUtil;

public class GameWinLayer extends ManagedLayer{
	
	private static final GameWinLayer INSTANCE = new GameWinLayer();
	//private static int themeNum = 0;
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	private Sprite LayerBG;
	private static int gameScore;
	private static int gameTime;
	private static int bossHP;
	//private Text mGoldNum;
	private Sprite goldSprite;
	private AnimatedSprite[] goldNumAS;
	
	public static GameWinLayer getInstance(int gameScore1, int gameTime1, int bossHP1) {
		gameScore = gameScore1;
		gameTime = gameTime1;
		bossHP = bossHP1;
		return INSTANCE;
	}
	
	IUpdateHandler mSlideInUpdateHandler = new IUpdateHandler() {
		
		public void onUpdate(final float pSecondsElapsed) {
			if (LayerBG.getY() > (mCameraHeight / 2f)) {
				LayerBG.setY(Math.max(LayerBG.getY() - (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS),0f));
			} else {
				LayerBG.setY(mCameraHeight / 2f);
				unregisterUpdateHandler(this);
			}
		}

		public void reset() {}
	};

	IUpdateHandler mSlideOutUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if (LayerBG.getY() < ((mCameraHeight / 2f) + (LayerBG.getHeight() / 2f))) {
				LayerBG.setY(Math.min(LayerBG.getY() + (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS),
								(mCameraHeight / 2f) + (LayerBG.getHeight() / 2f)));
			} else {
				unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}

		@Override
		public void reset() {}
	};

	public void onLoadLayer() {
		
		//父背景变成半透明
		Rectangle fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight, mVertexBufferObjectManager);
		fadableBGRect.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		fadableBGRect.setColor(0f, 0f, 0f, 0.6f);
		attachChild(fadableBGRect);
		
		LayerBG = new Sprite(0f, 0f,ResourceManager.gamePauseBG.getTextureRegion(0), mVertexBufferObjectManager);
		//LayerBG.setSize(mCameraWidth/2f, mCameraHeight*(2f/3f));
		EntityUtil.setSize("height", 2f/3f, LayerBG);
		LayerBG.setPosition(mCameraWidth/2f, (mCameraHeight / 2f) + (ResourceManager.loadingBG.getHeight() / 2f));
		attachChild(LayerBG);
		
		Sprite titleSprite = new Sprite(0f, 0f,ResourceManager.layerTitle.getTextureRegion(1), mVertexBufferObjectManager);
		titleSprite.setPosition(LayerBG.getWidth()/2f, LayerBG.getHeight()*(3f/4f));
		EntityUtil.setSizeInParent("width", 1f/2f, titleSprite, LayerBG);
		LayerBG.attachChild(titleSprite);
		
		//继续游戏
		ButtonSprite continueBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(0),mVertexBufferObjectManager);
		continueBS.setPosition(LayerBG.getWidth()*(3f/4f), LayerBG.getHeight()/4f);
		EntityUtil.setSizeInParent("width", 3f/8f, continueBS, LayerBG);
		continueBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					SFXManager.getInstance().playSound("a_click");
					SFXManager.getInstance().playMusic("mainMusic");
					onHideLayer();
					//int bossNumInSP = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.SHARED_PREFS_THEME_MXD);
					SceneManager.getInstance().showScene(ThemeBossForMXD.getInstance(true,GameLevel.bossModel.getBossLevel()));
			}});
		LayerBG.attachChild(continueBS);
		registerTouchArea(continueBS);
		
		//返回菜单
		ButtonSprite goBackBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(2),mVertexBufferObjectManager);
		goBackBS.setPosition(LayerBG.getWidth()/4f, LayerBG.getHeight()/4f);
		EntityUtil.setSizeInParent("width", 3f/8f, goBackBS, LayerBG);
		goBackBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					SFXManager.getInstance().playSound("a_click");
					onHideLayer();
					SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			}});
		LayerBG.attachChild(goBackBS);
		registerTouchArea(goBackBS);
	}
	
	public void onShowLayer() {
		//加载新BOSS
		ResourceManager.loadBossResources();
		int goldNum = Math.round((gameScore + gameTime + bossHP/100f)/2f);
		PointsManager.getInstance(ResourceManager.getActivity()).awardPoints(goldNum); 
		GameNumberUtil g = new GameNumberUtil();
		goldNumAS = g.gameScoreNum(LayerBG, goldNum);
		
		Sprite addSprite = new Sprite(0f,0f,ResourceManager.numberTTR.getTextureRegion(11),mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("height", 4f/3f, addSprite, goldNumAS[0]);
		addSprite.setPosition(goldNumAS[0].getX() - goldNumAS[0].getWidth()*2f, goldNumAS[2].getY());
		LayerBG.attachChild(addSprite);
		
		goldSprite = new Sprite(0f,0f,ResourceManager.gameGold,mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("height", 1f/7f, goldSprite,LayerBG);
		goldSprite.setPosition(goldNumAS[2].getX() + goldNumAS[2].getWidth()*2f, goldNumAS[2].getY());
		LayerBG.attachChild(goldSprite);
		
		int bossNum = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_BOSS) + 1;
		BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.ALL_BOSS, bossNum);
		
		int goldNumTemp = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_GOLD) + goldNum;
		BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.ALL_GOLD, goldNumTemp);
		registerUpdateHandler(mSlideInUpdateHandler);
	}
	
	public void onHideLayer() {
		registerUpdateHandler(mSlideOutUpdateHandler);
	}

	public void onUnloadLayer() {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			public void run() {
				if(goldNumAS != null){
					for(AnimatedSprite a:goldNumAS){
						LayerBG.detachChild(a);
						a = null;
					}
					goldNumAS = null;
					LayerBG.detachChild(goldSprite);
					goldSprite = null;
				}
				
//				detachChildren();
//				for(int i = 0; i < LayerBG.getChildCount(); i++){
//					LayerBG.getChildByIndex(i).dispose();
//					LayerBG.getChildByIndex(i).clearEntityModifiers();
//					LayerBG.getChildByIndex(i).clearUpdateHandlers();
//				}
//				LayerBG.clearEntityModifiers();
//				LayerBG.clearUpdateHandlers();
//				detachChild(LayerBG);
			}});
	}

}
