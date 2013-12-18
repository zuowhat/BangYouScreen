package org.game.bangyouscreen.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.EntityUtil;

/**
 * 装备库
 * @author zuowhat 2013-12-14
 * @version 1.0
 */
public class ShopScene extends ManagedScene implements IOnSceneTouchListener{

	private static final ShopScene INSTANCE = new ShopScene();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private ButtonSprite propFontBS;
	private Sprite propFontClickSprite;
	private ButtonSprite magicFontBS;
	private Sprite magicFontClickSprite;
	private ButtonSprite weaponFontBS;
	private Sprite weaponFontClickSprite;
	private ButtonSprite shareFontBS;
	private Sprite shareFontClickSprite;
	private Sprite weaponInfoBG;
	private Sprite magicInfoBG;
	private Sprite propsInfoBG;
	private boolean mIsScrolling = false;
	private Rectangle infoBackground;
    private float prevY = 0.0F;
	
	public static ShopScene getInstance(){
		return INSTANCE;
	}
	
	public Scene onLoadingScreenLoadAndShown() {
		return null;
	}

	public void onLoadingScreenUnloadAndHidden() {
		
	}

	public void onLoadScene() {
		ResourceManager.loadShopResources();
		setOnSceneTouchListener(this);
		Sprite backgroundSprite = new Sprite(0f,0f, ResourceManager.shopBG,mVertexBufferObjectManager);
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.mainMenuBackgroundTR.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-100);
		attachChild(backgroundSprite);
		
		//主页按钮
//		ButtonSprite homeBS = new ButtonSprite(0f,0f,ResourceManager.homeTR,mVertexBufferObjectManager);
//		homeBS.setPosition(10f+homeBS.getWidth()/2f, mCameraHeight-10f-homeBS.getHeight()/2f);
//		attachChild(homeBS);
//		homeBS.setOnClickListener(new OnClickListener(){
//			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				SFXManager.getInstance().playSound("a_click");
//				SceneManager.getInstance().showScene(MainMenuScene.getInstance());
//			}
//		});
//		registerTouchArea(homeBS);
		
