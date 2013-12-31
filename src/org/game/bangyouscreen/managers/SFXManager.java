package org.game.bangyouscreen.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.util.math.MathUtils;

import android.content.Context;

/**
 * 音效管理 
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class SFXManager {
	
	private static SFXManager INSTANCE = null;
	
	private Sound mClick;
	private Music mMusic;
    private HashMap<String, Music> mMusicMap = new HashMap();
    private HashMap<String, Sound> mSoundsMap = new HashMap();
	
	public static SFXManager getInstance(){
	    if (INSTANCE == null)
	    	INSTANCE = new SFXManager();
	    return INSTANCE;
    }
	
	//****************************  音乐   ****************************//
	
	/**
	 * 播放指定的音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void playMusic(String paramString){
    	mMusic = mMusicMap.get(paramString);
    	mMusic.setLooping(true);
    	mMusic.play();
    }
    
	/**
	 * 音乐暂停
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void pauseMusic(String paramString){
    	//mMusic = mMusicMap.get(paramString);
        if (mMusic.isPlaying())
        	mMusic.pause();
    }
    
	/**
	 * 加载音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void loadMusic(String paramString, MusicManager paramMusicManager, Context paramContext){
    	MusicFactory.setAssetBasePath("mfx/");
    	try {
    		if (!mMusicMap.containsKey(paramString)){
    			mMusicMap.put(paramString, MusicFactory.createMusicFromAsset(paramMusicManager, paramContext, paramString + ".ogg"));
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 加载音乐组
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void loadMusics(String[] paramString, MusicManager paramMusicManager, Context paramContext){
    	MusicFactory.setAssetBasePath("mfx/");
    	try {
    		for(String s:paramString){
    			if (!mMusicMap.containsKey(s)){
    				mMusicMap.put(s, MusicFactory.createMusicFromAsset(paramMusicManager, paramContext, s + ".ogg"));
    			}
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 暂停所有音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void pauseAllMusic(){
        Iterator localIterator = this.mMusicMap.keySet().iterator();
        while (true){
          boolean bool = localIterator.hasNext();
          if (!bool)
            return;
          pauseMusic((String)localIterator.next());
        }
    }
    
	/**
	 * 停止音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void stopMusic(String paramString){
    	mMusicMap.get(paramString).stop();
    }
    
	/**
	 * 停止所有音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void stopAllMusic(){
        Iterator localIterator = this.mMusicMap.keySet().iterator();
        while(true){
          boolean bool = localIterator.hasNext();
          if(!bool)
            return;
          stopMusic((String)localIterator.next());
        }
    }
    
	/**
	 * 卸载音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void unloadMusic(String paramString){
        if (this.mMusicMap.containsKey(paramString)){
          mMusicMap.get(paramString).release();
          mMusicMap.remove(paramString);
        }
    }
    
	/**
	 * 卸载所有音乐
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void unloadAllMusic(String[] paramString){
    	for(String s:paramString){
    		unloadMusic(s);
    	}
    }
    
    //****************************  声音   ****************************//

	/**
	 * 加载声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void loadSound(String paramString, SoundManager paramSoundManager, Context paramContext){
    	SoundFactory.setAssetBasePath("sfx/");
    	try {
    		if(!mSoundsMap.containsKey(paramString)){
        		mSoundsMap.put(paramString, SoundFactory.createSoundFromAsset(paramSoundManager, paramContext, paramString + ".ogg"));	
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 加载声音组
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void loadSounds(String[] paramString, SoundManager paramSoundManager, Context paramContext){
    	SoundFactory.setAssetBasePath("sfx/");
    	try {
    		for(String s:paramString){
    			if(!mSoundsMap.containsKey(s)){
    				mSoundsMap.put(s, SoundFactory.createSoundFromAsset(paramSoundManager, paramContext, s + ".ogg"));
    			}
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 播放指定的声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void playSound(String paramString){
		Sound localSound = mSoundsMap.get(paramString);
		localSound.setLoopCount(0);
		localSound.play();
    }
	
	/**
	 * 播放指定范围内的声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void playSound(String[] paramArrayOfString){
		int i = MathUtils.random(0, -1 + paramArrayOfString.length);
		Sound localSound = mSoundsMap.get(paramArrayOfString[i]);
		localSound.setLoopCount(0);
		localSound.play();
	}
	
	/**
	 * 播放指定的声音和次数
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void playSound(String paramString, int paramInt){
		Sound localSound = mSoundsMap.get(paramString);
		localSound.setLoopCount(paramInt);
		localSound.play();
    }
    
	/**
	 * 暂停所有声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void pauseAllSounds(){
        Iterator localIterator = this.mSoundsMap.keySet().iterator();
        while(true){
          boolean bool = localIterator.hasNext();
          if(!bool)
            return;
          pauseSound((String)localIterator.next());
        }
    }
    
	/**
	 * 暂停声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void pauseSound(String paramString){
         mSoundsMap.get(paramString).pause();
    }
    
	/**
	 * 停止声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void stopSound(String paramString){
    	mSoundsMap.get(paramString).stop();
    }
    
	/**
	 * 停止所有声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void stopAllSounds(){
        Iterator localIterator = this.mSoundsMap.keySet().iterator();
        while(true){
          boolean bool = localIterator.hasNext();
          if(!bool)
            return;
          stopSound((String)localIterator.next());
        }
    }
    
	/**
	 * 卸载声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void unloadSound(String paramString){
        if (this.mSoundsMap.containsKey(paramString)){
        	mSoundsMap.get(paramString).release();
        	mSoundsMap.remove(paramString);
        }
    }
    
	/**
	 * 卸载所有声音
	 * @author zuowhat 2013-12-06
	 * @since 1.0
	 */
    public void unloadAllSound(String[] paramString){
    	for(String s:paramString){
    		unloadSound(s);
    	}
    }
    
}
