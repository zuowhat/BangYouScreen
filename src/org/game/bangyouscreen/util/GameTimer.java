package org.game.bangyouscreen.util;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.game.bangyouscreen.gameLevels.GameLevel;
import org.game.bangyouscreen.managers.ResourceManager;

public class GameTimer implements IUpdateHandler, IEntityModifier.IEntityModifierListener
{
int mAddAlmond = 0;
int mAddClock = 0;
int mAddIronAcorn = 0;
int mAddPeanut = 0;
int mAddPecan = 0;
int mAddPistachio = 0;
public ScaleModifier mBounceIn1 = new ScaleModifier(0.5F, 1.2F, 1.0F, this, EaseLinear.getInstance());
public ScaleModifier mBounceIn2 = new ScaleModifier(0.5F, 1.2F, 1.0F, this, EaseLinear.getInstance());
public ScaleModifier mBounceOut1 = new ScaleModifier(0.5F, 1.0F, 1.2F, this, EaseLinear.getInstance());
public ScaleModifier mBounceOut2 = new ScaleModifier(0.5F, 1.0F, 1.2F, this, EaseLinear.getInstance());
public ColorModifier mColorIn1 = new ColorModifier(0.5F, 1.0F, 1.0F, 0.2F, 1.0F, 0.2F, 1.0F);
public ColorModifier mColorIn2 = new ColorModifier(0.5F, 1.0F, 1.0F, 0.2F, 1.0F, 0.2F, 1.0F);
public ColorModifier mColorOut1 = new ColorModifier(0.5F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F, 0.2F);
public ColorModifier mColorOut2 = new ColorModifier(0.5F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F, 0.2F);
public AnimatedSprite[] mDigitsSprite = new AnimatedSprite[4];
protected GameLevel mGameScene;
protected int mLastTime = -1;
int mNumberOfAlmonds = 5;
int mNumberOfClocks = 4;
int mNumberOfIronAcorns = 1;
int mNumberOfPeanuts = 3;
int mNumberOfPecans = 5;
int mNumberOfPistachios = 5;
ArrayList<Float> mRandomAlmondTime = new ArrayList(10);
ArrayList<Float> mRandomClockTime = new ArrayList(10);
ArrayList<Float> mRandomIronAcornTime = new ArrayList(10);
ArrayList<Float> mRandomPeanutTime = new ArrayList(10);
ArrayList<Float> mRandomPecanTime = new ArrayList(10);
ArrayList<Float> mRandomPistachioTime = new ArrayList(10);
protected float mSecondsPassed = 0.0F;
protected float mSecondsTriggerCount = 0.0F;
boolean mTenSeconds = false;
protected boolean mTiggeredTimer = true;
protected float mTriggerTime;

public GameTimer(GameLevel paramBaseGameScene)
{
  //super(paramBaseGameScene, paramGameTextures, paramGameSounds);
  this.mGameScene = paramBaseGameScene;
}

public void adjustTime(float secondNum)
{
  int i = Math.round(secondNum);
//  if (this.mLastTime == i)
//    return;
//  this.mLastTime = i;
  int j = i % 60;
  int k = i / 60;
  int m = j % 10;
  int n = j / 10;
  this.mDigitsSprite[3].setCurrentTileIndex(m);
  this.mDigitsSprite[3].setVisible(true);
  this.mDigitsSprite[2].setCurrentTileIndex(n);
  this.mDigitsSprite[2].setVisible(true);
  this.mDigitsSprite[1].setCurrentTileIndex(10);
  this.mDigitsSprite[1].setVisible(true);
  this.mDigitsSprite[0].setCurrentTileIndex(k % 10);
  this.mDigitsSprite[0].setVisible(true);
}

public void addToLayer(Entity paramEntity){
    for(int i = 0;i< mDigitsSprite.length; i++){
//    if (i >= this.mDigitsSprite.length){
//      reset();
//      return;
//    }
	    this.mDigitsSprite[i] = new AnimatedSprite(0f, 0f, ResourceManager.numberTTR.deepCopy(),ResourceManager.getEngine().getVertexBufferObjectManager());
	    EntityUtil.setSize("width", 20f/800f, mDigitsSprite[i]);
	    float f = ResourceManager.getCamera().getWidth() - this.mDigitsSprite.length * mDigitsSprite[i].getWidth();
	    mDigitsSprite[i].setPosition(f + i * mDigitsSprite[i].getWidth(), ResourceManager.getCamera().getHeight()-mDigitsSprite[i].getHeight()/2f);
	    paramEntity.attachChild(this.mDigitsSprite[i]);
    //adjustTime();
  }
 // mSecondsPassed = 120.0F;
 // adjustTime(120f);
}

@Override
public void onModifierFinished(IModifier<IEntity> paramIModifier, IEntity paramIEntity)
{
	//System.out.println("modfierListener..");
  if (paramIModifier == this.mBounceOut1){
    this.mBounceIn1.reset();
    paramIEntity.registerEntityModifier(this.mBounceIn1);
    this.mColorIn1.reset();
    paramIEntity.registerEntityModifier(this.mColorIn1);
    return;
  }
  if (paramIModifier == this.mBounceOut2){
    this.mBounceIn2.reset();
    paramIEntity.registerEntityModifier(this.mBounceIn2);
    this.mColorIn2.reset();
    paramIEntity.registerEntityModifier(this.mColorIn2);
    return;
  }
  if (paramIModifier == this.mBounceIn1){
    this.mBounceOut1.reset();
    paramIEntity.registerEntityModifier(this.mBounceOut1);
    this.mColorOut1.reset();
    paramIEntity.registerEntityModifier(this.mColorOut1);
    return;
  }
  this.mBounceOut2.reset();
  paramIEntity.registerEntityModifier(this.mBounceOut2);
  this.mColorOut2.reset();
  paramIEntity.registerEntityModifier(this.mColorOut2);
}

@Override
public void onModifierStarted(IModifier<IEntity> paramIModifier, IEntity paramIEntity){}

//======================未使用的方法====================//

public void disableTriggerTime()
{
  this.mSecondsTriggerCount = 0.0F;
  this.mTiggeredTimer = true;
  this.mTriggerTime = 0.0F;
}



public float getSecondsPassed()
{
  return 120.0F - this.mSecondsPassed;
}

public void increaseBy(float paramFloat)
{
  this.mSecondsPassed = (paramFloat + this.mSecondsPassed);
  //adjustTime();
}

@Override
public void onUpdate(float paramFloat)
{
	System.out.println("GameTimer....");
  this.mSecondsPassed -= paramFloat;
  if (!this.mTiggeredTimer)
  {
    this.mSecondsTriggerCount = (paramFloat + this.mSecondsTriggerCount);
    if (this.mSecondsTriggerCount > this.mTriggerTime)
    {
      this.mTiggeredTimer = true;
     // this.mGameScene.trigerPassedSeconds();
    }
  }
  if (this.mSecondsPassed <= 0.0F)
  {
    this.mSecondsPassed = 0.0F;
    //this.mGameScene.timeIsUp();
    this.mGameScene.unregisterUpdateHandler(this);
    this.mDigitsSprite[3].clearEntityModifiers();
    this.mDigitsSprite[2].clearEntityModifiers();
    this.mDigitsSprite[3].setScale(1.0F);
    this.mDigitsSprite[2].setScale(1.0F);
  }
  if ((this.mAddIronAcorn < this.mRandomIronAcornTime.size()) && (this.mSecondsPassed <= this.mRandomIronAcornTime.get(this.mAddIronAcorn).floatValue()))
  {
    this.mAddIronAcorn = (1 + this.mAddIronAcorn);
   // this.mGameScene.addRandomIronAcorn();
  }
  if ((this.mAddClock < this.mRandomClockTime.size()) && (this.mSecondsPassed <= this.mRandomClockTime.get(this.mAddClock).floatValue()))
  {
    this.mAddClock = (1 + this.mAddClock);
    //this.mGameScene.addRandomClock();
  }
  if ((this.mAddPecan < this.mRandomPecanTime.size()) && (this.mSecondsPassed <= this.mRandomPecanTime.get(this.mAddPecan).floatValue()))
  {
    this.mAddPecan = (1 + this.mAddPecan);
   // this.mGameScene.addRandomPecan();
  }
  if ((this.mAddAlmond < this.mRandomAlmondTime.size()) && (this.mSecondsPassed <= this.mRandomAlmondTime.get(this.mAddAlmond).floatValue()))
  {
    this.mAddAlmond = (1 + this.mAddAlmond);
   // this.mGameScene.addRandomAlmond();
  }
  if ((this.mAddPistachio < this.mRandomPistachioTime.size()) && (this.mSecondsPassed <= this.mRandomPistachioTime.get(this.mAddPistachio).floatValue()))
  {
    this.mAddPistachio = (1 + this.mAddPistachio);
    //this.mGameScene.addRandomPistachio();
  }
  if ((this.mAddPeanut < this.mRandomPeanutTime.size()) && (this.mSecondsPassed <= this.mRandomPeanutTime.get(this.mAddPeanut).floatValue()))
  {
    this.mAddPeanut = (1 + this.mAddPeanut);
   // this.mGameScene.addRandomPeanut();
  }
  if ((!this.mTenSeconds) && (this.mSecondsPassed <= 10.0F))
  {
    this.mTenSeconds = true;
   // this.mGameScene.tenSecondsRemaining();
    this.mBounceOut1.reset();
    this.mBounceOut2.reset();
    this.mDigitsSprite[3].registerEntityModifier(this.mBounceOut1);
    this.mDigitsSprite[2].registerEntityModifier(this.mBounceOut2);
    this.mColorOut1.reset();
    this.mColorOut2.reset();
    this.mDigitsSprite[3].registerEntityModifier(this.mColorOut1);
    this.mDigitsSprite[2].registerEntityModifier(this.mColorOut2);
  }
 // adjustTime();
}

public void remove()
{
  //this.mSceneBase.unregisterUpdateHandler(this);
  for (int i = 0; ; i++)
  {
    if (i >= this.mDigitsSprite.length)
      return;
    this.mDigitsSprite[i].detachSelf();
  }
}

/*
public void reset()
{
  this.mTenSeconds = false;
  this.mSecondsPassed = 120.0F;
  this.mAddPecan = 0;
  this.mRandomPecanTime.clear();
  int i = 0;
  int j;
  int k;
  int m;
  int n;
  int i1;
  if (i >= this.mNumberOfPecans)
  {
    Collections.sort(this.mRandomPecanTime);
    Collections.reverse(this.mRandomPecanTime);
    this.mAddAlmond = 0;
    this.mRandomAlmondTime.clear();
    j = 0;
    if (j < this.mNumberOfAlmonds)
      break label321;
    Collections.sort(this.mRandomAlmondTime);
    Collections.reverse(this.mRandomAlmondTime);
    this.mAddPistachio = 0;
    this.mRandomPistachioTime.clear();
    k = 0;
    if (k < this.mNumberOfPistachios)
      break label393;
    Collections.sort(this.mRandomPistachioTime);
    Collections.reverse(this.mRandomPistachioTime);
    this.mAddPeanut = 0;
    this.mRandomPeanutTime.clear();
    m = 0;
    if (m < this.mNumberOfPeanuts)
      break label465;
    Collections.sort(this.mRandomPeanutTime);
    Collections.reverse(this.mRandomPeanutTime);
    this.mAddIronAcorn = 0;
    this.mRandomIronAcornTime.clear();
    n = 0;
    if (n < this.mNumberOfIronAcorns)
      break label537;
    Collections.sort(this.mRandomIronAcornTime);
    Collections.reverse(this.mRandomIronAcornTime);
    this.mAddClock = 0;
    this.mRandomClockTime.clear();
    i1 = 0;
    if (i1 < this.mNumberOfClocks)
      break label610;
    Collections.sort(this.mRandomClockTime);
    Collections.reverse(this.mRandomClockTime);
  }
  for (int i2 = 0; ; i2++)
  {
    if (i2 >= this.mDigitsSprite.length)
    {
      adjustTime();
      return;
      float f1 = 120.0F * (0.2F + 0.6F * this.mGameTextures.mRandom.nextFloat());
      if (f1 < 12.0F)
        f1 = 12.0F;
      while (true)
      {
        this.mRandomPecanTime.add(Float.valueOf(f1));
        i++;
        break;
        if (f1 <= 96.0F)
          continue;
        f1 = 96.0F;
      }
      label321: float f2 = 120.0F * (0.2F + 0.6F * this.mGameTextures.mRandom.nextFloat());
      if (f2 < 12.0F)
        f2 = 12.0F;
      while (true)
      {
        this.mRandomAlmondTime.add(Float.valueOf(f2));
        j++;
        break;
        if (f2 <= 96.0F)
          continue;
        f2 = 96.0F;
      }
      label393: float f3 = 120.0F * (0.2F + 0.6F * this.mGameTextures.mRandom.nextFloat());
      if (f3 < 12.0F)
        f3 = 12.0F;
      while (true)
      {
        this.mRandomPistachioTime.add(Float.valueOf(f3));
        k++;
        break;
        if (f3 <= 96.0F)
          continue;
        f3 = 96.0F;
      }
      label465: float f4 = 120.0F * (0.2F + 0.6F * this.mGameTextures.mRandom.nextFloat());
      if (f4 < 12.0F)
        f4 = 12.0F;
      while (true)
      {
        this.mRandomPeanutTime.add(Float.valueOf(f4));
        m++;
        break;
        if (f4 <= 96.0F)
          continue;
        f4 = 96.0F;
      }
      label537: float f5 = 120.0F * (0.7F + 0.1F * this.mGameTextures.mRandom.nextFloat());
      if (f5 < 12.0F)
        f5 = 12.0F;
      while (true)
      {
        this.mRandomIronAcornTime.add(Float.valueOf(f5));
        n++;
        break;
        if (f5 <= 96.0F)
          continue;
        f5 = 96.0F;
      }
      label610: float f6 = 120.0F * (0.7F + 0.1F * this.mGameTextures.mRandom.nextFloat());
      if (f6 < 12.0F)
        f6 = 12.0F;
      while (true)
      {
        this.mRandomClockTime.add(Float.valueOf(f6));
        i1++;
        break;
        if (f6 <= 96.0F)
          continue;
        f6 = 96.0F;
      }
    }
    this.mDigitsSprite[i2].clearEntityModifiers();
    this.mDigitsSprite[i2].setScale(1.0F);
    this.mDigitsSprite[i2].setColor(1.0F, 1.0F, 1.0F);
  }
}


public void setNumberOfAlmonds(int paramInt)
{
  if (this.mNumberOfAlmonds < 1)
    this.mNumberOfAlmonds = 1;
  do
    return;
  while (this.mNumberOfAlmonds <= 10);
  this.mNumberOfAlmonds = 10;
}

public void setNumberOfClocks(int paramInt)
{
  if (this.mNumberOfClocks < 0)
    this.mNumberOfClocks = 0;
  do
    return;
  while (this.mNumberOfClocks <= 10);
  this.mNumberOfClocks = 10;
}

public void setNumberOfPeanuts(int paramInt)
{
  if (this.mNumberOfPeanuts < 1)
    this.mNumberOfPeanuts = 1;
  do
    return;
  while (this.mNumberOfPeanuts <= 10);
  this.mNumberOfPeanuts = 10;
}

public void setNumberOfPecans(int paramInt)
{
  if (this.mNumberOfPecans < 1)
    this.mNumberOfPecans = 1;
  do
    return;
  while (this.mNumberOfPecans <= 10);
  this.mNumberOfPecans = 10;
}
*/

public void setPaused(boolean paramBoolean)
{
  if (paramBoolean)
  {
    this.mGameScene.unregisterUpdateHandler(this);
    return;
  }
  this.mGameScene.unregisterUpdateHandler(this);
  this.mGameScene.registerUpdateHandler(this);
}

public void setTriggerTime(float paramFloat)
{
  this.mSecondsTriggerCount = 0.0F;
  this.mTiggeredTimer = false;
  this.mTriggerTime = paramFloat;
}

@Override
public void reset() {
	// TODO Auto-generated method stub
	
}
}
