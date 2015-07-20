package com.example.sinademo;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.PreferenceHelper;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.jugame.assistant.R;

public class MainActivity extends KJActivity {
	private static String TAG = "MainActivity";
	private TextView mTextView;

	@Override
	public void setRootView() {
		setContentView(R.layout.main);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		Button collectBtn = (Button) findViewById(R.id.collect_btn);
		collectBtn.setOnClickListener(this);
		mTextView = (TextView) findViewById(R.id.text);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.collect_btn:
			getCollectList();
			break;

		default:
			break;
		}
	}

	private void getCollectList() {
		HttpConfig config = new HttpConfig();
		config.cacheTime = 0;
		KJHttp kjh = new KJHttp(config);
		HttpParams params = new HttpParams();
		params.put("access_token", PreferenceHelper.readString(MainActivity.this, "WBAuthActivity", "token"));
		params.put("count", 50);
		params.put("page", 1);
		kjh.get("https://api.weibo.com/2/favorites.json", params, new HttpCallBack() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if (t != null) {
					Log.i("TAG", t);
					mTextView.setText(t);
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Log.e("TAG", strMsg);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				Log.i("TAG", "finish!");
			}
			
		});
	}

}
