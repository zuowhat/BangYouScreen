package org.game.bangyouscreen.scene;


import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
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
	
	private Sprite shareFontClickSprite;
	private ButtonSprite shareFontBS;
	private Sprite playClickSprite;
	private ButtonSprite playBS;
	private Sprite statisticsClickSprite;
	private ButtonSprite statisticsBS;
	private Sprite authorClickSprite;
	private ButtonSprite authorBS;
	private Sprite helpInfoBG;
	
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
		Sprite helpMenuBG = new Sprite(0f,0f, ResourceManager.shopMenuBG,mVertexBufferObjectManager);
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
				clickMenu(authorBS,authorClickSprite);
				shareFontClickSprite = authorClickSprite;
				shareFontBS = authorBS;	
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
				clickMenu(statisticsBS,statisticsClickSprite);
				shareFontClickSprite = statisticsClickSprite;
				shareFontBS = statisticsBS;	
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
		shareFontClickSprite = playClickSprite;
		
		//玩法文本
		playBS = new ButtonSprite(0f,0f,ResourceManager.helpTitleTTR.getTextureRegion(0),mVertexBufferObjectManager);
		playBS.setSize(playClickSprite.getWidth(), playClickSprite.getHeight());
		playBS.setPosition(playClickSprite.getX(), playClickSprite.getY());
		playBS.setVisible(false);
		helpMenuBG.attachChild(playBS);
		playBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				clickMenu(playBS,playClickSprite);
				shareFontClickSprite = playClickSprite;
				shareFontBS = playBS;
			}
		});
		shareFontBS = playBS;
		
		helpInfoBG = new Sprite(0f,0f,ResourceManager.shopInfoBG,mVertexBufferObjectManager);
		
		
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
	private void clickMenu(Entity entityBS,Entity entitySprite){
		entityBS.setVisible(false);
		unregisterTouchArea(entityBS);
		entitySprite.setVisible(true);
		
		shareFontClickSprite.setVisible(false);
		shareFontBS.setVisible(true);
		registerTouchArea(shareFontBS);
		
	}

}
