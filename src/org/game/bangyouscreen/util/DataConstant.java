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
	public static final int WEAPON_INIT = 9;//初始化武器编号
	public static final int MAGIC_INIT = 9;//初始化魔法编号
	
	public static final int WEAPON_NUM = 9;//装备库中武器的个数
	public static final int MAGIC_NUM = 9;//装备库中魔法的个数
	public static final int PROP_NUM = 3;//装备库中道具的个数
	
	public static final String WEAPON_NAME = "Weapon";//武器代号
	public static final String MAGIC_NAME = "Magic";//魔法代号
	public static final String PROP_NAME = "Prop";//道具代号
	public static final String SHARED_PREFS_MAIN = "BangYouScreenSettings";//主表名
	public static final String SHARED_PREFS_THEME_MXD = "ThemeSceneMXD";
	public static final String SHARED_PREFS_THEME_22 = "ThemeScene2";
	public static final String CURRENT_WEAPON = "CurrentWeapon";//当前使用的武器
	public static final String CURRENT_MAGIC = "CurrentMagic";//当前使用的魔法
	public static final String WEAPON_BUY = "WeaponBuy_";//标记哪些武器被购买 (未买:false  已买:true)
	public static final String MAGIC_BUY = "MagicBuy_";//标记哪些魔法被购买 (未买:false  已买:true)
	public static final String Prop_BUY = "PropBuy_";//标记拥有道具的个数
	public static final String MY_GOLD = "MyGold";//拥有的金币
	public static final int[] weaponPrice = {500,1500,3000,650,1800,3500,800,2000,5000};//武器价格
	public static final int[] magicPrice = {500,1500,3000,650,1800,3500,800,2000,5000};//魔法价格
	public static final int[] propPrice = {200,350,300};//道具价格
	
	/** 冒险岛主题BOSS个数 */
	public static final int THEME_1_BOSS_NUM = 8;
	/** 魔法效果个数 */
	public static final int MAGIC_AS_NUM = 10;
	
	
	
	
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
		switch(bossNum){
			case 1:
				b.setBossHP(150);
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
		return b;
	}
	
	/**
	 * 玩家属性
	 * @author zuowhat 2013-11-25
	 * @since 1.0
	 */
	public static PlayerModel getPlayerModel(){
		int weaponNum = BangYouScreenActivity.getWeaponFromSharedPreferences();
		int magicNum = BangYouScreenActivity.getMagicFromSharedPreferences();
		PlayerModel p = new PlayerModel();
		p.setWeaponNum(weaponNum);
		p.setMagicNum(magicNum);
		//武器
		switch(weaponNum){
			case 9:
				p.setWeaponType(4);
				p.setWeaponDPSMax(15);
				p.setWeaponDPSMin(10);
			break;
			
			case 0:
				p.setWeaponType(4);
				p.setWeaponDPSMax(15);
				p.setWeaponDPSMin(10);
			break;
			
			case 1:
				p.setWeaponType(4);
				p.setWeaponDPSMax(15);
				p.setWeaponDPSMin(10);
			break;
		
		}
		p.setWeaponTR(ResourceManager.weaponTTR.getTextureRegion(weaponNum));
		
		//魔法
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
		p.setMagicTR(ResourceManager.magicTTR.getTextureRegion(magicNum));
		
		return p;
	}

}
