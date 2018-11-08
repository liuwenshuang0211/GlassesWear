package com.example.wangluomokuai;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uilistviewtest.R;
import com.example.wangluomokuai.MyHorizontalScrollView.OnItemClickListener;
import com.squareup.picasso.Picasso;

import fuwuqi.JSONParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
public class ShiDaiActivity extends Activity{//眼镜试戴界面
	private String path;//保存图片路径
	private ImageView zhaopianImageView,yanjin;//头像图片、眼镜图片
	 private String[] urls1;//眼镜样式图片url
	 private String[] zhengmianurls;//眼镜正面照片PNG，透视眼镜url保存
	private String collectionString;//保存用户收藏眼镜眼镜
    private static String url_all_glasses = "http://qq282844655.hk623-hl.6464.cn/yanjing/get_all_glasses.php";
	private static String url_collection = "http://qq282844655.hk623-hl.6464.cn/yanjing/collection.php";
	private static String url_account_details = "http://qq282844655.hk623-hl.6464.cn/yanjing/get_account_details.php";
	private static final String TAG_GLASSES = "glasses";
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_ACCOUNT = "account";//json数据中数据数组名称
    private int positionInter;//保存上一个Activity传递过来眼镜系列下标，即第几副眼镜
       private static int[] urlszan=new int[120];//静态数组保存上面的positionInter
       private Context mContext1;
    JSONArray glasses = null;
	JSONArray accounts = null;//获取用户数据的json数组
    private static int urlnum=0;//下方眼镜试戴栏最大下标，即共有几副眼镜
    private boolean panduan=true;//判断positionInter是否存在于urlszan静态数组中
    private Button chongpaiBT,xiangqingBT,shouchangBT;//重新拍照按钮，查看眼镜详情按钮
    private static int number1;//保存当前正在试戴的眼镜在水平眼镜栏的下标
	  JSONParser jParser = new JSONParser();	
		private MyHorizontalScrollView mHorizontalScrollView;//自定义水平方向滑动控件
		private HorizontalScrollViewAdapter mAdapter;//自定义水平方向滑动控件适配器
		private List<String> mDatas = new ArrayList<String>();//保存眼镜样式url的String数组对象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shidai);
	mContext1=this;
		 final Data app = (Data)getApplication();  //定义全局变量
Intent intent1 = getIntent();
if(app.getpath()=="")//判断全局变量是否给path赋值，此模块代码是为了只打开一次SelectPicActivity
{ path=intent1.getStringExtra("one");
app.setpath(path);
}	 
else {
	path=app.getpath();
}
positionInter=Integer.parseInt(intent1.getStringExtra("position"));//保存眼镜样式系列下标，即第几副眼镜
		if(urlnum==0){//下标为0，直接将positionInter保存进urlszan静态数组中去
urlszan[urlnum]=positionInter;
urlszan[urlnum+1]=-1;
}
for(int i=1;i<=urlnum;i++){//判断urlszan静态数组中是否存在相同的positionInter，
	urlszan[urlnum+1]=-1;
		urlszan[urlnum+2]=-1;
		if(urlnum>0&&urlszan[0]==positionInter){
			panduan=false;//存在相同的positionInter		
		}			
	if(urlszan[i]==positionInter){
		panduan=false;//存在相同的positionInter		
	}
}
if(panduan){
	urlszan[urlnum]=positionInter;//将positionInter保存进urlszan静态数组中去
	urlnum++;//最大下标加一
}
for(int i=0;i<urlnum;i++)
if(positionInter==urlszan[i])
	number1=i;//保存当前正在试戴的眼镜在水平眼镜栏的下标


