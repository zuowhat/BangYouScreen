package org.game.bangyouscreen.layer;


import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.util.AnimatedButtonSprite;
import org.game.bangyouscreen.util.EntityUtil;

public class GameWinLayer extends ManagedLayer{
	
	private static final GameWinLayer INSTANCE = new GameWinLayer();
	//private static int themeNum = 0;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	private Sprite LayerBG;
	
	
	public static GameWinLayer getInstance() {
		//themeNum = theme;
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
		
		LayerBG = new Sprite(0f, 0f,ResourceManager.gamePauseBG, mVertexBufferObjectManager);
		//LayerBG.setSize(mCameraWidth/2f, mCameraHeight*(2f/3f));
		EntityUtil.setSize("height", 2f/3f, LayerBG);
		LayerBG.setPosition(mCameraWidth/2f, (mCameraHeight / 2f) + (ResourceManager.loadingBG.getHeight() / 2f));
		attachChild(LayerBG);
		
		//重新开始
		ButtonSprite restartBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(1),mVertexBufferObjectManager);
		restartBS.setPosition(LayerBG.getWidth()*(3f/4f), LayerBG.getHeight()/2f);
		restartBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					onHideLayer();
					//((GameLevel) SceneManager.getInstance().mCurrentScene).onResumeGameLevel();
			}});
		LayerBG.attachChild(restartBS);
		registerTouchArea(restartBS);
		
		//继续游戏
		ButtonSprite continueBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(0),mVertexBufferObjectManager);
		continueBS.setPosition(restartBS.getX(), restartBS.getY()+restartBS.getHeight()+10f);
		continueBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					onHideLayer();
					((GameLevel) SceneManager.getInstance().mCurrentScene).onResumeGameLevel();
			}});
		LayerBG.attachChild(continueBS);
		registerTouchArea(continueBS);
		
		//返回菜单
		ButtonSprite goBackBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(2),mVertexBufferObjectManager);
		goBackBS.setPosition(restartBS.getX(), restartBS.getY()-restartBS.getHeight()-10f);
		goBackBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
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
		registerUpdateHandler(mSlideInUpdateHandler);
	}
	
	public void onHideLayer() {
		registerUpdateHandler(mSlideOutUpdateHandler);
	}

	public void onUnloadLayer() {
		// TODO Auto-generated method stub
		
	}

}
