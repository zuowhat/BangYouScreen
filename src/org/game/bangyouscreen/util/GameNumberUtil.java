package org.game.bangyouscreen.util;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.bangyouscreen.managers.ResourceManager;

/**
 * 游戏数字工具类
 * @author zuowhat 2013-12-14
 * @version 1.0
 */
public class GameNumberUtil{

private VertexBufferObjectManager mVertexBufferObjectManager = ResourceManager.getEngine().getVertexBufferObjectManager();
public AnimatedSprite[] mDigitsSprite = new AnimatedSprite[5];
public AnimatedSprite[] myGoldAS;
public AnimatedSprite[] tiamatPotionNum;//魔龙之血数量
public AnimatedSprite[] bingpoPotionNum;//蓝色冰魄数量
public AnimatedSprite[] clockNum;//时光沙漏数量

float mScale = 1.0F;
private int mScore = 0;
boolean mShowZero = false;
float mX = 0.0F;
float mY = 0.0F;
private float mCurrentScore = 0.0F;

public GameNumberUtil(){}

/**
 * 更新游戏分数
 * @author zuowhat 2013-12-20
 * @param scoreNum 游戏分数
 * @since 1.0
 */
public void addScore(float scoreNum){
	if(scoreNum >= 10000 && scoreNum <= 99999){
		mDigitsSprite[0].setCurrentTileIndex((int)(scoreNum/10000));
		scoreNum = scoreNum%10000;
	}else{
		mDigitsSprite[0].setCurrentTileIndex(0);
	}
	if(scoreNum >= 1000 && scoreNum <= 9999){
		mDigitsSprite[1].setCurrentTileIndex((int)(scoreNum/1000));
		scoreNum = scoreNum%1000;
	}else{
		mDigitsSprite[1].setCurrentTileIndex(0);
	}
	if(scoreNum >= 100 && scoreNum <= 999){
		mDigitsSprite[2].setCurrentTileIndex((int)(scoreNum/100));
		scoreNum = scoreNum%100;
	}else{
		mDigitsSprite[2].setCurrentTileIndex(0);
	}
	if(scoreNum >= 10 && scoreNum <= 99){
		mDigitsSprite[3].setCurrentTileIndex((int)(scoreNum/10));
		scoreNum = scoreNum%10;
	}else{
		mDigitsSprite[3].setCurrentTileIndex(0);
	}
	if(scoreNum >= 1 && scoreNum <= 9){
		mDigitsSprite[4].setCurrentTileIndex((int)(scoreNum/1));
		//scoreNum = scoreNum%10;
	}else{
		mDigitsSprite[4].setCurrentTileIndex(0);
	}
}

public void detachSelf(){
	  for (int i = 0; ; i++){
	    if (i >= this.mDigitsSprite.length)
	      return;
	    if (this.mDigitsSprite[i] == null)
	      continue;
	    this.mDigitsSprite[i].detachSelf();
	  }
}

public void addToLayer(IEntity paramEntity){
	  detachSelf();
	  for (int i = 0;i< mDigitsSprite.length; i++){
	    this.mDigitsSprite[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),mVertexBufferObjectManager);
	    EntityUtil.setSize("width", 20f/800f, mDigitsSprite[i]);
	    mDigitsSprite[i].setPosition(mDigitsSprite[i].getWidth()/2f + i * mDigitsSprite[i].getWidth(), ResourceManager.getCamera().getHeight()-mDigitsSprite[i].getHeight()/2f);
	    paramEntity.attachChild(this.mDigitsSprite[i]);
	  }
}


public void addGoldToLayer(IEntity paramEntity,int mGold){
	if(myGoldAS != null){
		for(AnimatedSprite a:myGoldAS){
			paramEntity.detachChild(a);
			a = null;
		}
		myGoldAS = null;
	}
	if(mGold <= 0){
		myGoldAS = new AnimatedSprite[1];
	}else{
		myGoldAS = new AnimatedSprite[(mGold+"").length()];
	}
	for (int i = 0;i< myGoldAS.length; i++){
		myGoldAS[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),mVertexBufferObjectManager);
		EntityUtil.setSize("width", 20f/800f, myGoldAS[i]);
		myGoldAS[i].setPosition(paramEntity.getWidth()*(1f/8f)+myGoldAS[i].getWidth()/2f + i * myGoldAS[i].getWidth(), paramEntity.getHeight()*(2f/3f));
		paramEntity.attachChild(myGoldAS[i]);
	}
	for(int n = myGoldAS.length-1; n>=0; n--){
		int m = mGold % 10;
		myGoldAS[n].setCurrentTileIndex(m);
		mGold = mGold / 10;
	}
}

/**
 * 显示物品数量(ShopScene)
 * @author zuowhat 2014-1-6
 * @param type 物品类型
 * @param pEntity 父层
 * @param bEntity 兄弟实体
 * @since 1.0
 */
