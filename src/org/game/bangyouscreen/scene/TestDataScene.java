package org.game.bangyouscreen.scene;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.managers.ManagedScene;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.managers.SFXManager;
import org.game.bangyouscreen.managers.SceneManager;
import org.game.bangyouscreen.util.DataConstant;
import org.game.bangyouscreen.util.EntityUtil;

public class TestDataScene extends ManagedScene{
	
	private static final TestDataScene INSTANCE = new TestDataScene();
	private float mCameraWidth = ResourceManager.getCamera().getWidth();
	private float mCameraHeight = ResourceManager.getCamera().getHeight();
	private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();

	private int boss_hp;
	private int boss_type;
	private int boss_maxdef;
	private int boss_mindef;
	
	private int weapon_type;
	private int weapon_maxdps;
	private int weapon_mindps;
	
	private int magic_type;
	private int magic_maxaoe;
	private int magic_minaoe;
	
	public static TestDataScene getInstance(){
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
		ResourceManager.getInstance().loadTestResources();
		boss_hp = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_BOSSHP);
		boss_type = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_BOSSTYPE);
		boss_maxdef = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_BOSSMAXDEF);
		boss_mindef = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_BOSSMINDEF);
		
		weapon_type = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_WEAPONTYPE);
		weapon_maxdps = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_WEAPONMAXDPS);
		weapon_mindps = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_WEAPONMINDPS);
		
		magic_type = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_MAGICTYPE);
		magic_maxaoe = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_MAGICMAXAOE);
		magic_minaoe = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.TEST_MAGICMINAOE);
		
		Sprite backgroundSprite = new Sprite(0f,0f, ResourceManager.loadingBG,mVertexBufferObjectManager);
		backgroundSprite.setScale(ResourceManager.getInstance().cameraWidth / ResourceManager.loadingBG.getWidth());
		backgroundSprite.setPosition(mCameraWidth / 2f, mCameraHeight / 2f);
		backgroundSprite.setZIndex(-5000);
		attachChild(backgroundSprite);
		
		Text bossTitle = new Text(0f,0f,ResourceManager.sysFont,"BOSS属性",mVertexBufferObjectManager);
		bossTitle.setColor(255, 255, 255);
		bossTitle.setPosition(mCameraWidth/6f, mCameraHeight*(11f/12f));
		attachChild(bossTitle);
		
		addItem(11,"血量",boss_hp,9f,0f);
		addItem(12,"类型",boss_type,7f,0f);
		addItem(13,"上限",boss_maxdef,5f,0f);
		addItem(14,"下限",boss_mindef,3f,0f);
		
		addItem(21,"类型",weapon_type,9f,1f);
		addItem(22,"上限",weapon_maxdps,7f,1f);
		addItem(23,"下限",weapon_mindps,5f,1f);
		
		addItem(31,"类型",magic_type,9f,2f);
		addItem(32,"上限",magic_maxaoe,7f,2f);
		addItem(33,"下限",magic_minaoe,5f,2f);
		
		
		
		Text weaponProperty = new Text(0f,0f,ResourceManager.sysFont,"武器属性",mVertexBufferObjectManager);
		weaponProperty.setColor(255, 255, 255);
		weaponProperty.setPosition(mCameraWidth/2f, mCameraHeight*(11f/12f));
		attachChild(weaponProperty);
		
		Text magicProperty = new Text(0f,0f,ResourceManager.sysFont,"魔",mVertexBufferObjectManager);
		magicProperty.setColor(255, 255, 255);
		magicProperty.setPosition(mCameraWidth*(5f/6f), mCameraHeight*(11f/12f));
		attachChild(magicProperty);
		
		
		
		
		
		//保存
		ButtonSprite saveBS = new ButtonSprite(0f,0f,ResourceManager.test_saveOrBack.getTextureRegion(1),mVertexBufferObjectManager);
		EntityUtil.setSize("height", 1f/6f, saveBS);
		saveBS.setPosition(mCameraWidth*(1f/4f), mCameraHeight*(1f/12f));
		attachChild(saveBS);
		saveBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_BOSSHP, boss_hp);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_BOSSTYPE, boss_type);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_BOSSMAXDEF, boss_maxdef);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_BOSSMINDEF, boss_mindef);
				
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_WEAPONTYPE, weapon_type);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_WEAPONMAXDPS, weapon_maxdps);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_WEAPONMINDPS, weapon_mindps);
				
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_MAGICTYPE, magic_type);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_MAGICMAXAOE, magic_maxaoe);
				BangYouScreenActivity.writeIntToSharedPreferences(DataConstant.TEST_MAGICMINAOE, magic_minaoe);
			}
		});
		registerTouchArea(saveBS);
		
		//返回
		ButtonSprite backBS = new ButtonSprite(0f,0f,ResourceManager.test_saveOrBack.getTextureRegion(0),mVertexBufferObjectManager);
		backBS.setSize(saveBS.getWidth(), saveBS.getHeight());
		backBS.setPosition(mCameraWidth*(3f/4f), saveBS.getY());
		attachChild(backBS);
		backBS.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SFXManager.getInstance().playSound("a_click");
				SceneManager.getInstance().showScene(MainMenuScene.getInstance());
			}
		});
		registerTouchArea(backBS);
	}
	
	public void addItem(final int type, String title, int num, float rows, float cols){
		Text bossHPText = new Text(0f,0f,ResourceManager.sysFont,title,mVertexBufferObjectManager);
		bossHPText.setColor(255, 255, 255);
		bossHPText.setPosition((mCameraWidth*(cols/3f))+bossHPText.getWidth()/2f, mCameraHeight*(rows/12f));
		attachChild(bossHPText);
		
		ButtonSprite subtractBossHPButton = new ButtonSprite(0f,0f,ResourceManager.test_addOrSubtract.getTextureRegion(1),mVertexBufferObjectManager);
		subtractBossHPButton.setSize(bossHPText.getHeight(), bossHPText.getHeight());
		subtractBossHPButton.setPosition(bossHPText.getX()+2*subtractBossHPButton.getWidth(), bossHPText.getY());
		attachChild(subtractBossHPButton);
		final Text bossHPValue;
		if(type == 12){
			String titleType = "";
			if(num == 0){
				num = 1;
			}
			if(num == 1){
				//titleType = "光明系";
				titleType = "1";
			}else if(num == 2){
				//titleType = "暗黑系";
				titleType = "2";
			}else if(num == 3){
				//titleType = "混沌系";
				titleType = "3";
			}else if(num == 4){
				//titleType = "无";
				titleType = "4";
			}
			bossHPValue = new Text(0f,0f,ResourceManager.sysFont,titleType,99,mVertexBufferObjectManager);
		}else{
			bossHPValue = new Text(0f,0f,ResourceManager.sysFont,String.valueOf(num),99,mVertexBufferObjectManager);
		}
		bossHPValue.setColor(255, 255, 255);
		bossHPValue.setPosition(subtractBossHPButton.getX()+2*subtractBossHPButton.getWidth(), bossHPText.getY());
		attachChild(bossHPValue);
		//bossHPValue.setText("123");
		
		ButtonSprite addBossHPButton = new ButtonSprite(0f,0f,ResourceManager.test_addOrSubtract.getTextureRegion(0),mVertexBufferObjectManager);
		addBossHPButton.setSize(subtractBossHPButton.getWidth(), subtractBossHPButton.getHeight());
		addBossHPButton.setPosition(bossHPValue.getX()+2*subtractBossHPButton.getWidth(), bossHPText.getY());
		attachChild(addBossHPButton);
		addBossHPButton.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(type == 11){
					boss_hp = boss_hp + 100;
					bossHPValue.setText(String.valueOf(boss_hp));
				}else if(type == 12){
					boss_type++;
					if(boss_type > 4){
						boss_type = 1;
					}
					String titleType1 = "";
					if(boss_type == 1){
						//titleType1 = "光明系";
						titleType1 = "1";
					}else if(boss_type == 2){
						//titleType1 = "暗黑系";
						titleType1 = "2";
					}else if(boss_type == 3){
						//titleType1 = "混沌系";
						titleType1 = "3";
					}else if(boss_type == 4){
						//titleType1 = "无";
						titleType1 = "4";
					}
					bossHPValue.setText(titleType1);
				}else if(type == 13){
					boss_maxdef = boss_maxdef + 10;
					bossHPValue.setText(String.valueOf(boss_maxdef));
				}else if(type == 14){
					boss_mindef = boss_mindef + 10;
					bossHPValue.setText(String.valueOf(boss_mindef));
				}else if(type == 21){
					weapon_type++;
					if(weapon_type > 4){
						weapon_type = 1;
					}
					bossHPValue.setText(String.valueOf(weapon_type));
				}else if(type == 22){
					weapon_maxdps = weapon_maxdps + 10;
					bossHPValue.setText(String.valueOf(weapon_maxdps));
				}else if(type == 23){
					weapon_mindps = weapon_mindps + 10;
					bossHPValue.setText(String.valueOf(weapon_mindps));
				}else if(type == 31){
					magic_type++;
					if(magic_type > 4){
						magic_type = 1;
					}
					bossHPValue.setText(String.valueOf(magic_type));
				}else if(type == 32){
					magic_maxaoe = magic_maxaoe + 10;
					bossHPValue.setText(String.valueOf(magic_maxaoe));
				}else if(type == 33){
					magic_minaoe = magic_minaoe + 10;
					bossHPValue.setText(String.valueOf(magic_minaoe));
				}
				
				
				
				
				
				
			}
		});
		registerTouchArea(addBossHPButton);
		
		subtractBossHPButton.setOnClickListener(new OnClickListener(){
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(type == 11){
					boss_hp = boss_hp - 100;
					if(boss_hp < 0){
						boss_hp = 0;
					}
					bossHPValue.setText(String.valueOf(boss_hp));
				}else if(type == 12){
					boss_type--;
					if(boss_type < 1){
						boss_type = 4;
					}
					String titleType1 = "";
					if(boss_type == 1){
						//titleType1 = "光明系";
						titleType1 = "1";
					}else if(boss_type == 2){
						//titleType1 = "暗黑系";
						titleType1 = "2";
					}else if(boss_type == 3){
						//titleType1 = "混沌系";
						titleType1 = "3";
					}else if(boss_type == 4){
						//titleType1 = "无";
						titleType1 = "4";
					}
					bossHPValue.setText(titleType1);
				}else if(type == 13){
					boss_maxdef = boss_maxdef - 10;
					if(boss_maxdef < 0){
						boss_maxdef = 0;
					}
					bossHPValue.setText(String.valueOf(boss_maxdef));
				}else if(type == 14){
					boss_mindef = boss_mindef - 10;
					if(boss_mindef < 0){
						boss_mindef = 0;
					}
					bossHPValue.setText(String.valueOf(boss_mindef));
				}else if(type == 21){
					weapon_type--;
					if(weapon_type < 1){
						weapon_type = 4;
					}
					bossHPValue.setText(String.valueOf(weapon_type));
					
				}else if(type == 22){
					weapon_maxdps = weapon_maxdps - 10;
					if(weapon_maxdps < 0){
						weapon_maxdps = 0;
					}
					bossHPValue.setText(String.valueOf(weapon_maxdps));
				}else if(type == 23){
					weapon_mindps = weapon_mindps - 10;
					if(weapon_mindps < 0){
						weapon_mindps = 0;
					}
					bossHPValue.setText(String.valueOf(weapon_mindps));
				}else if(type == 31){
					magic_type--;
					if(magic_type < 1){
						magic_type = 4;
					}
					bossHPValue.setText(String.valueOf(magic_type));
					
				}else if(type == 32){
					magic_maxaoe = magic_maxaoe - 10;
					if(magic_maxaoe < 0){
						magic_maxaoe = 0;
					}
					bossHPValue.setText(String.valueOf(magic_maxaoe));
				}else if(type == 33){
					magic_minaoe = magic_minaoe - 10;
					if(magic_minaoe < 0){
						magic_minaoe = 0;
					}
					bossHPValue.setText(String.valueOf(magic_minaoe));
				}
				
				
				
				
				
				
			}
		});
		registerTouchArea(subtractBossHPButton);
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

}
