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
	//private Entity singleModeChildren;//单人模式子菜单
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
		ResourceManager.setupForMenus();
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
		
		//singleModeChildren = new Entity(0,-mCameraHeight);
		final ButtonSprite challengeModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		challengeModeBS.setPosition(mCameraWidth / 2f, 0f);
		challengeModeBS.setVisible(false);
		final Text challengeModeText = new Text(0f, 0f,ResourceManager.mFont,"挑战模式",mVertexBufferObjectManager);
		challengeModeText.setPosition(challengeModeBS.getWidth()/2f, challengeModeBS.getHeight()/2f);
		challengeModeBS.attachChild(challengeModeText);
		//singleModeChildren.attachChild(challengeModeBS);
		mainMenuScreen.attachChild(challengeModeBS);
		
		
		mainMenuTitleSprite = new Sprite(0f, mCameraHeight + ResourceManager.mainMenuTitleTR.getHeight(), ResourceManager.mainMenuTitleTR, mVertexBufferObjectManager);
		mainMenuTitleSprite.registerEntityModifier(new MoveModifier(1f, mCameraWidth / 2f, 
				mainMenuTitleSprite.getY(), mCameraWidth / 2f, mCameraHeight - (mainMenuTitleSprite.getHeight() / 2f)));
		//mainMenuTitleSprite.setPosition(mCameraWidth / 2f, mCameraHeight - (mainMenuTitleSprite.getHeight() / 2f) );
		mainMenuTitleSprite.setZIndex(-80);
		
		
		final float ButtonSpacing = 25f * ResourceManager.getInstance().cameraScaleFactorY;//按钮之间的间隔
		
		
		final ButtonSprite singleModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		//singleModeSprite.setPosition(mCameraWidth / 2f, mCameraHeight + buttonsBG.getHeight() + ButtonSpacing);
		singleModeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		final Text singleModeText = new Text(0f, 0f,ResourceManager.mFont,"单人模式",mVertexBufferObjectManager);
		singleModeText.setPosition(buttonsBG.getWidth() / 2f, buttonsBG.getHeight() / 2f);
		singleModeBS.attachChild(singleModeText);
		mainMenuScreen.attachChild(singleModeBS);
		
		//单人模式按钮点击事件
		singleModeBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				singleModeBS.registerEntityModifier(new ParallelEntityModifier(
						new FadeOutModifier(1f),
						new MoveModifier(1f,singleModeBS.getX(),singleModeBS.getY(),singleModeBS.getX(),mCameraHeight)
						));
				singleModeText.registerEntityModifier(new FadeOutModifier(1f));
//				singleModeChildren.registerEntityModifier(new ParallelEntityModifier(
//						new MoveModifier(1f,singleModeChildren.getX(),singleModeChildren.getY(),0f,0f),
//						new FadeInModifier(0.5f)));
				challengeModeBS.setVisible(true);
				challengeModeBS.registerEntityModifier(new ParallelEntityModifier(
						new MoveModifier(1f,challengeModeBS.getX(),challengeModeBS.getY(),challengeModeBS.getX(), mCameraHeight / 2f),
						new FadeInModifier(1f)));
				challengeModeText.registerEntityModifier(new FadeInModifier(1f));
			}
		});
		registerTouchArea(singleModeBS);
	
		this.attachChild(mainMenuTitleSprite);
		this.attachChild(this.mainMenuScreen);
		//this.attachChild(this.singleModeChildren);
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
