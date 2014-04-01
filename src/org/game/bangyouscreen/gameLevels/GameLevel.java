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
import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.layer.GameFailLayer;
import org.game.bangyouscreen.layer.GameWinLayer;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.model.BossModel;
import org.game.bangyouscreen.model.PlayerModel;
import org.game.bangyouscreen.util.Constants;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameNumberUtil;
import org.game.bangyouscreen.util.GameTimer;


public class GameLevel extends ManagedScene {
	
	public GameLevel INSTANCE = this;
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	//private static final String TIME_FORMAT = "00:00";
	//private Text mTimeText;
	private float gameTime = Constants.GAMETIME_INIT;//初始游戏时间
	private int mScore = 0;
	private int clickNum = 0;
	private GameTimer mGameTime;
	private boolean mTenSeconds = false;
	private GameNumberUtil mGameNumber;
	private AnimatedSprite magicAS;
	private AnimatedSprite bossAS;
	private PhysicsHandler mPhysicsHandler;
	//private long[] frameDur;
	private float xue3P;
	private float xue3S;
	private Sprite xue1Sprite;
	private Sprite xue2Sprite;
	private ButtonSprite greenButtonBS;
	private ButtonSprite redButtonBS;
	private ButtonSprite magicBS;
	private ButtonSprite clockBS;
	private ButtonSprite tiamatBS;
	private ButtonSprite bingpoBS;
	
	public static BossModel bossModel;
	public static PlayerModel playerModel;
	//private int playerDPS;//玩家物理伤害
	//private int playerAOE;//玩家魔法伤害
	private float dpsXS;//物理伤害系数
	private float aoeXS;//魔法伤害系数
	private int bossHP;
	private int allBossHP;
	private AnimatedSprite clockTimeAS;
	private String[] sounds = {"g_win","g_fail","g_time","g_bomb","g_button","a_CountDown","g_go","g_notenough","g_drink"};
	private String[] musics = {"zhan","zhu","ming","shi"};
	private Rectangle fadableBGRect;
	private AnimatedSprite clockCooling;
	private AnimatedSprite magicCooling;
	private AnimatedSprite tiamatCooling;
	private Sprite bingpoCooling;
	private boolean isAddAOE = false;
	private boolean isAddDPS = false;
	private float tiamatTime;
	private int weaponPotionNum;
	private int magicPotionNum;
	private int clockNum;
	private long[] frameDur = new long[8];
	
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
		weaponPotionNum = BangYouScreenActivity.getIntFromSharedPreferences(Constants.Prop_BUY+0);
		magicPotionNum = BangYouScreenActivity.getIntFromSharedPreferences(Constants.Prop_BUY+1);
		clockNum = BangYouScreenActivity.getIntFromSharedPreferences(Constants.Prop_BUY+2);
		countDpsXS();
		countAoeXS();
//		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(true);
//		ResourceManager.getInstance().engine.setTouchController(new MultiTouchController());
		//ResourceManager.getInstance().engine.getTouchController();
		bossHP = bossModel.getBossHP();
		allBossHP = bossModel.getBossHP();
		//背景
		Sprite gameBG = new Sprite(0f,0f,bossModel.getGameBGTR(),mVertexBufferObjectManager);
		gameBG.setScale(ResourceManager.getInstance().cameraHeight / bossModel.getGameBGTR().getHeight());
		gameBG.setPosition(mCameraWidth/2f,mCameraHeight/2f);
		gameBG.setZIndex(-90);
		attachChild(gameBG);
		
		//倒计时
		mGameTime = new GameTimer(this);
		mGameTime.addToLayer(this);
		
		//得分
		mGameNumber = new GameNumberUtil();
		mGameNumber.addToLayer(this);
		
		//BOSS血条
		xue1Sprite = new Sprite(0f,0f,ResourceManager.xue1,mVertexBufferObjectManager);
		xue1Sprite.setPosition(mCameraWidth/2f, mGameNumber.mDigitsSprite[0].getY());
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
		//frameDur = new long[2];
		//Arrays.fill(frameDur, 300);
		bossAS.animate(100,true);
		mPhysicsHandler = new PhysicsHandler(bossAS);
		bossAS.registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocity(Constants.BOSS_VELOCITY);
		attachChild(bossAS);
		
		//操作区半透明背景
		fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight/4f+10f, mVertexBufferObjectManager);
		fadableBGRect.setPosition(mCameraWidth/2f, mCameraHeight/8f);
		fadableBGRect.setColor(0f, 0f, 0f, 0.5f);
		attachChild(fadableBGRect);
		
		//草地
