package org.game.bangyouscreen.gameLevels;

import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.input.touch.controller.SingleTouchController;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameScore;
import org.game.bangyouscreen.util.GameTimer;

public class GameLevel extends ManagedScene {
	
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	//private static final String TIME_FORMAT = "00:00";
	//private Text mTimeText;
	private float gameTime = 30f;
	private int mScore = 0;
	private GameTimer mGameTime;
	private boolean mTenSeconds = false;
	private GameScore mGameScore;
	
	private AnimatedSprite aidSkill1AS;
	private AnimatedSprite bossAS;
	private TiledTextureRegion bossTTR;
	private static final float BOSS_VELOCITY = 50.0f;
	private PhysicsHandler mPhysicsHandler;
	private long[] frameDur;
	
	private int bossHP;
	private float xue3P;
	private float xue3S;
	private Sprite xue1Sprite;
	private Sprite xue2Sprite;
	
	private ButtonSprite greenButtonBS;
	private ButtonSprite redButtonBS;
	private TextureRegion gameBGTR;
	private int bossType;
	
	public GameLevel (TextureRegion pGameBG,TiledTextureRegion pBossTTR, int pBossHP, int pBossType){
		gameBGTR = pGameBG;
		bossTTR = pBossTTR;
		bossHP = pBossHP;
		bossType = pBossType;
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
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(true);
		ResourceManager.getInstance().engine.setTouchController(new MultiTouchController());
		
		//背景
		Sprite gameBG = new Sprite(0f,0f,gameBGTR,mVertexBufferObjectManager);
		gameBG.setScale(ResourceManager.getInstance().cameraHeight / ResourceManager.gameBG10.getHeight());
		gameBG.setPosition(mCameraWidth/2f,mCameraHeight/2f);
		gameBG.setZIndex(-90);
		attachChild(gameBG);
		
		//倒计时
		mGameTime = new GameTimer(this);
		mGameTime.addToLayer(this);
		registerUpdateHandler(gameRunTimer);
		
		//得分
		mGameScore = new GameScore();
		mGameScore.addToLayer(this);
		
		//BOSS血条
		xue1Sprite = new Sprite(0f,0f,ResourceManager.xue1,mVertexBufferObjectManager);
		xue1Sprite.setPosition(mCameraWidth/2f, mCameraHeight - xue1Sprite.getHeight()/2f-5f);
		xue1Sprite.setSize(0.6875f*mCameraWidth, 0.05833f*mCameraHeight);
		//xue1Sprite.setSize(0.5f*mCameraWidth, 0.1f*mCameraHeight);
		//EntityUtil.setSize("width", 0.6875f, xue1Sprite);
		attachChild(xue1Sprite);
		xue2Sprite = new Sprite(0f,0f,ResourceManager.xue2,mVertexBufferObjectManager);
		xue2Sprite.setPosition(xue1Sprite.getWidth()/2f, xue1Sprite.getHeight()/2f);
		xue2Sprite.setSize(xue1Sprite.getWidth(), xue1Sprite.getHeight());
		//EntityUtil.setSize("width", 0.67875f, xue2Sprite);
		xue1Sprite.attachChild(xue2Sprite);
		//xue2Sprite.setScaleCenterX(xue1Sprite.getWidth());
//		xue3Sprite = new Sprite(0f,0f,ResourceManager.xue3,mVertexBufferObjectManager);
//		xue3Sprite.setPosition(xue1Sprite.getWidth()/2f, xue3Sprite.getHeight()/2f);
//		xue3Sprite.setSize(xue1Sprite.getWidth()-10f, xue1Sprite.getHeight()-10f);
		//xue1Sprite.attachChild(xue3Sprite);
		xue3P = (xue2Sprite.getWidth()/2f)/bossHP;
		xue3S = (xue2Sprite.getWidth())/bossHP;
		
		//BOSS
		bossAS = new AnimatedSprite(0f,0f,bossTTR,mVertexBufferObjectManager);
		bossAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		EntityUtil.setSize("width", 0.32f, bossAS);
		//bossAS.setScale(2f);
		frameDur = new long[2];
		Arrays.fill(frameDur, 300);
		bossAS.animate(frameDur,0,1,true);
		//bossAS.registerEntityModifier(pEntityModifier);
		mPhysicsHandler = new PhysicsHandler(bossAS);
		bossAS.registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocity(BOSS_VELOCITY);
		attachChild(bossAS);
		
		//绿色按钮
		greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mScore++;
				bossHP--;
				//血条变化
				//float bloodLength = xue3Sprite.getWidth()-xue3S;
				if(bossHP >= 0 && gameTime > 0){
					xue2Sprite.setPosition(xue2Sprite.getX()-xue3P, xue2Sprite.getY());
					xue2Sprite.setSize(xue2Sprite.getWidth()-xue3S, xue2Sprite.getHeight());
					mGameScore.adjustScore(mScore);
					//xue2Sprite.setScaleX(bossHP / 100.0F);
					
					
					if(bossHP == 0 && gameTime > 0){
						bossAS.unregisterUpdateHandler(mPhysicsHandler);
						unregisterUpdateHandler(gameRunTimer);
					}
				}
				
				
//				if(mScore % 5 == 0){
//					bossAS.animate(frameDur,1,2,true);
//					bossAS.stopAnimation(2);
//					bossAS.animate(frameDur,0,1,true);
//				}
			}});
		attachChild(greenButtonBS);
		registerTouchArea(greenButtonBS);
		
		//蓝色按钮
		redButtonBS = new ButtonSprite(0f,0f,ResourceManager.redButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, redButtonBS);
		redButtonBS.setPosition(mCameraWidth - redButtonBS.getWidth() / 2f, redButtonBS.getHeight() / 2f);
		redButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mScore++;
				bossHP--;
				
				//血条变化
				if(bossHP >= 0 && gameTime > 0){
					xue2Sprite.setPosition(xue2Sprite.getX()-xue3P, xue2Sprite.getY());
					xue2Sprite.setSize(xue2Sprite.getWidth()-xue3S, xue2Sprite.getHeight());
					//xue2Sprite.setScaleX(bossHP / 100.0F);
					mGameScore.adjustScore(mScore);
					if(bossHP == 0 && gameTime > 0){
						bossAS.unregisterUpdateHandler(mPhysicsHandler);
						unregisterUpdateHandler(gameRunTimer);
					}
				}
				
				
