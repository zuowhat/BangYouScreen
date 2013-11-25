package org.game.bangyouscreen.util;

import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.model.BossModel;

/**
 * 游戏数据常量
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class DataConstant {
	
	
	public static BossModel getMXDBoss(int bossNum){
		BossModel b = new BossModel();
		b.setBossLevel(bossNum);
		b.setGameBGTR(ResourceManager.shareGameBG);
		b.setBossTTR(ResourceManager.themeSceneOneBossTotalTT[bossNum-1]);
		if(bossNum == 1){
			
		}
		
		return b;
	}

}
