package com.example.wangluomokuai;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.internal.AdapterView;
import com.example.internal.ListView;
import com.example.uilistviewtest.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import fuwuqi.JSONParser;

public class GlassActivity1 extends Activity {//多种眼镜样式界面
	private String[] urls1;//保存眼镜图片url
	private String[] names1;//保存眼镜名称
	private String[] id ;//保存眼镜序号
	private String[] sex;//保存眼镜性别
	private String[] glassstyle ;//保存眼镜类型
	private String[] feature;//保存脸型
	private int[] numbers11;//保存新的眼镜序号
	private String collectionString;//保存用户收藏眼镜眼镜
	private static String url_account_details = "http://qq282844655.hk623-hl.6464.cn/yanjing/get_account_details.php";
	private static String url_all_glasses = "http://qq282844655.hk623-hl.6464.cn/yanjing/get_all_glasses.php";
	private static String url_collection = "http://qq282844655.hk623-hl.6464.cn/yanjing/collection.php";
	private static String url_del_collection = "http://qq282844655.hk623-hl.6464.cn/yanjing/del_collection.php";
	//访问MYSQL的php文件，获取json数据
	private static final String TAG_GLASSES = "glasses";//json数据中数据库名称
	private static final String TAG_ACCOUNTS = "account";//json数据中数据数组名称
	private static final String TAG_SUCCESS = "success";//json数据中访问是否成功回调
	JSONArray glasses = null;//获取眼镜数据的json数组
	JSONParser jParser = new JSONParser();    //访问网络数据对象
	private Context mContext4;
	private List<Glass> glassList = new ArrayList<Glass>();//类对象数组，保存全部眼镜的数据
	private GlassAdapter adapter;//眼镜栏适配器
	private ListView listView;//自定义瀑布流ListView
	private EditText shuaixuanET;//筛选框
	private Dialog dialog;//对话框
	private Button shoucangbt;//收藏按钮
	private boolean Del=false;//收藏判断
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext4 = this;
		if (isNetworkAvailable(mContext4))//判断是否可以连接网络
			new Thread() {
				public void run() {
					new AnotherTask().execute("JSON");
				}
			}.start();  //异步线程获取数据，更新UI
		else {
			Toast.makeText(GlassActivity1.this, "网络连接不可用",
					Toast.LENGTH_SHORT).show();
		}
		listView = (ListView) findViewById(R.id.list_view);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Data app = (Data) getApplication(); //定义全局变量，获取图片路径
				if (app.getpath() == "") {//判断图片路径是否为空，意思为是否已经拍过照或者从相册里获取过图片
					Intent intent = new Intent(mContext4, SelectPicActivity.class);//打开获取照片界面
					intent.putExtra("position", String.valueOf(numbers11[position]));    //传递眼镜栏下标，即为第几副眼镜
					startActivity(intent);
				} else {
					Intent intent = new Intent(GlassActivity1.this, ShiDaiActivity.class);//直接打开眼镜试戴界面
					intent.putExtra("position", String.valueOf(numbers11[position]));//传递眼镜栏下标，即为第几副眼镜
					startActivity(intent);
				}
			}
		});
		shuaixuanET = (EditText) findViewById(R.id.xuanzheET);
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("筛选");
		shuaixuanET.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

					dialog.show();
					Button shaixuanbt = (Button) dialog.findViewById(R.id.shaixuanBT);
					//根据ID获取RadioButton的实例
					shaixuanbt.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (isNetworkAvailable(mContext4))//判断是否可以连接网络
						{
							String shaixuanstring = "", shaixuanstring1 = "", shaixuanstring2 = "";
							String sexString = "0", glassstyleString = "0", featureString = "0";
							RadioGroup group = (RadioGroup) dialog.findViewById(R.id.sex);
							int radioButtonId = group.getCheckedRadioButtonId();
							if (radioButtonId != -1) {
								RadioButton rb = (RadioButton) dialog.findViewById(radioButtonId);
								shaixuanstring = rb.getText() + "";
								if (shaixuanstring.equals("男")) {
									sexString = "1";
								} else {
									sexString = "2";
								}
							}

							RadioGroup group1 = (RadioGroup) dialog.findViewById(R.id.leixing);
							int radioButtonId1 = group1.getCheckedRadioButtonId();
							if (radioButtonId1 != -1) {
								RadioButton rb1 = (RadioButton) dialog.findViewById(radioButtonId1);
								shaixuanstring1 = rb1.getText() + "";
								if (shaixuanstring1.equals("光学眼镜")) {
									glassstyleString = "1";
								} else {
									glassstyleString = "2";
								}
							}

							RadioGroup group2 = (RadioGroup) dialog.findViewById(R.id.lianxing);
							int radioButtonId2 = group2.getCheckedRadioButtonId();
							if (radioButtonId2 != -1) {
								RadioButton rb2 = (RadioButton) dialog.findViewById(radioButtonId2);
								shaixuanstring2 = rb2.getText() + "";
								if (shaixuanstring2.equals("圆型脸")) {
									featureString = "1";
								} else if (shaixuanstring2.equals("长型脸")) {
									featureString = "2";
								} else if (shaixuanstring2.equals("方型脸")) {
									featureString = "3";
								} else if (shaixuanstring2.equals("椭圆型脸")) {
									featureString = "4";
								} else if (shaixuanstring2.equals("三角型脸")) {
									featureString = "5";
								} else {
									featureString = "6";
								}

							}
//					  Toast.makeText(GlassActivity1.this,sexString+glassstyleString+featureString,
//							  Toast.LENGTH_SHORT).show();
							glassList.clear();
							//筛选眼镜
							for (int i = 0, j = 0; i < id.length; i++) {
								if (sex[i] != null)
									if ((sex[i].equals(sexString) || sexString.equals("0")) &&
											(glassstyle[i].equals(glassstyleString) || glassstyleString.equals("0")) &&
											(feature[i].equals(featureString) || featureString.equals("0"))) {
										glassList.add(new Glass(names1[i], urls1[i]));//新建Glass对象添加到GlassList中
										numbers11[j] = i;
										j++;
									}
							}
							Del=false;
							rebuildAdapter();
							shuaixuanET.setText(shaixuanstring + " " + shaixuanstring1 + " " + shaixuanstring2);
							dialog.dismiss();
						}else{
								Toast.makeText(GlassActivity1.this, "网络连接不可用", Toast.LENGTH_SHORT).show();
							}
						}
					});

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(shuaixuanET.getWindowToken(), 0);//隐藏输入框

			}
		});
