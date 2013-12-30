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
	
	public static final String SHARED_PREFS_MAIN = "BangYouScreenSettings";//主表名
	public static final String SHARED_PREFS_THEME_1 = "ThemeScene1";
	public static final String SHARED_PREFS_THEME_2 = "ThemeScene2";
	public static final String CURRENT_WEAPON = "CurrentWeapon";//当前使用的武器
	public static final String CURRENT_MAGIC = "CurrentMagic";//当前使用的魔法
	public static final String WEAPON_BUY = "WeaponBuy_";//标记哪些武器被购买
	public static final String MY_GOLD = "MyGold";//标记哪些武器被购买
	
	
	/** 冒险岛主题BOSS个数 */
	public static final int THEME_1_BOSS_NUM = 8;
	/** 魔法效果个数 */
	public static final int MAGIC_NUM = 10;
	
	
	
	
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
		int weaponNum = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.CURRENT_WEAPON);
		int magicNum = BangYouScreenActivity.getIntFromSharedPreferences(DataConstant.CURRENT_MAGIC);
		PlayerModel p = new PlayerModel();
		p.setWeaponNum(weaponNum);
		p.setMagicNum(magicNum);
		//武器
		switch(weaponNum){
			case 0:
				p.setWeaponType(4);
				p.setWeaponDPSMax(15);
				p.setWeaponDPSMin(10);
				p.setWeaponTR(ResourceManager.weaponTTR.getTextureRegion(0));
			break;
			
			case 11:
				p.setWeaponType(4);
				p.setWeaponDPSMax(15);
				p.setWeaponDPSMin(10);
				p.setWeaponTR(ResourceManager.weaponTTR.getTextureRegion(4));
			break;
		
		}
		
		//魔法
		switch(magicNum){
			case 0:
				p.setMagicType(4);
				p.setMagicAOEMax(20);
				p.setMagicAOEMin(15);
				p.setMagicTR(ResourceManager.magicTTR.getTextureRegion(0));
				p.setMagicTTR(ResourceManager.magicASTTRArray[0]);
			break;
	
			case 11:
				p.setMagicType(4);
				p.setMagicAOEMax(20);
				p.setMagicAOEMin(15);
				p.setMagicTR(ResourceManager.magicTTR.getTextureRegion(4));
				p.setMagicTTR(ResourceManager.magicASTTRArray[1]);
			break;
		}
		
		return p;
	}

}
