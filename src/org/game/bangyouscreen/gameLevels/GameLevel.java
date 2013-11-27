package org.game.bangyouscreen.gameLevels;

import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.input.touch.controller.SingleTouchController;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.model.BossModel;
import org.game.bangyouscreen.model.PlayerModel;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameScore;
import org.game.bangyouscreen.util.GameTimer;

public class GameLevel extends ManagedScene {
	
	public GameLevel INSTANCE = this;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	//private static final String TIME_FORMAT = "00:00";
	//private Text mTimeText;
	private float gameTime = 300f;//初始游戏时间
	private int mScore = 0;
	private GameTimer mGameTime;
	private boolean mTenSeconds = false;
	private GameScore mGameScore;
	private AnimatedSprite magicAS;
	private AnimatedSprite bossAS;
	private static final float BOSS_VELOCITY = 50.0f;//BOSS移动速度
	private PhysicsHandler mPhysicsHandler;
	private long[] frameDur;
	private float xue3P;
	private float xue3S;
	private Sprite xue1Sprite;
	private Sprite xue2Sprite;
	private ButtonSprite greenButtonBS;
	private ButtonSprite redButtonBS;
	
	private BossModel bossModel;
	private PlayerModel playerModel;
	//private int playerDPS;//玩家物理伤害
	//private int playerAOE;//玩家魔法伤害
	private float dpsXS;//物理伤害系数
	private float aoeXS;//魔法伤害系数
	private int bossHP;
	
	public GameLevel (BossModel pBossModel, PlayerModel pPlayerModel){
		bossModel = pBossModel;
		playerModel = pPlayerModel;
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
		countDpsXS();
		countAoeXS();
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(true);
		ResourceManager.getInstance().engine.setTouchController(new MultiTouchController());
		bossHP = bossModel.getBossHP();
		//背景
		Sprite gameBG = new Sprite(0f,0f,bossModel.getGameBGTR(),mVertexBufferObjectManager);
		gameBG.setScale(ResourceManager.getInstance().cameraHeight / bossModel.getGameBGTR().getHeight());
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
		xue1Sprite.setPosition(mCameraWidth/2f, mGameScore.mDigitsSprite[0].getY());
		xue1Sprite.setSize(0.6875f*mCameraWidth, 0.05833f*mCameraHeight);
		attachChild(xue1Sprite);
		xue2Sprite = new Sprite(0f,0f,ResourceManager.xue2,mVertexBufferObjectManager);
		xue2Sprite.setPosition(xue1Sprite.getWidth()/2f, xue1Sprite.getHeight()/2f);
		xue2Sprite.setSize(xue1Sprite.getWidth(), xue1Sprite.getHeight());
		xue1Sprite.attachChild(xue2Sprite);
		
		//BOSS移动
		bossAS = new AnimatedSprite(0f,0f,bossModel.getBossTTR(),mVertexBufferObjectManager);
		bossAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		EntityUtil.setSize("height", 0.5f, bossAS);
		//bossAS.setScale(2f);
		frameDur = new long[2];
		Arrays.fill(frameDur, 300);
		bossAS.animate(frameDur,0,1,true);
		//bossAS.registerEntityModifier(pEntityModifier);
		mPhysicsHandler = new PhysicsHandler(bossAS);
		bossAS.registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocity(BOSS_VELOCITY);
		attachChild(bossAS);
		
		//操作区半透明背景
		Rectangle fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight/4f+10f, mVertexBufferObjectManager);
		fadableBGRect.setPosition(mCameraWidth/2f, mCameraHeight/8f);
		fadableBGRect.setColor(0f, 0f, 0f, 0.5f);
		attachChild(fadableBGRect);
		
