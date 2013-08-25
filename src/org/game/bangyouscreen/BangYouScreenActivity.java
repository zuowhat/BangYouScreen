package org.game.bangyouscreen;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.View.MeasureSpec;


public class BangYouScreenActivity extends BaseGameActivity {
	
	//开发时使用的手机屏幕分辨率
	static float DESIGN_WINDOW_WIDTH_PIXELS = 800f;
	static float DESIGN_WINDOW_HEIGHT_PIXELS = 480f;
	
	//开发时使用的手机屏幕物理尺寸
	static float DESIGN_WINDOW_WIDTH_INCHES = 4.472441f;
	static float DESIGN_WINDOW_HEIGHT_INCHES = 2.805118f;
	
	//定义最大和最小屏幕分辨率
	static float MIN_WIDTH_PIXELS = 320f, MIN_HEIGHT_PIXELS = 240f;
	static float MAX_WIDTH_PIXELS = 1600f, MAX_HEIGHT_PIXELS = 960f;
	
	public float cameraWidth;
	public float cameraHeight;
	public float actualWindowWidthInches;
	public float actualWindowHeightInches;
	public Camera mCamera;

	@Override
	public EngineOptions onCreateEngineOptions() {
		//重写ResolutionPolicy中的onMeasure()方法来设置镜头的大小
		//这种方式比使用DisplayMetrics.getWidth方法好，因为它使用window来代替display
		//This should also be better for if  the game is placed in a layout where simply measuring the display would give entirely wrong results.
		FillResolutionPolicy EngineFillResolutionPolicy = new FillResolutionPolicy() {
			
			public void onMeasure(final IResolutionPolicy.Callback pResolutionPolicyCallback, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
				super.onMeasure(pResolutionPolicyCallback, pWidthMeasureSpec, pHeightMeasureSpec);
				
				final int measuredWidth = MeasureSpec.getSize(pWidthMeasureSpec);
				final int measuredHeight = MeasureSpec.getSize(pHeightMeasureSpec);
				
				//获取设备窗口物理大小，单位英寸
				actualWindowWidthInches = measuredWidth / getResources().getDisplayMetrics().xdpi;
				actualWindowHeightInches = measuredHeight / getResources().getDisplayMetrics().ydpi;
				
				// Get an initial width for the camera, and bound it to the minimum or maximum values.
				float actualScaledWidthInPixels = DESIGN_WINDOW_WIDTH_PIXELS * (actualWindowWidthInches / DESIGN_WINDOW_WIDTH_INCHES);
				float boundScaledWidthInPixels = Math.round(Math.max(Math.min(actualScaledWidthInPixels,MAX_WIDTH_PIXELS),MIN_WIDTH_PIXELS));
				
				// Get the height for the camera based on the width and the height/width ratio of the device
				float boundScaledHeightInPixels = boundScaledWidthInPixels * (actualWindowHeightInches / actualWindowWidthInches);
				// If the height is outside of the set bounds, scale the width to match it.
				if(boundScaledHeightInPixels > MAX_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MAX_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				} else if(boundScaledHeightInPixels < MIN_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MIN_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				}
				// set the height and width variables
				cameraHeight = boundScaledHeightInPixels;
				cameraWidth = boundScaledWidthInPixels;
				// apply the height and width variables
				mCamera.set(0f, 0f, cameraWidth, cameraHeight);
			}
		};
		return null;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

  

   
}
