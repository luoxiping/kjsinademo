package com.example.sinademo;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.PreferenceHelper;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.jugame.assistant.R;

public class WBAuthActivity extends KJActivity {
	private static final String TAG = "WBAuthActivity";
	private AuthInfo mAuthInfo;
	private Oauth2AccessToken mAccessToken;
	private SsoHandler mSsoHandler;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_main);
		if (PreferenceHelper.readString(WBAuthActivity.this, TAG, "token") != null) {
			Intent intent = new Intent(WBAuthActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		Button button = (Button) findViewById(R.id.login_btn);
		button.setOnClickListener(this);
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.login_btn:
			doLogin();
			break;

		default:
			break;
		}
	}

	class AuthListener implements WeiboAuthListener {
		
		@SuppressLint("NewApi")
		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
			if (mAccessToken.isSessionValid()) {
				PreferenceHelper.write(WBAuthActivity.this, TAG, "token", mAccessToken.getToken()); //保存Token
				PreferenceHelper.write(WBAuthActivity.this, TAG, "uid", mAccessToken.getUid()); //保存Uid
			} else {
				// 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
				String code = values.getString("code", "");
				Log.e(TAG, code + "");
			}
		}

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			
		}
	}

	private void doLogin() {
//		HttpConfig config = new HttpConfig();
//		config.cacheTime = 0;
//		KJHttp kjh = new KJHttp(config);
//		HttpParams params = new HttpParams();
		mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
		mSsoHandler. authorizeClientSso(new AuthListener());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
			Intent intent = new Intent(WBAuthActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
