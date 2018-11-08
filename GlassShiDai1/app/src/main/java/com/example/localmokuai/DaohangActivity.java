package com.example.localmokuai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.uilistviewtest.R;
import com.example.wangluomokuai.GlassActivity1;

import account.LoginActivity;
import account.RegisterActivity;

public class DaohangActivity extends Activity {//首页Activity,导航
	
	private ImageView LocalBT,WangluoBT;//两张图片
	private Button loginBT,registerBT;
	  protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.first_layout);
	        LocalBT=(ImageView) findViewById(R.id.localBT);
	        WangluoBT=(ImageView) findViewById(R.id.wangluoBT);
		   loginBT=(Button)findViewById(R.id.loginBT);
		    registerBT=(Button)findViewById(R.id.registerBT);
		 loginBT.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view) {
				 Intent intent=new Intent(DaohangActivity.this, LoginActivity.class);
				 startActivity(intent);//打开本地的登录界面
			 }
		 });
		  registerBT.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  Intent intent=new Intent(DaohangActivity.this, RegisterActivity.class);
				  startActivity(intent);//打开本地的注册界面
			  }
		  });

		  LocalBT.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				Intent intent=new Intent(DaohangActivity.this, GlassActivity.class);
				startActivity(intent);//打开本地的眼镜试戴界面
				}
			});
	        WangluoBT.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 Intent	intent =new Intent(DaohangActivity.this,GlassActivity1.class);
   	         startActivity(intent);//打开网络的眼镜试戴界面
				}
			});
	        
	  }
}
