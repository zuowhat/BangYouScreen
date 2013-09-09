package org.game.bangyouscreen.Managers;


import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;
import org.game.bangyouscreen.BangYouScreenActivity;

import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager extends Object{
	
	private static final ResourceManager INSTANCE = new ResourceManager();
	public FixedStepEngine engine;
	public Context context;
	public BangYouScreenActivity activity;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;
	private String mPreviousAssetBasePath = ""; //这个变量将被用来恢复TextureFactory的默认路径
	private static final TextureOptions mNormalTextureOption = TextureOptions.BILINEAR;
	
	public static TextureRegion mainMenuBackgroundTR;
	public static TiledTextureRegion mainMenuButtonsTTR;
	public static TextureRegion mainMenuTitleTR;
	public static Font mFont;
	
	public ResourceManager(){
	}

	public static ResourceManager getInstance(){
		return INSTANCE;
	}
	
	public static Context getContext(){
		return getInstance().context;
	}
	
	public static FixedStepEngine getEngine(){
		return getInstance().engine;
	}
	
	public static SmoothCamera getCamera(){
		return (SmoothCamera) getInstance().engine.getCamera();
	}

	public static void setup(BangYouScreenActivity pActivity, FixedStepEngine pEngine, Context pContext, float pCameraWidth, float pCameraHeight, float pCameraScaleX, float pCameraScaleY){
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().context = pContext;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		getInstance().cameraScaleFactorX = pCameraScaleX;
		getInstance().cameraScaleFactorY = pCameraScaleY;
	}
	
	public static void loadMenuResources(){
		getInstance().loadMenuTextures();
		getInstance().loadSharedResources();
	}
	
	private void loadSharedResources(){
		getInstance().loadSharedTextures();
		getInstance().loadFonts();
	}
	
	// ============================ 菜单纹理  ================= //
	private void loadMenuTextures(){
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu");
		if(mainMenuBackgroundTR == null){
			BitmapTextureAtlas mainMenuBackgroundBTA = new BitmapTextureAtlas(activity.getTextureManager(),540,480,mNormalTextureOption);
			mainMenuBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuBackgroundBTA, activity, "background.png",0,0);
			mainMenuBackgroundBTA.load();
		}
		if(mainMenuTitleTR == null){
			BitmapTextureAtlas mainMenuTitleBTA = new BitmapTextureAtlas(activity.getTextureManager(),256,128,mNormalTextureOption);
			mainMenuTitleTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTitleBTA, activity, "BangYouScreenTitle.png",0,0);
			mainMenuTitleBTA.load();
		}
		if(mainMenuButtonsTTR == null){
			BitmapTextureAtlas mainMenuButtonsBTA = new BitmapTextureAtlas(activity.getTextureManager(),256,128,mNormalTextureOption);
			mainMenuButtonsTTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuButtonsBTA, activity, "MainMenuButtons.png", 0,0,1,4);
			mainMenuButtonsBTA.load();
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	
	// ============================ 公共纹理  ================= //
	private void loadSharedTextures(){
		
	}
	
	// ============================ 字体  ================= //
	private static String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()-_=+[] {};:'\",<.>?/\\";
	
	private void loadFonts(){
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("fonts/");
		if(mFont == null){
			mFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256,Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),32f,true,Color.WHITE_ABGR_PACKED_INT);
			mFont.load();
			mFont.prepareLetters(DEFAULT_CHARS.toCharArray());
		}
	}
}
