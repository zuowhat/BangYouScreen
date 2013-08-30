package org.game.bangyouscreen.Managers;



import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.game.bangyouscreen.Menus.SplashScreen;



public class SceneManager {
	
	private static final SceneManager INSTANCE = new SceneManager();
	public SceneManager() {}
	public static SceneManager getInstance(){
		return INSTANCE;
	}
	
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
	 */
	public void showScene(ManagedScene pManagedScene){
		//初始化镜头
		mEngine.getCamera().set(0, 0, ResourceManager.getInstance().cameraWidth, ResourceManager.getInstance().cameraHeight);
		if(pManagedScene.hasLoadingScreen){
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
			mEngine.setScene(pManagedScene);
			return;
		}
		
		pManagedScene.onLoadScene();
		mEngine.setScene(pManagedScene);
	}
	
	// 快速显示主菜单
	public void showMainMenu() {
		//showScene(MainMenu.getInstance());
	}
	
	

}