//		Sprite meadowSprite = new Sprite(0f,0f,ResourceManager.meadow,mVertexBufferObjectManager);
//		EntityUtil.setSize("width", 1f, meadowSprite);
//		meadowSprite.setPosition(mCameraWidth/2f, fadableBGRect.getY()+fadableBGRect.getHeight()/2f);
//		attachChild(meadowSprite);
		
		//左边按钮
		greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					//SFXManager.getInstance().playSound("g_leftButton");
					if(bossHP > 0){
						updateHP(1);
					}
			}});
		fadableBGRect.attachChild(greenButtonBS);
		
		//右边按钮
		redButtonBS = new ButtonSprite(0f,0f,ResourceManager.redButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, redButtonBS);
		redButtonBS.setPosition(mCameraWidth - redButtonBS.getWidth() / 2f, redButtonBS.getHeight() / 2f);
		redButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					//SFXManager.getInstance().playSound("g_rightButton");
					if(bossHP > 0){
						updateHP(1);
					}
			}});
		fadableBGRect.attachChild(redButtonBS);
		
		//时钟
		clockBS = new ButtonSprite(0f,0f,ResourceManager.propsTTR.getTextureRegion(3),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, clockBS);
		clockBS.setPosition((1f/2f)*mCameraWidth, redButtonBS.getY());
		clockBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(clockNum > 0){
					unregisterTouchArea(clockBS);
					SFXManager.getInstance().playSound("g_time");
					gameTime+=Constants.CLOCKTIME;
					clockNum--;
					mGameNumber.updateGoodsNum(Constants.PROP_NAME, clockNum);
					BangYouScreenActivity.writeIntToSharedPreferences(Constants.Prop_BUY+2, clockNum);
					clockCooling = new AnimatedSprite(0f,0f,ResourceManager.iconCooling,mVertexBufferObjectManager);
					clockCooling.setPosition(clockBS.getWidth()/2f,clockBS.getHeight()/2f);
					clockCooling.setSize(clockBS.getWidth(), clockBS.getHeight());
					//时钟冷却时间10秒，待定
					Arrays.fill(frameDur, 1250);
					clockCooling.animate(frameDur,0,7,false,new IAnimationListener(){
	
						@Override
						public void onAnimationStarted(AnimatedSprite pAnimatedSprite,int pInitialLoopCount) {
							
						}
	
						@Override
						public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex,int pNewFrameIndex) {
							
						}
	
						@Override
						public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,int pRemainingLoopCount, int pInitialLoopCount) {
							
						}
	
						@Override
						public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
							clockBS.detachChild(clockCooling);
							clockCooling = null;
							if(gameTime > 0){
								registerTouchArea(clockBS);
							}
						}
					});
					clockBS.attachChild(clockCooling);
				}else{
					//数量不足音效
					SFXManager.getInstance().playSound("g_notenough");
				}
			}});
		fadableBGRect.attachChild(clockBS);
		mGameNumber.addGoodsNumInGameLevel(Constants.PROP_NAME, clockBS);
		mGameNumber.updateGoodsNum(Constants.PROP_NAME, clockNum);
		
		//魔法按钮
		magicBS = new ButtonSprite(0f,0f,playerModel.getMagicTR(),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, magicBS);
		magicBS.setPosition(clockBS.getX()-(53f/400f)*mCameraWidth, clockBS.getY());
		magicBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				unregisterTouchArea(magicBS);
				SFXManager.getInstance().playSound(playerModel.getMagicSound());
				
				magicCooling = new AnimatedSprite(0f,0f,ResourceManager.iconCooling,mVertexBufferObjectManager); 
				magicCooling.setPosition(magicBS.getWidth()/2f,magicBS.getHeight()/2f);
				magicCooling.setSize(magicBS.getWidth(), magicBS.getHeight());
				//魔法冷却时间为9秒，待定
				Arrays.fill(frameDur, 1000);
				magicCooling.animate(frameDur, 0,7,false, new IAnimationListener(){
					@Override
					public void onAnimationStarted(
							AnimatedSprite pAnimatedSprite,
							int pInitialLoopCount) {
					}

					@Override
					public void onAnimationFrameChanged(
							AnimatedSprite pAnimatedSprite, int pOldFrameIndex,
							int pNewFrameIndex) {
					}

					@Override
					public void onAnimationLoopFinished(
							AnimatedSprite pAnimatedSprite,
							int pRemainingLoopCount, int pInitialLoopCount) {
					}

					@Override
					public void onAnimationFinished(
							AnimatedSprite pAnimatedSprite) {
						magicBS.detachChild(magicCooling);
						magicCooling = null;
						if(gameTime > 0){
							registerTouchArea(magicBS);
						}
					}
				});
				magicBS.attachChild(magicCooling);
				//魔法效果
				magicAS = new AnimatedSprite(mCameraWidth/2f,mCameraHeight/2f,playerModel.getMagicTTR(),mVertexBufferObjectManager);
				EntityUtil.setSize("height", 2f / 3f, magicAS);
				magicAS.animate(100,2,new IAnimationListener(){

					@Override
					public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
							int pInitialLoopCount) {
						//bossAS.stopAnimation(2);
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
						if(gameTime > 0){
							detachChild(magicAS);
							magicAS = null;
							//bossAS.animate(frameDur,0,1,true);
							if(bossHP > 0){
								updateHP(2);
							}
						}
					}
				});
				attachChild(magicAS);
				
			}});
		fadableBGRect.attachChild(magicBS);
		
		//武器图标
		Sprite weaponBS = new Sprite(0f,0f,playerModel.getWeaponTR(),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, weaponBS);
		weaponBS.setPosition(magicBS.getX()-(53f/400f)*mCameraWidth, clockBS.getY());
		fadableBGRect.attachChild(weaponBS);

		//魔龙
		tiamatBS = new ButtonSprite(0f,0f,ResourceManager.propsTTR.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, tiamatBS);
		tiamatBS.setPosition(clockBS.getX()+(53f/400f)*mCameraWidth, clockBS.getY());
		tiamatBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(weaponPotionNum > 0){
					unregisterTouchArea(tiamatBS);
					SFXManager.getInstance().playSound("g_drink");
					isAddDPS = true;
					tiamatTime = Constants.ADD_DPS_TIME;
					weaponPotionNum--;
					mGameNumber.updateGoodsNum(Constants.WEAPON_NAME, weaponPotionNum);
					BangYouScreenActivity.writeIntToSharedPreferences(Constants.Prop_BUY+0, weaponPotionNum);
					tiamatCooling = new AnimatedSprite(0f,0f,ResourceManager.iconCooling,mVertexBufferObjectManager); 
					tiamatCooling.setPosition(tiamatBS.getWidth()/2f,tiamatBS.getHeight()/2f);
					tiamatCooling.setSize(tiamatBS.getWidth(), tiamatBS.getHeight());
					//冷却时间为14秒，待定
					Arrays.fill(frameDur, 1500);
					tiamatCooling.animate(frameDur, 0,7,false, new IAnimationListener(){
						@Override
						public void onAnimationStarted(
								AnimatedSprite pAnimatedSprite,
								int pInitialLoopCount) {
						}

						@Override
						public void onAnimationFrameChanged(
								AnimatedSprite pAnimatedSprite, int pOldFrameIndex,
								int pNewFrameIndex) {
						}

						@Override
						public void onAnimationLoopFinished(
								AnimatedSprite pAnimatedSprite,
								int pRemainingLoopCount, int pInitialLoopCount) {
						}

						@Override
						public void onAnimationFinished(
								AnimatedSprite pAnimatedSprite) {
							tiamatBS.detachChild(tiamatCooling);
							tiamatCooling = null;
							if(gameTime > 0){
								registerTouchArea(tiamatBS);
							}
						}
					});
					tiamatBS.attachChild(tiamatCooling);
				}else{
					//数量不足音效
					SFXManager.getInstance().playSound("g_notenough");
				}
			}});
		fadableBGRect.attachChild(tiamatBS);
		mGameNumber.addGoodsNumInGameLevel(Constants.WEAPON_NAME, tiamatBS);
		mGameNumber.updateGoodsNum(Constants.WEAPON_NAME, weaponPotionNum);

		//冰魄
		bingpoBS = new ButtonSprite(0f,0f,ResourceManager.propsTTR.getTextureRegion(1),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f / 10f, bingpoBS);
		bingpoBS.setPosition(tiamatBS.getX()+(53f/400f)*mCameraWidth, clockBS.getY());
		bingpoBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(magicPotionNum > 0){
					unregisterTouchArea(bingpoBS);
					SFXManager.getInstance().playSound("g_drink");
					isAddAOE = true;
					magicPotionNum--;
					mGameNumber.updateGoodsNum(Constants.MAGIC_NAME, magicPotionNum);
					BangYouScreenActivity.writeIntToSharedPreferences(Constants.Prop_BUY+1, magicPotionNum);
					bingpoCooling = new Sprite(0f,0f,ResourceManager.iconCooling.getTextureRegion(0),mVertexBufferObjectManager);
					bingpoCooling.setSize(bingpoBS.getWidth(), bingpoBS.getHeight());
					bingpoCooling.setPosition(bingpoBS.getWidth()/2f, bingpoBS.getHeight()/2f);
					bingpoBS.attachChild(bingpoCooling);
					//可以增加一些动画效果
				}else{
					//数量不足音效
					SFXManager.getInstance().playSound("g_notenough");
				}
			}});
		fadableBGRect.attachChild(bingpoBS);
		mGameNumber.addGoodsNumInGameLevel(Constants.MAGIC_NAME, bingpoBS);
		mGameNumber.updateGoodsNum(Constants.MAGIC_NAME, magicPotionNum);
		
		//游戏开始倒计时动画
		long [] frameDur = new long[3];
		Arrays.fill(frameDur, 1000);
		clockTimeAS = new AnimatedSprite(0f,0f,ResourceManager.clockTime,mVertexBufferObjectManager);
		clockTimeAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		EntityUtil.setSize("height", 1f / 4f, clockTimeAS);
		clockTimeAS.animate(frameDur,0,2,0,new IAnimationListener(){

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
				SFXManager.getInstance().loadSounds(sounds, ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity());
				SFXManager.getInstance().loadSound(playerModel.getMagicSound(), ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity());
			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {
				SFXManager.getInstance().playSound("a_CountDown");
			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {
			}

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				SFXManager.getInstance().playSound("g_go");
				SFXManager.getInstance().playMusic(musics);
				detachChild(clockTimeAS);
				clockTimeAS = null;
				enableButtons();
			}
		});
		attachChild(clockTimeAS);
	}

	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(false);
		ResourceManager.getInstance().engine.setTouchController(new SingleTouchController());
		System.out.println("GameLevel资源onUnloadScene");
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
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
				SFXManager.getInstance().unloadAllSound(sounds);
			}});
	}

	@Override
	public void onShowScene() {
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(true);
		ResourceManager.getInstance().engine.setTouchController(new MultiTouchController());
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

		@Override
		public void onUpdate(float pSecondsElapsed) {
			//倒计时
			gameTime-=pSecondsElapsed;
			if(isAddDPS){
				tiamatTime-=pSecondsElapsed;
				//System.out.println("魔龙增益时间 --> "+ tiamatTime);
				if(tiamatTime <= 0f){
					isAddDPS = false;
				}
			}
			if(gameTime<=0) {
				mGameTime.adjustTime(0f);
				disableButtons(true);
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
				mPhysicsHandler.setVelocityY(Constants.BOSS_VELOCITY);
			} else if(bossAS.getY() + (bossAS.getHeight() * 0.5f) > xue1Sprite.getY() - xue1Sprite.getHeight()/2f) {
				mPhysicsHandler.setVelocityY(-Constants.BOSS_VELOCITY);
			}
			if(bossAS.getX() - bossAS.getWidth()/2f < 2f) {
				mPhysicsHandler.setVelocityX(Constants.BOSS_VELOCITY);
			} else if(bossAS.getX() + (bossAS.getWidth() * 0.5f) > mCameraWidth - 2f) {
				mPhysicsHandler.setVelocityX(-Constants.BOSS_VELOCITY);
			}
			
			//统计得分
			mGameNumber.addScore(mScore);
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
		SFXManager.getInstance().pauseMusic();
		disableButtons(true);
	}
	
	/**
	 * 继续游戏
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public void onResumeGameLevel(){
		SFXManager.getInstance().playMusic();
//		setIgnoreUpdate(false);
//		clockTimeAS = new AnimatedSprite(0f,0f,ResourceManager.clockTime,mVertexBufferObjectManager);
//		clockTimeAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
//		EntityUtil.setSize("height", 1f / 4f, clockTimeAS);
//		clockTimeAS.animate(1000,0,new IAnimationListener(){
//
//			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
//					int pInitialLoopCount) {
//			}
//
//			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
//					int pOldFrameIndex, int pNewFrameIndex) {
//			}
//
//			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
//					int pRemainingLoopCount, int pInitialLoopCount) {
//			}
//
//			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
//				SFXManager.getInstance().playSound("g_go");
//				detachChild(clockTimeAS);
//				clockTimeAS = null;
//				enableButtons();
//			}
//		});
//		attachChild(clockTimeAS);
		
		enableButtons();
	}
	
	/**
	 * 游戏胜利
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void gameWin(){
		detachChild(magicAS);
		SFXManager.getInstance().stopSound(playerModel.getMagicSound());
		System.out.println("游戏胜利111111111");
		SFXManager.getInstance().stopMusic();
		updateGameStatDPSNum();
		int bossNumInSP = BangYouScreenActivity.getIntFromSharedPreferences(Constants.SHARED_PREFS_THEME_MXD);
		if(bossModel.getBossLevel() > bossNumInSP){
			BangYouScreenActivity.writeIntToSharedPreferences(Constants.SHARED_PREFS_THEME_MXD, bossModel.getBossLevel());
		}
		AnimatedSprite bigBang = new AnimatedSprite(0f,0f,ResourceManager.bigBang,mVertexBufferObjectManager);
		bigBang.setPosition(bossAS.getX(), bossAS.getY());
		EntityUtil.setSize("height", 0.5f, bigBang);
		bigBang.animate(100,3,new IAnimationListener(){

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
				SFXManager.getInstance().playSound("g_bomb",2);
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
				//setIgnoreUpdate(true);
				SceneManager.getInstance().showLayer(GameWinLayer.getInstance(mScore,Math.round(gameTime),allBossHP), false, false, false);
				SFXManager.getInstance().playSound("g_win");
			}
		});
		attachChild(bigBang);
	}
	
	/**
	 * 游戏失败
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void gameFail(){
		SFXManager.getInstance().stopMusic();
		updateGameStatDPSNum();
//		AlphaModifier a = new AlphaModifier(3,0.1f,0.5f,new IEntityModifier.IEntityModifierListener(){
//			public void onModifierStarted(IModifier<IEntity> pModifier,
//					IEntity pItem) {}
//			public void onModifierFinished(IModifier<IEntity> pModifier,
//					IEntity pItem) {
//				SceneManager.getInstance().showLayer(GameFailLayer.getInstance(), false, false, false);
//				SFXManager.getInstance().playSound("g_fail");
//			}
//		});
//		Rectangle r = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight, mVertexBufferObjectManager);
//		r.setColor(255, 255, 255, 0.1f);
//		r.setPosition(mCameraWidth/2f, mCameraHeight/2f);
//		r.registerEntityModifier(a);
//		attachChild(r);
		SceneManager.getInstance().showLayer(GameFailLayer.getInstance(mScore,Math.round(gameTime), allBossHP - bossHP), false, false, false);
		SFXManager.getInstance().playSound("g_fail");
	}
	
	/**
	 * 更新BOSS血量和得分
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	private void updateHP(int type){
		System.out.println("BOSSHP之前 --> "+ bossHP);
		mScore++;
		clickNum++;
		if(type == 1){
			System.out.println("物理伤害系数 --> "+ dpsXS);
			int dps = countPlayerDPS();
			if(dps < 0){
				dps = 0;
			}
			System.out.println("DPS --> "+ dps);
			//mScore = mScore + dps;
			xue3S = xue2Sprite.getWidth()-(xue2Sprite.getWidth()*dps)/bossHP;
			xue3P = xue3S/2f;
			bossHP = bossHP - dps;
			System.out.println("BOSSHP之后 --> "+ bossHP);
		}else{
			System.out.println("魔法伤害系数 --> "+ aoeXS);
			int aoe = countPlayerAOE();
			if(aoe < 0){
				aoe = 0;
			}
			System.out.println("AOE --> "+ aoe);
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
		mGameNumber.addScore(mScore);
		if(bossHP <= 0 && gameTime > 0){
			bossAS.unregisterUpdateHandler(mPhysicsHandler);
			disableButtons(false);
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
		if(isAddDPS){
			playerDPS = playerDPS*(1f + Constants.ADD_DPS);
		}
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
		if(isAddAOE){
			playerAOE = playerAOE*(1f+Constants.ADD_AOE);
		}
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
	
	private void disableButtons(boolean type){
		setIgnoreUpdate(type);
		unregisterUpdateHandler(gameRunTimer);
		unregisterTouchArea(greenButtonBS);
		unregisterTouchArea(redButtonBS);
		unregisterTouchArea(bingpoBS);
		unregisterTouchArea(tiamatBS);
		unregisterTouchArea(magicBS);
		unregisterTouchArea(clockBS);
	}
	
	private void enableButtons(){
		setIgnoreUpdate(false);
		registerTouchArea(bingpoBS);
		registerTouchArea(tiamatBS);
		registerTouchArea(magicBS);
		registerTouchArea(clockBS);
		registerTouchArea(redButtonBS);
		registerTouchArea(greenButtonBS);
		registerUpdateHandler(gameRunTimer);
	}
	
	private void updateGameStatDPSNum(){
		int num = BangYouScreenActivity.getIntFromSharedPreferences(Constants.ALL_DPS) + clickNum;
		BangYouScreenActivity.writeIntToSharedPreferences(Constants.ALL_DPS, num);
	}

}