//收藏功能
		shoucangbt = (Button) findViewById(R.id.shoucangBT);
		shoucangbt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isNetworkAvailable(mContext4))//判断是否可以连接网络
				{

					Getcollection();
					SharedPreferences share = getSharedPreferences("login", MODE_PRIVATE);
					if (share.getString("account", "").length() == 0)
						Toast.makeText(GlassActivity1.this, "请登录", Toast.LENGTH_SHORT).show();
					else {
						String[] temp;
						temp = collectionString.split("#");
						glassList.clear();
						for (int i = 0; i < temp.length; i++)
							if(!temp[i].equals("")){
							int j = Integer.parseInt(temp[i]);
							glassList.add(new Glass(names1[j], urls1[j]));//新建Glass对象添加到GlassList中
							numbers11[i] = j;
						}
						Del=true;
						rebuildAdapter();
					}
				}else {
					Toast.makeText(GlassActivity1.this, "网络连接不可用",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("GlassActivity1 Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}

	private class AnotherTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			//对UI组件的更新操作
			rebuildAdapter();

		}

		@Override
		protected String doInBackground(String... params) {
			//耗时的操作
			initGlasses();//获取和处理数据

			return params[0];
		}
	}
    public  void rebuildAdapter(){
		adapter = new GlassAdapter(GlassActivity1.this, R.layout.fruit_item, glassList,Del);//建立适配器对象
		listView.setAdapter(adapter);
		adapter.setOnClickListener(new MyClickListener() {//在适配器里封装子控件监听事件
			@Override
			public void onPlayVideo(BaseAdapter adapter, View view, int position) {
				//详情按钮，打开详情界面
				Intent intent = new Intent(mContext4, XiangQingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.putExtra("position", String.valueOf(numbers11[position]));//传递眼镜栏下标，即为第几副眼镜
				startActivity(intent);
			}

			@Override
			public void onCollection(GlassAdapter glassAdapter, View v, int position) {
				Getcollection();
				SharedPreferences share=getSharedPreferences("login", MODE_PRIVATE);
				boolean shoucang=true;
				if(share.getString("account","").length()>0) {
					String[] temp ;
					temp = collectionString.split("#");
						for (int i = 0; i < temp.length; i++) {
							if (String.valueOf(numbers11[position]).equals(temp[i]))
								shoucang = false;
					}
					if(shoucang){
					int success;
					String collectionString=String.valueOf(numbers11[position])+"#";

					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("account", share.getString("account","")));
						params.add(new BasicNameValuePair("collection",collectionString));
						JSONObject json = jParser.makeHttpRequest(url_collection,
								"POST", params);
						success = json.getInt(TAG_SUCCESS);

						if (success == 1) {
							// successfully updated
							Toast.makeText(GlassActivity1.this,"收藏成功",Toast.LENGTH_SHORT).show();

						} else {
							Toast.makeText(GlassActivity1.this,json.getString("message"),Toast.LENGTH_SHORT).show();
							// failed to update product
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					}else{
						Toast.makeText(GlassActivity1.this, "已收藏过该眼镜",
								Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(GlassActivity1.this, "请登录",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onDelCollection(GlassAdapter glassAdapter, View v, int position) {
				Getcollection();
					String[] temp ;
					temp = collectionString.split("#");
				glassList.clear();
				int k=0;
				for (int i = 0; i < temp.length; i++) {
					if (!String.valueOf(numbers11[position]).equals(temp[i])) {
						int j=Integer.parseInt(temp[i]);
						glassList.add(new Glass(names1[j], urls1[j]));//新建Glass对象添加到GlassList中
						numbers11[k]=j;k++;
					}

				}
				rebuildAdapter();
				int success;
				SharedPreferences share = getSharedPreferences("login", MODE_PRIVATE);
				collectionString="";
				for(int i=0;i<k;i++){
					collectionString=collectionString+String.valueOf(numbers11[i])+"#";
				}
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("account", share.getString("account","")));
					params.add(new BasicNameValuePair("collection",collectionString));
					JSONObject json = jParser.makeHttpRequest(url_del_collection,
							"POST", params);
					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						// successfully updated
						Toast.makeText(GlassActivity1.this,"删除成功",Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(GlassActivity1.this,json.getString("message"),Toast.LENGTH_SHORT).show();
						// failed to update product
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void initGlasses() {

		Geturls1();//通过网络从（php获取的数据）中获取相应的数据
         numbers11=new int[id.length];
		for (int i = 0, j = 0; i < id.length; i++) {
			if (urls1[i] != null)
				glassList.add(new Glass(names1[i], urls1[i]));//新建Glass对象添加到GlassList中
			numbers11[j] = i;
			j++;
		}

	}

	public void Geturls1() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// getting JSON string from URL
		JSONObject json = jParser.makeHttpRequest(url_all_glasses, "GET", params);
		// Check your log cat for JSON reponse

		try {
			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				// products found
				// Getting Array of Products
				glasses = json.getJSONArray(TAG_GLASSES);
				// looping through All Products
				urls1=new String[glasses.length()];
				names1=new String[glasses.length()];
				id=new String[glasses.length()];
				sex=new String[glasses.length()];
				glassstyle=new String[glasses.length()];
				feature=new String[glasses.length()];
				for (int i = 0; i < glasses.length(); i++) {
					JSONObject c = glasses.getJSONObject(i);
					// Storing each json item in variable
					urls1[i] = c.getString("url1");
					names1[i] = c.getString("name");
					id[i] = c.getString("id");
					sex[i] = c.getString("sex");
					glassstyle[i] = c.getString("glassstyle");
					feature[i] = c.getString("feature");
					Log.d("url: ", urls1[i]);
					// creating new HashMap
					// adding each child node to HashMap key => value
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
					.getJSONArray(TAG_ACCOUNTS); // JSON Array
			// get first product object from JSON Array
			JSONObject account = accountObj.getJSONObject(0);
			collectionString=account.getString("collection");
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