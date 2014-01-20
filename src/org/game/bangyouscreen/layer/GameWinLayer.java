package org.game.bangyouscreen.layer;


import net.youmi.android.offers.PointsManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.boss.ThemeBossForMXD;
import org.game.bangyouscreen.managers.ManagedLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
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
	
	public static GameWinLayer getInstance(int gameScore1, int gameTime1) {
		gameScore = gameScore1;
		gameTime = gameTime1;
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
		final Rectangle fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight, mVertexBufferObjectManager);
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
		EntityUtil.setSizeInParent("width", 1f/4f, continueBS, LayerBG);
		continueBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					SFXManager.getInstance().playSound("a_click");
					onHideLayer();
					SceneManager.getInstance().showScene(ThemeBossForMXD.getInstance());
			}});
		LayerBG.attachChild(continueBS);
		registerTouchArea(continueBS);
		
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
		
		
//		AnimatedButtonSprite closeButton = new AnimatedButtonSprite(0f, 0f,ResourceManager.themeSceneOneBossTotalTT[0], mVertexBufferObjectManager);
//		closeButton.setPosition(LayerBG.getWidth()/2f, LayerBG.getHeight()/2f);
//		closeButton.setScale(3f);
//		long []frameDur = new long[4];
//		Arrays.fill(frameDur, 300);
//		closeButton.animate(frameDur,0,3,true);
//		closeButton.setOnClickListener(new OnClickListener(){
//
//			public void onClick(AnimatedButtonSprite pButtonSprite,
//					float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				onHideLayer();
//				//SceneManager.getInstance().showScene(GameLevel.getInstance());
//			}});
//		LayerBG.attachChild(closeButton);
//		registerTouchArea(closeButton);
		
	}
	
	public void onShowLayer() {
		//加载新BOSS
		ResourceManager.loadBossResources();
		int goldNum = gameScore + gameTime;
		PointsManager.getInstance(ResourceManager.getActivity()).awardPoints(goldNum); 
		GameNumberUtil g = new GameNumberUtil();
		g.gameScoreNum(LayerBG, goldNum);
		
//		Text mGoldNum = new Text(0f,0f,ResourceManager.sysFont,goldNum+"",mVertexBufferObjectManager);
//		mGoldNum.setColor(255, 107, 0);
//		mGoldNum.setPosition(LayerBG.getWidth()/2f, LayerBG.getHeight()/2f);
//		LayerBG.attachChild(mGoldNum);
//		Sprite goldSprite = new Sprite(0f,0f,ResourceManager.gameGold,mVertexBufferObjectManager);
//		goldSprite.setPosition(mGoldNum.getX()+mGoldNum.getWidth(), mGoldNum.getY());
//		LayerBG.attachChild(goldSprite);		
				
		registerUpdateHandler(mSlideInUpdateHandler);
	}
	
	public void onHideLayer() {
		registerUpdateHandler(mSlideOutUpdateHandler);
	}

	public void onUnloadLayer() {
		
	}

}
