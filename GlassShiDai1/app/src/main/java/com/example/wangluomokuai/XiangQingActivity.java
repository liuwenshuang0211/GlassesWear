package com.example.wangluomokuai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uilistviewtest.R;
import com.squareup.picasso.Picasso;


import fuwuqi.JSONParser;

import java.util.ArrayList;
import java.util.List;
		public class XiangQingActivity extends Activity {//详情界面Activity
			  private static String url_all_glasses = "http://qq282844655.hk623-hl.6464.cn/yanjing/get_all_glasses.php";
			    private static final String TAG_GLASSES = "glasses";
			    private static final String TAG_SUCCESS = "success";
			    JSONArray glasses = null;
				  JSONParser jParser = new JSONParser();	
			private ViewPager mViewPaper;
			private List<ImageView> images;
			private List<View> dots;
			private int oldPosition = 0;
			private String[]  imageStrings = new String[3];
			private TextView title,description;//图片名称及描述
			private String desString;
			private ViewPagerAdapter adapter;
			private Context context1;
			private int positionInter;//传递过来的眼镜样式下标
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.xiangqing);
		        context1=this;
		        Intent intent1 = getIntent();
		        positionInter=Integer.parseInt(intent1.getStringExtra("position"));	        	  
		    	mViewPaper = (ViewPager) findViewById(R.id.vp);			
				dots = new ArrayList<View>();		
				dots.add(findViewById(R.id.dot_1));
				dots.add(findViewById(R.id.dot_2));
				dots.add(findViewById(R.id.dot_3));			
				title = (TextView) findViewById(R.id.title);
				description=(TextView) findViewById(R.id.textView1);
				title.setText("");
				if (isNetworkAvailable(context1))//判断是否可以连接网络
				{
					new Thread() {
						public void run() {
							new AnotherTask().execute("JSON");
						}
					}.start();
				}else {
					Toast.makeText(XiangQingActivity.this, "网络连接不可用", Toast.LENGTH_SHORT).show();
				}
				mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						title.setText("");
						dots.get(position).setBackgroundResource(R.drawable.dot_focused);
						dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);						
						oldPosition = position;
					}					
					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {						
					}					
					@Override
					public void onPageScrollStateChanged(int arg0) {						
					}
				});		        
		}		    
		    private class AnotherTask extends AsyncTask<String, Void, String>{  
			    @Override  
			        protected void onPostExecute(String result) {  
					images = new ArrayList<ImageView>();
					for(int i = 0; i < 3; i++){//三张照片
						ImageView imageView = new ImageView(context1);				
						 Picasso.with(XiangQingActivity.this)
			    		   	.load(imageStrings[i])
			    		   	.resize(800,360).into(imageView); 
					images.add(imageView);
					}	
					description.setText(desString);
					adapter = new ViewPagerAdapter();//新建适配器
					mViewPaper.setAdapter(adapter);
			        }  
			    @Override  
			    protected String doInBackground(String... params) {  
			    	Geturls1();//获取数据
			        return params[0];  
			    }  
			    }  		    
		    public void Geturls1(){	 
				 List<NameValuePair> params = new ArrayList<NameValuePair>();
			     JSONObject json = jParser.makeHttpRequest(url_all_glasses, "GET", params);
			     Log.d("All Glasses: ", json.toString());
			    try {
			         int success = json.getInt(TAG_SUCCESS);
			         if (success == 1) {
			      glasses= json.getJSONArray(TAG_GLASSES);	  
			                 JSONObject c = glasses.getJSONObject(positionInter);			            
			                 imageStrings[0]=  c.getString("url2");		
			                 imageStrings[1]=  c.getString("url1");			
			                 imageStrings[2]=  c.getString("url3");		
			                 desString=c.getString("description");
			         } 
			     } catch (JSONException e) {
			         e.printStackTrace();
			     }				
			 }		    
		    private class ViewPagerAdapter extends PagerAdapter{
			@Override
				public int getCount() {
					return images.size();
				}
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					return arg0 == arg1;
				}
				@Override
				public void destroyItem(ViewGroup view, int position, Object object) {
					view.removeView(images.get(position));
				}
				@Override
				public Object instantiateItem(ViewGroup view, int position) {
					// TODO Auto-generated method stub
					view.addView(images.get(position));
					return images.get(position);
				}				
			}			
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.main, menu);
				return true;
			}

			/**
			 * 检测网络连接
			 *
			 * @param con
			 * @return
			 */
			public boolean isNetworkAvailable(Context con) {
				ConnectivityManager cm = (ConnectivityManager) con
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (cm == null)
					return false;
				NetworkInfo netinfo = cm.getActiveNetworkInfo();
				if (netinfo == null) {
					return false;
				}
				if (netinfo.isConnected()) {
					return true;
				}
				return false;
			}
		}
		
	