public void addGoodsNumInShopScene(String type, IEntity pEntity,IEntity bEntity){
	AnimatedSprite[] goodsNum = null;
	if(DataConstant.WEAPON_NAME.equals(type)){
		tiamatPotionNum  = new AnimatedSprite[3];
		goodsNum = tiamatPotionNum;
	}else if(DataConstant.MAGIC_NAME.equals(type)){
		bingpoPotionNum  = new AnimatedSprite[3];
		goodsNum = bingpoPotionNum;
	}else{
		clockNum  = new AnimatedSprite[3];
		goodsNum = clockNum;
	}
	 for (int i = 0;i< goodsNum.length; i++){
	    goodsNum[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),mVertexBufferObjectManager);
	    EntityUtil.setSize("width", 15f/800f, goodsNum[i]);
	    goodsNum[i].setPosition(bEntity.getX()+bEntity.getWidth() + i * goodsNum[i].getWidth(),bEntity.getY());
	    pEntity.attachChild(goodsNum[i]);
	}
}

/**
 * 更新物品数量
 * @author zuowhat 2014-1-6
 * @param type     物品类型(药水和时钟)
 * @param scoreNum 物品数量
 * @since 1.0
 */
public void updateGoodsNum(String type, int num){
	AnimatedSprite[] goodsNum = null;
	if(DataConstant.WEAPON_NAME.equals(type)){
		goodsNum = tiamatPotionNum;
	}else if(DataConstant.MAGIC_NAME.equals(type)){
		goodsNum = bingpoPotionNum;
	}else{
		goodsNum = clockNum;
	}
	dataSort(num,goodsNum,0);
}

/**
 * 显示物品数量(GameLevel)
 * @author zuowhat 2014-1-7
 * @param type 物品类型
 * @param pEntity 父层
 * @since 1.0
 */
public void addGoodsNumInGameLevel(String type, IEntity pEntity){
	AnimatedSprite[] goodsNum = null;
	if(DataConstant.WEAPON_NAME.equals(type)){
		tiamatPotionNum  = new AnimatedSprite[3];
		goodsNum = tiamatPotionNum;
	}else if(DataConstant.MAGIC_NAME.equals(type)){
		bingpoPotionNum  = new AnimatedSprite[3];
		goodsNum = bingpoPotionNum;
	}else{
		clockNum  = new AnimatedSprite[3];
		goodsNum = clockNum;
	}
	 for (int i = 0;i< goodsNum.length; i++){
	    goodsNum[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),mVertexBufferObjectManager);
	    EntityUtil.setSizeInParent("height", 1f/3f, goodsNum[i],pEntity);
	    goodsNum[i].setPosition(pEntity.getWidth() - (goodsNum.length-i) * goodsNum[i].getWidth()/2f,goodsNum[i].getHeight()/2f);
	    pEntity.attachChild(goodsNum[i]);
	}
}

/**
 * 显示得分(GameWinLayer)
 * @author zuowhat 2014-1-20
 * @param pEntity 父层
 * @since 1.0
 */
public AnimatedSprite[] gameScoreNum(IEntity pEntity, int getGold){
	AnimatedSprite[] goldNum = new AnimatedSprite[3];
	for (int i = 0;i< goldNum.length; i++){
		goldNum[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),mVertexBufferObjectManager);
	    EntityUtil.setSizeInParent("height", 1f/7f, goldNum[i],pEntity);
	    goldNum[i].setPosition(pEntity.getWidth()/2f - (goldNum.length-i) * goldNum[i].getWidth()/2f,pEntity.getHeight()/2f);
	    pEntity.attachChild(goldNum[i]);
	}
	dataSort(getGold,goldNum,0);
	return goldNum;
}

/**
 * 数据统计(HelpScene)
 * @author zuowhat 2014-2-14
 * @param type 统计类型
 * @param pWidth 位置
 * @param pEntity 父层
 * @param num 数值
 * @since 1.0
 */