		//绿色按钮
		greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					updateHP(1);
			}});
		fadableBGRect.attachChild(greenButtonBS);
		registerTouchArea(greenButtonBS);
		
		//蓝色按钮
		redButtonBS = new ButtonSprite(0f,0f,ResourceManager.redButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, redButtonBS);
		redButtonBS.setPosition(mCameraWidth - redButtonBS.getWidth() / 2f, redButtonBS.getHeight() / 2f);
		redButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					updateHP(1);
			}});
		fadableBGRect.attachChild(redButtonBS);
		registerTouchArea(redButtonBS);
		
		//时钟
		ButtonSprite clockSprite = new ButtonSprite(0f,0f,ResourceManager.propsTTR.getTextureRegion(3),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, clockSprite);
		clockSprite.setPosition((1f/2f)*mCameraWidth, redButtonBS.getY());
		clockSprite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				gameTime+=30f;
				
			}});
		fadableBGRect.attachChild(clockSprite);
		registerTouchArea(clockSprite);
		
		//魔法按钮
		ButtonSprite magicBS = new ButtonSprite(0f,0f,playerModel.getMagicTR(),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, magicBS);
		magicBS.setPosition(clockSprite.getX()-(53f/400f)*mCameraWidth, clockSprite.getY());
		magicBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				//魔法效果
				magicAS = new AnimatedSprite(mCameraWidth/2f,mCameraHeight/2f,playerModel.getMagicTTR(),mVertexBufferObjectManager);
				//magicAS.setScale(2f);
				magicAS.animate(100,3,new IAnimationListener(){

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
						detachChild(magicAS);
						magicAS = null;
						bossAS.animate(frameDur,0,1,true);
						updateHP(2);
					}
				});
				attachChild(magicAS);
				
			}});
		fadableBGRect.attachChild(magicBS);
		registerTouchArea(magicBS);
		
		//武器图标
		Sprite weaponBS = new Sprite(0f,0f,playerModel.getWeaponTR(),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, weaponBS);
		weaponBS.setPosition(magicBS.getX()-(53f/400f)*mCameraWidth, clockSprite.getY());
		fadableBGRect.attachChild(weaponBS);

		//道具
		ButtonSprite propsBS = new ButtonSprite(0f,0f,ResourceManager.propsTTR.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, propsBS);
		propsBS.setPosition(clockSprite.getX()+(53f/400f)*mCameraWidth, clockSprite.getY());
		propsBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
			}});
		fadableBGRect.attachChild(propsBS);
		registerTouchArea(propsBS);
		
		//其他
		ButtonSprite otherBS = new ButtonSprite(0f,0f,ResourceManager.propsTTR.getTextureRegion(1),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, otherBS);
		otherBS.setPosition(propsBS.getX()+(53f/400f)*mCameraWidth, clockSprite.getY());
		otherBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
			}});
		fadableBGRect.attachChild(otherBS);
		registerTouchArea(otherBS);
		
		
		
		
		
		
	}

	public void onUnloadScene() {
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(false);
		ResourceManager.getInstance().engine.setTouchController(new SingleTouchController());
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChildren();
				for(int i = 0; i < INSTANCE.getChildCount(); i++){
					INSTANCE.getChildByIndex(i).dispose();
					INSTANCE.getChildByIndex(i).clearEntityModifiers();
					INSTANCE.getChildByIndex(i).clearUpdateHandlers();
				}
				INSTANCE.clearEntityModifiers();
				INSTANCE.clearTouchAreas();
				INSTANCE.clearUpdateHandlers();
			}});
	}

	@Override
	public void onShowScene() {

	}

	@Override
	public void onHideScene() {

	}
	
	/**
	 * 实时更新游戏状态
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private IUpdateHandler gameRunTimer = new IUpdateHandler() {

		public void onUpdate(float pSecondsElapsed) {
			//倒计时
			gameTime-=pSecondsElapsed;
			if(gameTime<=0) {
				mGameTime.adjustTime(0f);
				unregisterUpdateHandler(this);
				setIgnoreUpdate(true);
				unregisterTouchArea(greenButtonBS);
				unregisterTouchArea(redButtonBS);
				bossAS.unregisterUpdateHandler(mPhysicsHandler);
				mGameTime.mDigitsSprite[3].clearEntityModifiers();
				mGameTime.mDigitsSprite[2].clearEntityModifiers();
				mGameTime.mDigitsSprite[3].setScale(1.0F);
				mGameTime.mDigitsSprite[2].setScale(1.0F);
				gameFail();
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
			mGameScore.addScore(mScore);
		}

		@Override
		public void reset() {}
	};
	
	/**
	 * 暂停游戏
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public void onPauseGameLevel(){
		unregisterUpdateHandler(gameRunTimer);
		setIgnoreUpdate(true);
		unregisterTouchArea(greenButtonBS);
		unregisterTouchArea(redButtonBS);
	}
	
	/**
	 * 继续游戏
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public void onResumeGameLevel(){
		registerUpdateHandler(gameRunTimer);
		setIgnoreUpdate(false);
		registerTouchArea(greenButtonBS);
		registerTouchArea(redButtonBS);
	}
	
	/**
	 * 游戏胜利
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void gameWin(){
		
	}
	
	/**
	 * 游戏失败
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void gameFail(){
		
	}
	
	/**
	 * 更新BOSS血量和得分
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void updateHP(int type){
		System.out.println("BOSSHP之前 --> "+ bossHP);
		mScore++;
		if(type == 1){
			int dps = countPlayerDPS();
			//mScore = mScore + dps;
			xue3S = xue2Sprite.getWidth()-(xue2Sprite.getWidth()*dps)/bossHP;
			xue3P = xue3S/2f;
			bossHP = bossHP - dps;
			System.out.println("BOSSHP之后 --> "+ bossHP);
		}else{
			int aoe = countPlayerAOE();
			//mScore = mScore + aoe;
			xue3S = xue2Sprite.getWidth()-(xue2Sprite.getWidth()*aoe)/bossHP;
			xue3P = xue3S/2f;
			bossHP = bossHP - aoe;
			System.out.println("BOSSHP之后 --> "+ bossHP);
		}
		if(bossHP <= 0){
			xue2Sprite.setPosition(0, xue2Sprite.getY());
			xue2Sprite.setSize(0, xue2Sprite.getHeight());
		}else{
			xue2Sprite.setPosition(xue3P, xue2Sprite.getY());
			xue2Sprite.setSize(xue3S, xue2Sprite.getHeight());
		}
		mGameScore.addScore(mScore);
		if(bossHP <= 0 && gameTime > 0){
			bossAS.unregisterUpdateHandler(mPhysicsHandler);
			unregisterUpdateHandler(gameRunTimer);
			unregisterTouchArea(greenButtonBS);
			unregisterTouchArea(redButtonBS);
			gameWin();
		}
	}
	
	/**
	 * 计算玩家物理攻击力
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private int countPlayerDPS(){
		float playerDPS = (float)(Math.random()*(playerModel.getWeaponDPSMax() - playerModel.getWeaponDPSMin())) + playerModel.getWeaponDPSMin();
		float bossDEF = (float)(Math.random()*(bossModel.getMaxBossDEF() - bossModel.getMinBossDEF())) + bossModel.getMinBossDEF();
		return Math.round(playerDPS * dpsXS - bossDEF);
	}
	
	/**
	 * 计算玩家物理伤害系数
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void countDpsXS(){
		int wt = playerModel.getWeaponType();
		int bd = bossModel.getBossDefType();
		if(wt == 1){
			if(bd == 1){
				dpsXS = 1f;
			}else if(bd == 2){
				dpsXS = 2f;
			}else if(bd == 3){
				dpsXS = 0.5f;
			}else if(bd == 4){
				dpsXS = 1f;
			}else if(bd == 5){
				dpsXS = 0.5f;
			}
		}else if(wt == 2){
			if(bd == 1){
				dpsXS = 0.5f;
			}else if(bd == 2){
				dpsXS = 1f;
			}else if(bd == 3){
				dpsXS = 2f;
			}else if(bd == 4){
				dpsXS = 1f;
			}else if(bd == 5){
				dpsXS = 0.5f;
			}
		}else if(wt == 3){
			if(bd == 1){
				dpsXS = 2f;
			}else if(bd == 2){
				dpsXS = 0.5f;
			}else if(bd == 3){
				dpsXS = 1f;
			}else if(bd == 4){
				dpsXS = 1f;
			}else if(bd == 5){
				dpsXS = 0.5f;
			}
		}else if(wt == 4){
			if(bd == 1){
				dpsXS = 1f;
			}else if(bd == 2){
				dpsXS = 1f;
			}else if(bd == 3){
				dpsXS = 1f;
			}else if(bd == 4){
				dpsXS = 1f;
			}else if(bd == 5){
				dpsXS = 0.5f;
			}
		}else if(wt == 5){
			if(bd == 1){
				dpsXS = 2f;
			}else if(bd == 2){
				dpsXS = 2f;
			}else if(bd == 3){
				dpsXS = 2f;
			}else if(bd == 4){
				dpsXS = 2f;
			}else if(bd == 5){
				dpsXS = 1f;
			}
		}
	}
	
	/**
	 * 计算玩家魔法攻击力
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private int countPlayerAOE(){
		float playerAOE = (float)(Math.random()*(playerModel.getMagicAOEMax() - playerModel.getMagicAOEMin())) + playerModel.getMagicAOEMin();
		float bossDEF = (float)(Math.random()*(bossModel.getMaxBossDEF() - bossModel.getMinBossDEF())) + bossModel.getMinBossDEF();
		return Math.round(playerAOE * aoeXS - bossDEF);
	}
	
	/**
	 * 计算玩家魔法伤害系数
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void countAoeXS(){
		int wt = playerModel.getMagicType();
		int bd = bossModel.getBossDefType();
		if(wt == 1){
			if(bd == 1){
				aoeXS = 1f;
			}else if(bd == 2){
				aoeXS = 2f;
			}else if(bd == 3){
				aoeXS = 0.5f;
			}else if(bd == 4){
				aoeXS = 1f;
			}else if(bd == 5){
				aoeXS = 0.5f;
			}
		}else if(wt == 2){
			if(bd == 1){
				aoeXS = 0.5f;
			}else if(bd == 2){
				aoeXS = 1f;
			}else if(bd == 3){
				aoeXS = 2f;
			}else if(bd == 4){
				aoeXS = 1f;
			}else if(bd == 5){
				aoeXS = 0.5f;
			}
		}else if(wt == 3){
			if(bd == 1){
				aoeXS = 2f;
			}else if(bd == 2){
				aoeXS = 0.5f;
			}else if(bd == 3){
				aoeXS = 1f;
			}else if(bd == 4){
				aoeXS = 1f;
			}else if(bd == 5){
				aoeXS = 0.5f;
			}
		}else if(wt == 4){
			if(bd == 1){
				aoeXS = 1f;
			}else if(bd == 2){
				aoeXS = 1f;
			}else if(bd == 3){
				aoeXS = 1f;
			}else if(bd == 4){
				aoeXS = 1f;
			}else if(bd == 5){
				aoeXS = 0.5f;
			}
		}else if(wt == 5){
			if(bd == 1){
				aoeXS = 2f;
			}else if(bd == 2){
				aoeXS = 2f;
			}else if(bd == 3){
				aoeXS = 2f;
			}else if(bd == 4){
				aoeXS = 2f;
			}else if(bd == 5){
				aoeXS = 1f;
			}
		}
	}

}
