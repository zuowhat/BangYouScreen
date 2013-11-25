package org.game.bangyouscreen.managers;

import org.andengine.entity.scene.Scene;

public abstract class ManagedScene extends Scene{
	
	/**
	 * 是否包含加载画面
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public final boolean hasLoadingScreen;
	
	/**
	 * 加载画面显示多长时间，在SceneManaher中设置
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public float elapsedLoadingScreenTime = 0f;
	
	/**
	 * 加载画面最少显示的时间（单位秒）
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public final float minLoadingScreenTime;
	
	/**
	 * 场景是否被加载
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public boolean isLoaded = false;
	
	public ManagedScene(){
		this(0f);
	}
	
	public ManagedScene(final float pLoadingScreenMinimumSecondsShown) {
		minLoadingScreenTime = pLoadingScreenMinimumSecondsShown;
		hasLoadingScreen = (minLoadingScreenTime > 0f);
	}
	
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
	public abstract void onShowScene();
	public abstract void onHideScene();
	public abstract void onUnloadScene();
}
