package org.game.bangyouscreen.Menus;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.Managers.ManagedScene;
import org.game.bangyouscreen.Managers.ResourceManager;

import android.widget.Toast;

public class MainMenu extends ManagedScene{
	
	private static final MainMenu INSTANCE = new MainMenu();
	private Sprite backgroundSprite;
	private static final float mCameraWidth = ResourceManager.getCamera().getWidth();
	private static final float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private Entity mainMenuScreen;
	
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
		backgroundSprite.setScale(ResourceManager.getCamera().getHeight() / ResourceManager.mainMenuBackgroundTR.getHeight());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-99);
		managerLoadScreen.attachChild(backgroundSprite);
		managerLoadScreen.attachChild(new Text(mCameraWidth / 2f, mCameraHeight / 2f,ResourceManager.mFont,"Load...",mVertexBufferObjectManager));
		return managerLoadScreen;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		//backgroundSprite.detachSelf();
	}
	
	@Override
	public void onLoadScene() {
		VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
		ResourceManager.loadGameResources();
		mainMenuScreen = new Entity(0,mCameraHeight){
			boolean hasLoaded = false;
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(!hasLoaded){
					hasLoaded = true;
					this.registerEntityModifier(new MoveModifier (0.2f,0,mCameraHeight,0,0));
				}
			}
		};
		
		Sprite mainMenuTitleSprite = new Sprite(0f,0f,ResourceManager.mainMenuTitleTR,mVertexBufferObjectManager);
		mainMenuScreen.attachChild(mainMenuTitleSprite);
		
		final float ButtonSpacing = 25f * ResourceManager.getInstance().cameraScaleFactorY;//按钮上下之间的间隔
		TextureRegion buttonsBG = ResourceManager.mainMenuButtonsTR;
		
		ButtonSprite singleModeSprite = new ButtonSprite(0f,0f,buttonsBG,mVertexBufferObjectManager);
		singleModeSprite.setPosition(mCameraWidth / 2f, mCameraHeight + buttonsBG.getHeight() + ButtonSpacing);
		Text singleModeText = new Text(0f, 0f,ResourceManager.mFont,"单人模式",mVertexBufferObjectManager);
		singleModeText.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		singleModeSprite.attachChild(singleModeText);
		mainMenuScreen.attachChild(singleModeSprite);
		singleModeSprite.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Toast.makeText(ResourceManager.getInstance().activity, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();
			}
		});
		registerTouchArea(singleModeSprite);
	
		
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
