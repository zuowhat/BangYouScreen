package org.game.bangyouscreen.Managers;


import org.andengine.engine.Engine;
import org.game.bangyouscreen.Menus.SplashScreen;



public class SceneManager {
	
	private static final SceneManager INSTANCE = new SceneManager();
	
	private Engine mEngine = ResourceManager.getInstance().engine;
	
	public SceneManager() {}
	
	public static SceneManager getInstance(){
		return INSTANCE;
	}
	
	// 快速显示主菜单
	public void showMainMenu() {
		//showScene(MainMenu.getInstance());
	}
	
	public void showScene(){
		SplashScreen splashScreen = new SplashScreen();
		splashScreen.onLoadScene();
		mEngine.setScene(splashScreen);
	}

}
