package org.game.bangyouscreen.scene;


import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.DataConstant;
import org.game.bangyouscreen.util.EntityUtil;
import org.game.bangyouscreen.util.GameNumberUtil;

public class HelpScene extends ManagedScene{

	private static final HelpScene INSTANCE = new HelpScene();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
	
	private Sprite currentFontClickSprite;
	private ButtonSprite currentFontBS;
	private Sprite playClickSprite;
	private ButtonSprite playBS;
	private Sprite statisticsClickSprite;
	private ButtonSprite statisticsBS;
	private Sprite authorClickSprite;
	private ButtonSprite authorBS;
	private Sprite playInfoBG;
	private Sprite statisticsInfoBG;
	private Sprite authorInfoBG;
	private Sprite currentInfoBG;
	private int arrowNum = 1;
	private Rectangle playInfoBG_S;
	private boolean isArrowUp = true;
	private boolean isArrowDown = true;
	
	
	
	public static HelpScene getInstance(){
		return INSTANCE;
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
	public void onLoadScene() {
		ResourceManager.loadHelpResources();
		
		Sprite backgroundSprite = new Sprite(0f,0f, ResourceManager.helpBG,mVertexBufferObjectManager);
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.mainMenuBackgroundTR.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-100);
		attachChild(backgroundSprite);
		
		//进入数据测试界面
		ButtonSprite testBS = new ButtonSprite(0f,0f, ResourceManager.gameGold,mVertexBufferObjectManager);
		testBS.setPosition(testBS.getWidth(), mCameraHeight-testBS.getHeight());
		attachChild(testBS);
		testBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(new TestDataScene());
			}
		});
		registerTouchArea(testBS);
		
		
		
		//菜单背景
		Sprite helpMenuBG = new Sprite(0f,0f, ResourceManager.helpMenuBG,mVertexBufferObjectManager);
		EntityUtil.setSize("height", 0.5f, helpMenuBG);
		helpMenuBG.setPosition(0f-helpMenuBG.getWidth()/2f, mCameraHeight/2f);
		helpMenuBG.registerEntityModifier(new MoveModifier(0.5f, helpMenuBG.getX(), helpMenuBG.getY(), 
				helpMenuBG.getWidth()/2f, helpMenuBG.getY(), EaseElasticInOut.getInstance()));
		attachChild(helpMenuBG);
		
		//制作方
		authorBS = new ButtonSprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(4),mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("width", 3f/4f, authorBS, helpMenuBG);
		//文本之间空隙的高度
		float spaceHeight = (helpMenuBG.getHeight() - (3f*authorBS.getHeight()))/4f;
		authorBS.setPosition(helpMenuBG.getWidth()/2f, spaceHeight+authorBS.getHeight()/2f);
		helpMenuBG.attachChild(authorBS);
		authorBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(authorBS,authorClickSprite,authorInfoBG);
				currentFontClickSprite = authorClickSprite;
				currentFontBS = authorBS;	
				currentInfoBG = authorInfoBG;
			}
		});
		registerTouchArea(authorBS);
		
		//制作方(点击后)
		authorClickSprite = new Sprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(5),mVertexBufferObjectManager);
		authorClickSprite.setSize(authorBS.getWidth(), authorBS.getHeight());
		authorClickSprite.setPosition(authorBS.getX(), authorBS.getY());
		authorClickSprite.setVisible(false);
		helpMenuBG.attachChild(authorClickSprite);
		
		//统计文本
		statisticsBS = new ButtonSprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(2),mVertexBufferObjectManager);
		statisticsBS.setSize(authorBS.getWidth(), authorBS.getHeight());
		statisticsBS.setPosition(helpMenuBG.getWidth()/2f, authorBS.getY()+spaceHeight+authorBS.getHeight());
		helpMenuBG.attachChild(statisticsBS);
		statisticsBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(statisticsBS,statisticsClickSprite,statisticsInfoBG);
				currentFontClickSprite = statisticsClickSprite;
				currentFontBS = statisticsBS;	
				currentInfoBG = statisticsInfoBG;
			}
		});
		registerTouchArea(statisticsBS);
		
		//统计文本(点击后)
		statisticsClickSprite = new Sprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(3),mVertexBufferObjectManager);
		statisticsClickSprite.setSize(statisticsBS.getWidth(), statisticsBS.getHeight());
		statisticsClickSprite.setPosition(statisticsBS.getX(), statisticsBS.getY());
		statisticsClickSprite.setVisible(false);
		helpMenuBG.attachChild(statisticsClickSprite);
		
		//玩法文本(点击后)
		playClickSprite = new Sprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(1),mVertexBufferObjectManager);
		playClickSprite.setSize(authorBS.getWidth(), authorBS.getHeight());
		playClickSprite.setPosition(helpMenuBG.getWidth()/2f, statisticsBS.getY()+spaceHeight+statisticsBS.getHeight());
		helpMenuBG.attachChild(playClickSprite);
		currentFontClickSprite = playClickSprite;
		
		//玩法文本
		playBS = new ButtonSprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(0),mVertexBufferObjectManager);
		playBS.setSize(playClickSprite.getWidth(), playClickSprite.getHeight());
		playBS.setPosition(playClickSprite.getX(), playClickSprite.getY());
		playBS.setVisible(false);
		helpMenuBG.attachChild(playBS);
		playBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(playBS,playClickSprite,playInfoBG);
				currentFontClickSprite = playClickSprite;
				currentFontBS = playBS;
				currentInfoBG = playInfoBG;
			}
		});
		currentFontBS = playBS;
		
		//玩法介绍
		playInfoBG = new Sprite(0f,0f,ResourceManager.helpInfoBG,mVertexBufferObjectManager);
		playInfoBG.setSize(mCameraWidth-(3f/2f)*helpMenuBG.getWidth(), mCameraHeight);
		playInfoBG.setPosition(mCameraWidth+playInfoBG.getWidth()/2f, mCameraHeight/2f);
		attachChild(playInfoBG);
		playInfoBG.registerEntityModifier(new MoveModifier(0.5f, playInfoBG.getX(), playInfoBG.getY(),
				mCameraWidth-playInfoBG.getWidth()/2f, playInfoBG.getY(), EaseElasticInOut.getInstance()));
		currentInfoBG = playInfoBG;
		
		//假设有3层
		playInfoBG_S = new Rectangle(0f,0f,0f,0f,mVertexBufferObjectManager);
		playInfoBG_S.setSize(playInfoBG.getWidth()*(41f/44f), playInfoBG.getHeight()*3);
		playInfoBG_S.setPosition(playInfoBG.getWidth()-playInfoBG_S.getWidth()/2f, -mCameraHeight/2f);
		playInfoBG_S.setAlpha(0f);
		playInfoBG.attachChild(playInfoBG_S);
		
		for(int i=0; i<3; i++){
			Sprite s = new Sprite(0f,0f,ResourceManager.helpExplanation[i],mVertexBufferObjectManager);
			EntityUtil.setSizeInParent("width", 7f/8f, s, playInfoBG_S);
			s.setPosition(playInfoBG_S.getWidth()/2f, playInfoBG_S.getHeight()*(5f/6f)-i*playInfoBG_S.getHeight()/3f);
			playInfoBG_S.attachChild(s);
		}
		
		//向上箭头
		ButtonSprite arrowUpSprite = new ButtonSprite(0f,0f,ResourceManager.arrowTTR.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f/10f, arrowUpSprite);
		arrowUpSprite.setPosition(playInfoBG_S.getWidth()/2f, playInfoBG.getHeight()-10f-arrowUpSprite.getHeight()/2f);
		playInfoBG.attachChild(arrowUpSprite);
		arrowUpSprite.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				if(arrowNum > 1 && arrowNum <= 3 && isArrowUp){
					isArrowUp = false;
					playInfoBG_S.registerEntityModifier(new MoveYModifier(0.3F, playInfoBG_S.getY(), playInfoBG_S.getY()-playInfoBG_S.getHeight()/3f,
							new IEntityModifierListener(){

						@Override
						public void onModifierStarted(
								IModifier<IEntity> pModifier,
								IEntity pItem) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onModifierFinished(
								IModifier<IEntity> pModifier,
								IEntity pItem) {
							isArrowUp = true;
						}
					}));
					arrowNum--;
				}
			}
		});
		registerTouchArea(arrowUpSprite);
		//向下箭头
		ButtonSprite arrowDownSprite = new ButtonSprite(0f,0f,ResourceManager.arrowTTR.getTextureRegion(1),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f/10f, arrowDownSprite);
		arrowDownSprite.setPosition(playInfoBG_S.getWidth()/2f, 10f+arrowDownSprite.getHeight()/2f);
		playInfoBG.attachChild(arrowDownSprite);
		arrowDownSprite.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				if(arrowNum < 3 && arrowNum >= 1 && isArrowDown){
					isArrowDown = false;
					playInfoBG_S.registerEntityModifier(new MoveYModifier(0.3F, playInfoBG_S.getY(), playInfoBG_S.getY()+playInfoBG_S.getHeight()/3f,
							new IEntityModifierListener(){

						@Override
						public void onModifierStarted(
								IModifier<IEntity> pModifier,
								IEntity pItem) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onModifierFinished(
								IModifier<IEntity> pModifier,
								IEntity pItem) {
							isArrowDown = true;
						}
					}));
					arrowNum++;
				}
			}
		});
		registerTouchArea(arrowDownSprite);
		
		//数据统计
		statisticsInfoBG = new Sprite(0f,0f,ResourceManager.helpInfoBG,mVertexBufferObjectManager);
		statisticsInfoBG.setSize(mCameraWidth-(3f/2f)*helpMenuBG.getWidth(), mCameraHeight);
		statisticsInfoBG.setPosition(mCameraWidth-statisticsInfoBG.getWidth()/2f, mCameraHeight/2f);
		statisticsInfoBG.setVisible(false);
		attachChild(statisticsInfoBG);
		
		Rectangle statisticsInfoBG_S = new Rectangle(0f, 0f, 0f, 0f, mVertexBufferObjectManager);
		statisticsInfoBG_S.setSize(statisticsInfoBG.getWidth()*(41f/44f), statisticsInfoBG.getHeight());
		statisticsInfoBG_S.setPosition(statisticsInfoBG.getWidth()-statisticsInfoBG_S.getWidth()/2f, statisticsInfoBG.getHeight()/2f);
		statisticsInfoBG_S.setAlpha(0f);
		statisticsInfoBG.attachChild(statisticsInfoBG_S);
		
		GameNumberUtil gnUtil = new GameNumberUtil();
		int[] statAll = new int[5];
		statAll[0] = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_BOSS);
		statAll[1] = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_GOLD);
		statAll[2] = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_DPS);
		statAll[3] = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_GOOD);
		statAll[4] = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.ALL_APPS);
		
		Sprite[] infoArray = new Sprite[5];
		for(int i=0; i<infoArray.length; i++){
			if(i%2==0){
				infoArray[i] = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(1),mVertexBufferObjectManager); 
			}else{
				infoArray[i] = new Sprite(0f,0f,ResourceManager.shopInfoRowsBG.getTextureRegion(0),mVertexBufferObjectManager); 
			}
			infoArray[i].setSize(statisticsInfoBG_S.getWidth(), statisticsInfoBG_S.getHeight()/6f);
			if(i == 0){
				infoArray[i].setPosition(statisticsInfoBG_S.getWidth()/2f, statisticsInfoBG_S.getHeight()-infoArray[i].getHeight()/2f);
			}else{
				infoArray[i].setPosition(statisticsInfoBG_S.getWidth()/2f, infoArray[i-1].getY()-infoArray[i].getHeight()-statisticsInfoBG_S.getHeight()/24f);
			}
			statisticsInfoBG_S.attachChild(infoArray[i]);
			Sprite s = new Sprite(0f,0f,ResourceManager.statPicTTR.getTextureRegion(i),mVertexBufferObjectManager);
			EntityUtil.setSizeInParent("height", 9f/10f, s, infoArray[i]);
			s.setPosition(5f+s.getWidth()/2f, infoArray[i].getHeight()/2f);
			infoArray[i].attachChild(s);
			float f = infoArray[i].getWidth()-(infoArray[i].getWidth()-(s.getX()+s.getWidth()/2f))/3f;
			gnUtil.addNumInHelpScene(i,f,infoArray[i], statAll[i]);
		}
		
		//制作方
		authorInfoBG = new Sprite(0f,0f,ResourceManager.helpInfoBG,mVertexBufferObjectManager);
		authorInfoBG.setSize(mCameraWidth-(3f/2f)*helpMenuBG.getWidth(), mCameraHeight);
		authorInfoBG.setPosition(mCameraWidth-authorInfoBG.getWidth()/2f, mCameraHeight/2f);
		authorInfoBG.setVisible(false);
		attachChild(authorInfoBG);
		Sprite authorTitle = new Sprite(0f,0f,ResourceManager.mainMenuTitleTR,mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("height", 1f/5f, authorTitle, authorInfoBG);
		authorTitle.setPosition(authorInfoBG.getWidth()/2f, mCameraHeight-authorTitle.getHeight()*(3f/4f));
		authorInfoBG.attachChild(authorTitle);
		Sprite authorInfo = new Sprite(0f,0f,ResourceManager.authorInfo,mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("width", 3f/4f, authorInfo, authorInfoBG);
		authorInfo.setPosition(authorInfoBG.getWidth()/2f, authorTitle.getHeight()/2f+authorInfo.getHeight()/2f);
		authorInfoBG.attachChild(authorInfo);
		
		
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
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
	 * @author zuowhat 2014-02-10
	 * @since 1.0
	 */
	private void clickMenu(Entity entityBS,Entity entitySprite, Sprite bgSprite){
		entityBS.setVisible(false);
		unregisterTouchArea(entityBS);
		entitySprite.setVisible(true);
		
		currentFontClickSprite.setVisible(false);
		currentFontBS.setVisible(true);
		registerTouchArea(currentFontBS);
		
		currentInfoBG.setVisible(false);
		bgSprite.setVisible(true);
		
	}

}
