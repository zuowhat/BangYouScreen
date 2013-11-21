package org.game.bangyouscreen;


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
import org.game.bangyouscreen.layer.GameLayer;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.scene.SplashScreen;
import org.game.bangyouscreen.scene.ThemeScene;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View.MeasureSpec;


public class BangYouScreenActivity extends BaseGameActivity {
	
	public static final String SHARED_PREFS_MAIN = "MagneTankSettings";
	public static final String SHARED_PREFS_THEME_1 = "ThemeScene1";
	public static final String SHARED_PREFS_THEME_2 = "ThemeScene2";
	
	public static boolean getBooleanFromSharedPreferences(final String pStr) {
		return ResourceManager.getActivity()
				.getSharedPreferences(SHARED_PREFS_MAIN, 0)
				.getBoolean(pStr, false);
	}

	public static int getIntFromSharedPreferences(final String pStr) {
		return ResourceManager.getActivity()
				.getSharedPreferences(SHARED_PREFS_MAIN, 0).getInt(pStr, 0);
	}

	public static void writeBooleanToSharedPreferences(final String pStr,
			final boolean pValue) {
		ResourceManager.getActivity()
				.getSharedPreferences(SHARED_PREFS_MAIN, 0).edit()
				.putBoolean(pStr, pValue).apply();
	}

	public static int writeIntToSharedPreferences(final String pStr,
			final int pValue) {
		ResourceManager.getActivity()
				.getSharedPreferences(SHARED_PREFS_MAIN, 0).edit()
				.putInt(pStr, pValue).apply();
		return pValue;
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
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback){
		ResourceManager.setup(this,  (FixedStepEngine)this.getEngine(), this.getApplicationContext(), 
				cameraWidth, cameraHeight, cameraWidth/DESIGN_WINDOW_WIDTH_PIXELS, cameraHeight/DESIGN_WINDOW_HEIGHT_PIXELS);
		//System.out.println("onCreateResources");
		
		
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback){
		//System.out.println("onCreateScene");
		SceneManager.getInstance().showScene(new SplashScreen());
		
		
		pOnCreateSceneCallback.onCreateSceneFinished(mEngine.getScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected synchronized void onResume() {
		super.onResume();
		System.gc();
//		if(this.isGameLoaded())
//			SFXManager.resumeMusic();
	}
	
	  public void onBackPressed() {
		  if (ResourceManager.getInstance().engine != null) {
			  if (SceneManager.getInstance().mIsLayerShown) {
				  SceneManager.getInstance().mCurrentLayer.onHideLayer();
				  ((GameLevel) SceneManager.getInstance().mCurrentScene).onResumeGameLevel();
			  }else if(SceneManager.getInstance().mCurrentScene.getClass().equals(GameLevel.class)){
				  ((GameLevel) SceneManager.getInstance().mCurrentScene).onPauseGameLevel();
				  SceneManager.getInstance().showLayer(GameLayer.getInstance(), false, false, false);
			  }else{
				  showExitConfirmDialog(); 
			  }
		  }
	  }
	  
	  public void showExitConfirmDialog(){
	    runOnUiThread(new Runnable(){
	      @Override
		public void run()
	      {
	        AlertDialog localAlertDialog = new AlertDialog.Builder(BangYouScreenActivity.this).create();
	        localAlertDialog.setTitle("QUIT GAME");
	        localAlertDialog.setMessage("Are you sure?");
	        localAlertDialog.setButton("EXIT", new DialogInterface.OnClickListener()
	        {
	          @Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
	          {
	        	 // BangYouScreenActivity.this.finish();
	        	  System.exit(0);
	          }
	        });
	        localAlertDialog.setButton3("CANCEL", new DialogInterface.OnClickListener()
	        {
	          @Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
	          {
	          }
	        });
	        localAlertDialog.show();
	      }
	    });
	  }
   
}
