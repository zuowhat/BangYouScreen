package org.game.bangyouscreen.scene;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleAtModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.ScreenCapture;
import org.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.input.touch.controller.SingleTouchController;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.FileUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.share.sinaSDK.SinaWeiboUtil;
import org.game.bangyouscreen.share.tencentSDK.TencentWeiboUtil;
import org.game.bangyouscreen.util.Constants;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameNumberUtil;
import org.game.bangyouscreen.util.GameTimer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class FingerScene extends ManagedScene{

	private static final FingerScene INSTANCE = new FingerScene();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	public FingerScene() {}
	public static FingerScene getInstance(){
		return INSTANCE;
	}
	private AnimatedSprite submarineAS;
	private float upHeight = 0f;
	private GameTimer mGameTime;
	private float gameTime = Constants.GAMETIME_INIT;//初始游戏时间
	private ButtonSprite greenButtonBS;
	private ButtonSprite redButtonBS;
	private boolean mTenSeconds = false;
	private GameNumberUtil mGameNumber;
	private AnimatedSprite clockTimeAS;
	private String[] sounds = {"g_win","g_go","a_CountDown","f_over"};
	private String[] musics = {"zhan","zhu","ming","shi"};
	private Rectangle mRectangle;
	private int currentHighestScore;
	public static boolean isOver;
	private Rectangle fadableBGRect;
	private Bitmap bitmap;
	private Sprite fingerScoreBG;
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadScene() {
		SFXManager.getInstance().stopMusic();
		System.out.println("FingerScene---->onLoadScene");
		isOver = false;
		mGameNumber = new GameNumberUtil();
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(true);
		ResourceManager.getInstance().engine.setTouchController(new MultiTouchController());
		Sprite backgroundSprite = new Sprite(0f,0f, ResourceManager.fingerBG[Math.round((float)Math.random()*2)],mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f, backgroundSprite);
		if(mCameraHeight > backgroundSprite.getHeight()){
			backgroundSprite.setHeight(mCameraHeight);
		}
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-5000);
		attachChild(backgroundSprite);
		
		
		mGameTime = new GameTimer(this);
		mGameTime.addToLayer(this);
		
		mRectangle = new Rectangle(0f,0f,mCameraWidth/2f,mCameraHeight/4f,mVertexBufferObjectManager);
		mRectangle.setPosition(mCameraWidth/2f, mRectangle.getHeight()/2f);
		mRectangle.setAlpha(0f);
		attachChild(mRectangle);
		//潜水艇
		submarineAS = new AnimatedSprite(0f,0f,ResourceManager.submarineTTR,mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("height", 2f/3f, submarineAS,mRectangle);
		submarineAS.setPosition(mRectangle.getWidth()/2f, submarineAS.getHeight()/2f);
		//long[] frameDur = new long[2];
		//Arrays.fill(frameDur, 300);
		submarineAS.animate(300,true);
		mRectangle.attachChild(submarineAS);
		
		//实时上升高度
		Sprite mSprite = new Sprite(0f,0f,ResourceManager.mPic,mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("height", 4f/10f, mSprite, mRectangle);
		mSprite.setPosition(submarineAS.getX()-submarineAS.getWidth()/2f-mSprite.getWidth()/2f, submarineAS.getY());
		mRectangle.attachChild(mSprite);
		mGameNumber.upHeightNum(mRectangle,mSprite,0);
		
		//左边按钮
		greenButtonBS = new ButtonSprite(0f,0f,ResourceManager.greenButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, greenButtonBS);
		greenButtonBS.setPosition(greenButtonBS.getWidth() / 2f, greenButtonBS.getHeight() / 2f);
		greenButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					//SFXManager.getInstance().playSound("g_button");
					upHeight = upHeight + 1f;
					mGameNumber.updateUpHighNum(Math.round(upHeight));
					mRectangle.setY(mRectangle.getHeight()/2f+(4f*mCameraHeight/5f-mRectangle.getHeight()/2f)*upHeight/360f);
			}});
		attachChild(greenButtonBS);
		
		
		//右边按钮
		redButtonBS = new ButtonSprite(0f,0f,ResourceManager.redButtonTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 4f, redButtonBS);
		redButtonBS.setPosition(mCameraWidth - redButtonBS.getWidth() / 2f, redButtonBS.getHeight() / 2f);
		redButtonBS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					//SFXManager.getInstance().playSound("g_button");
					upHeight = upHeight + 1f;
					mGameNumber.updateUpHighNum(Math.round(upHeight));
					mRectangle.setY(mRectangle.getHeight()/2f+(4f*mCameraHeight/5f-mRectangle.getHeight()/2f)*upHeight/360f);
			}});
		attachChild(redButtonBS);
		
		Sprite highFont = new Sprite(0f,0f,ResourceManager.highScoreFont,mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f/5f, highFont);
		highFont.setPosition(5f+highFont.getWidth()/2f, mCameraHeight-highFont.getHeight());
		attachChild(highFont);
		//当前最高得分
		currentHighestScore = BangYouScreenActivity.getIntFromSharedPreferences(Constants.FINGER_HIGHESTSCORE);
		AnimatedSprite[] highestScoreAS = mGameNumber.fingerHighestScore(this, highFont, currentHighestScore);
		Sprite highestScoreM = new Sprite(0f,0f,ResourceManager.mPic,mVertexBufferObjectManager);
		highestScoreM.setSize(highestScoreAS[2].getHeight()*4f/7f, highestScoreAS[2].getHeight()*2f/3f);
		highestScoreM.setPosition(highestScoreAS[2].getX() + highestScoreAS[2].getWidth()*2f, highestScoreAS[2].getY());
		attachChild(highestScoreM);
		
		//游戏开始倒计时动画
		long [] frameDur1 = new long[3];
		Arrays.fill(frameDur1, 1000);
		clockTimeAS = new AnimatedSprite(0f,0f,ResourceManager.clockTime,mVertexBufferObjectManager);
		clockTimeAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		EntityUtil.setSize("height", 1f / 4f, clockTimeAS);
		clockTimeAS.animate(frameDur1,0,2,0,new IAnimationListener(){

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
				SFXManager.getInstance().loadSounds(sounds, ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity());
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
				blisterAnimation();
				registerTouchArea(greenButtonBS);
				registerTouchArea(redButtonBS);
				registerUpdateHandler(gameRunTimer);
			}
		});
		attachChild(clockTimeAS);
	}
	
	/**
	 * 实时更新游戏状态
	 * @author zuowhat 2014-2-20
	 * @since 1.0
	 */
	private IUpdateHandler gameRunTimer = new IUpdateHandler() {

		@Override
		public void onUpdate(float pSecondsElapsed) {
			//倒计时
			gameTime-=pSecondsElapsed;
			if(gameTime<=0) {
				mGameTime.adjustTime(0f);
				unregisterTouchArea(greenButtonBS);
				unregisterTouchArea(redButtonBS);
				mGameTime.mDigitsSprite[3].clearEntityModifiers();
				mGameTime.mDigitsSprite[2].clearEntityModifiers();
				mGameTime.mDigitsSprite[3].setScale(1.0F);
				mGameTime.mDigitsSprite[2].setScale(1.0F);
				gameOver();
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
			
		}

		@Override
		public void reset() {}
	};
	
	/**
	 * 游戏结束
	 * @author zuowhat 2014-2-20
	 * @since 1.0
	 */
	private void gameOver(){
		SFXManager.getInstance().stopMusic();
		SFXManager.getInstance().playSound("f_over");
		unregisterUpdateHandler(gameRunTimer);
		
		isOver = true;
		//父背景变成半透明
		fadableBGRect = new Rectangle(0f, 0f,mCameraWidth,mCameraHeight, mVertexBufferObjectManager);
		fadableBGRect.setPosition(mCameraWidth/2f, mCameraHeight/2f);
		fadableBGRect.setColor(0f, 0f, 0f, 0.6f);
		//fadableBGRect.setAlpha(0f);
		attachChild(fadableBGRect);
		//得分背景
		fingerScoreBG = new Sprite(0f,0f,ResourceManager.fingerScoreBG,mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f/2f, fingerScoreBG);
		fingerScoreBG.setPosition(mCameraWidth/2f, mCameraHeight+fingerScoreBG.getHeight());
		fadableBGRect.attachChild(fingerScoreBG);
		
		
		fingerScoreBG.registerEntityModifier(new MoveYModifier(0.5f, fingerScoreBG.getY(), fadableBGRect.getHeight()*2f/3f, new IEntityModifierListener(){

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,IEntity pItem) {
				
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				if(upHeight > currentHighestScore){
					BangYouScreenActivity.writeIntToSharedPreferences(Constants.FINGER_HIGHESTSCORE, Math.round(upHeight));
					//打破记录后的效果
					Sprite highestPic = new Sprite(pItem.getX()+pItem.getWidth()/2f,pItem.getY()+pItem.getHeight()/2f-(pItem.getHeight()*40f/138f),ResourceManager.highScorePic,mVertexBufferObjectManager);
					EntityUtil.setSizeInParent("height", 8f/27f, highestPic, fadableBGRect);
					fadableBGRect.attachChild(highestPic);
					ScaleAtModifier highestPicScale = new ScaleAtModifier(0.5f, 25f, 1f, 0.5f, 0.5f);//实体缩放
					FadeInModifier highestPicFadeIn = new FadeInModifier(0.5f);//在0.5秒内改变透明度由0f变为1f
					ParallelEntityModifier highestPicParalle = new ParallelEntityModifier(highestPicScale,highestPicFadeIn);//同时执行修饰符
					highestPic.registerEntityModifier(highestPicParalle);
					
					//星星效果(待写)
					
				
				}
				//金币背景
				Sprite fingerGoldBG = new Sprite(0f,0f,ResourceManager.fingerGoldBG,mVertexBufferObjectManager);
				EntityUtil.setSize("width", 1f/2f, fingerGoldBG);
				fingerGoldBG.setPosition(-fingerGoldBG.getWidth(), pItem.getY()-pItem.getHeight()/2f-5f-fingerGoldBG.getHeight()/2f);
				fadableBGRect.attachChild(fingerGoldBG);
				
				AnimatedSprite[] goldNumAS = mGameNumber.fingerOverGold(fingerGoldBG, Math.round(upHeight/2f));
				Sprite gLayerSprite = new Sprite(0f,0f,ResourceManager.gameGold,mVertexBufferObjectManager);
				gLayerSprite.setSize(goldNumAS[2].getHeight(), goldNumAS[2].getHeight());
				gLayerSprite.setPosition(goldNumAS[2].getX() + goldNumAS[2].getWidth()*2f, goldNumAS[2].getY());
				fingerGoldBG.attachChild(gLayerSprite);
				
				Sprite addSprite = new Sprite(0f,0f,ResourceManager.numberTTR.getTextureRegion(11),mVertexBufferObjectManager);
				EntityUtil.setSizeInParent("height", 4f/5f, addSprite, fingerGoldBG);
				addSprite.setPosition(goldNumAS[0].getX() - goldNumAS[0].getWidth()*2f, goldNumAS[2].getY());
				fingerGoldBG.attachChild(addSprite);
				
				AnimatedSprite[] scoreNumAS = mGameNumber.fingerOverScore(pItem, Math.round(upHeight));
				Sprite mLayerSprite = new Sprite(0f,0f,ResourceManager.mPic,mVertexBufferObjectManager);
				mLayerSprite.setSize(scoreNumAS[2].getHeight(), scoreNumAS[2].getHeight());
				mLayerSprite.setPosition(scoreNumAS[2].getX() + scoreNumAS[2].getWidth()*2f, scoreNumAS[2].getY());
				pItem.attachChild(mLayerSprite);
				fingerGoldBG.registerEntityModifier(new MoveXModifier(0.5f,fingerGoldBG.getX(),fadableBGRect.getWidth()/2f,new IEntityModifierListener(){

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,IEntity pItem) {
						
					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						Sprite fingerButtonBG = new Sprite(0f,0f,ResourceManager.fingerGoldBG,mVertexBufferObjectManager);
						EntityUtil.setSize("width", 1f/2f, fingerButtonBG);
						fingerButtonBG.setHeight(fingerButtonBG.getHeight()*9f/4f);
						fingerButtonBG.setPosition(-fingerButtonBG.getWidth(), pItem.getY()-pItem.getHeight()/2f-5f-fingerButtonBG.getHeight()/2f);
						fadableBGRect.attachChild(fingerButtonBG);
						
						//返回菜单
						ButtonSprite backBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(2),mVertexBufferObjectManager);
						EntityUtil.setSizeInParent("height", 2f/5f, backBS, fingerButtonBG);
						backBS.setPosition(fingerButtonBG.getWidth()/4f, fingerButtonBG.getHeight()/5f+5f);
						fingerButtonBG.attachChild(backBS);
						registerTouchArea(backBS);
						backBS.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(ButtonSprite pButtonSprite,float pTouchAreaLocalX,
									float pTouchAreaLocalY) {
								SFXManager.getInstance().playSound("a_click");
								SceneManager.getInstance().showScene(MainMenuScene.getInstance());
							}
						});
						
						//重新开始
						ButtonSprite restartBS = new ButtonSprite(0f,0f,ResourceManager.gamePauseMenu.getTextureRegion(1),mVertexBufferObjectManager);
						EntityUtil.setSizeInParent("height", 2f/5f, restartBS, fingerButtonBG);
						restartBS.setPosition(fingerButtonBG.getWidth()*3f/4f, backBS.getY());
						fingerButtonBG.attachChild(restartBS);
						registerTouchArea(restartBS);
						restartBS.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(ButtonSprite pButtonSprite,float pTouchAreaLocalX,
									float pTouchAreaLocalY) {
								SFXManager.getInstance().playSound("a_click");
								SceneManager.getInstance().showScene(new FingerScene());
							}
						});
						
						//新浪微博
						ButtonSprite sinaLogo = new ButtonSprite(0f,0f,ResourceManager.sinaLogo,mVertexBufferObjectManager);
						EntityUtil.setSizeInParent("height", 2f/5f, sinaLogo, fingerButtonBG);
						sinaLogo.setPosition(backBS.getX(), backBS.getY()+backBS.getHeight()/2f+5f+sinaLogo.getHeight()/2f);
						fingerButtonBG.attachChild(sinaLogo);
						sinaLogo.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
									float pTouchAreaLocalY) {
								SinaWeiboUtil.getInstance().showShare(bitmap);
							}
						});
						registerTouchArea(sinaLogo);
						
						//腾讯微博
						ButtonSprite tencentLogo = new ButtonSprite(0f,0f,ResourceManager.tencentLogo,mVertexBufferObjectManager);
						//EntityUtil.setSizeInParent("height", 2f/5f, tencentLogo, fingerButtonBG);
						tencentLogo.setSize(sinaLogo.getWidth(), sinaLogo.getHeight());
						tencentLogo.setPosition(restartBS.getX(),sinaLogo.getY());
						fingerButtonBG.attachChild(tencentLogo);
						tencentLogo.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
									float pTouchAreaLocalY) {
								TencentWeiboUtil.getInstance().authToken(bitmap);
							}
						});
						registerTouchArea(tencentLogo);
						
						fingerButtonBG.registerEntityModifier(new MoveXModifier(0.5f,fingerButtonBG.getX(),fadableBGRect.getWidth()/2f,new IEntityModifierListener(){

							@Override
							public void onModifierStarted(
									IModifier<IEntity> pModifier, IEntity pItem) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onModifierFinished(
									IModifier<IEntity> pModifier, IEntity pItem) {
								//开始截屏
								screenShot();
							}},EaseElasticInOut.getInstance()));
						
					}
					
				},EaseElasticInOut.getInstance()));
			}}, EaseElasticInOut.getInstance()));
	}
	
	/**
	 * 继续游戏
	 * @author zuowhat 2014-2-20
	 * @since 1.0
	 */
	public void onResumeGameLevel(){
		SFXManager.getInstance().playMusic();
		setIgnoreUpdate(false);
		registerTouchArea(greenButtonBS);
		registerTouchArea(redButtonBS);
		registerUpdateHandler(gameRunTimer);
	}
	
	/**
	 * 暂停游戏
	 * @author zuowhat 2014-2-20
	 * @since 1.0
	 */
	public void onPauseGameLevel(){
		SFXManager.getInstance().pauseMusic();
		setIgnoreUpdate(true);
		unregisterTouchArea(greenButtonBS);
		unregisterTouchArea(redButtonBS);
		unregisterUpdateHandler(gameRunTimer);
	}
	
	/**
	 * 水泡动画
	 * @author zuowhat 2014-2-20
	 * @since 1.0
	 */
	public void blisterAnimation(){
		AnimatedSprite b1 = new AnimatedSprite(0f,0f,ResourceManager.bubbleTTR,mVertexBufferObjectManager);
		EntityUtil.setSize("width", 1f/10f, b1);
		float randomX = (float)(Math.random()*((mCameraWidth-redButtonBS.getWidth())-redButtonBS.getWidth()))+redButtonBS.getWidth();
		b1.setPosition(randomX, b1.getHeight()/2f);
		b1.animate(200,false, new IAnimationListener(){

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
					//SFXManager.getInstance().playSound("g_countdown",3);
			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {
				if(pNewFrameIndex == 4){
					blisterAnimation();
				}
			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {
			}

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				//detachChild(pAnimatedSprite);
				//pAnimatedSprite = null;
				pAnimatedSprite.setVisible(false);
			}
		});
		attachChild(b1);
	}

	@Override
	public void onShowScene() {
		System.out.println("FingerScene---->onShowScene");
		
	}

	@Override
	public void onHideScene() {
		System.out.println("FingerScene---->onHideScene");
		
	}

	@Override
	public void onUnloadScene() {
		System.out.println("FingerScene---->onUnloadScene");
		ResourceManager.getInstance().engine.getEngineOptions().getTouchOptions().setNeedsMultiTouch(false);
		ResourceManager.getInstance().engine.setTouchController(new SingleTouchController());
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
	
	private void screenShot(){
		ScreenCapture screenCapture = new ScreenCapture();
		attachChild(screenCapture);
		final String path = FileUtils.getAbsolutePathOnExternalStorage(ResourceManager.getInstance().activity, Constants.SCREENCAPTURE);
		int viewWidth = ResourceManager.getInstance().screenWidth;
	    int viewHeight = ResourceManager.getInstance().screenHeight;
		int cx = Math.round((fingerScoreBG.getX()-fingerScoreBG.getWidth()/2f)*viewWidth/mCameraWidth);
		int cy = Math.round((fingerScoreBG.getY()-fingerScoreBG.getHeight()/2f)*viewHeight/mCameraHeight);
		int cw = Math.round(fingerScoreBG.getWidth()*viewWidth/mCameraWidth);
		int ch = Math.round(fingerScoreBG.getHeight()*viewHeight/mCameraHeight);
		
		screenCapture.capture(cx, cy, cw, ch, path, new IScreenCaptureCallback() {  
		      @Override  
		      public void onScreenCaptured(final String pFilePath) {  
		        System.out.println("success --> "+pFilePath);
		        bitmap = BitmapFactory.decodeFile(path);
		        try {
					FileOutputStream fos = new FileOutputStream(path);
					bitmap.compress(CompressFormat.JPEG, 50, fos);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		      }  
		  
		      @Override  
		      public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {  
		    	  System.out.println("failed --> "+pFilePath);
		      }  
		     });
		//GLES20.glReadPixels(0, 0, 800, 400 , GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, sourceBuffer); 
	}

}
