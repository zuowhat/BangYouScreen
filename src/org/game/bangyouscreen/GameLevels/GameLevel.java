package org.game.bangyouscreen.gamelevels;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.TimeUtils;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.util.EntityUtil;

public class GameLevel extends ManagedScene {
	
	private static final GameLevel INSTANCE = new GameLevel();
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private static final String TIME_FORMAT = "00:00";
	private Text mTimeText;
	private float startTime = 60f;
	
	public static GameLevel getInstance(){
		return INSTANCE;
	}

	@Override
	public Scene onLoadingScreenLoadAndShown() {
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {

	}

	@Override
	public void onLoadScene() {
		ButtonSprite greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		//greenButtonBS.setOnClickListener(new OnClickListener());
		attachChild(greenButtonBS);
		registerTouchArea(greenButtonBS);
		
		ButtonSprite clockSprite = new ButtonSprite(0f,0f,ResourceManager.clockTR,mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 5f, clockSprite);
		clockSprite.setPosition((4f/5f)*mCameraWidth, mCameraHeight - clockSprite.getHeight());
		//clockSprite.setOnClickListener(new OnClickListener());
		attachChild(clockSprite);
		registerTouchArea(clockSprite);
		
		mTimeText = new Text(mCameraWidth/2f, mCameraHeight/2f, ResourceManager.mFont, "01:00", 
				TIME_FORMAT.length(), mVertexBufferObjectManager);
		attachChild(mTimeText);
		registerUpdateHandler(gameRunTimer);
		
		
		
	}

	@Override
	public void onUnloadScene() {

	}

	@Override
	public void onShowScene() {

	}

	@Override
	public void onHideScene() {

	}
	
	//游戏时间倒计时
	private IUpdateHandler gameRunTimer = new IUpdateHandler() {

		public void onUpdate(float pSecondsElapsed) {
			startTime-=pSecondsElapsed;
			if(startTime<=0) {
				// The timer has ended
				mTimeText.setText(TimeUtils.formatSeconds(0));
				unregisterUpdateHandler(this);
			} else {
				mTimeText.setText(TimeUtils.formatSeconds(Math.round(startTime)));
			}
		}

		public void reset() {}
	};

}
