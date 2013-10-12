package org.game.bangyouscreen.menus;

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
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.EntityUtil;

public class MainMenu extends ManagedScene{
	
	private static final MainMenu INSTANCE = new MainMenu();
	private Sprite backgroundSprite;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private Entity mainMenuScreen; //主菜单
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
		ResourceManager.loadGameResources();
		TextureRegion buttonsBG = ResourceManager.singleModeTR;
		
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
		
		mainMenuTitleSprite = new Sprite(0f, mCameraHeight + ResourceManager.mainMenuTitleTR.getHeight(), ResourceManager.mainMenuTitleTR, mVertexBufferObjectManager);
		//mainMenuTitleSprite.setSize(0.5f * mCameraWidth, (0.5f * mCameraWidth)/(mainMenuTitleSprite.getWidth() / mainMenuTitleSprite.getHeight()));
		EntityUtil.setSize("width", 0.4f, mainMenuTitleSprite);
		mainMenuTitleSprite.registerEntityModifier(new MoveModifier(1f, mCameraWidth / 2f, 
				mainMenuTitleSprite.getY(), mCameraWidth / 2f, mCameraHeight - (mainMenuTitleSprite.getHeight() / 2f)));
		mainMenuTitleSprite.setZIndex(-80);
		
		//挑战模式
		final ButtonSprite challengeModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		//singleModeBS.setSize(0.3f * mCameraWidth, (0.3f * mCameraWidth)/(singleModeBS.getWidth() / singleModeBS.getHeight()));
		EntityUtil.setSize("height", 1f / 7f, challengeModeBS);
		challengeModeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		mainMenuScreen.attachChild(challengeModeBS);
		
		challengeModeBS.setOnClickListener(new OnClickListener(){
			
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SceneManager.getInstance().showScene(new ThemeScene());
			
			}
		});
		registerTouchArea(challengeModeBS);
		

	
		this.attachChild(mainMenuTitleSprite);
		this.attachChild(this.mainMenuScreen);
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