public void addNumInHelpScene(int type, float pWidth, IEntity pEntity, int num){
	AnimatedSprite[] goodsNum = new AnimatedSprite[7];
	 for (int i = 0;i< goodsNum.length; i++){
	    goodsNum[i] = new AnimatedSprite(0f, 0f, ResourceManager.numWhiteTTR.deepCopy(),mVertexBufferObjectManager);
	    EntityUtil.setSizeInParent("height", 1f/2f, goodsNum[i],pEntity);
	    goodsNum[i].setPosition(pWidth - (goodsNum.length-i) * goodsNum[i].getWidth()/2f,pEntity.getHeight()/2f);
	    pEntity.attachChild(goodsNum[i]);
	}
	dataSort(num,goodsNum,2);
	
	if(type == 3 || type == 4){
		//int len = (num+"").length();
		//int offsetPosition = goodsNum.length-len;
		Sprite slash = new Sprite(0f,0f,ResourceManager.numWhiteTTR.getTextureRegion(1),mVertexBufferObjectManager);
		slash.setSize(goodsNum[0].getWidth(), goodsNum[0].getHeight());
		slash.setPosition(goodsNum[goodsNum.length-1].getX()+goodsNum[0].getWidth(), goodsNum[0].getY());
		pEntity.attachChild(slash);
		
		AnimatedSprite[] goodsNumAll = new AnimatedSprite[2];
		for (int i = 0;i< goodsNumAll.length; i++){
			goodsNumAll[i] = new AnimatedSprite(0f, 0f, ResourceManager.numWhiteTTR.deepCopy(),mVertexBufferObjectManager);
		    EntityUtil.setSizeInParent("height", 1f/2f, goodsNumAll[i],pEntity);
		    goodsNumAll[i].setPosition((slash.getX()+slash.getWidth()+goodsNum[goodsNum.length-1].getWidth()) - (goodsNumAll.length-i) * goodsNumAll[i].getWidth()/2f,pEntity.getHeight()/2f);
		    pEntity.attachChild(goodsNumAll[i]);
		}
		
		if(type == 3){
			dataSort(DataConstant.ALL_GOOD_INT,goodsNumAll,2);
		}else if(type == 4){
			dataSort(DataConstant.ALL_APPS_INT,goodsNumAll,2);
		}
	}
}

/**
 * 数据值排序(公共方法)
 * @author zuowhat 2014-2-14
 * @param num 数值
 * @param goodsNum 精灵数组
 * @since 1.0
 */
private void dataSort(int num, AnimatedSprite[] goodsNum, int offs){
	int len = (num+"").length();
	int offsetPosition = goodsNum.length-len;
	for(int n = goodsNum.length-1; n>=0; n--){
		if(n > 0){
			//位置初始化
			goodsNum[n].setPosition(goodsNum[0].getX()+n*goodsNum[0].getWidth(), goodsNum[n].getY());
		}
		if(n >= offsetPosition){
			int m = num % 10;
			goodsNum[n].setCurrentTileIndex(m+offs);
			num = num / 10;
			if(offsetPosition > 0){
				//根据偏移量重新定位
				goodsNum[n].setPosition(goodsNum[n].getX()-offsetPosition*goodsNum[n].getWidth(),goodsNum[n].getY());
			}
			goodsNum[n].setVisible(true);
		}else{
			goodsNum[n].setVisible(false);
		}
	}
}

//======================未使用的方法====================//



public int getCurrentScore()
{
  return this.mScore;
}

public float getHeight()
{
  if (this.mDigitsSprite[0] != null)
    return this.mDigitsSprite[0].getHeight();
  return 0.0F;
}

public float getWidth()
{
  float f = 0.0F;
  for (int i = 0; ; i++)
  {
    if (i >= this.mDigitsSprite.length);
    do
    {
      if (this.mDigitsSprite[0] != null)
        f *= this.mDigitsSprite[0].getWidth();
      return f;
    }
    while ((this.mDigitsSprite[i] == null) || (!this.mDigitsSprite[i].isVisible()));
    //f += 1.0F;
  }
}

public void increaseBy(int paramInt)
{
  this.mScore = (paramInt + this.mScore);
  if (this.mScore < 0)
    this.mScore = 0;
  //this.mSceneBase.registerUpdateHandler(this);
}

//public void onUpdate(float paramFloat)
//{
//  int i = (int)this.mCurrentScore;
//  float f = this.mScore - this.mCurrentScore;
//  if (Math.abs(f) < 50.0F)
//    f = 50.0F * Math.signum(f);
//  this.mCurrentScore += paramFloat * (0.5F * f);
//  if ((f > 0.0F) && (this.mCurrentScore > this.mScore));
//  for (this.mCurrentScore = this.mScore; this.mCurrentScore != i; this.mCurrentScore = this.mScore)
//  {
//    label78: adjustScore();
//    return;
//    if ((f >= 0.0F) || (this.mCurrentScore >= this.mScore))
//      break label78;
//  }
//  this.mSceneBase.unregisterUpdateHandler(this);
//}

public void reset()
{
  this.mScore = 0;
  this.mCurrentScore = 0.0F;
  //adjustScore();
}

public void setPosition(float paramFloat1, float paramFloat2)
{
  this.mX = paramFloat1;
  this.mY = paramFloat2;
  //adjustScore();
}

public void setScale(float paramFloat)
{
  this.mScale = paramFloat;
}

public void setScore(int paramInt)
{
  this.mScore = paramInt;
  this.mCurrentScore = paramInt;
  //adjustScore();
}

public void setShowZero(boolean paramBoolean)
{
  this.mShowZero = paramBoolean;
}
}