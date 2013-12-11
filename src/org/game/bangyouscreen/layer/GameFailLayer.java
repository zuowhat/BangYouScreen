package org.game.bangyouscreen.layer;


import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.util.EntityUtil;

public class GameFailLayer extends ManagedLayer{
	
	private static final GameFailLayer INSTANCE = new GameFailLayer();
	//private static int themeNum = 0;
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	private Sprite LayerBG;
	//private static BossModel b;
	//private static PlayerModel p;
	
	
	public static GameFailLayer getInstance() {
		//b = pBossModel;
		//p = pPlayerModel;
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
		ButtonSprite continueBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(1),mVertexBufferObjectManager);
		continueBS.setPosition(LayerBG.getWidth()*(3f/4f), LayerBG.getHeight()/4f);
		EntityUtil.setSizeInParent("width", 1f/4f, continueBS, LayerBG);
		continueBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					SFXManager.getInstance().playSound("a_click");
					onHideLayer();
					SceneManager.getInstance().showScene(new GameLevel(GameLevel.bossModel,GameLevel.playerModel));
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
