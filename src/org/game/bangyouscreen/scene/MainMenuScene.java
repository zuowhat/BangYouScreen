package org.game.bangyouscreen.scene;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.EntityUtil;

/**
 * 主菜单画面
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class MainMenuScene extends ManagedScene{
	
	private static final MainMenuScene INSTANCE = new MainMenuScene();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private Rectangle mainMenuScreen; //主菜单
	private Sprite mainMenuTitleSprite;//主菜单标题
	private Rectangle mainMenuTheme; //模式菜单
	private ButtonSprite themeBS;
	private ButtonSprite fingerBS;
	private ButtonSprite backBS;
	private ButtonSprite chooseModeBS;
	private ButtonSprite shopModeBS;
	private ButtonSprite helpModeBS;
	
	public static MainMenuScene getInstance(){
		return INSTANCE;
	}
	
	public MainMenuScene(){
		super(0.1f);
	}
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
//		Sprite backgroundSprite = new Sprite(0f,0f, ResourceManager.mainMenuBackgroundTR,mVertexBufferObjectManager);
//		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.mainMenuBackgroundTR.getWidth());
//		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
//		backgroundSprite.setZIndex(-5000);
//		attachChild(backgroundSprite);
		ResourceManager.loadLoadingResources();
		LoadingScene.getInstance().onLoadScene();
		return LoadingScene.getInstance();
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		LoadingScene.getInstance().unloadScene();
	}
	
	@Override
	public void onLoadScene() {
		ResourceManager.getInstance().loadAdResources();
		ResourceManager.loadMenuResources();
		ResourceManager.setupForMenus();
		SFXManager.getInstance().loadMusic("mainMusic", ResourceManager.getActivity().getMusicManager(), ResourceManager.getActivity());
		SFXManager.getInstance().loadSound("a_click", ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity());
		ResourceManager.loadThemeResources();
		ResourceManager.loadBossResources();
		ResourceManager.loadGameResources();
		ResourceManager.loadFingerResources();
		//SFXManager.getInstance().playMusic("mainMusic");
		
		//白云
//		Sprite mMenuCloudsLayerOne = new Sprite(0.0F, mCameraHeight, ResourceManager.menuClouds1, mVertexBufferObjectManager);
//		Sprite mMenuCloudsLayerTwo = new Sprite(0.0F, mCameraHeight-100.0F, ResourceManager.menuClouds2, mVertexBufferObjectManager);
//		AutoParallaxBackground localAutoParallaxBackground = new AutoParallaxBackground(1.0F, 1.0F, 1.0F, 7.0F);
//		localAutoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0.0F, backgroundSprite));
//	    localAutoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(1.0F, mMenuCloudsLayerOne));
//	    localAutoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(3.0F, mMenuCloudsLayerTwo));
//	    setBackground(localAutoParallaxBackground);
//	    setBackgroundEnabled(true);

		Sprite backgroundSprite = new Sprite(0f,0f, ResourceManager.mainMenuBackgroundTR,mVertexBufferObjectManager);
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.mainMenuBackgroundTR.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-5000);
		attachChild(backgroundSprite);
		
		Sprite[] CloudSprites; CloudSprites = new Sprite[20];
		for(Sprite curCloudSprite: CloudSprites){
			curCloudSprite = new Sprite(
					MathUtils.random(-(this.getWidth()*this.getScaleX())/2,ResourceManager.getInstance().cameraWidth+(this.getWidth()*this.getScaleX())/2),
					MathUtils.random(-(this.getHeight()*this.getScaleY())/2,ResourceManager.getInstance().cameraHeight + (this.getHeight()*this.getScaleY())/2),
					ResourceManager.menuClouds1,mVertexBufferObjectManager) {
				private float XSpeed = MathUtils.random(0.2f, 2f);
				private boolean initialized = false;
				@Override
				protected void onManagedUpdate(final float pSecondsElapsed) {
					super.onManagedUpdate(pSecondsElapsed);
					if(!initialized) {
						initialized = true;
						this.setScale(XSpeed/2);
						this.setZIndex(-4000+Math.round(XSpeed*1000f));
						MainMenuScene.getInstance().sortChildren();
					}
					if(this.getX()<-(this.getWidth()*this.getScaleX())/2) {
						XSpeed = MathUtils.random(0.2f, 2f);
						this.setScale(XSpeed/2);
						this.setPosition(ResourceManager.getInstance().cameraWidth+(this.getWidth()*this.getScaleX())/2, MathUtils.random(-(this.getHeight()*this.getScaleY())/2,ResourceManager.getInstance().cameraHeight + (this.getHeight()*this.getScaleY())/2));
						
						this.setZIndex(-4000+Math.round(XSpeed*1000f));
						MainMenuScene.getInstance().sortChildren();
					}
					this.setPosition(this.getX()-(XSpeed*(pSecondsElapsed/0.016666f)), this.getY());
				}
			};
			this.attachChild(curCloudSprite);
		}
	    
		mainMenuScreen = new Rectangle(mCameraWidth / 2f, -mCameraHeight/2f, mCameraWidth, mCameraHeight, mVertexBufferObjectManager){
			boolean hasLoaded = false;
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(!hasLoaded){
					hasLoaded = true;
					this.registerEntityModifier(new MoveModifier (0.5f,mainMenuScreen.getX(),mainMenuScreen.getY(),mCameraWidth / 2f,mCameraHeight/2f,EaseElasticInOut.getInstance()));
				}
			}
		};
		mainMenuScreen.setAlpha(0f);
		
		mainMenuTitleSprite = new Sprite(0f, mCameraHeight + ResourceManager.mainMenuTitleTR.getHeight(), ResourceManager.mainMenuTitleTR, mVertexBufferObjectManager);
		//mainMenuTitleSprite.setSize(0.5f * mCameraWidth, (0.5f * mCameraWidth)/(mainMenuTitleSprite.getWidth() / mainMenuTitleSprite.getHeight()));
		EntityUtil.setSize("width", 0.4f, mainMenuTitleSprite);
		mainMenuTitleSprite.registerEntityModifier(new MoveModifier(0.5f, mCameraWidth / 2f, 
				mainMenuTitleSprite.getY(), mCameraWidth / 2f, mCameraHeight*(4f/5f),new IEntityModifierListener(){

					public void onModifierStarted(IModifier<IEntity> pModifier,IEntity pItem) {
						
					}

					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						pItem.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(
								new ScaleModifier(2, 1f, 1.1f),
								new ScaleModifier(2, 1.1f, 1f)
								)));
						
					}},EaseElasticInOut.getInstance()));
		mainMenuTitleSprite.setZIndex(-80);
		
		//模式选择
		chooseModeBS = new ButtonSprite(0f,0f,ResourceManager.mainMenuButtons.getTextureRegion(0),mVertexBufferObjectManager);
		//singleModeBS.setSize(0.3f * mCameraWidth, (0.3f * mCameraWidth)/(singleModeBS.getWidth() / singleModeBS.getHeight()));
		EntityUtil.setSize("height", 1f / 7f, chooseModeBS);
		chooseModeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		mainMenuScreen.attachChild(chooseModeBS);
		chooseModeBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				mainMenuScreen.registerEntityModifier(new ScaleModifier(0.3f,1f,0f, new IEntityModifierListener(){

						public void onModifierStarted(IModifier<IEntity> pModifier,IEntity pItem) {
							// TODO Auto-generated method stub
						}

						public void onModifierFinished(IModifier<IEntity> pModifier,IEntity pItem) {
							mainMenuScreen.setVisible(false);
							mainMenuTheme.setVisible(true);
							mainMenuTheme.registerEntityModifier(new ScaleModifier(0.3f,0f,1f,new IEntityModifierListener(){

								public void onModifierStarted(IModifier<IEntity> pModifier,IEntity pItem) {
									
								}

								public void onModifierFinished(IModifier<IEntity> pModifier,IEntity pItem) {
									unregisterTouchArea(chooseModeBS);
									unregisterTouchArea(shopModeBS);
									unregisterTouchArea(helpModeBS);
									registerTouchArea(themeBS);
									registerTouchArea(fingerBS);
									registerTouchArea(backBS);
								}
							},EaseElasticInOut.getInstance()));
						}
					},EaseElasticInOut.getInstance()));
				
			}
		});
		registerTouchArea(chooseModeBS);
		
		//商店
		shopModeBS = new ButtonSprite(0f,0f,ResourceManager.mainMenuButtons.getTextureRegion(1),mVertexBufferObjectManager);
		//singleModeBS.setSize(0.3f * mCameraWidth, (0.3f * mCameraWidth)/(singleModeBS.getWidth() / singleModeBS.getHeight()));
		EntityUtil.setSize("height", 1f / 7f, shopModeBS);
		shopModeBS.setPosition(mCameraWidth / 2f, chooseModeBS.getY() - chooseModeBS.getHeight() - 10f);
		mainMenuScreen.attachChild(shopModeBS);
		shopModeBS.setOnClickListener(new OnClickListener(){
			
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(ShopScene.getInstance());
			}
		});
		registerTouchArea(shopModeBS);
		
		//帮助
		helpModeBS = new ButtonSprite(0f,0f,ResourceManager.mainMenuButtons.getTextureRegion(2),mVertexBufferObjectManager);
		//singleModeBS.setSize(0.3f * mCameraWidth, (0.3f * mCameraWidth)/(singleModeBS.getWidth() / singleModeBS.getHeight()));
		EntityUtil.setSize("height", 1f / 7f, helpModeBS);
		helpModeBS.setPosition(mCameraWidth / 2f, shopModeBS.getY() - shopModeBS.getHeight() - 10f);
		mainMenuScreen.attachChild(helpModeBS);
		helpModeBS.setOnClickListener(new OnClickListener(){
			
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(new HelpScene());
				//待完善
				//ResourceManager.getInstance().showPopAd();
				//ResourceManager.getInstance().showYouMiAd();
				
			}
		});
		registerTouchArea(helpModeBS);

		this.attachChild(mainMenuTitleSprite);
		this.attachChild(this.mainMenuScreen);
		
		mainMenuTheme = new Rectangle(mCameraWidth / 2f, mCameraHeight/2f, mCameraWidth, mCameraHeight, mVertexBufferObjectManager);
		//主题模式
		themeBS = new ButtonSprite(0f,0f,ResourceManager.mainMenuButtons.getTextureRegion(3),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 7f, themeBS);
		themeBS.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		mainMenuTheme.attachChild(themeBS);
		themeBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(ThemeScene.getInstance());
			}
		});
		//指力模式
		fingerBS = new ButtonSprite(0f,0f,ResourceManager.mainMenuButtons.getTextureRegion(4),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 7f, fingerBS);
		fingerBS.setPosition(mCameraWidth / 2f, themeBS.getY()-10f-fingerBS.getHeight());
		mainMenuTheme.attachChild(fingerBS);
		fingerBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(new FingerScene());
			}
		});
		//返回
		backBS = new ButtonSprite(0f,0f,ResourceManager.mainMenuButtons.getTextureRegion(5),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f / 7f, backBS);
		backBS.setPosition(mCameraWidth / 2f, fingerBS.getY()-10f-fingerBS.getHeight());
		mainMenuTheme.attachChild(backBS);
		backBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				
				mainMenuTheme.registerEntityModifier(new ScaleModifier(0.3f,1f,0f, new IEntityModifierListener(){

					public void onModifierStarted(IModifier<IEntity> pModifier,IEntity pItem) {
						// TODO Auto-generated method stub
						
					}

					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						unregisterTouchArea(themeBS);
						unregisterTouchArea(fingerBS);
						unregisterTouchArea(backBS);
						registerTouchArea(chooseModeBS);
						registerTouchArea(shopModeBS);
						registerTouchArea(helpModeBS);
						mainMenuTheme.setVisible(false);
						mainMenuScreen.setVisible(true);
						mainMenuScreen.registerEntityModifier(new ScaleModifier(0.3f,0f,1f, new IEntityModifierListener(){

							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
								// TODO Auto-generated method stub
								
							}

							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								// TODO Auto-generated method stub
								
							}
							
						},EaseElasticInOut.getInstance()));
					}
				},EaseElasticInOut.getInstance()));
			}
		});
		unregisterTouchArea(themeBS);
		unregisterTouchArea(fingerBS);
		unregisterTouchArea(backBS);
		mainMenuTheme.setAlpha(0f);
		mainMenuTheme.setScale(0f);
		mainMenuTheme.setVisible(false);
		attachChild(mainMenuTheme);
		
	}
	
	@Override
	public void onShowScene() {
//		if(!this.backgroundSprite.hasParent()) {
//			this.attachChild(this.backgroundSprite);
//			this.sortChildren();
//		}
		
		//sortChildren();
	}

	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChildren();
				for(int i = 0; i < INSTANCE.getChildCount(); i++){
					INSTANCE.getChildByIndex(i).dispose();
					INSTANCE.getChildByIndex(i).clearEntityModifiers();
					INSTANCE.getChildByIndex(i).clearUpdateHandlers();
				}
				INSTANCE.clearEntityModifiers();
				INSTANCE.clearTouchAreas();
				INSTANCE.clearUpdateHandlers();
			}});
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

}
