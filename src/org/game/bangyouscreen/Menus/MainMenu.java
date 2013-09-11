package org.game.bangyouscreen.Menus;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.Managers.ManagedScene;
import org.game.bangyouscreen.Managers.ResourceManager;

public class MainMenu extends ManagedScene{
	
	private static final MainMenu INSTANCE = new MainMenu();
	private Sprite backgroundSprite;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private Entity mainMenuScreen; //主菜单
	private Entity singleModeChildren;//单人模式子菜单
	Sprite mainMenuTitleSprite;//主菜单标题
	
	public static MainMenu getInstance(){
		return INSTANCE;
	}
	
	public MainMenu(){
		super(0.1f);
	}
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		ResourceManager.loadMenuResources();
		Scene managerLoadScreen = new Scene();
		backgroundSprite = new Sprite(0f,0f, ResourceManager.mainMenuBackgroundTR,mVertexBufferObjectManager);
		//根据屏幕尺寸，对背景图片进行缩放
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.mainMenuBackgroundTR.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-99);
		managerLoadScreen.attachChild(backgroundSprite);
		managerLoadScreen.attachChild(new Text(mCameraWidth / 2f, mCameraHeight / 2f,ResourceManager.mFont,"Load...",mVertexBufferObjectManager));
		return managerLoadScreen;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		backgroundSprite.detachSelf();
	}
	
	@Override
	public void onLoadScene() {
		//VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
		ResourceManager.loadGameResources();
		TextureRegion buttonsBG = ResourceManager.mainMenuButtonsTR;
		mainMenuScreen = new Entity(0,mCameraHeight){
			boolean hasLoaded = false;
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(!hasLoaded){
					hasLoaded = true;
					this.registerEntityModifier(new MoveModifier (1f,0,mCameraHeight,0,0));
				}
			}
		};
		
		singleModeChildren = new Entity(0,-mCameraHeight);
		ButtonSprite challengeModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		challengeModeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		Text challengeModeText = new Text(0f, 0f,ResourceManager.mFont,"挑战模式",mVertexBufferObjectManager);
		challengeModeText.setPosition(challengeModeBS.getWidth()/2f, challengeModeBS.getHeight()/2f);
		challengeModeBS.attachChild(challengeModeText);
		singleModeChildren.attachChild(challengeModeBS);
		
		
		mainMenuTitleSprite = new Sprite(0f, mCameraHeight + mainMenuTitleSprite.getHeight(), ResourceManager.mainMenuTitleTR, mVertexBufferObjectManager);
		mainMenuTitleSprite.registerEntityModifier(new MoveModifier(1f, mainMenuTitleSprite.getX(), 
				mainMenuTitleSprite.getY(), mCameraWidth / 2f, mCameraHeight - (mainMenuTitleSprite.getHeight() / 2f)));
		//mainMenuTitleSprite.setPosition(mCameraWidth / 2f, mCameraHeight - (mainMenuTitleSprite.getHeight() / 2f) );
		mainMenuTitleSprite.setZIndex(-80);
		
		
		final float ButtonSpacing = 25f * ResourceManager.getInstance().cameraScaleFactorY;//按钮之间的间隔
		
		
		ButtonSprite singleModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		//singleModeSprite.setPosition(mCameraWidth / 2f, mCameraHeight + buttonsBG.getHeight() + ButtonSpacing);
		singleModeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		Text singleModeText = new Text(0f, 0f,ResourceManager.mFont,"单人模式",mVertexBufferObjectManager);
		singleModeText.setPosition(buttonsBG.getWidth() / 2f, buttonsBG.getHeight() / 2f);
		singleModeBS.attachChild(singleModeText);
		mainMenuScreen.attachChild(singleModeBS);
		
		singleModeBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mainMenuScreen.registerEntityModifier(new ParallelEntityModifier(
						new MoveModifier(1f,mainMenuScreen.getX(),mainMenuScreen.getY(),0f,mCameraHeight),
						new FadeOutModifier(1f)));
				singleModeChildren.registerEntityModifier(new ParallelEntityModifier(
						new MoveModifier(1f,singleModeChildren.getX(),singleModeChildren.getY(),0f,0f),
						new FadeInModifier(1f)));
			}
		});
		registerTouchArea(singleModeBS);
	
		this.attachChild(mainMenuTitleSprite);
		this.attachChild(this.mainMenuScreen);
		this.attachChild(this.singleModeChildren);
	}
	
	@Override
	public void onShowScene() {
		if(!this.backgroundSprite.hasParent()) {
			this.attachChild(this.backgroundSprite);
			this.sortChildren();
		}
		
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

}
