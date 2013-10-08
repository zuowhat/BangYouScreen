package org.game.bangyouscreen.gamelevels;

import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
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
	
	private AnimatedSprite aidSkill1AS;
	private AnimatedSprite angelbossAS;
	private static final float BOSS_VELOCITY = 100.0f;
	private PhysicsHandler mPhysicsHandler;
	private long[] frameDur;
	
	private int clickNum = 10;
	private float xue3P;
	private float xue3S;
	private Sprite xue1Sprite;
	private Sprite xue2Sprite;
	
	private ButtonSprite greenButtonBS;
	private ButtonSprite blueButtonBS;
	
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
		greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mScore++;
				clickNum--;
				//血条变化
				//float bloodLength = xue3Sprite.getWidth()-xue3S;
				if(clickNum > 0 && gameTime > 0){
					xue2Sprite.setPosition(xue1Sprite.getX()-xue3P, xue2Sprite.getY());
					xue2Sprite.setSize(xue2Sprite.getWidth()-xue3S, xue2Sprite.getHeight());
					if(clickNum == 0 && gameTime > 0){
						angelbossAS.unregisterUpdateHandler(mPhysicsHandler);
						unregisterUpdateHandler(gameRunTimer);
					}
				}
				
				
//				if(mScore % 5 == 0){
//					angelbossAS.animate(frameDur,1,2,true);
//					angelbossAS.stopAnimation(2);
//					angelbossAS.animate(frameDur,0,1,true);
//				}
			}});
		attachChild(greenButtonBS);
		registerTouchArea(greenButtonBS);
		
		//蓝色按钮
		blueButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, blueButtonBS);
		blueButtonBS.setPosition(mCameraWidth - blueButtonBS.getWidth() / 2f, blueButtonBS.getHeight() / 2f);
		blueButtonBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mScore++;
				clickNum--;
				
				//血条变化
				//float bloodLength = xue3Sprite.getWidth()-xue3S;
				if(clickNum >= 0 && gameTime > 0){
					xue2Sprite.setPosition(xue1Sprite.getX()-xue3P, xue2Sprite.getY());
					xue2Sprite.setSize(xue2Sprite.getWidth()-xue3S, xue2Sprite.getHeight());
					if(clickNum == 0 && gameTime > 0){
						angelbossAS.unregisterUpdateHandler(mPhysicsHandler);
						unregisterUpdateHandler(gameRunTimer);
					}
				}
				
				
//				if(mScore % 5 == 0){
//					//angelbossAS.stopAnimation(2);
//					angelbossAS.animate(frameDur,1,2,true);
//					angelbossAS.stopAnimation(2);
//					angelbossAS.animate(frameDur,0,1,true);
//				}
			}});
		attachChild(blueButtonBS);
		registerTouchArea(blueButtonBS);
		
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
		xue1Sprite = new Sprite(0f,0f,ResourceManager.xue1,mVertexBufferObjectManager);
		xue1Sprite.setPosition(mCameraWidth/2f, clockSprite.getY()-(clockSprite.getHeight()/2f)-30f);
		xue1Sprite.setSize(0.6875f*mCameraWidth, 0.05833f*mCameraHeight);
		//xue1Sprite.setSize(0.5f*mCameraWidth, 0.1f*mCameraHeight);
		//EntityUtil.setSize("width", 0.6875f, xue1Sprite);
		attachChild(xue1Sprite);
		xue2Sprite = new Sprite(0f,0f,ResourceManager.xue2,mVertexBufferObjectManager);
		xue2Sprite.setPosition(xue1Sprite.getWidth()/2f, xue1Sprite.getHeight()/2f);
		xue2Sprite.setSize(xue1Sprite.getWidth(), xue1Sprite.getHeight());
		//EntityUtil.setSize("width", 0.67875f, xue2Sprite);
		xue1Sprite.attachChild(xue2Sprite);
