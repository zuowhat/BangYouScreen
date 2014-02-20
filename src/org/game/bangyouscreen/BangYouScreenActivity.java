package org.game.bangyouscreen;


import net.youmi.android.offers.PointsChangeNotify;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.layer.FingerPauseLayer;
import org.game.bangyouscreen.layer.GamePauseLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.FingerScene;
import org.game.bangyouscreen.scene.HelpScene;
import org.game.bangyouscreen.scene.MainMenuScene;
import org.game.bangyouscreen.scene.ShopScene;
import org.game.bangyouscreen.scene.SplashScreen;
import org.game.bangyouscreen.util.DataConstant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View.MeasureSpec;

import com.zuo.what.uti.JMPManager;


public class BangYouScreenActivity extends BaseGameActivity implements PointsChangeNotify{
	
	public static boolean getBooleanFromSharedPreferences(final String pStr) {
		return ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0)
				.getBoolean(pStr, false);
	}

	public static int getIntFromSharedPreferences(final String pStr) {
		return ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0).getInt(pStr, 0);
	}
	
	//第一次进入游戏时获取初始化金币
	public static int getGoldFromSharedPreferences() {
		return ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0).getInt(DataConstant.MY_GOLD, DataConstant.GOLD_INIT);
	}
	
	//第一次进入游戏时获取初始化武器
	public static int getWeaponFromSharedPreferences() {
		return ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0).getInt(DataConstant.CURRENT_WEAPON, DataConstant.WEAPON_INIT);
	}
	
	//第一次进入游戏时获取初始化魔法
	public static int getMagicFromSharedPreferences() {
		return ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0).getInt(DataConstant.CURRENT_MAGIC, DataConstant.MAGIC_INIT);
	}

	public static void writeBooleanToSharedPreferences(final String pStr,
			final boolean pValue) {
		ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0).edit()
				.putBoolean(pStr, pValue).apply();
	}

	public static void writeIntToSharedPreferences(final String pStr,
			final int pValue) {
		ResourceManager.getActivity()
				.getSharedPreferences(DataConstant.SHARED_PREFS_MAIN, 0).edit()
				.putInt(pStr, pValue).apply();
	}
	
	//开发时使用的手机屏幕分辨率
	static float DESIGN_WINDOW_WIDTH_PIXELS = 800f;
	static float DESIGN_WINDOW_HEIGHT_PIXELS = 480f;
	
	//开发时使用的手机屏幕物理尺寸, 分辨率除以dpi
	static float DESIGN_WINDOW_WIDTH_INCHES = 3.33333333f;
	static float DESIGN_WINDOW_HEIGHT_INCHES = 2.0f;
	
	//定义最大和最小屏幕分辨率
	static float MIN_WIDTH_PIXELS = 320f, MIN_HEIGHT_PIXELS = 240f;
	static float MAX_WIDTH_PIXELS = 1600f, MAX_HEIGHT_PIXELS = 960f;
	
	public float cameraWidth;
	public float cameraHeight;
	public float actualWindowWidthInches;
	public float actualWindowHeightInches;
	public SmoothCamera mCamera;

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) { 
		return new FixedStepEngine(pEngineOptions, 60); 
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		//System.out.println("onCreateEngineOptions");
		//重写ResolutionPolicy中的onMeasure()方法来设置镜头的大小
		//这种方式比使用DisplayMetrics.getWidth方法好，因为它使用window来代替display
		//This should also be better for if  the game is placed in a layout where simply measuring the display would give entirely wrong results.
		FillResolutionPolicy EngineFillResolutionPolicy = new FillResolutionPolicy() {
			
			@Override
			public void onMeasure(final IResolutionPolicy.Callback pResolutionPolicyCallback, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
				super.onMeasure(pResolutionPolicyCallback, pWidthMeasureSpec, pHeightMeasureSpec);
				
				final int measuredWidth = MeasureSpec.getSize(pWidthMeasureSpec);
				final int measuredHeight = MeasureSpec.getSize(pHeightMeasureSpec);
				
				//获取设备窗口物理大小，单位英寸
				actualWindowWidthInches = measuredWidth / getResources().getDisplayMetrics().xdpi;
				actualWindowHeightInches = measuredHeight / getResources().getDisplayMetrics().ydpi;
				
				// 初始化镜头的宽度
				float actualScaledWidthInPixels = DESIGN_WINDOW_WIDTH_PIXELS * (actualWindowWidthInches / DESIGN_WINDOW_WIDTH_INCHES);
				float boundScaledWidthInPixels = Math.round(Math.max(Math.min(actualScaledWidthInPixels,MAX_WIDTH_PIXELS),MIN_WIDTH_PIXELS));
				
				//根据设备的宽度和 高度/宽度比来获取镜头的高度
				float boundScaledHeightInPixels = boundScaledWidthInPixels * (actualWindowHeightInches / actualWindowWidthInches);
				// 如果高度超出界限，则重新设置宽度值来匹配它
				if(boundScaledHeightInPixels > MAX_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MAX_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				} else if(boundScaledHeightInPixels < MIN_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MIN_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				}
				cameraHeight = boundScaledHeightInPixels;
				cameraWidth = boundScaledWidthInPixels;
				mCamera.set(0f, 0f, cameraWidth, cameraHeight);
			}
		};
		mCamera = new SmoothCamera(0, 0, 320, 240, 4000f, 2000f, 0.5f);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, EngineFillResolutionPolicy, mCamera);

		// Enable sounds.
		engineOptions.getAudioOptions().setNeedsSound(true);
		// Enable music.
		engineOptions.getAudioOptions().setNeedsMusic(true);
		// Turn on Dithering to smooth texture gradients.打开抖动平滑纹理梯度。
		engineOptions.getRenderOptions().setDithering(true);
		// Turn on MultiSampling to smooth the alias of hard-edge elements.
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		// Set the Wake Lock options to prevent the engine from dumping textures when focus changes.
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		//启动多点触控
		//engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		JMPManager manager = new JMPManager ();
	    manager.startService(this,1);
		super.onCreate(pSavedInstanceState);
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback){
		
		ResourceManager.setup(this,  (FixedStepEngine)this.getEngine(), this.getApplicationContext(), 
				cameraWidth, cameraHeight, cameraWidth/DESIGN_WINDOW_WIDTH_PIXELS, cameraHeight/DESIGN_WINDOW_HEIGHT_PIXELS);
		//System.out.println("onCreateResources");
		
		ResourceManager.getInstance().initAdResources();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback){
		//this.mEngine.registerUpdateHandler(new FPSLogger());
		//this.mEngine.registerUpdateHandler(new MemoryLogger());
		//System.out.println("onCreateScene");
		SceneManager.getInstance().showScene(new SplashScreen());
		System.out.println("程序启动");
		
		pOnCreateSceneCallback.onCreateSceneFinished(mEngine.getScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected synchronized void onResume() {
		System.out.println("程序重新启动");
		super.onResume();
		//System.gc();
		if(this.isGameLoaded()){
			//SFXManager.getInstance().playMusic("mainMusic");
		}
	}
	
	protected void onPause() {
		System.out.println("程序暂停");
		super.onPause();
		if (this.isGameLoaded()) {
			//SFXManager.getInstance().pauseMusic("mainMusic");
		}
	}
	
	protected void onDestroy() {
		System.out.println("游戏关闭");
		// 释放资源，原finalize()方法名修改为close()
		//AppConnect.getInstance(this).close();
		ResourceManager.getInstance().unloadAdResources();
		super.onDestroy();
		System.exit(0);
	}
	
	  public void onBackPressed() {
		  SFXManager.getInstance().playSound("a_click");
		  if (ResourceManager.getInstance().engine != null) {
			  if (SceneManager.getInstance().mIsLayerShown && SceneManager.getInstance().mCurrentLayer.getClass()
					  .equals(GamePauseLayer.class)) {
				  SceneManager.getInstance().mCurrentLayer.onHideLayer();
				  ((GameLevel) SceneManager.getInstance().mCurrentScene).onResumeGameLevel();
			  }else if(SceneManager.getInstance().mCurrentScene.getClass().equals(GameLevel.class)){
				  if(!SceneManager.getInstance().mIsLayerShown){
					  ((GameLevel) SceneManager.getInstance().mCurrentScene).onPauseGameLevel();
					  SceneManager.getInstance().showLayer(GamePauseLayer.getInstance(), false, false, false);
				  }
			  }else if(SceneManager.getInstance().mCurrentScene.getClass().equals(FingerScene.class)){
				  if(!SceneManager.getInstance().mIsLayerShown){
					  ((FingerScene) SceneManager.getInstance().mCurrentScene).onPauseGameLevel();
					  SceneManager.getInstance().showLayer(FingerPauseLayer.getInstance(), false, false, false);
				  }
			  }else if (SceneManager.getInstance().mIsLayerShown && SceneManager.getInstance().mCurrentLayer.getClass()
					  .equals(FingerPauseLayer.class)) {
				  SceneManager.getInstance().mCurrentLayer.onHideLayer();
				  ((FingerScene) SceneManager.getInstance().mCurrentScene).onResumeGameLevel();
			  }else if(SceneManager.getInstance().mCurrentScene.getClass().equals(ShopScene.class)){
				  SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			  }else if(SceneManager.getInstance().mCurrentScene.getClass().equals(MainMenuScene.class)){
				  //SpotManager.getInstance(this).showSpotAds(this);
				  AlertDialog.Builder exitBuilder = new AlertDialog.Builder(this);
					exitBuilder.setMessage("确定要退出吗?");
					exitBuilder.setCancelable(false); //返回键是否可以关闭对话框
					exitBuilder.setPositiveButton("是", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//playSoundPool.playSound(1);
							System.exit(0);
						}
					});
					
					exitBuilder.setNegativeButton("否", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//playSoundPool.playSound(1);
							dialog.cancel();
						}
					});
					exitBuilder.show();
					exitBuilder.create();
				  
				  //System.exit(0);
			  }else if(SceneManager.getInstance().mCurrentScene.getClass().equals(HelpScene.class)){
				  SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			  }
		  }
	  }

	public void onPointBalanceChange(int arg0) {
		System.out.println("积分变动");
		ShopScene.getInstance().myGold = arg0;
		ShopScene.getInstance().mGameNumber.addGoldToLayer(ShopScene.getInstance().propTopBG, ShopScene.getInstance().myGold);
		if(ShopScene.getInstance().myApps != -1 && ShopScene.getInstance().myApps != arg0){
			int appNum = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_APPS) + 1;
			BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.ALL_APPS, appNum);
			System.out.println("安装应用数 +1");
		}
		ShopScene.getInstance().myApps = -1;
	}
	  
}
