package org.game.bangyouscreen.model;

public class PlayerModel {
	
	private int weaponNum;//武器编号
	private int weaponType;//武器属性
	private int weaponDPSMax;//武器攻击力上限
	private int weaponDPSMin;//武器攻击力下限
	private int magicNum;//魔法编号
	private int magicType;//魔法属性
	private int magicAOEMax;//魔法攻击力上限
	private int magicAOEMin;//魔法攻击力下限
	
	public int getWeaponNum() {
		return weaponNum;
	}
	public void setWeaponNum(int weaponNum) {
		this.weaponNum = weaponNum;
	}
	public int getWeaponDPSMax() {
		return weaponDPSMax;
	}
	public void setWeaponDPSMax(int weaponDPSMax) {
		this.weaponDPSMax = weaponDPSMax;
	}
	public int getWeaponDPSMin() {
		return weaponDPSMin;
	}
	public void setWeaponDPSMin(int weaponDPSMin) {
		this.weaponDPSMin = weaponDPSMin;
	}
	public int getMagicNum() {
		return magicNum;
	}
	public void setMagicNum(int magicNum) {
		this.magicNum = magicNum;
	}
	public int getMagicAOEMax() {
		return magicAOEMax;
	}
	public void setMagicAOEMax(int magicAOEMax) {
		this.magicAOEMax = magicAOEMax;
	}
	public int getMagicAOEMin() {
		return magicAOEMin;
	}
	public void setMagicAOEMin(int magicAOEMin) {
		this.magicAOEMin = magicAOEMin;
	}
	public int getWeaponType() {
		return weaponType;
	}
	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}
	public int getMagicType() {
		return magicType;
	}
	public void setMagicType(int magicType) {
		this.magicType = magicType;
	}
	
}
