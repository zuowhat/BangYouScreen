package org.game.bangyouscreen.managers;


import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
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
	public static TextureRegion singleModeTR;
	public static TextureRegion mainMenuTitleTR;
	public static Font mFont;
	public static Font sysFont;
	public static TiledTextureRegion numberTTR;
	
	public static TiledTextureRegion greenButtonTTR;
	public static TiledTextureRegion redButtonTTR;
	public static TextureRegion clockTR;
	public static TextureRegion aiderHead;
	public static TextureRegion muTR;
	
	public static TextureRegion gameBG1;
	public static TiledTextureRegion aider1;
	public static TiledTextureRegion boss1;
	public static TiledTextureRegion angelboss;
	
	public static TextureRegion xue1;
	public static TextureRegion xue2;
	//public static TextureRegion xue3;
	
	public static TiledTextureRegion aidSkill1;
	public static TiledTextureRegion aidSkill2;
	
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
	
	//用于复位镜头
	public static void setupForMenus() {
		final SmoothCamera sc = ResourceManager.getCamera();
		sc.setBoundsEnabled(false);
		sc.setChaseEntity(null);
		sc.setZoomFactorDirect(1f);
		sc.setZoomFactor(1f);
		sc.setCenterDirect(ResourceManager.getInstance().cameraWidth / 2f,
				ResourceManager.getInstance().cameraHeight / 2f);
		sc.setCenter(ResourceManager.getInstance().cameraWidth / 2f,
				ResourceManager.getInstance().cameraHeight / 2f);
		sc.clearUpdateHandlers();
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
	
	private TextureRegion getLimitableTR(final String pTextureRegionPath,
			final TextureOptions pTextureOptions) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource
				.create(this.activity.getAssets(),BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + pTextureRegionPath);
		final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				this.activity.getTextureManager(),
				bitmapTextureAtlasSource.getTextureWidth(),
				bitmapTextureAtlasSource.getTextureHeight(), pTextureOptions);
		final TextureRegion textureRegion = new TextureRegion(
				bitmapTextureAtlas, 0, 0,
				bitmapTextureAtlasSource.getTextureWidth(),
				bitmapTextureAtlasSource.getTextureHeight(), false);
		bitmapTextureAtlas.addTextureAtlasSource(bitmapTextureAtlasSource, 0, 0);
		bitmapTextureAtlas.load();
		return textureRegion;
	}
	
	private TiledTextureRegion getLimitableTTR(String pTiledTextureRegionPath, int pColumns, int pRows, TextureOptions pTextureOptions) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(activity.getAssets(), BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + pTiledTextureRegionPath);
		final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(), pTextureOptions);
		final ITextureRegion[] textureRegions = new ITextureRegion[pColumns * pRows];

		final int tileWidth = bitmapTextureAtlas.getWidth() / pColumns;
		final int tileHeight = bitmapTextureAtlas.getHeight() / pRows;

		for(int tileColumn = 0; tileColumn < pColumns; tileColumn++) {
			for(int tileRow = 0; tileRow < pRows; tileRow++) {
				final int tileIndex = tileRow * pColumns + tileColumn;
				final int x = tileColumn * tileWidth;
				final int y = tileRow * tileHeight;
				textureRegions[tileIndex] = new TextureRegion(bitmapTextureAtlas, x, y, tileWidth, tileHeight, false);
			}
		}

		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(bitmapTextureAtlas, false, textureRegions);
		bitmapTextureAtlas.addTextureAtlasSource(bitmapTextureAtlasSource, 0, 0);
		bitmapTextureAtlas.load();
		return tiledTextureRegion;
	}
	
	public static void loadMenuResources(){
		getInstance().loadMenuTextures();
		getInstance().loadSharedResources();
	}
	
	public static void loadGameResources(){
		getInstance().loadGameTextures();
		getInstance().loadSharedResources();
	}
	
	private void loadSharedResources(){
		getInstance().loadSharedTextures();
		getInstance().loadFonts();
	}
	
	// ============================ 菜单纹理  ================= //
	private void loadMenuTextures(){
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		if(mainMenuBackgroundTR == null){
			mainMenuBackgroundTR = getLimitableTR("background.png",mNormalTextureOption);
		}
		if(mainMenuTitleTR == null){
			mainMenuTitleTR = getLimitableTR("BangYouScreenTitle.png",mNormalTextureOption);
		}
		if(singleModeTR == null){
			singleModeTR = getLimitableTR("MainMenuButtons.png",mNormalTextureOption);
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	
	// ============================ 游戏纹理  ================= //
	private void loadGameTextures(){
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		if(greenButtonTTR == null){
			greenButtonTTR = getLimitableTTR("greenbutton.png",2,1,mNormalTextureOption);
		}
		if(redButtonTTR == null){
			redButtonTTR = getLimitableTTR("redbutton.png",2,1,mNormalTextureOption);
		}
		if(clockTR == null){
			clockTR = getLimitableTR("clock.png",mNormalTextureOption);
		}
		if(aiderHead == null){
			aiderHead = getLimitableTR("tuoba.png",mNormalTextureOption);
		}
		if(muTR == null){
			muTR = getLimitableTR("multiply.png",mNormalTextureOption);
		}
		if(gameBG1 == null){
			gameBG1 = getLimitableTR("bg1.jpg",mNormalTextureOption);
		}
		if(aider1 == null){
			aider1 = getLimitableTTR("boss1.png",5,5,mNormalTextureOption);
		}
		if(boss1 == null){
			boss1 = getLimitableTTR("boss.png",5,6,mNormalTextureOption);
		}
		if(angelboss == null){
			angelboss = getLimitableTTR("angelboss.png",3,1,mNormalTextureOption);
		}
		if(xue1 == null){
			xue1 = getLimitableTR("xue1.png",mNormalTextureOption);
		}
		if(xue2 == null){
			xue2 = getLimitableTR("xue2.png",mNormalTextureOption);
		}
//		if(xue3 == null){
//			xue3 = getLimitableTR("xue3.png",mNormalTextureOption);
//		}
		if(aidSkill1 == null){
			aidSkill1 = getLimitableTTR("skill1.png",3,4,mNormalTextureOption);
		}
		if(aidSkill2 == null){
			aidSkill2 = getLimitableTTR("skill2.png",3,4,mNormalTextureOption);
		}
		if(numberTTR == null){
			numberTTR = getLimitableTTR("number.png",12,1,mNormalTextureOption);
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	
	// ============================ 公共纹理  ================= //
	private void loadSharedTextures(){
		
	}
	
	// ============================ 字体  ================= //
	private static String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()-_=+[] {};:'\",<.>?/\\";
	
	private void loadFonts(){
		if(mFont == null){
			mFont = FontFactory.createFromAsset(engine.getFontManager(), engine.getTextureManager(), 256, 256,activity.getAssets(),"fonts/Chunkfive.otf",32f,true,Color.WHITE_ABGR_PACKED_INT);
			mFont.load();
			mFont.prepareLetters(DEFAULT_CHARS.toCharArray());
		}
		if(sysFont == null){
			sysFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256,Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),32f,true,Color.WHITE_ABGR_PACKED_INT);
			sysFont.load();
			sysFont.prepareLetters(DEFAULT_CHARS.toCharArray());
		}
	}
}
