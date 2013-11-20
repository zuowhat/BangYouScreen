package org.game.bangyouscreen.managers;


import java.util.ArrayList;
import java.util.List;

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
	private TextureOptions mNormalTextureOption = TextureOptions.BILINEAR;
	
	public static TextureRegion mainMenuBackgroundTR;
	public static TiledTextureRegion mainMenuButtons;
	public static TextureRegion mainMenuTitleTR;
	public static Font mFont;
	public static Font sysFont;
	public static TiledTextureRegion numberTTR;
	public static TextureRegion menuClouds1;
	public static TextureRegion menuClouds2;
	
	public static TiledTextureRegion greenButtonTTR;
	public static TiledTextureRegion redButtonTTR;
	public static TextureRegion clockTR;
	
	public static TextureRegion gameBG10;
	public static TiledTextureRegion boss10;
	public static TiledTextureRegion boss11;
	public static TiledTextureRegion boss19;
	
	public static TextureRegion xue1;
	public static TextureRegion xue2;
	//public static TextureRegion xue3;
	
	public static TextureRegion theme1Temp;
	public static TextureRegion theme2Temp;
	public static TextureRegion themeBG;
	
	public static TiledTextureRegion aidSkill1Temp;
	public static TiledTextureRegion aidSkill2Temp;
	
	public static TextureRegion loadingBG;
	public static TextureRegion loadingBG1;
	public static TiledTextureRegion loadingBG2;
	public static TiledTextureRegion loadingFont;
	
	public static TextureRegion themeLevelBG;
	public static TextureRegion themeLevelLock;
	
	private static final int BOSS_NUM_ONE = 8;
	public static TiledTextureRegion[] themeSceneOneBossTotalTT = new TiledTextureRegion[BOSS_NUM_ONE];
	public static TextureRegion[] themeSceneOneBossInfoTR = new TextureRegion[BOSS_NUM_ONE];
	public static TextureRegion homeTR;
	public static TextureRegion backTR;
	
	
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
	
	public static BangYouScreenActivity getActivity(){
		return getInstance().activity;
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
	
	public static void loadThemeResources(){
		getInstance().loadThemeTextures();
		getInstance().loadSharedResources();
	}
	
	public static void loadGameResources(){
		getInstance().loadGameTextures();
		getInstance().loadSharedResources();
	}
	
	public static void loadBossResources(){
		getInstance().loadThemeBoss();
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
			mainMenuBackgroundTR = getLimitableTR("mainBG.png",mNormalTextureOption);
		}
		if(mainMenuTitleTR == null){
			mainMenuTitleTR = getLimitableTR("mainTitle.png",mNormalTextureOption);
		}
		if(mainMenuButtons == null){
			mainMenuButtons = getLimitableTTR("mainMenu.png",1,3,mNormalTextureOption);
		}
		if(menuClouds1 == null){
			menuClouds1 = getLimitableTR("menuClouds1.png",mNormalTextureOption);
		}
		if(menuClouds2 == null){
			menuClouds2 = getLimitableTR("menuClouds2.png",mNormalTextureOption);
		}
		if(loadingBG == null){
			loadingBG = getLimitableTR("loadingBG.png",mNormalTextureOption);
		}
		if(loadingBG1 == null){
			loadingBG1 = getLimitableTR("loadingBG1.png",mNormalTextureOption);
		}
		if(loadingBG2 == null){
			loadingBG2 = getLimitableTTR("loadingBG2.png",2,1,mNormalTextureOption);
		}
		if(loadingFont == null){
			loadingFont = getLimitableTTR("loadingFont.png",1,3,mNormalTextureOption);
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	
	// ============================ 主题纹理  ================= //
	private void loadThemeTextures(){
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/theme/");
		if(homeTR == null){
			homeTR = getLimitableTR("home.png",mNormalTextureOption);
		}
		if(backTR == null){
			backTR = getLimitableTR("back.png",mNormalTextureOption);
		}
		if(theme1Temp == null){
			theme1Temp = getLimitableTR("theme1.png",mNormalTextureOption);
		}
		if(theme2Temp == null){
			theme2Temp = getLimitableTR("theme2.png",mNormalTextureOption);
		}
		if(themeBG == null){
			themeBG = getLimitableTR("themeBG.png",mNormalTextureOption);
		}
		if(themeLevelBG == null){
			themeLevelBG = getLimitableTR("themeLevelBG.png",mNormalTextureOption);
		}
		if(themeLevelLock == null){
			themeLevelLock = getLimitableTR("themeLevelLock.png",mNormalTextureOption);
		}
		if(boss19 == null){
			boss19 = getLimitableTTR("boss19.png",3,1,mNormalTextureOption);
		}
		//根据完成的关卡数来加载BOSS纹理
//		int themeSceneOneBossTotal = BangYouScreenActivity.getIntFromSharedPreferences(BangYouScreenActivity.SHARED_PREFS_THEME_1);
//		themeSceneOneBossTotalTT = new ArrayList<TiledTextureRegion>();
//		for(int i=0; i<themeSceneOneBossTotal+1; i++){
//			String bossTexture = "boss1" + i + ".png";
//			themeSceneOneBossTotalTT.add(getLimitableTTR(bossTexture,3,2,mNormalTextureOption));
//		}
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	
	private void loadThemeBoss(){
		//下面一行是测试代码,待删除
		BangYouScreenActivity.writeIntToSharedPreferences(BangYouScreenActivity.SHARED_PREFS_THEME_1, 1);
		
		
		//根据完成的关卡数来加载BOSS纹理
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/theme/");
		
		int themeSceneOneBossTotal = BangYouScreenActivity.getIntFromSharedPreferences(BangYouScreenActivity.SHARED_PREFS_THEME_1);
		for(int i=0; i<themeSceneOneBossTotal+1; i++){
			if(themeSceneOneBossTotalTT[i] == null){
				String bossTexture = "boss1" + i + ".png";
				String bossInfo = "infoboss1" + i + ".png";
				themeSceneOneBossInfoTR[i] = getLimitableTR(bossInfo,mNormalTextureOption);
				switch (i) {
				case 0:
					themeSceneOneBossTotalTT[i] = getLimitableTTR(bossTexture,3,2,mNormalTextureOption);
					break;

				case 1:
					themeSceneOneBossTotalTT[i] = getLimitableTTR(bossTexture,3,2,mNormalTextureOption);
					break;
				}
				
			}

			
//			if(themeSceneOneBossTotalTT.get(i) == null){
//				String bossTexture = "boss1" + i + ".png";
//				themeSceneOneBossTotalTT.add(getLimitableTTR(bossTexture,3,2,mNormalTextureOption));
//			}
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
		if(gameBG10 == null){
			gameBG10 = getLimitableTR("bg10.jpg",mNormalTextureOption);
		}
		if(xue1 == null){
			xue1 = getLimitableTR("xue1.png",mNormalTextureOption);
		}
		if(xue2 == null){
			xue2 = getLimitableTR("xue2.png",mNormalTextureOption);
		}
		if(aidSkill1Temp == null){
			aidSkill1Temp = getLimitableTTR("skill1.png",3,4,mNormalTextureOption);
		}
		if(aidSkill2Temp == null){
			aidSkill2Temp = getLimitableTTR("skill2.png",3,4,mNormalTextureOption);
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