		//菜单背景
		Sprite shopMenuBG = new Sprite(0f,0f, ResourceManager.shopMenuBG,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 0.5f, shopMenuBG);
		shopMenuBG.setPosition(0f-shopMenuBG.getWidth()/2f, mCameraHeight/2f);
		shopMenuBG.registerEntityModifier(new MoveModifier(0.5f, shopMenuBG.getX(), shopMenuBG.getY(), shopMenuBG.getWidth()/2f, shopMenuBG.getY(), EaseElasticInOut.getInstance()));
		
		
		//道具文本
		propFontBS = new ButtonSprite(0f,0f,ResourceManager.shopMenu.getTextureRegion(4),mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("width", 3f/4f, propFontBS, shopMenuBG);
		//文本之间空隙的高度
		float spaceHeight = (shopMenuBG.getHeight() - (3f*propFontBS.getHeight()))/4f;
		propFontBS.setPosition(shopMenuBG.getWidth()/2f, spaceHeight+propFontBS.getHeight()/2f);
		shopMenuBG.attachChild(propFontBS);
		propFontBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(propFontBS,propFontClickSprite);
				shareFontClickSprite = propFontClickSprite;
				shareFontBS = propFontBS;	
				
				
			}
		});
		registerTouchArea(propFontBS);
		
		//道具文本(点击后)
		propFontClickSprite = new Sprite(0f,0f,ResourceManager.shopMenu.getTextureRegion(5),mVertexBufferObjectManager);
		propFontClickSprite.setSize(propFontBS.getWidth(), propFontBS.getHeight());
		propFontClickSprite.setPosition(propFontBS.getX(), propFontBS.getY());
		propFontClickSprite.setVisible(false);
		shopMenuBG.attachChild(propFontClickSprite);
		
		//魔法文本
		magicFontBS = new ButtonSprite(0f,0f,ResourceManager.shopMenu.getTextureRegion(2),mVertexBufferObjectManager);
		magicFontBS.setSize(propFontBS.getWidth(), propFontBS.getHeight());
		magicFontBS.setPosition(shopMenuBG.getWidth()/2f, propFontBS.getY()+spaceHeight+propFontBS.getHeight());
		shopMenuBG.attachChild(magicFontBS);
		magicFontBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(magicFontBS,magicFontClickSprite);
				shareFontClickSprite = magicFontClickSprite;
				shareFontBS = magicFontBS;	
				
				
				
			}
		});
		registerTouchArea(magicFontBS);
		
		//魔法文本(点击后)
		magicFontClickSprite = new Sprite(0f,0f,ResourceManager.shopMenu.getTextureRegion(3),mVertexBufferObjectManager);
		magicFontClickSprite.setSize(magicFontBS.getWidth(), magicFontBS.getHeight());
		magicFontClickSprite.setPosition(magicFontBS.getX(), magicFontBS.getY());
		magicFontClickSprite.setVisible(false);
		shopMenuBG.attachChild(magicFontClickSprite);
		
		//武器文本(点击后)
		weaponFontClickSprite = new Sprite(0f,0f,ResourceManager.shopMenu.getTextureRegion(1),mVertexBufferObjectManager);
		weaponFontClickSprite.setSize(propFontBS.getWidth(), propFontBS.getHeight());
		weaponFontClickSprite.setPosition(shopMenuBG.getWidth()/2f, magicFontBS.getY()+spaceHeight+magicFontBS.getHeight());
		shopMenuBG.attachChild(weaponFontClickSprite);
		shareFontClickSprite = weaponFontClickSprite;
		
		//武器文本
		weaponFontBS = new ButtonSprite(0f,0f,ResourceManager.shopMenu.getTextureRegion(0),mVertexBufferObjectManager);
		weaponFontBS.setSize(weaponFontClickSprite.getWidth(), weaponFontClickSprite.getHeight());
		weaponFontBS.setPosition(weaponFontClickSprite.getX(), weaponFontClickSprite.getY());
		weaponFontBS.setVisible(false);
		shopMenuBG.attachChild(weaponFontBS);
		weaponFontBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(weaponFontBS,weaponFontClickSprite);
				shareFontClickSprite = weaponFontClickSprite;
				shareFontBS = weaponFontBS;
				
			}
		});
		shareFontBS = weaponFontBS;
		
		Sprite propTopBG = new Sprite(0f,0f,ResourceManager.shopPropBG.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 342f/800f, propTopBG);
		propTopBG.setPosition(propTopBG.getWidth()/2f, mCameraHeight+propTopBG.getHeight()/2f);
		propTopBG.registerEntityModifier(new MoveModifier(0.5f, propTopBG.getX(), propTopBG.getY(),propTopBG.getX(), mCameraHeight-propTopBG.getHeight()/2f, EaseElasticInOut.getInstance()));
		attachChild(propTopBG);
		
		Sprite propBottomBG = new Sprite(0f,0f,ResourceManager.shopPropBG.getTextureRegion(1),mVertexBufferObjectManager);
		propBottomBG.setSize(propTopBG.getWidth(), propTopBG.getHeight());
		propBottomBG.setPosition(propBottomBG.getWidth()/2f, -propBottomBG.getHeight()/2f);
		propBottomBG.registerEntityModifier(new MoveModifier(0.5f, propBottomBG.getX(), propBottomBG.getY(),propBottomBG.getX(), propBottomBG.getHeight()/2f, EaseElasticInOut.getInstance()));
		attachChild(propBottomBG);
		
		weaponInfoBG = new Sprite(0f,0f,ResourceManager.shopInfoBG,mVertexBufferObjectManager){
		      public boolean onAreaTouched(TouchEvent paramTouchEvent, float paramFloat1, float paramFloat2){
		        if (paramTouchEvent.getAction() == 0)
		          prevY = paramTouchEvent.getY();
		        if (paramTouchEvent.getAction() == 2){
		          float f = paramTouchEvent.getY() - prevY;
		          infoBackground.setPosition(infoBackground.getX(), f + infoBackground.getY());
		          prevY = paramTouchEvent.getY();
		        }
		        return super.onAreaTouched(paramTouchEvent, paramFloat1, paramFloat2);
		      }
		};
		EntityUtil.setSize("height", 1f, weaponInfoBG);
		//float f1 = (mCameraWidth - shopMenuBG.getWidth() - weaponInfoBG.getWidth())/2f;
		weaponInfoBG.setPosition(mCameraWidth+weaponInfoBG.getWidth()/2f, mCameraHeight/2f);
		weaponInfoBG.registerEntityModifier(new MoveModifier(0.5f, weaponInfoBG.getX(), weaponInfoBG.getY(),mCameraWidth-weaponInfoBG.getWidth()/2f, weaponInfoBG.getY(), EaseElasticInOut.getInstance()));
		
		//参照物
		final Sprite tempSprite = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(0),mVertexBufferObjectManager);
		tempSprite.setPosition(weaponInfoBG.getWidth()/2f, weaponInfoBG.getHeight()/2f);
		EntityUtil.setSizeInParent("width", 1f, tempSprite,weaponInfoBG);
		tempSprite.setVisible(false);
		attachChild(tempSprite);
		
		infoBackground = new Rectangle(0f, 0f, 0f, 0f, mVertexBufferObjectManager){
		      protected void onManagedUpdate(float paramFloat){
		        if ((infoBackground.getY() > weaponInfoBG.getY()) && (!mIsScrolling))
		          setPosition(getX(), getY() - 3.0F);
		        if ((infoBackground.getY() + infoBackground.getHeightScaled() - tempSprite.getHeight()/2f < weaponInfoBG.getY()) && (!mIsScrolling))
		          setPosition(getX(), 3.0F + getY());
		        super.onManagedUpdate(paramFloat);
		      }
		};
		infoBackground.setPosition(weaponInfoBG.getX(), weaponInfoBG.getY());
		infoBackground.setSize(9f*tempSprite.getWidth(), 9f*tempSprite.getHeight());
		infoBackground.setAlpha(0f);
		//infoBackground.setColor(4f, 1f, 5f);
		attachChild(infoBackground);
		registerTouchArea(infoBackground);
		registerUpdateHandler(infoBackground);
		
		attachChild(weaponInfoBG);
		registerTouchArea(weaponInfoBG);
		attachChild(shopMenuBG);
		
		Sprite a = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(1),mVertexBufferObjectManager);
		a.setPosition(infoBackground.getWidth()/2f, infoBackground.getHeight()/2f);
		a.setSize(tempSprite.getWidth(), tempSprite.getHeight());
		infoBackground.attachChild(a);
		
		
		
		
		
	}

	public void onShowScene() {
		// TODO Auto-generated method stub
		
	}

	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

	public void onUnloadScene() {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChildren();
				for(int i = 0; i < INSTANCE.getChildCount(); i++){
					INSTANCE.getChildByIndex(i).dispose();
					INSTANCE.getChildByIndex(i).clearEntityModifiers();
					//INSTANCE.getChildByIndex(i).clearTouchAreas();
					INSTANCE.getChildByIndex(i).clearUpdateHandlers();
				}
				INSTANCE.clearEntityModifiers();
				INSTANCE.clearTouchAreas();
				INSTANCE.clearUpdateHandlers();
			}});
	}
	
	/**
	 * 切换菜单文本
	 * @author zuowhat 2013-12-14
	 * @since 1.0
	 */
	private void clickMenu(Entity entityBS,Entity entitySprite){
		entityBS.setVisible(false);
		unregisterTouchArea(entityBS);
		entitySprite.setVisible(true);
		
		shareFontClickSprite.setVisible(false);
		shareFontBS.setVisible(true);
		registerTouchArea(shareFontBS);
	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	    if (pSceneTouchEvent.getAction() == 0){
	      mIsScrolling = true;
	      return true;
	    }
	    if (pSceneTouchEvent.getAction() == 1){
	      mIsScrolling = false;
	      return true;
	    }
		return false;
	}

}