//				if(mScore % 5 == 0){
//					//bossAS.stopAnimation(2);
//					bossAS.animate(frameDur,1,2,true);
//					bossAS.stopAnimation(2);
//					bossAS.animate(frameDur,0,1,true);
//				}
			}});
		attachChild(redButtonBS);
		registerTouchArea(redButtonBS);
		
		//时钟
		ButtonSprite clockSprite = new ButtonSprite(0f,0f,ResourceManager.clockTR,mVertexBufferObjectManager);
		//EntityUtil.setSize("width", 1f / 8f, clockSprite);
		clockSprite.setPosition((1f/2f)*mCameraWidth, clockSprite.getHeight()/2f);
		clockSprite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				gameTime+=30f;
				
			}});
		attachChild(clockSprite);
		registerTouchArea(clockSprite);
		
		//头像
		ButtonSprite aiderHeadSprite = new ButtonSprite(0f,0f,ResourceManager.clockTR,mVertexBufferObjectManager);
		aiderHeadSprite.setPosition((1f / 2f) * mCameraWidth + clockSprite.getWidth(), clockSprite.getHeight()/2f);
		aiderHeadSprite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				//辅助技能
				aidSkill1AS = new AnimatedSprite(mCameraWidth/2f,mCameraHeight/2f,ResourceManager.aidSkill2Temp,mVertexBufferObjectManager);
				aidSkill1AS.setScale(2f);
				aidSkill1AS.animate(100,3,new IAnimationListener(){

					@Override
					public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
							int pInitialLoopCount) {
						bossAS.stopAnimation(2);
					}

					@Override
					public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
							int pOldFrameIndex, int pNewFrameIndex) {
						
					}

					@Override
					public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
							int pRemainingLoopCount, int pInitialLoopCount) {
						
					}

					@Override
					public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
						detachChild(aidSkill1AS);
						aidSkill1AS = null;
						bossAS.animate(frameDur,0,1,true);
					}
				});
				attachChild(aidSkill1AS);
				
			}});
		attachChild(aiderHeadSprite);
		registerTouchArea(aiderHeadSprite);
		
		

		

		
		
	}

	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(false);
		ResourceManager.getInstance().engine.setTouchController(new SingleTouchController());
	}

	@Override
	public void onShowScene() {

	}

	@Override
	public void onHideScene() {

	}
	
	//游戏时间倒计时,BOSS移动
	private IUpdateHandler gameRunTimer = new IUpdateHandler() {

		@Override
		public void onUpdate(float pSecondsElapsed) {
			//倒计时
			gameTime-=pSecondsElapsed;
			if(gameTime<=0) {
				//mTimeText.setText(TimeUtils.formatSeconds(0));
				mGameTime.adjustTime(0f);
				unregisterUpdateHandler(this);
				bossAS.unregisterUpdateHandler(mPhysicsHandler);
				mGameTime.mDigitsSprite[3].clearEntityModifiers();
				mGameTime.mDigitsSprite[2].clearEntityModifiers();
				mGameTime.mDigitsSprite[3].setScale(1.0F);
				mGameTime.mDigitsSprite[2].setScale(1.0F);
			} else {
				if((!mTenSeconds) && gameTime <= 10f){
					mTenSeconds = true;
					mGameTime.mBounceOut1.reset();
					mGameTime.mBounceOut2.reset();
					mGameTime.mDigitsSprite[3].registerEntityModifier(mGameTime.mBounceOut1);
					mGameTime.mDigitsSprite[2].registerEntityModifier(mGameTime.mBounceOut2);
					mGameTime.mColorOut1.reset();
					mGameTime.mColorOut2.reset();
					mGameTime.mDigitsSprite[3].registerEntityModifier(mGameTime.mColorOut1);
					mGameTime.mDigitsSprite[2].registerEntityModifier(mGameTime.mColorOut2);
				}else if(mTenSeconds && gameTime > 10f){
					mTenSeconds = false;
					//mGameTime.mDigitsSprite[3].registerEntityModifier(mGameTime.mColorOut1);
					//mGameTime.mDigitsSprite[2].registerEntityModifier(mGameTime.mColorOut2);
					mGameTime.mDigitsSprite[3].clearEntityModifiers();
					mGameTime.mDigitsSprite[2].clearEntityModifiers();
					mGameTime.mDigitsSprite[3].setScale(1.0F);
					mGameTime.mDigitsSprite[2].setScale(1.0F);
				}
				mGameTime.adjustTime(gameTime);
			}
			
			//BOSS移动
			if(bossAS.getY() - bossAS.getHeight()/2f < redButtonBS.getY() + redButtonBS.getHeight()/2f) {
				mPhysicsHandler.setVelocityY(BOSS_VELOCITY);
			} else if(bossAS.getY() + (bossAS.getHeight() * 0.5f) > xue1Sprite.getY() - xue1Sprite.getHeight()/2f) {
				mPhysicsHandler.setVelocityY(-BOSS_VELOCITY);
			}
			if(bossAS.getX() - bossAS.getWidth()/2f < 2f) {
				mPhysicsHandler.setVelocityX(BOSS_VELOCITY);
			} else if(bossAS.getX() + (bossAS.getWidth() * 0.5f) > mCameraWidth - 2f) {
				mPhysicsHandler.setVelocityX(-BOSS_VELOCITY);
			}
			
			//统计得分
			mGameScore.adjustScore(mScore);
		}

		@Override
		public void reset() {}
	};
	
	public void onPauseGameLevel(){
		unregisterUpdateHandler(gameRunTimer);
		setIgnoreUpdate(true);
		unregisterTouchArea(greenButtonBS);
		unregisterTouchArea(redButtonBS);
		
		
	}
	
	public void onResumeGameLevel(){
		registerUpdateHandler(gameRunTimer);
		setIgnoreUpdate(false);
		registerTouchArea(greenButtonBS);
		registerTouchArea(redButtonBS);
		
		
	}

}
