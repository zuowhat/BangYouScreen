package org.game.bangyouscreen.menus;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.game.bangyouscreen.managers.ResourceManager;

public class LoadingScene extends Scene{
	
	private static final LoadingScene INSTANCE = new LoadingScene();
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	private Sprite backgroundSprite;
	private Sprite mSunRaySprite;
	private AnimatedSprite picAS;
	private AnimatedSprite loadingFont;
	
	public static LoadingScene getInstance(){
		return INSTANCE;
	}
	
	RotationModifier mRotationModifier = new RotationModifier(34.0F, 0.0F, 360.0F, new IEntityModifier.IEntityModifierListener(){
	    @Override
		public void onModifierFinished(IModifier<IEntity> paramIModifier, IEntity paramIEntity){
	      mRotationModifier.reset();
	      mSunRaySprite.unregisterEntityModifier(mRotationModifier);
	      mSunRaySprite.registerEntityModifier(mRotationModifier);
	    }

	    @Override
		public void onModifierStarted(IModifier<IEntity> paramIModifier, IEntity paramIEntity){
	    }
	 });
	
	
	public void onLoadScene(){
		backgroundSprite = new Sprite(0f,0f, ResourceManager.mainMenuBackgroundTR,mVertexBufferObjectManager);
		//根据屏幕尺寸，对背景图片进行缩放
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.mainMenuBackgroundTR.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-5000);
		attachChild(backgroundSprite);
		
	    mSunRaySprite = new Sprite(0.0F, 0.0F, ResourceManager.loadingBG1,mVertexBufferObjectManager);
	    mSunRaySprite.setScale(2.0F);
	    mSunRaySprite.setPosition((mCameraWidth - this.mSunRaySprite.getWidth()) / 2.0F, (mCameraHeight - this.mSunRaySprite.getHeight()) / 2.0F);
	    mSunRaySprite.registerEntityModifier(this.mRotationModifier);
	    attachChild(this.mSunRaySprite);
	    
	    picAS = new AnimatedSprite(0f,0f,ResourceManager.loadingBG2,mVertexBufferObjectManager);
	    picAS.setPosition(mCameraWidth/2f, mCameraHeight/2f);
	    picAS.animate(300, true);
	    attachChild(picAS);
	    
	    loadingFont = new AnimatedSprite(0f,0f,ResourceManager.loadingFont,mVertexBufferObjectManager);
	    loadingFont.setPosition(picAS.getX(), picAS.getY()-picAS.getHeight()/2f-10f);
	    loadingFont.animate(300, true);
	    attachChild(loadingFont);
	}
	
	public void unloadScene(){
		INSTANCE.detachChildren();
	}
	
}
