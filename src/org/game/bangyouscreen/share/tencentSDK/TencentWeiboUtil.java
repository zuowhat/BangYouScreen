package org.game.bangyouscreen.share.tencentSDK;

import org.game.bangyouscreen.managers.ResourceManager;
import org.game.bangyouscreen.util.DataConstant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.tencent.weibo.sdk.android.api.WeiboAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;


public class TencentWeiboUtil {
	
	public static TencentWeiboUtil INSTANCE;
    private long APP_KEY = 801489744;
    private String APP_SECKET = "d93f407145645e6797d07fb03288dd93";
    private Context context;
    private String requestFormat = "json";
    private double longitude = 0d;  //初始
	private double latitude = 0d;
	private HttpCallback mCallBack;//回调函数
	private String accessToken;
	
	public static TencentWeiboUtil getInstance(){
		if(INSTANCE == null){
			INSTANCE = new TencentWeiboUtil();
		}
		return INSTANCE;
	}

    public void authToken(final Bitmap bitmap) {
    	accessToken = Util.getSharePersistent(ResourceManager.getActivity().getApplicationContext(),"ACCESS_TOKEN");
    	context = ResourceManager.getActivity().getApplicationContext();
    	if (accessToken == null || "".equals(accessToken)) {
			//注册当前应用的appid和appkeysec，并指定一个OnAuthListener
			//OnAuthListener在授权过程中实施监听
			AuthHelper.register(ResourceManager.getActivity(), APP_KEY, APP_SECKET, new OnAuthListener() {

				//如果当前设备没有安装腾讯微博客户端，走这里
				@Override
				public void onWeiBoNotInstalled() {
					ResourceManager.getActivity().toastOnUiThread("onWeiBoNotInstalled", Toast.LENGTH_LONG);
					AuthHelper.unregister(ResourceManager.getActivity());
					Intent i = new Intent(ResourceManager.getActivity(),Authorize.class);
					ResourceManager.getActivity().startActivity(i);
				}

				//如果当前设备没安装指定版本的微博客户端，走这里
				@Override
				public void onWeiboVersionMisMatch() {
					ResourceManager.getActivity().toastOnUiThread("onWeiboVersionMisMatch", Toast.LENGTH_LONG);
					AuthHelper.unregister(ResourceManager.getActivity());
					Intent i = new Intent(ResourceManager.getActivity(),Authorize.class);
					ResourceManager.getActivity().startActivity(i);
				}

				//如果授权失败，走这里
				@Override
				public void onAuthFail(int result, String err) {
					ResourceManager.getActivity().toastOnUiThread("result : " + result, Toast.LENGTH_LONG);
					AuthHelper.unregister(ResourceManager.getActivity());
				}

				//授权成功，走这里
				//授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
				//在这里，存放到了applicationcontext中
				@Override
				public void onAuthPassed(String name, WeiboToken token) {
					ResourceManager.getActivity().toastOnUiThread("passed", Toast.LENGTH_LONG);
					Util.saveSharePersistent(context, "ACCESS_TOKEN", token.accessToken);
					Util.saveSharePersistent(context, "EXPIRES_IN", String.valueOf(token.expiresIn));
					Util.saveSharePersistent(context, "OPEN_ID", token.openID);
//					Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
					Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
//					Util.saveSharePersistent(context, "NAME", name);
//					Util.saveSharePersistent(context, "NICK", name);
					Util.saveSharePersistent(context, "CLIENT_ID", APP_KEY+"");
					Util.saveSharePersistent(context, "AUTHORIZETIME",
							String.valueOf(System.currentTimeMillis() / 1000l));
					AuthHelper.unregister(ResourceManager.getActivity());
					showShare(bitmap);
				}
			});
			AuthHelper.auth(ResourceManager.getActivity(), "");
		}else{
			showShare(bitmap);
		}
	}
    
    private void showShare(Bitmap bitmap){
    	if(mCallBack == null){
	       	 mCallBack = new HttpCallback() {
	 			@Override
	 			public void onResult(Object object) {
	 				ModelResult result = (ModelResult) object;
	// 				if(loadingWindow!=null && loadingWindow.isShowing()){
	// 					loadingWindow.dismiss();
	// 				}
	 				if(result!=null && result.isSuccess()){
	 					ResourceManager.getActivity().toastOnUiThread("分享微博成功", Toast.LENGTH_LONG);
	// 					Intent i = new Intent(GeneralInterfaceActivity.this,GeneralDataShowActivity.class);
	// 					i.putExtra("data", result.getObj().toString());					
	// 					startActivity(i);
	 				}else{
	 					ResourceManager.getActivity().toastOnUiThread("分享微博失败", Toast.LENGTH_LONG);
	 				}
	 				
	 			}
	 		};
   	}
		AccountModel account = new AccountModel(accessToken);
		WeiboAPI weiboAPI = new WeiboAPI(account);
		try{
			//Bitmap bm = BitmapFactory.decodeStream(context.getAssets().open("logo.png"));//BitmapFactory.decodeFile(pic);
			weiboAPI.addPic(context, DataConstant.weiboText, requestFormat, longitude, latitude, bitmap, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);	
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
}
