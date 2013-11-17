package org.game.bangyouscreen.scene;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.managers.ResourceManager;

public class LoadingScene extends Scene{
	
	private static final LoadingScene INSTANCE = new LoadingScene();
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	private Sprite backgroundSprite;
	private AnimatedSprite picAS;
	private AnimatedSprite loadingFont;
	
	public static LoadingScene getInstance(){
		return INSTANCE;
	}
	
	
	
	public void onLoadScene(){
		backgroundSprite = new Sprite(0f,0f, ResourceManager.loadingBG,mVertexBufferObjectManager);
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.loadingBG.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-5000);
		attachChild(backgroundSprite);
		
		//动画效果无法生效，原因待查
	    picAS = new AnimatedSprite(0f,0f,ResourceManager.loadingBG2,mVertexBufferObjectManager);
	    picAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
	    picAS.animate(300, true);
	    attachChild(picAS);
	    
	    //动画效果无法生效，原因待查
	    loadingFont = new AnimatedSprite(0f,0f,ResourceManager.loadingFont,mVertexBufferObjectManager);
	    loadingFont.setPosition(picAS.getX(), picAS.getY()-picAS.getHeight()/2f-40f);
	    loadingFont.animate(300, true);
	    attachChild(loadingFont);
	}
	
	public void unloadScene(){
		INSTANCE.detachChildren();
//		mSunRaySprite.detachSelf();
//		picAS.detachSelf();
//		loadingFont.detachSelf();
	}
	
}
