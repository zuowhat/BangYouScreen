package org.game.bangyouscreen.scene;


import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.EntityUtil;

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
		
		//进入测试界面
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
		
		Sprite authorInfo1 = new Sprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(0),mVertexBufferObjectManager);
		EntityUtil.setSizeInParent("width", 3f/4f, authorInfo1, playInfoBG);
		authorInfo1.setPosition(playInfoBG.getWidth()/2f, playInfoBG.getHeight()/2f);
		playInfoBG.attachChild(authorInfo1);
		
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
			EntityUtil.setSizeInParent("height", 4f/5f, s, infoArray[i]);
			s.setPosition(5f+s.getWidth()/2f, infoArray[i].getHeight()/2f);
			infoArray[i].attachChild(s);
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
		// TODO Auto-generated method stub
		
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
