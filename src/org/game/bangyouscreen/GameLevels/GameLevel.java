package org.game.bangyouscreen.gamelevels;

import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
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
	
	private AnimatedSprite bossAS;
	private AnimatedSprite angelbossAS;
	private PhysicsHandler mPhysicsHandler;
	private boolean maxLeft = false;
	private boolean maxRight = false;
	private long[] frameDur;
	
	private int clickNum = 50;
	private float xue3P;
	private float xue3S;
	private Sprite xue3Sprite;
	
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
		
		//背景
		Sprite bg1 = new Sprite(0f,0f,ResourceManager.gameBG1,mVertexBufferObjectManager);
		bg1.setScale(ResourceManager.getInstance().cameraHeight / ResourceManager.gameBG1.getHeight());
		bg1.setPosition(mCameraWidth/2f,mCameraHeight/2f);
		bg1.setZIndex(-90);
		attachChild(bg1);
		
		//绿色按钮
		ButtonSprite greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mScore++;
				
				//血条变化
				xue3Sprite.setPosition(xue3Sprite.getX()-xue3P, xue3Sprite.getHeight()/2f);
				xue3Sprite.setSize(xue3Sprite.getWidth()-xue3S, xue3Sprite.getHeight());
				
				if(mScore % 5 == 0){
					angelbossAS.stopAnimation(2);
					//angelbossAS.animate(frameDur,0,1,true);
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
		
		//BOSS血条
		Sprite xue1Sprite = new Sprite(0f,0f,ResourceManager.xue1,mVertexBufferObjectManager);
		xue1Sprite.setPosition(mCameraWidth/2f, clockSprite.getY()-(clockSprite.getHeight()/2f)-30f);
		attachChild(xue1Sprite);
		Sprite xue2Sprite = new Sprite(0f,0f,ResourceManager.xue2,mVertexBufferObjectManager);
		xue2Sprite.setPosition(xue1Sprite.getWidth()/2f,xue1Sprite.getHeight()/2f);
		xue1Sprite.attachChild(xue2Sprite);
		xue3Sprite = new Sprite(0f,0f,ResourceManager.xue3,mVertexBufferObjectManager);
		xue3Sprite.setPosition(xue2Sprite.getWidth()/2f, xue3Sprite.getHeight()/2f);
		xue3Sprite.setSize(xue2Sprite.getWidth(), xue2Sprite.getHeight());
		xue2Sprite.attachChild(xue3Sprite);
		xue3P = (xue2Sprite.getWidth()/2f)/clickNum;
		xue3S = (xue2Sprite.getWidth())/clickNum;
		
		
		//辅助效果
		bossAS = new AnimatedSprite(3f*mCameraWidth/4f,mCameraHeight/2f,ResourceManager.aider1,mVertexBufferObjectManager);
		bossAS.setScale(2f);
		bossAS.animate(100,1);
		
		//头像
		ButtonSprite aiderHeadSprite = new ButtonSprite(0f,0f,ResourceManager.aiderHead,mVertexBufferObjectManager);
		aiderHeadSprite.setPosition((1f / 2f) * mCameraWidth + clockSprite.getWidth(), mCameraHeight - clockSprite.getHeight()/2f);
		aiderHeadSprite.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				attachChild(bossAS);
				
			}});
		attachChild(aiderHeadSprite);
		registerTouchArea(aiderHeadSprite);
		
		
		//乘号
//		Sprite muSprite = new Sprite(0f,0f,ResourceManager.muTR,mVertexBufferObjectManager);
//		muSprite.setPosition(clockSprite.getX()+muSprite.getWidth(), clockSprite.getY()-muSprite.getHeight()/2f);
//		attachChild(muSprite);
		
		//时钟道具数量
//		Text clockNum = new Text(0f,0f,ResourceManager.sysFont,"99",5,mVertexBufferObjectManager);
//		clockNum.setPosition(muSprite.getX()+clockNum.getWidth(),muSprite.getY());
//		attachChild(clockNum);
		
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
		
		
		angelbossAS = new AnimatedSprite(0f,0f,ResourceManager.angelboss,mVertexBufferObjectManager);
		angelbossAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		angelbossAS.setScale(2f);
		frameDur = new long[2];
		Arrays.fill(frameDur, 300);
		angelbossAS.animate(frameDur,0,1,true);
		//angelbossAS.registerEntityModifier(pEntityModifier);
		mPhysicsHandler = new PhysicsHandler(angelbossAS);
		angelbossAS.registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocityX(100f);
		attachChild(angelbossAS);
		
		
		
		
		
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
	
	//游戏时间倒计时,BOSS移动
	private IUpdateHandler gameRunTimer = new IUpdateHandler() {

		public void onUpdate(float pSecondsElapsed) {
			gameTime-=pSecondsElapsed;
			if(gameTime<=0) {
				mTimeText.setText(TimeUtils.formatSeconds(0));
				unregisterUpdateHandler(this);
				angelbossAS.unregisterUpdateHandler(mPhysicsHandler);
			} else {
				mTimeText.setText(TimeUtils.formatSeconds(Math.round(gameTime)));
			}
			
			if(!maxRight){
				if(angelbossAS.getX() > (3f/4f)*mCameraWidth){
					mPhysicsHandler.setVelocityX(-100f);
					maxRight = true;
					maxLeft = false;
				}
			}
			if(!maxLeft){
				if(angelbossAS.getX() < (1f/4f)*mCameraWidth){
					mPhysicsHandler.setVelocityX(100f);
					maxRight = false;
					maxLeft = true;
				}
			}
			
			if(mScore >= 10 && mScore < 100){
				mScoreText.setText("00"+mScore);
			}else if(mScore >= 100 && mScore < 999){
				mScoreText.setText("0"+mScore);
			}else if(mScore >= 1000){
				mScoreText.setText(""+mScore);
			}else{
				mScoreText.setText("000"+mScore);
			}
		}

		public void reset() {}
	};

}
