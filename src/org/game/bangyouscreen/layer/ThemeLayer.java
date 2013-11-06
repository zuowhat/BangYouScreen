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
import org.game.bangyouscreen.menus.ThemeScene;

public class ThemeLayer extends ManagedLayer{
	
	private static final ThemeLayer INSTANCE = new ThemeLayer();
	private static int themeNum = 0;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	public static ThemeLayer getInstance(int theme) {
		themeNum = theme;
		return INSTANCE;
	}
	
	private Sprite LayerBG;
	
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
		
		//this.setTouchAreaBindingOnActionDownEnabled(true);
		//this.setTouchAreaBindingOnActionMoveEnabled(true);
		//父背景变成半透明
		final Rectangle fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight, mVertexBufferObjectManager);
		fadableBGRect.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		fadableBGRect.setColor(0f, 0f, 0f, 0.5f);
		attachChild(fadableBGRect);
		
		LayerBG = new Sprite(0f, 0f,ResourceManager.loadingBG, mVertexBufferObjectManager);
		LayerBG.setSize(mCameraWidth/2f, mCameraHeight*(2f/3f));
		LayerBG.setPosition(mCameraWidth/2f, (mCameraHeight / 2f) + (ResourceManager.loadingBG.getHeight() / 2f));
		attachChild(LayerBG);
		
		ButtonSprite closeButton = new ButtonSprite(0f, 0f,ResourceManager.clockTR, mVertexBufferObjectManager);
		closeButton.setPosition(LayerBG.getWidth(), LayerBG.getHeight());
		closeButton.setSize(30f, 30f);
		closeButton.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				onHideLayer();
				ThemeScene.getInstance().setThemeScene();
			}});
		LayerBG.attachChild(closeButton);
		registerTouchArea(closeButton);
		
		ButtonSprite gameButton = new ButtonSprite(0f, 0f,ResourceManager.clockTR, mVertexBufferObjectManager);
		gameButton.setPosition(LayerBG.getWidth()/2f, LayerBG.getHeight()/2f);
		gameButton.setSize(100f, 100f);
		gameButton.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				onHideLayer();
				SceneManager.getInstance().showScene(GameLevel.getInstance());
				
			}});
		LayerBG.attachChild(gameButton);
		registerTouchArea(gameButton);
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
