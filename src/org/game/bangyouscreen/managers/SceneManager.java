package org.game.bangyouscreen.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

/**
 * 管理场景和层 
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class SceneManager {
	
	private static final SceneManager INSTANCE = new SceneManager();
	public SceneManager() {}
	public static SceneManager getInstance(){
		return INSTANCE;
	}
	
	// Set to TRUE in the showLayer() method if the camera had a HUD before the
	// layer was shown.
	private boolean mCameraHadHud = false;
	// Boolean to reflect whether there is a layer currently shown on the
	// screen.
	public boolean mIsLayerShown = false;
	// An empty place-holder scene that we use to apply the modal properties of
	// the layer to the currently shown scene.
	private Scene mPlaceholderModalScene;

	// Hold a reference to the current managed layer (if one exists).
	public ManagedLayer mCurrentLayer;
	
	public ManagedScene mCurrentScene;//当前场景
	private ManagedScene mNextScene;//下一个场景
	
	private Engine mEngine = ResourceManager.getInstance().engine;
	//在加载资源之间，确保加载画面显示为1帧
	private int mNumFramesPassed = -1;
	//用于标记mLoadingScreenHandler是否被引擎注册
	private boolean mLoadingScreenHandlerRegistered = false;
	
	//用于显示加载画面的方法
	private IUpdateHandler mLoadingScreenHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			mNumFramesPassed++;
			
			//设置加载画面显示的时间
			mNextScene.elapsedLoadingScreenTime += pSecondsElapsed;
			
			//加载画面显示后，帧数达到1帧的时候
			if(mNumFramesPassed==1) {
				//卸载当前场景
				if(mCurrentScene!=null) {
					mCurrentScene.onHideManagedScene();
					mCurrentScene.onUnloadManagedScene();
				}
				//加载新场景
				mNextScene.onLoadManagedScene();
			}
			
			//当场景完成加载，并且加载画面的显示时间超过最低时长的时候
			if(mNumFramesPassed>1 && mNextScene.elapsedLoadingScreenTime>=mNextScene.minLoadingScreenTime) {
				//显示加载画面调用了setChildScene()方法，所以这里使用clearChildScene()来删除加载画面
				mNextScene.clearChildScene();
				//卸载并隐藏'加载画面'
				mNextScene.onLoadingScreenUnloadAndHidden();
				// 显示新场景
				mNextScene.onShowManagedScene();
				// 新场景变成当前场景
				mCurrentScene = mNextScene;
				// Reset the handler & loading screen variables to be ready for another use.
				//重置处理'加载画面'的变量，以便下次使用
				mNextScene.elapsedLoadingScreenTime = 0f;
				mNumFramesPassed = -1;
				mEngine.unregisterUpdateHandler(this);
				mLoadingScreenHandlerRegistered = false;
			}
		}

		@Override
		public void reset() {}
	};
	
	/**
	 * 显示场景
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public void showScene(ManagedScene pManagedScene){
		//初始化镜头
		mEngine.getCamera().set(0, 0, ResourceManager.getInstance().cameraWidth, ResourceManager.getInstance().cameraHeight);
		if(pManagedScene.hasLoadingScreen){
			//暂停pManagedScene场景的效果，更新和触摸事件,(true表示暂停),不影响加载画面的效果
			pManagedScene.setChildScene(pManagedScene.onLoadingScreenLoadAndShown(), true, true, true);
			if(mLoadingScreenHandlerRegistered){
				//一般情况不会进入这条if语句，但是如果使用UI线程来显示场景的话，则会进入。
				mNumFramesPassed = -1;
				mNextScene.clearChildScene();
				mNextScene.onLoadingScreenUnloadAndHidden();
			}else{
				mEngine.registerUpdateHandler(mLoadingScreenHandler);
				mLoadingScreenHandlerRegistered = true;
			}
			mNextScene = pManagedScene;
			mEngine.setScene(mNextScene);
			return;
		}
		mNextScene = pManagedScene;
		mEngine.setScene(mNextScene);
		//卸载当前场景
		if(mCurrentScene != null){
			mCurrentScene.onHideManagedScene();
			mCurrentScene.onUnloadManagedScene();
		}
		mNextScene.onLoadManagedScene();
		mNextScene.onShowManagedScene();
		mCurrentScene = mNextScene;
	}
	
	public void hideLayer() {
		if (this.mIsLayerShown) {
			mCurrentLayer.onUnloadLayer();
			// Clear the HUD's child scene to remove modal properties.
			this.mEngine.getCamera().getHUD().clearChildScene();
			// If we had to use a place-holder scene, clear it.
			if (this.mCurrentScene.hasChildScene()) {
				if (this.mCurrentScene.getChildScene() == this.mPlaceholderModalScene) {
					this.mCurrentScene.clearChildScene();
				}
			}
			// If the camera did not have a HUD before we showed the layer,
			// remove the place-holder HUD.
			if (!this.mCameraHadHud) {
				this.mEngine.getCamera().setHUD(null);
			}
			// Reflect that a layer is no longer shown.
			this.mIsLayerShown = false;
			// Remove the reference to the layer.
			this.mCurrentLayer = null;
		}
	}

	// Shows a layer by placing it as a child to the Camera's HUD.
	public void showLayer(final ManagedLayer pLayer,
			final boolean pSuspendSceneDrawing,
			final boolean pSuspendSceneUpdates,
			final boolean pSuspendSceneTouchEvents) {
		// If the camera already has a HUD, we will use it.
		if (this.mEngine.getCamera().hasHUD()) {
			this.mCameraHadHud = true;
		} else {
			// Otherwise, we will create one to use.
			this.mCameraHadHud = false;
			final HUD placeholderHud = new HUD();
			this.mEngine.getCamera().setHUD(placeholderHud);
		}
		// If the managed layer needs modal properties, set them.
		if (pSuspendSceneDrawing || pSuspendSceneUpdates|| pSuspendSceneTouchEvents) {
			// Apply the managed layer directly to the Camera's HUD
			this.mEngine.getCamera().getHUD().setChildScene(pLayer, pSuspendSceneDrawing,
							pSuspendSceneUpdates, pSuspendSceneTouchEvents);
			this.mEngine.getCamera().getHUD().setOnSceneTouchListenerBindingOnActionDownEnabled(true);
			// Create the place-holder scene if it needs to be created.
			if (this.mPlaceholderModalScene == null) {
				this.mPlaceholderModalScene = new Scene();
				this.mPlaceholderModalScene.setBackgroundEnabled(false);
			}
			// Apply the place-holder to the current scene.
			this.mCurrentScene.setChildScene(this.mPlaceholderModalScene,
					pSuspendSceneDrawing, pSuspendSceneUpdates,
					pSuspendSceneTouchEvents);
		} else {
			// If the managed layer does not need to be modal, simply set it to
			// the HUD.
			this.mEngine.getCamera().getHUD().setChildScene(pLayer);
		}
		// Set the camera for the managed layer so that it binds to the camera
		// if the camera is moved/scaled/rotated.
		pLayer.setCamera(this.mEngine.getCamera());
		// Scale the layer according to screen size.
		// pLayer.setScale(ResourceManager.getInstance().cameraScaleFactorX,
		// ResourceManager.getInstance().cameraScaleFactorY);
		// Let the layer know that it is being shown.
		pLayer.onShowManagedLayer();
		// Reflect that a layer is shown.
		this.mIsLayerShown = true;
		// Set the current layer to pLayer.
		this.mCurrentLayer = pLayer;
	}

}
