package org.game.bangyouscreen.scene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleAtModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SceneManager;


/**
 * 启动画面
 * @author zuowhat
 * @version 1.0
 * @since 2013.08.26 
 */
public class SplashScreen extends ManagedScene{
	
	public SplashScreen thisSplashScene = this;
	
	//对显示的图片进行缩放
	private static final float mEachScaleToSize = 2f * ResourceManager.getInstance().cameraScaleFactorY;
	
	//启动画面1
	private static final BitmapTextureAtlas beginOneTexture = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 200, 200, TextureOptions.BILINEAR);
	private static final ITextureRegion beginOneTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(beginOneTexture, ResourceManager.getContext(), "gfx/splash/begin1.png", 0, 0);
	private static final Sprite beginOneSprite = new Sprite((ResourceManager.getInstance().cameraWidth) / 2f, (ResourceManager.getInstance().cameraHeight) / 2f, beginOneTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager());
	
	//启动画面2
	private static final BitmapTextureAtlas beginTwoTexture = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 200, 200, TextureOptions.BILINEAR);
	private static final ITextureRegion beginTwoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(beginTwoTexture, ResourceManager.getContext(), "gfx/splash/begin2.png", 0, 0);
	private static final Sprite beginTwoSprite = new Sprite(beginOneSprite.getX(), beginOneSprite.getY(), beginTwoTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager());
	
	//旋转光环
	private static final BitmapTextureAtlas nimbusBG = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 512, 512, TextureOptions.BILINEAR);
	private static final ITextureRegion nimbusBGTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(nimbusBG, ResourceManager.getContext(), "gfx/menu/loadingBG1.png", 0, 0);
	private static final Sprite nimbusSprite = new Sprite(beginOneSprite.getX(), beginOneSprite.getY(), nimbusBGTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager());
	
	RotationModifier mRotationModifier = new RotationModifier(34.0F, 0.0F, 360.0F, new IEntityModifier.IEntityModifierListener(){
	    @Override
		public void onModifierFinished(IModifier<IEntity> paramIModifier, IEntity paramIEntity){
	      mRotationModifier.reset();
	      nimbusSprite.unregisterEntityModifier(mRotationModifier);
	      nimbusSprite.registerEntityModifier(mRotationModifier);
	    }

	    @Override
		public void onModifierStarted(IModifier<IEntity> paramIModifier, IEntity paramIEntity){
	    }
	 });
	
	//启动画面1的实体修饰符
	private static ScaleAtModifier beginOneScaleAtOne = new ScaleAtModifier(0.5f, 25f, mEachScaleToSize, 0.5f, 0.5f);//实体缩放
	private static FadeInModifier beginOneFadeIn = new FadeInModifier(0.5f);//在0.5秒内改变透明度由0f变为1f
	private static ParallelEntityModifier beginOneParalleOne = new ParallelEntityModifier(beginOneScaleAtOne,beginOneFadeIn);//同时执行修饰符
	
	private static DelayModifier beginOneDelay = new DelayModifier(2f);//画面保持2秒
	
	private static ScaleAtModifier beginOneScaleAtTwo = new ScaleAtModifier(0.5f, mEachScaleToSize, 0f, 0.5f, 0.5f);//实体缩放
	private static FadeOutModifier beginOneFadeOut = new FadeOutModifier(0.5f);//在0.5秒内改变透明度由0f变为1f
	private static ParallelEntityModifier beginOneParalleTwo = new ParallelEntityModifier(beginOneScaleAtTwo,beginOneFadeOut);//同时执行修饰符
	
	private static final SequenceEntityModifier beginOneSequence = new SequenceEntityModifier(beginOneParalleOne,beginOneDelay,beginOneParalleTwo);
	
	//启动画面2的实体修饰符
	private static ScaleAtModifier beginTwoScaleAtOne = new ScaleAtModifier(0.5f, 25f, mEachScaleToSize, 0.5f, 0.5f);//实体缩放
	private static FadeInModifier beginTwoFadeIn = new FadeInModifier(0.5f);//在0.5秒内改变透明度由0f变为1f
	private static ParallelEntityModifier beginTwoParalleOne = new ParallelEntityModifier(beginTwoScaleAtOne,beginTwoFadeIn);//同时执行修饰符
	
	private static DelayModifier beginTwoDelay = new DelayModifier(2f);//画面保持2秒
	
	private static ScaleAtModifier beginTwoScaleAtTwo = new ScaleAtModifier(0.5f, mEachScaleToSize, 0f, 0.5f, 0.5f);//实体缩放
	private static FadeOutModifier beginTwoFadeOut = new FadeOutModifier(0.5f);//在0.5秒内改变透明度由0f变为1f
	private static ParallelEntityModifier beginTwoParalleTwo = new ParallelEntityModifier(beginTwoScaleAtTwo,beginTwoFadeOut);//同时执行修饰符
	
	private static final SequenceEntityModifier beginTwoSequence = new SequenceEntityModifier(beginTwoParalleOne,beginTwoDelay,beginTwoParalleTwo);
	
	@Override
	public void onLoadScene() {
		beginOneTexture.load();
		beginTwoTexture.load();
		nimbusBG.load();
		ResourceManager.getCamera().setCenterDirect(ResourceManager.getInstance().cameraWidth / 2f, ResourceManager.getInstance().cameraHeight / 2f);
		this.setBackgroundEnabled(true);
		this.setBackground(new Background(0.1f, 0.1f, 0.1f));
		
		nimbusSprite.setScale(2f);
		nimbusSprite.registerEntityModifier(mRotationModifier);
		attachChild(nimbusSprite);
		this.attachChild(beginOneSprite);
		
		beginTwoSprite.setAlpha(0.001f);
		beginTwoSprite.setScale(0.01f);
		this.attachChild(beginTwoSprite);
		
		beginOneSequence.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
				SplashScreen.beginTwoSprite.registerEntityModifier(beginTwoSequence);
			}
			
			@Override
			public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
		});
		
		beginTwoSequence.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
				//显示主界面
				SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			}
			
			@Override
			public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
		});
		
		this.registerUpdateHandler(new IUpdateHandler() {
			int counter = 0;
			
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				this.counter++;
				if(this.counter > 2) {
					beginOneSprite.registerEntityModifier(beginOneSequence);
					unregisterUpdateHandler(this);
				}
			}
			
			@Override
			public void reset() {}
		});
	}
	
	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				thisSplashScene.detachChildren();
				for(int i = 0; i < thisSplashScene.getChildCount(); i++){
					thisSplashScene.getChildByIndex(i).dispose();
				}
				thisSplashScene.clearEntityModifiers();
				thisSplashScene.clearTouchAreas();
				thisSplashScene.clearUpdateHandlers();
				beginOneTexture.unload();
				beginTwoTexture.unload();
				nimbusBG.unload();
			}});
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
	public void onShowScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}
}