	       zhaopianImageView=(ImageView) findViewById(R.id.tupain);
	       yanjin =(ImageView) findViewById(R.id.tupain1);
	       chongpaiBT=(Button) findViewById(R.id.button1);
	       chongpaiBT.setOnClickListener(new OnClickListener() {	//重新拍照按钮		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext1,SelectPicActivity.class);//打开选择获取照片方式的界面
			   intent.putExtra("position", String.valueOf(urlszan[number1]));//传递眼镜样式下标
			startActivity(intent);
			finish();
			}
		});
	       xiangqingBT=(Button) findViewById(R.id.button2);
	       xiangqingBT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext1,XiangQingActivity.class);//打开选择获取照片方式的界面
							  intent.putExtra("position", String.valueOf(urlszan[number1]));//传递眼镜样式下标
			startActivity(intent);
			}
		});
	       
	       shouchangBT=(Button) findViewById(R.id.shoucangBT);
		shouchangBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isNetworkAvailable(mContext1))//判断是否可以连接网络
				{
				SharedPreferences share=getSharedPreferences("login", MODE_PRIVATE);
				boolean shoucang=true;
				if(share.getString("account","").length()>0) {
					Getcollection();
					String[] temp ;
		     temp = collectionString.split("#");
					for (int i = 0; i < temp.length; i++) {
						if (String.valueOf(urlszan[number1]).equals(temp[i]))
							shoucang = false;
					}
					if(shoucang){
						int success;
						String collectionString=String.valueOf(urlszan[number1])+"#";

						try {

							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("account", share.getString("account","")));
							params.add(new BasicNameValuePair("collection",collectionString));
							JSONObject json = jParser.makeHttpRequest(url_collection,
									"POST", params);
							success = json.getInt(TAG_SUCCESS);

							if (success == 1) {
								// successfully updated
								Toast.makeText(ShiDaiActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();

							} else {
								Toast.makeText(ShiDaiActivity.this,json.getString("message"),Toast.LENGTH_SHORT).show();
								// failed to update product
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						Toast.makeText(ShiDaiActivity.this, "已收藏过该眼镜",
								Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(ShiDaiActivity.this, "请登录",
							Toast.LENGTH_SHORT).show();
				}
				}else{
					Toast.makeText(ShiDaiActivity.this, "网络连接不可用",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	  
	       Bitmap bm = BitmapFactory.decodeFile(path);
	       zhaopianImageView.setImageBitmap(bm);    //设置头像照片
	   	mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
	  mAdapter = new HorizontalScrollViewAdapter(this, mDatas);
	
//添加点击回调
mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener()
{
	public void onClick(View view, int position)
	{
	number1=position;//保存当前正在试戴的眼镜在水平眼镜栏的下标
		for(int i=0;i<urlnum;i++){
			if(i==position)		//设置试戴的眼镜图片				
		 Picasso.with(ShiDaiActivity.this)
		   	.load(zhengmianurls[urlszan[i]])
		   	.resize(760,260).into(yanjin); 			
			}			
if(position==urlnum)//点击加号按钮
	finish();			
	}
});

	              	       new Thread(){  
	            public void run(){  
	                new AnotherTask().execute("JSON");  
	            }  
	        }.start();  	 //异步线程
			if (!isNetworkAvailable(mContext1))//判断是否可以连接网络
			Toast.makeText(ShiDaiActivity.this, "网络连接不可用",
					Toast.LENGTH_SHORT).show();


	}
	
	 private class AnotherTask extends AsyncTask<String, Void, String>{  
		    @Override  
		        protected void onPostExecute(String result) {  
		             //对UI组件的更新操作  	

   mAdapter.setOnClickListener(new MyClickListener1() {//删除按钮监听
				@Override
				public void Delete(
						HorizontalScrollViewAdapter horizontalScrollViewAdapter, View v,
						int position) {								
				                   mDatas.remove(position); 				               
				                   mAdapter=mHorizontalScrollView.getAdapter();
				                   mAdapter.notifyDataSetChanged();
				                   for(int i=position;i<urlnum;i++){
									urlszan[i]=urlszan[i+1];
									} 
				                   urlnum--; 			                   
				                  if(urlnum<4)//更新UI
				       		    	mHorizontalScrollView.initDatas(mAdapter);
				       		    	else {
				       		    		mHorizontalScrollView.initDatas(mAdapter,urlnum+1);
				       				}
				}					
				});
   
   
		    	if(urlnum<4)//更新UI
		    	mHorizontalScrollView.initDatas(mAdapter);
		    	else {
		    		mHorizontalScrollView.initDatas(mAdapter,urlnum+1);
				}
 	       Picasso.with(ShiDaiActivity.this)
	   	.load(zhengmianurls[positionInter])
	   	.resize(760,260).into(yanjin); //设置眼镜试戴图片	
 	     
		        }  
		    @Override  
		    protected String doInBackground(String... params) {  
		        //耗时的操作  
		Geturls1();

		for(int i=0;i<urlnum;i++)	
			mDatas.add(urls1[urlszan[i]]);	//mDatas数组对象添加眼镜样式照片url
		        return params[0];  
		    }  
		    }
	public void Getcollection() {
		SharedPreferences share=getSharedPreferences("login", MODE_PRIVATE);
		int success;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			params.add(new BasicNameValuePair("account",share.getString("account","")));
			JSONObject json = jParser.makeHttpRequest(url_account_details, "GET", params);
			// Check your log cat for JSON reponse
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				JSONArray accountObj = json
						.getJSONArray(TAG_ACCOUNT); // JSON Array
				// get first product object from JSON Array
				JSONObject account = accountObj.getJSONObject(0);
				collectionString=account.getString("collection");
			}

		} catch (JSONException e) {
			e.printStackTrace();

		}
	}
	 public void Geturls1(){	 
		 @SuppressWarnings("deprecation")
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	     // getting JSON string from URL
	     JSONObject json = jParser.makeHttpRequest(url_all_glasses, "GET", params);
	  // Check your log cat for JSON reponse
	     Log.d("All Glasses: ", json.toString());
	    try {
	         // Checking for SUCCESS TAG
	         int success = json.getInt(TAG_SUCCESS);
	         if (success == 1) {
	             // products found
	             // Getting Array of Products
	      glasses= json.getJSONArray(TAG_GLASSES);
	             // looping through All Products
				 urls1=new String[glasses.length()];
				 zhengmianurls=new String[glasses.length()];
	          for (int i = 0; i < glasses.length(); i++) {
	                 JSONObject c = glasses.getJSONObject(i);
	                 // Storing each json item in variable
	           
	                 urls1[i] = c.getString("url1");   
	                 zhengmianurls[i] = c.getString("zhengmainurl");
	                 // creating new HashMap
	      // adding each child node to HashMap key => value      
	             }
	         } 
	     } catch (JSONException e) {
	         e.printStackTrace();
	     }
		
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
