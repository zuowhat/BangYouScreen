package org.game.bangyouscreen.util;

import org.game.bangyouscreen.BangYouScreenActivity;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.model.BossModel;
import org.game.bangyouscreen.model.PlayerModel;

/**
 * 游戏数据常量
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class DataConstant {
	
	/**游戏初始化数据*/
	public static final int GOLD_INIT = 10000;//初始化游戏金币
	public static final int[] weaponPrice = {500,1500,3000,650,1800,3500,800,2000,5000};//武器价格
	public static final int[] magicPrice = {500,1500,3000,650,1800,3500,800,2000,5000};//魔法价格
	public static final int[] propPrice = {200,350,300};//道具价格
	public static final float CLOCKTIME = 10f;//时光沙漏增益时间
	public static final int ADD_DPS = 50;//魔龙之血增益效果
	public static final float ADD_DPS_TIME = 10f;//魔龙之血增益时间
	public static final int ADD_AOE = 80;//蓝色冰魄增益效果
	public static final float GAMETIME_INIT = 30f;//初始化游戏时间
	public static final float BOSS_VELOCITY = 50.0f;//BOSS移动速度
	
	public static final int WEAPON_INIT = 9;//初始化武器编号
	public static final int MAGIC_INIT = 9;//初始化魔法编号
	public static final int WEAPON_NUM = 9;//装备库中武器的个数
	public static final int MAGIC_NUM = 9;//装备库中魔法的个数
	public static final int PROP_NUM = 3;//装备库中道具的个数
	public static final String WEAPON_NAME = "Weapon";//武器代号
	public static final String MAGIC_NAME = "Magic";//魔法代号
	public static final String PROP_NAME = "Prop";//道具代号
	public static final String SHARED_PREFS_MAIN = "BangYouScreenSettings";//主表名
	public static final String SHARED_PREFS_THEME_MXD = "ThemeSceneMXD";//冒险岛主题
	public static final String SHARED_PREFS_THEME_22 = "ThemeScene2";//未开放
	public static final String CURRENT_WEAPON = "CurrentWeapon";//当前使用的武器
	public static final String CURRENT_MAGIC = "CurrentMagic";//当前使用的魔法
	public static final String WEAPON_BUY = "WeaponBuy_";//标记哪些武器被购买 (未买:false  已买:true)
	public static final String MAGIC_BUY = "MagicBuy_";//标记哪些魔法被购买 (未买:false  已买:true)
	public static final String Prop_BUY = "PropBuy_";//标记拥有道具的个数
	public static final String MY_GOLD = "MyGold";//拥有的金币
	
	/** 冒险岛主题BOSS个数 */
	public static final int THEME_1_BOSS_NUM = 8;
	/** 魔法效果个数 */
	public static final int MAGIC_AS_NUM = 10;
	
	/**测试数据*/
	public static final String TEST_BOSSHP = "bossHP";//BOSS血量
	public static final String TEST_BOSSTYPE = "bossType";//BOSS属性
	public static final String TEST_BOSSMAXDEF = "bossMaxDef";//BOSS防御力上限
	public static final String TEST_BOSSMINDEF = "bossMinDef";//BOSS防御力下限
	
	public static final String TEST_WEAPONTYPE = "weaponType";//武器类型
	public static final String TEST_WEAPONMAXDPS = "weaponMaxDps";//武器类型
	public static final String TEST_WEAPONMINDPS = "weaponMinDps";//武器类型
	
	public static final String TEST_MAGICTYPE = "magicType";//武器类型
	public static final String TEST_MAGICMAXAOE = "magicMaxAoe";//武器类型
	public static final String TEST_MAGICMINAOE = "magicMinAoe";//武器类型
	
	/**
	 * 冒险岛BOSS属性
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public static BossModel getMXDBoss(int bossNum){
		BossModel b = new BossModel();
		b.setBossLevel(bossNum);
		b.setGameBGTR(ResourceManager.shareGameBG);
		b.setBossTTR(ResourceManager.mxdBoss_TTRArray[bossNum-1]);
		
		//测试语句
		b.setBossHP(BangYouScreenActivity.getIntFromSharedPreferences(TEST_BOSSHP));
		b.setBossDefType(BangYouScreenActivity.getIntFromSharedPreferences(TEST_BOSSTYPE));
		b.setMaxBossDEF(BangYouScreenActivity.getIntFromSharedPreferences(TEST_BOSSMAXDEF));
		b.setMinBossDEF(BangYouScreenActivity.getIntFromSharedPreferences(TEST_BOSSMINDEF));
		
		
		/*
		switch(bossNum){
			case 1:
				b.setBossHP(500);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
				
			case 2:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
			
			case 3:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
			
			case 4:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
			
			case 5:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
			
			case 6:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
			
			case 7:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
			
			case 8:
				b.setBossHP(120);
				b.setBossDefType(3);
				b.setMaxBossDEF(10);
				b.setMinBossDEF(5);
			break;
		}
		*/
		
		return b;
	}
	
	/**
	 * 玩家装备属性
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public static PlayerModel getPlayerModel(){
		int weaponNum = BangYouScreenActivity.getWeaponFromSharedPreferences();
		int magicNum = BangYouScreenActivity.getMagicFromSharedPreferences();
		PlayerModel p = new PlayerModel();
		p.setWeaponNum(weaponNum);
		p.setMagicNum(magicNum);
		p.setWeaponTR(ResourceManager.weaponTTR.getTextureRegion(weaponNum));
		p.setMagicTR(ResourceManager.magicTTR.getTextureRegion(magicNum));
		
		//测试语句
		p.setWeaponType(BangYouScreenActivity.getIntFromSharedPreferences(TEST_WEAPONTYPE));
		p.setWeaponDPSMax(BangYouScreenActivity.getIntFromSharedPreferences(TEST_WEAPONMAXDPS));
		p.setWeaponDPSMin(BangYouScreenActivity.getIntFromSharedPreferences(TEST_WEAPONMINDPS));
		
		p.setMagicType(BangYouScreenActivity.getIntFromSharedPreferences(TEST_MAGICTYPE));
		p.setMagicAOEMax(BangYouScreenActivity.getIntFromSharedPreferences(TEST_MAGICMAXAOE));
		p.setMagicAOEMin(BangYouScreenActivity.getIntFromSharedPreferences(TEST_MAGICMINAOE));
		p.setMagicTTR(ResourceManager.magicASTTRArray[0]);
		
		/*
		//武器属性
		switch(weaponNum){
			case 9:
				p.setWeaponType(4);
				p.setWeaponDPSMax(15);
				p.setWeaponDPSMin(10);
			break;
			
			case 0:
				p.setWeaponType(3);
				p.setWeaponDPSMax(40);
				p.setWeaponDPSMin(30);
			break;
			
			case 1:
				p.setWeaponType(3);
				p.setWeaponDPSMax(100);
				p.setWeaponDPSMin(80);
			break;
			
			case 2:
				p.setWeaponType(3);
				p.setWeaponDPSMax(300);
				p.setWeaponDPSMin(200);
			break;
			
			case 3:
				p.setWeaponType(2);
				p.setWeaponDPSMax(60);
				p.setWeaponDPSMin(10);
			break;
			
			case 4:
				p.setWeaponType(2);
				p.setWeaponDPSMax(130);
				p.setWeaponDPSMin(50);
			break;
			
			case 5:
				p.setWeaponType(2);
				p.setWeaponDPSMax(320);
				p.setWeaponDPSMin(180);
			break;
			
			case 6:
				p.setWeaponType(1);
				p.setWeaponDPSMax(50);
				p.setWeaponDPSMin(50);
			break;
			
			case 7:
				p.setWeaponType(1);
				p.setWeaponDPSMax(90);
				p.setWeaponDPSMin(90);
			break;
			
			case 8:
				p.setWeaponType(1);
				p.setWeaponDPSMax(280);
				p.setWeaponDPSMin(280);
			break;
		
		}
		
		//魔法属性
		switch(magicNum){
			case 9:
				p.setMagicType(4);
				p.setMagicAOEMax(20);
				p.setMagicAOEMin(15);
				p.setMagicTTR(ResourceManager.magicASTTRArray[0]);
			break;
		
			case 0:
				p.setMagicType(4);
				p.setMagicAOEMax(20);
				p.setMagicAOEMin(15);
				p.setMagicTTR(ResourceManager.magicASTTRArray[0]);
			break;
	
			case 11:
				p.setMagicType(4);
				p.setMagicAOEMax(20);
				p.setMagicAOEMin(15);
				p.setMagicTTR(ResourceManager.magicASTTRArray[1]);
			break;
		}
		*/
		
		return p;
	}

}
