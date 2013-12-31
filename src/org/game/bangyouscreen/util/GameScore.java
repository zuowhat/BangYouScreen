package org.game.bangyouscreen.util;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.game.bangyouscreen.managers.ResourceManager;

public class GameScore{
private float mCurrentScore = 0.0F;
public AnimatedSprite[] mDigitsSprite = new AnimatedSprite[5];
public AnimatedSprite[] myGoldAS;
float mScale = 1.0F;
private int mScore = 0;
boolean mShowZero = false;
float mX = 0.0F;
float mY = 0.0F;

public GameScore(){}

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

public void addToLayer(Entity paramEntity){
	  for (int i = 0;i< mDigitsSprite.length; i++){
	    this.mDigitsSprite[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),ResourceManager.getEngine().getVertexBufferObjectManager());
	    EntityUtil.setSize("width", 20f/800f, mDigitsSprite[i]);
	    mDigitsSprite[i].setPosition(mDigitsSprite[i].getWidth()/2f + i * mDigitsSprite[i].getWidth(), ResourceManager.getCamera().getHeight()-mDigitsSprite[i].getHeight()/2f);
	    paramEntity.attachChild(this.mDigitsSprite[i]);
	  }
}


public void addGoldToLayer(Entity paramEntity,int mGold){
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
		myGoldAS[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),ResourceManager.getEngine().getVertexBufferObjectManager());
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

//======================未使用的方法====================//

public void detachSelf()
{
  for (int i = 0; ; i++)
  {
    if (i >= this.mDigitsSprite.length)
      return;
    if (this.mDigitsSprite[i] == null)
      continue;
    this.mDigitsSprite[i].detachSelf();
  }
}

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