package org.game.bangyouscreen.Menus;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.game.bangyouscreen.Managers.ManagedScene;
import org.game.bangyouscreen.Managers.ResourceManager;

public class MainMenu extends ManagedScene{
	
	private static final MainMenu INSTANCE = new MainMenu();
	private Sprite backgroundSprite;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	
	public static MainMenu getInstance(){
		return INSTANCE;
	}
	
	public MainMenu(){
		super(0.1f);
	}

	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Scene onLoadingScreenLoadAndShown() {
		Scene managerLoadScreen = new Scene();
		backgroundSprite = new Sprite(0f,0f, ResourceManager.mainMenuBackgroundTR,ResourceManager.getEngine().getVertexBufferObjectManager());
		//根据屏幕尺寸，对背景图片进行缩放
		backgroundSprite.setScale(ResourceManager.getCamera().getHeight() / ResourceManager.mainMenuBackgroundTR.getHeight());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-99);
		managerLoadScreen.attachChild(backgroundSprite);
		//managerLoadScreen.attachChild(new Text(mCameraWidth / 2f, mCameraHeight / 2f,));
		return managerLoadScreen;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

}
