package org.game.bangyouscreen.gamelevels;

import org.andengine.entity.scene.Scene;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.menus.MainMenu;

public class GameLevel extends ManagedScene {
	
	private static final GameLevel INSTANCE = new GameLevel();
	
	public static GameLevel getInstance(){
		return INSTANCE;
	}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnloadScene() {
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
