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
import org.game.bangyouscreen.GameLevels.GameLevel;
import org.game.bangyouscreen.Managers.ManagedScene;
import org.game.bangyouscreen.Managers.ResourceManager;
import org.game.bangyouscreen.Managers.SceneManager;

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
		TextureRegion buttonsBG = ResourceManager.mainMenuButtonsTR;
		final float ButtonSpacing = 25f * ResourceManager.getInstance().cameraScaleFactorY;//按钮之间的间隔
		
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
		mainMenuTitleSprite.registerEntityModifier(new MoveModifier(1f, mCameraWidth / 2f, 
				mainMenuTitleSprite.getY(), mCameraWidth / 2f, mCameraHeight - (mainMenuTitleSprite.getHeight() / 2f)));
		mainMenuTitleSprite.setZIndex(-80);
		
		final ButtonSprite singleModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		singleModeBS.setScale(2f * ResourceManager.getInstance().cameraScaleFactorY);
		singleModeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		mainMenuScreen.attachChild(singleModeBS);
		
		final ButtonSprite challengeModeBS = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		challengeModeBS.setScale(2f * ResourceManager.getInstance().cameraScaleFactorY);
		challengeModeBS.setPosition(mCameraWidth / 2f, 0f);
		challengeModeBS.setVisible(false);
		mainMenuScreen.attachChild(challengeModeBS);
		
		//单人模式点击事件
		singleModeBS.setOnClickListener(new OnClickListener(){
			
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				singleModeBS.registerEntityModifier(new ParallelEntityModifier(
						new FadeOutModifier(1f),
						new MoveModifier(1f,singleModeBS.getX(),singleModeBS.getY(),singleModeBS.getX(),mCameraHeight)
						));
				challengeModeBS.setVisible(true);
				challengeModeBS.registerEntityModifier(new ParallelEntityModifier(
						new MoveModifier(1f,challengeModeBS.getX(),challengeModeBS.getY(),challengeModeBS.getX(), mCameraHeight / 2f),
						new FadeInModifier(1f)));
			}
		});
		registerTouchArea(singleModeBS);
		
		//挑战模式点击事件
		challengeModeBS.setOnClickListener(new OnClickListener(){

			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SceneManager.getInstance().showScene(new GameLevel());
				
			}
		});
	
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
