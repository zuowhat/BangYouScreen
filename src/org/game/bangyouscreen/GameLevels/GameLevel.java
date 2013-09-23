package org.game.bangyouscreen.gamelevels;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
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
	private float gameTime = 60f;
	private Text mScoreText;
	private int mScore = 0;
	
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
		//绿色按钮
		ButtonSprite greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mScore++;
				if(mScore >= 10 && mScore < 100){
					mScoreText.setText("00"+mScore);
				}else if(mScore >= 100 && mScore < 999){
					mScoreText.setText("0"+mScore);
				}else if(mScore >= 1000){
					mScoreText.setText(""+mScore);
				}else{
					mScoreText.setText("000"+mScore);
				}
			}});
		attachChild(greenButtonBS);
		registerTouchArea(greenButtonBS);
		
		//时钟
		ButtonSprite clockSprite = new ButtonSprite(0f,0f,ResourceManager.clockTR,mVertexBufferObjectManager);
		//EntityUtil.setSize("width", 1f / 8f, clockSprite);
		clockSprite.setPosition((1f/2f)*mCameraWidth, mCameraHeight - clockSprite.getHeight()/2f);
		clockSprite.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				gameTime+=30f;
				
			}});
		attachChild(clockSprite);
		registerTouchArea(clockSprite);
		
		//乘号
		Sprite muSprite = new Sprite(0f,0f,ResourceManager.muTR,mVertexBufferObjectManager);
		muSprite.setPosition(clockSprite.getX()+muSprite.getWidth(), clockSprite.getY()-muSprite.getHeight()/2f);
		attachChild(muSprite);
		
		//时钟道具数量
		Text clockNum = new Text(0f,0f,ResourceManager.sysFont,"99",5,mVertexBufferObjectManager);
		clockNum.setPosition(muSprite.getX()+clockNum.getWidth(),muSprite.getY());
		attachChild(clockNum);
		
		//倒计时
		mTimeText = new Text(0f, 0f, ResourceManager.sysFont, "01:00",TIME_FORMAT.length(), mVertexBufferObjectManager);
		mTimeText.setPosition(mCameraWidth - mTimeText.getWidth(), mCameraHeight - mTimeText.getHeight()*1.1f);
		mTimeText.setScale(2f);
		attachChild(mTimeText);
		registerUpdateHandler(gameRunTimer);
		
		//得分
		mScoreText = new Text(0f,0f,ResourceManager.sysFont,"0000",6,mVertexBufferObjectManager);
		mScoreText.setPosition(mScoreText.getWidth(), mCameraHeight - mScoreText.getHeight()*1.1f);
		mScoreText.setScale(2f);
		attachChild(mScoreText);
		
		
		
		
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
			gameTime-=pSecondsElapsed;
			if(gameTime<=0) {
				mTimeText.setText(TimeUtils.formatSeconds(0));
				unregisterUpdateHandler(this);
			} else {
				mTimeText.setText(TimeUtils.formatSeconds(Math.round(gameTime)));
			}
		}

		public void reset() {}
	};

}
