package org.game.bangyouscreen.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.util.EntityUtil;

/**
 * 装备库
 * @author zuowhat 2013-12-14
 * @version 1.0
 */
public class ShopScene extends ManagedScene implements IScrollDetectorListener{

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
	private Sprite shareInfoBG;
	private Rectangle weaponInfoBG_S;
	private Rectangle magicInfoBG_S;
	private Rectangle propsInfoBG_S;
	private Rectangle shareInfoBG_S;
	private SurfaceScrollDetector mScrollDetector;
	private boolean isScrolling = false;
	private boolean isTouch = true;//控制道具栏不能滚动
	
	
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
		weaponInfoBG = new Sprite(0f,0f,ResourceManager.shopInfoBG,mVertexBufferObjectManager);
		magicInfoBG = new Sprite(0f,0f,ResourceManager.shopInfoBG,mVertexBufferObjectManager);
		propsInfoBG = new Sprite(0f,0f,ResourceManager.shopInfoBG,mVertexBufferObjectManager);
		weaponInfoBG_S = new Rectangle(0f, 0f, 0f, 0f, mVertexBufferObjectManager);
		magicInfoBG_S = new Rectangle(0f, 0f, 0f, 0f, mVertexBufferObjectManager);
		propsInfoBG_S = new Rectangle(0f, 0f, 0f, 0f, mVertexBufferObjectManager);
		
