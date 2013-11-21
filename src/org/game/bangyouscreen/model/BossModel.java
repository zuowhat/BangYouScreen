package org.game.bangyouscreen.model;

import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class BossModel {
	
	private int bossWorld;//BOSS主题编号
	private int bossLevel;//BOSS编号
	private TextureRegion gameBGTR;//游戏背景
	private TiledTextureRegion bossTTR;//BOSS纹理
	private int bossHP;//BOSS血量
	private int bossDefType;//BOSS属性
	private int minBossDEF;//BOSS防御下限
	private int maxBossDEF;//BOSS防御上限
	
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