//		xue3Sprite = new Sprite(0f,0f,ResourceManager.xue3,mVertexBufferObjectManager);
//		xue3Sprite.setPosition(xue1Sprite.getWidth()/2f, xue3Sprite.getHeight()/2f);
//		xue3Sprite.setSize(xue1Sprite.getWidth()-10f, xue1Sprite.getHeight()-10f);
		//xue1Sprite.attachChild(xue3Sprite);
		xue3P = (xue1Sprite.getWidth()/2f)/clickNum;
		xue3S = (xue1Sprite.getWidth())/clickNum;
		
		
		
		
		//头像
		ButtonSprite aiderHeadSprite = new ButtonSprite(0f,0f,ResourceManager.aiderHead,mVertexBufferObjectManager);
		aiderHeadSprite.setPosition((1f / 2f) * mCameraWidth + clockSprite.getWidth(), mCameraHeight - clockSprite.getHeight()/2f);
		aiderHeadSprite.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				//辅助技能
				aidSkill1AS = new AnimatedSprite(mCameraWidth/2f,mCameraHeight/2f,ResourceManager.aidSkill2,mVertexBufferObjectManager);
				aidSkill1AS.setScale(2f);
				aidSkill1AS.animate(100,3,new IAnimationListener(){

					public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
							int pInitialLoopCount) {
						angelbossAS.stopAnimation(2);
					}

					public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
							int pOldFrameIndex, int pNewFrameIndex) {
						
					}

					public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
							int pRemainingLoopCount, int pInitialLoopCount) {
						
					}

					public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
						detachChild(aidSkill1AS);
						aidSkill1AS = null;
						angelbossAS.animate(frameDur,0,1,true);
					}
				});
				attachChild(aidSkill1AS);
				
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
		//EntityUtil.setSize("width", 0.1f, mTimeText);
		//mTimeText.setScale(0.1f*ResourceManager.getInstance().cameraScaleFactorX);
		mTimeText.setScale(2f);
		attachChild(mTimeText);
		registerUpdateHandler(gameRunTimer);
		
		//得分
		mScoreText = new Text(0f,0f,ResourceManager.sysFont,"0000",8,mVertexBufferObjectManager);
		mScoreText.setPosition(mScoreText.getWidth(), mCameraHeight - mScoreText.getHeight()*1.1f);
		//EntityUtil.setSize("width", 0.1f, mScoreText);
		mScoreText.setScale(2f);
		//mScoreText.setScale(0.1f*ResourceManager.getInstance().cameraScaleFactorX);
		attachChild(mScoreText);
		
		//BOSS
		angelbossAS = new AnimatedSprite(0f,0f,ResourceManager.angelboss,mVertexBufferObjectManager);
		angelbossAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		EntityUtil.setSize("width", 0.32f, angelbossAS);
		//angelbossAS.setScale(2f);
		frameDur = new long[2];
		Arrays.fill(frameDur, 300);
		angelbossAS.animate(frameDur,0,1,true);
		//angelbossAS.registerEntityModifier(pEntityModifier);
		mPhysicsHandler = new PhysicsHandler(angelbossAS);
		angelbossAS.registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocity(BOSS_VELOCITY);
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
			//倒计时
			gameTime-=pSecondsElapsed;
			if(gameTime<=0) {
				mTimeText.setText(TimeUtils.formatSeconds(0));
				unregisterUpdateHandler(this);
				angelbossAS.unregisterUpdateHandler(mPhysicsHandler);
			} else {
				mTimeText.setText(TimeUtils.formatSeconds(Math.round(gameTime)));
			}
			
			//BOSS移动
			if(angelbossAS.getY() < (angelbossAS.getHeight() * 0.5f)) {
				mPhysicsHandler.setVelocityY(BOSS_VELOCITY);
			} else if(angelbossAS.getY() + (angelbossAS.getHeight() * 0.5f) > xue1Sprite.getY() - xue1Sprite.getHeight()/2f) {
				mPhysicsHandler.setVelocityY(-BOSS_VELOCITY);
			}
			if(angelbossAS.getX() - angelbossAS.getWidth()/2f < greenButtonBS.getX()+(greenButtonBS.getWidth()/2f)) {
				mPhysicsHandler.setVelocityX(BOSS_VELOCITY);
			} else if(angelbossAS.getX() + (angelbossAS.getWidth() * 0.5f) > blueButtonBS.getX()-(blueButtonBS.getWidth()/2f)) {
				mPhysicsHandler.setVelocityX(-BOSS_VELOCITY);
			}
			
			//统计得分
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
