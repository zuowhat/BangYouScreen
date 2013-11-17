package org.game.bangyouscreen.layer;


import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.AnimatedButtonSprite;
import org.game.bangyouscreen.util.AnimatedButtonSprite.OnClickListener;
import org.game.bangyouscreen.util.EntityUtil;

public class GameLayer extends ManagedLayer{
	
	private static final GameLayer INSTANCE = new GameLayer();
	//private static int themeNum = 0;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	private Sprite LayerBG;
	
	
	public static GameLayer getInstance() {
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
		
		LayerBG = new Sprite(0f, 0f,ResourceManager.themeLevelBG, mVertexBufferObjectManager);
		//LayerBG.setSize(mCameraWidth/2f, mCameraHeight*(2f/3f));
		EntityUtil.setSize("height", 2f/3f, LayerBG);
		LayerBG.setPosition(mCameraWidth/2f, (mCameraHeight / 2f) + (ResourceManager.loadingBG.getHeight() / 2f));
		attachChild(LayerBG);
		
//		if(themeNum == 1){
//			
//			
//			int themeOneBossNum = BangYouScreenActivity.getIntFromSharedPreferences(BangYouScreenActivity.SHARED_PREFS_THEME_1);
//			for(int i=0; i<themeOneBossNum+1; i++){
//				ResourceManager.themeSceneOneBossTotalTT.
//			}
//		}
		
		AnimatedButtonSprite closeButton = new AnimatedButtonSprite(0f, 0f,ResourceManager.themeSceneOneBossTotalTT[0], mVertexBufferObjectManager);
		closeButton.setPosition(LayerBG.getWidth()/2f, LayerBG.getHeight()/2f);
		closeButton.setScale(3f);
		long []frameDur = new long[4];
		Arrays.fill(frameDur, 300);
		closeButton.animate(frameDur,0,3,true);
		closeButton.setOnClickListener(new OnClickListener(){

			public void onClick(AnimatedButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				onHideLayer();
				SceneManager.getInstance().showScene(GameLevel.getInstance());
			}});
		LayerBG.attachChild(closeButton);
		registerTouchArea(closeButton);
		
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
