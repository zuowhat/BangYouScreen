package org.game.bangyouscreen.Managers;

import org.andengine.entity.scene.Scene;

public abstract class ManagedScene extends Scene{
	
	//是否包含加载画面
	public final boolean hasLoadingScreen;
	
	//加载画面显示多长时间，在SceneManaher中设置
	public float elapsedLoadingScreenTime = 0f;
	
	//加载画面最少显示的时间（单位秒）
	public final float minLoadingScreenTime;
	
	//场景是否被加载
	public boolean isLoaded = false;
	
	public void onShowManagedScene() {
		this.setIgnoreUpdate(false);
		onShowScene();
	}
	
	public void onHideManagedScene() {
		this.setIgnoreUpdate(true);
		onHideScene();
	}
	
	public void onLoadManagedScene() {
		if(!isLoaded) {
			onLoadScene();
			isLoaded = true;
			this.setIgnoreUpdate(true);
		}
	}
	
	public void onUnloadManagedScene() {
		if(isLoaded) {
			isLoaded = false;
			onUnloadScene();
		}
	}
	
	public abstract Scene onLoadingScreenLoadAndShown();//加载，显示--'加载画面'
	public abstract void onLoadingScreenUnloadAndHidden();//卸载，隐藏--'加载画面'
	
	public abstract void onLoadScene();
	public abstract void onUnloadScene();
	public abstract void onShowScene();
	public abstract void onHideScene();
}
