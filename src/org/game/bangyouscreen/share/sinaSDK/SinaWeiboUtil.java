/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.game.bangyouscreen.share.sinaSDK;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.andengine.util.FileUtils;
import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.util.Constants;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.exception.WeiboShareException;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class SinaWeiboUtil{
	
	public static SinaWeiboUtil INSTANCE;
	public static SinaWeiboUtil getInstance(){
		if(INSTANCE == null){
			INSTANCE = new SinaWeiboUtil();
		}
		return INSTANCE;
	}

    private String APP_KEY = "3974289953";

    private String weibosdk_not_support_api_hint = "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本";
    
    private String weibosdk_cancel_download = "取消下载";
    
    public static final String weibosdk_share_success = "分享成功";
    
    public static final String weibosdk_share_failed = "分享失败";
    
    public static final String weibosdk_share_canceled = "取消分享";
    
    public static final String weibosdk_share_install = "未安装新浪微博客户端...";
    
    /** 微博微博分享接口实例 */
    public IWeiboShareAPI  mWeiboShareAPI = null;
    
    public void init(AuthActivity activity,Bundle savedInstanceState) {
    	System.out.println("SinaWeiBoUtil--->onCreate");
    	
    	// 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, APP_KEY);
        
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        
        // 如果未安装微博客户端，设置下载微博对应的回调
//        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
//            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
//                @Override
//                public void onCancel() {
//                	Toast.makeText(activity.this, 
//                			weibosdk_cancel_download, 
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
        
		// 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), ResourceManager.getActivity());
//        }
        
        showShare();
    	
    }
    
    public void showShare(){
    	System.out.println("SinaWeiBoUtil--->showShare");
    	try {
            // 检查微博客户端环境是否正常，如果未安装微博，弹出对话框询问用户下载微博客户端
    		if(mWeiboShareAPI.isWeiboAppInstalled()){
    			if (mWeiboShareAPI.checkEnvironment(true)) {                    
                	if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                            sendMultiMessage(true, true);
                        } else {
                            sendSingleMessage(true, true);
                        }
                    } else {
                        ResourceManager.getActivity().toastOnUiThread(weibosdk_not_support_api_hint, Toast.LENGTH_SHORT);
                    }
                }
    		}else{
    			ResourceManager.getActivity().toastOnUiThread(weibosdk_share_install, Toast.LENGTH_LONG);
    		}
        } catch (WeiboShareException e) {
            e.printStackTrace();
            ResourceManager.getActivity().toastOnUiThread(e.getMessage(), Toast.LENGTH_LONG);
            //Toast.makeText(WBShareActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
    	
    	
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            TextObject textObject = new TextObject();
            textObject.text = Constants.weiboTextS;
            weiboMessage.textObject = textObject;
        }
        
        if (hasImage) {
        	String path = FileUtils.getAbsolutePathOnExternalStorage(ResourceManager.getInstance().activity, Constants.SCREENCAPTURE);
        	Bitmap bitmap = BitmapFactory.decodeFile(path);
            try {
    			FileOutputStream fos = new FileOutputStream(path);
    			bitmap.compress(CompressFormat.JPEG, 50, fos);
    			fos.flush();
    			fos.close();
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        	
        	
        	
        	ImageObject imageObject = new ImageObject();
            imageObject.setImageObject(bitmap);
            weiboMessage.imageObject = imageObject;
        }
        
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private void sendSingleMessage(boolean hasText, boolean hasImage) {
        
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            TextObject textObject = new TextObject();
            textObject.text = Constants.weiboTextS;
            weiboMessage.mediaObject = textObject;
        }
        if (hasImage) {
            //weiboMessage.mediaObject = getImageObj();
        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }
    
//    public void onResponse(BaseResponse baseResp) {
//    	System.out.println("SinaWeiBoUtil --> onResponse --> "+baseResp.errCode);
//        switch (baseResp.errCode) {
//        case WBConstants.ErrorCode.ERR_OK:
//            Toast.makeText(this, weibosdk_share_success, Toast.LENGTH_LONG).show();
//            break;
//        case WBConstants.ErrorCode.ERR_CANCEL:
//            Toast.makeText(this, weibosdk_share_canceled, Toast.LENGTH_LONG).show();
//            break;
//        case WBConstants.ErrorCode.ERR_FAIL:
//            Toast.makeText(this, 
//            		weibosdk_share_failed + "Error Message: " + baseResp.errMsg, 
//                    Toast.LENGTH_LONG).show();
//            break;
//        }
//    }
	
//	 protected void onNewIntent(Intent intent) {
//	        super.onNewIntent(intent);
//	        
//	        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
//	        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
//	        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
//	       mWeiboShareAPI.handleWeiboResponse(intent, ResourceManager.getActivity());
//	    }
    
}
