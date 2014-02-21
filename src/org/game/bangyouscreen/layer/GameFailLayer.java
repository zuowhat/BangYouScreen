package org.game.bangyouscreen.layer;


import net.youmi.android.offers.PointsManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameNumberUtil;

public class GameFailLayer extends ManagedLayer{
	
	private static final GameFailLayer INSTANCE = new GameFailLayer();
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
	
	public static GameFailLayer getInstance(int gameScore1, int gameTime1, int bossHP1) {
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

		public void reset() {}
	};

	public void onLoadLayer() {
		//父背景变成半透明
		final Rectangle fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight, mVertexBufferObjectManager);
		fadableBGRect.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		fadableBGRect.setColor(0f, 0f, 0f, 0.6f);
		attachChild(fadableBGRect);
		
		LayerBG = new Sprite(0f, 0f,ResourceManager.gamePauseBG.getTextureRegion(1), mVertexBufferObjectManager);
		EntityUtil.setSize("height", 2f/3f, LayerBG);
		LayerBG.setPosition(mCameraWidth/2f, (mCameraHeight / 2f) + (ResourceManager.loadingBG.getHeight() / 2f));
		attachChild(LayerBG);
		
		Sprite titleSprite = new Sprite(0f, 0f,ResourceManager.layerTitle.getTextureRegion(0), mVertexBufferObjectManager);
		titleSprite.setPosition(LayerBG.getWidth()/2f, LayerBG.getHeight()*(3f/4f));
		EntityUtil.setSizeInParent("width", 1f/2f, titleSprite, LayerBG);
		LayerBG.attachChild(titleSprite);
		
		AnimatedSprite wuyaAS = new AnimatedSprite(0f,0f,ResourceManager.wuya,mVertexBufferObjectManager){
			protected void onManagedUpdate(float pSecondsElapsed) {
				if(this.getX() > LayerBG.getWidth()){
					this.setPosition(0f, LayerBG.getHeight()/2f);
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		wuyaAS.setPosition(0f, LayerBG.getHeight()/2f);
		EntityUtil.setSizeInParent("height", 1f/5f, wuyaAS, LayerBG);
		wuyaAS.animate(100, true);
		PhysicsHandler pWuya = new PhysicsHandler(wuyaAS);
		wuyaAS.registerUpdateHandler(pWuya);
		pWuya.setVelocityX(60f);
		LayerBG.attachChild(wuyaAS);
		
		//重新开始
		ButtonSprite restartBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(1),mVertexBufferObjectManager);
		restartBS.setPosition(LayerBG.getWidth()*(3f/4f), LayerBG.getHeight()/4f);
		EntityUtil.setSizeInParent("width", 1f/4f, restartBS, LayerBG);
		restartBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					SFXManager.getInstance().playSound("a_click");
					onHideLayer();
					SceneManager.getInstance().showScene(new GameLevel(GameLevel.bossModel,GameLevel.playerModel));
			}});
		LayerBG.attachChild(restartBS);
		registerTouchArea(restartBS);
		
		//返回菜单
		ButtonSprite goBackBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(2),mVertexBufferObjectManager);
		goBackBS.setPosition(LayerBG.getWidth()/4f, LayerBG.getHeight()/4f);
		EntityUtil.setSizeInParent("width", 1f/4f, goBackBS, LayerBG);
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
		int goldNum = Math.round((gameScore + gameTime + bossHP/100f)/4f);
		PointsManager.getInstance(ResourceManager.getActivity()).awardPoints(goldNum); 
		GameNumberUtil g = new GameNumberUtil();
		goldNumAS = g.gameScoreNum(LayerBG, goldNum);
		
//		mGoldNum = new Text(0f,0f,ResourceManager.sysFont,"获得",mVertexBufferObjectManager);
//		mGoldNum.setColor(0f, 0f, 0f);
//		EntityUtil.setSizeInParent("height", 1f/7f, mGoldNum,LayerBG);
//		mGoldNum.setPosition(goldNumAS[0].getX() - goldNumAS[2].getWidth()*2f, goldNumAS[2].getY());
//		LayerBG.attachChild(mGoldNum);
		
		goldSprite = new Sprite(0f,0f,ResourceManager.gameGold,mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("height", 1f/7f, goldSprite,LayerBG);
		goldSprite.setPosition(goldNumAS[2].getX() + goldNumAS[2].getWidth()*2f, goldNumAS[2].getY());
		LayerBG.attachChild(goldSprite);
		
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
					//LayerBG.detachChild(mGoldNum);
					LayerBG.detachChild(goldSprite);
					//mGoldNum = null;
					goldSprite = null;
				}
			}});
	}

}
