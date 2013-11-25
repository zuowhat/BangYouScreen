package org.game.bangyouscreen.model;

import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * BOSS模型 
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class BossModel {
	
	/** BOSS主题编号  */
	private int bossWorld;
	/** BOSS编号  */
	private int bossLevel;
	/** 游戏背景  */
	private TextureRegion gameBGTR;
	/** BOSS纹理  */
	private TiledTextureRegion bossTTR;
	/** BOSS血量  */
	private int bossHP;
	/** BOSS属性  */
	private int bossDefType;
	/** BOSS防御下限  */
	private int minBossDEF;
	/** BOSS防御上限  */
	private int maxBossDEF;
	
	public TextureRegion getGameBGTR() {
		return gameBGTR;
	}
	public void setGameBGTR(TextureRegion gameBGTR) {
		this.gameBGTR = gameBGTR;
	}
	public TiledTextureRegion getBossTTR() {
		return bossTTR;
	}
	public void setBossTTR(TiledTextureRegion bossTTR) {
		this.bossTTR = bossTTR;
	}
	public int getBossHP() {
		return bossHP;
	}
	public void setBossHP(int bossHP) {
		this.bossHP = bossHP;
	}
	
	public int getBossDefType() {
		return bossDefType;
	}
	public void setBossDefType(int bossDefType) {
		this.bossDefType = bossDefType;
	}
	public int getMinBossDEF() {
		return minBossDEF;
	}
	public void setMinBossDEF(int minBossDEF) {
		this.minBossDEF = minBossDEF;
	}
	public int getMaxBossDEF() {
		return maxBossDEF;
	}
	public void setMaxBossDEF(int maxBossDEF) {
		this.maxBossDEF = maxBossDEF;
	}
	public int getBossWorld() {
		return bossWorld;
	}
	public void setBossWorld(int bossWorld) {
		this.bossWorld = bossWorld;
	}
	public int getBossLevel() {
		return bossLevel;
	}
	public void setBossLevel(int bossLevel) {
		this.bossLevel = bossLevel;
	}
	
}