		if(mScrollDetector == null){
			mScrollDetector = new SurfaceScrollDetector(this);
		}
		mScrollDetector.setTriggerScrollMinimumDistance(10f);
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
		shopMenuBG.registerEntityModifier(new MoveModifier(0.5f, shopMenuBG.getX(), shopMenuBG.getY(), 
				shopMenuBG.getWidth()/2f, shopMenuBG.getY(), EaseElasticInOut.getInstance()));
		
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
				clickMenu(propFontBS,propFontClickSprite,propsInfoBG,propsInfoBG_S);
				shareFontClickSprite = propFontClickSprite;
				shareFontBS = propFontBS;	
				shareInfoBG = propsInfoBG;
				shareInfoBG_S = propsInfoBG_S;
				isTouch = false;
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
				clickMenu(magicFontBS,magicFontClickSprite,magicInfoBG,magicInfoBG_S);
				shareFontClickSprite = magicFontClickSprite;
				shareFontBS = magicFontBS;	
				shareInfoBG = magicInfoBG;
				shareInfoBG_S = magicInfoBG_S;
				isTouch = true;
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
				clickMenu(weaponFontBS,weaponFontClickSprite,weaponInfoBG,weaponInfoBG_S);
				shareFontClickSprite = weaponFontClickSprite;
				shareFontBS = weaponFontBS;
				shareInfoBG = weaponInfoBG;
				shareInfoBG_S = weaponInfoBG_S;
				isTouch = true;
			}
		});
		shareFontBS = weaponFontBS;
		
		Sprite propTopBG = new Sprite(0f,0f,ResourceManager.shopPropBG.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 342f/800f, propTopBG);
		propTopBG.setPosition(propTopBG.getWidth()/2f, mCameraHeight+propTopBG.getHeight()/2f);
		propTopBG.registerEntityModifier(new MoveModifier(0.5f, propTopBG.getX(), propTopBG.getY(),propTopBG.getX(), 
				mCameraHeight-propTopBG.getHeight()/2f, EaseElasticInOut.getInstance()));
		attachChild(propTopBG);
		
		Sprite propBottomBG = new Sprite(0f,0f,ResourceManager.shopPropBG.getTextureRegion(1),mVertexBufferObjectManager);
		propBottomBG.setSize(propTopBG.getWidth(), propTopBG.getHeight());
		propBottomBG.setPosition(propBottomBG.getWidth()/2f, -propBottomBG.getHeight()/2f);
		propBottomBG.registerEntityModifier(new MoveModifier(0.5f, propBottomBG.getX(), propBottomBG.getY(),
				propBottomBG.getX(), propBottomBG.getHeight()/2f, EaseElasticInOut.getInstance()));
		attachChild(propBottomBG);
		
		addInfoBGByType("weapon",weaponInfoBG,weaponInfoBG_S,9);
		addInfoBGByType("magic",magicInfoBG,magicInfoBG_S,9);
		addInfoBGByType("prop",propsInfoBG,propsInfoBG_S,3);
		attachChild(shopMenuBG);
		

		
		
		
		
		
	}
	
	/**
	 * 添加道具展示区域背景(包括块和栏)
	 * @author zuowhat 2013-12-20
	 * @param type 类型(包含武器,魔法,道具)
	 * @param infoBG 右侧圆角白色大背景
	 * @param infoBG_S 可以滚动的背景(不显示),包含所有栏目
	 * @param infoNum 栏目的数量
	 * @since 1.0
	 */
	private void addInfoBGByType(String type, Sprite infoBG, Rectangle infoBG_S, int infoNum){
		EntityUtil.setSize("height", 1f, infoBG);
		infoBG.setPosition(mCameraWidth+infoBG.getWidth()/2f, mCameraHeight/2f);
		
		//参照物
		Sprite temp = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("width", 41f/44f, temp, infoBG);
		temp.setAlpha(0f);
		infoBG.attachChild(temp);
		
		infoBG_S.setSize(infoBG.getWidth()*(41f/44f), temp.getHeight()*9f+80f);
		infoBG_S.setPosition(mCameraWidth+infoBG_S.getWidth()/2f, mCameraHeight-infoBG_S.getHeight()/2f);
		infoBG_S.setAlpha(0f);
		attachChild(infoBG_S);
		attachChild(infoBG);
		
		Sprite[] infoArray = new Sprite[infoNum];
		for(int i=0; i<infoArray.length; i++){
			if(i%2==0){
				infoArray[i] = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(1),mVertexBufferObjectManager); 
			}else{
				infoArray[i] = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(0),mVertexBufferObjectManager); 
			}
			infoArray[i].setSize(temp.getWidth(), temp.getHeight());
			if(i == 0){
				infoArray[i].setPosition(infoBG_S.getWidth()/2f, infoBG_S.getHeight()-infoArray[i].getHeight()/2f);
			}else{
				infoArray[i].setPosition(infoBG_S.getWidth()/2f, infoArray[i-1].getY()-infoArray[i].getHeight()-10f);
			}
			infoBG_S.attachChild(infoArray[i]);
		}
		
		if("weapon".equals(type)){
			infoBG.registerEntityModifier(new MoveModifier(0.5f, infoBG.getX(), infoBG.getY(),
					mCameraWidth-infoBG.getWidth()/2f, infoBG.getY(), EaseElasticInOut.getInstance()));
			infoBG_S.registerEntityModifier(new MoveModifier(0.5f, infoBG_S.getX(), infoBG_S.getY(),
					mCameraWidth-infoBG_S.getWidth()/2f, mCameraHeight-infoBG_S.getHeight()/2f, EaseElasticInOut.getInstance()));
			shareInfoBG = infoBG;
			shareInfoBG_S = infoBG_S;
			addInfoByType(infoArray,ResourceManager.weaponInfosTTR);
		}else if("magic".equals(type)){
			Sprite s = new Sprite(infoBG.getWidth()/2f,infoBG.getHeight()/2f,ResourceManager.magicTTR.getTextureRegion(0),mVertexBufferObjectManager);
			infoBG.attachChild(s);
		}else{
			Sprite s = new Sprite(infoBG.getWidth()/2f,infoBG.getHeight()/2f,ResourceManager.propsTTR.getTextureRegion(0),mVertexBufferObjectManager);
			infoBG.attachChild(s);
		}
		
		
	}
	
	/**
	 * 添加道具信息(包括图标,描述,金币,购买,装备)
	 * @author zuowhat 2013-12-20
	 * @param infoSpriteArray 栏目精灵数组
	 * @param infoPicTTR 图标
	 * @param infoContentTTR 描述信息
	 * @since 1.0
	 */
	private void addInfoByType(Sprite[] infoSpriteArray, TiledTextureRegion infoContentTTR){
		for(int i=0; i<infoSpriteArray.length; i++){
			Sprite s = new Sprite(0f,0f,infoContentTTR.getTextureRegion(i),mVertexBufferObjectManager);
			EntityUtil.setSizeInParent("height", 4f/5f, s, infoSpriteArray[i]);
			//s.setSize(infoSpriteArray[i].getWidth()*(4f/5f), infoSpriteArray[i].getHeight()*(4f/5f));
			s.setPosition(s.getWidth()/2f, infoSpriteArray[i].getHeight()/2f);
			//s.setZIndex(100);
			infoSpriteArray[i].attachChild(s);
		}
		
		
		
		
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
	private void clickMenu(Entity entityBS,Entity entitySprite, Sprite infoBG, Rectangle infoBG_S ){
		entityBS.setVisible(false);
		unregisterTouchArea(entityBS);
		entitySprite.setVisible(true);
		
		shareFontClickSprite.setVisible(false);
		shareFontBS.setVisible(true);
		registerTouchArea(shareFontBS);
		
		shareInfoBG.registerEntityModifier(new MoveYModifier(0.2f, shareInfoBG.getY(), -shareInfoBG.getHeight()/2f));
		shareInfoBG_S.registerEntityModifier(new MoveYModifier(0.2f, shareInfoBG_S.getY(), -shareInfoBG_S.getHeight()/2f));
		infoBG.registerEntityModifier(new MoveModifier(0.5f, mCameraWidth+infoBG.getWidth()/2f, mCameraHeight/2f,
				mCameraWidth-infoBG.getWidth()/2f, mCameraHeight/2f, EaseElasticInOut.getInstance()));
		infoBG_S.registerEntityModifier(new MoveModifier(0.5f, mCameraWidth+infoBG_S.getWidth()/2f, mCameraHeight-infoBG_S.getHeight()/2f,
				mCameraWidth-infoBG_S.getWidth()/2f, mCameraHeight-infoBG_S.getHeight()/2f, EaseElasticInOut.getInstance()));
	}

	public boolean onSceneTouchEvent(TouchEvent paramTouchEvent){
		if(paramTouchEvent.getX() > shareInfoBG.getX()-shareInfoBG.getWidth()/2f){
			isScrolling = true;
		}else{
			isScrolling = false;
		}
	    this.mScrollDetector.onTouchEvent(paramTouchEvent);
	    return super.onSceneTouchEvent(paramTouchEvent);
	}
	
	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		if(isScrolling && isTouch){
			shareInfoBG_S.setY(shareInfoBG_S.getY()-pDistanceY);
		}
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		if(shareInfoBG_S.getY()+shareInfoBG_S.getHeight()/2f <= mCameraHeight){
			shareInfoBG_S.registerEntityModifier(new MoveYModifier(0.5f,shareInfoBG_S.getY(),mCameraHeight-shareInfoBG_S.getHeight()/2f));
		}else if(shareInfoBG_S.getY()-shareInfoBG_S.getHeight()/2f >= 0f){
			shareInfoBG_S.registerEntityModifier(new MoveYModifier(0.5f,shareInfoBG_S.getY(),shareInfoBG_S.getHeight()/2f));
		}
	}

}
