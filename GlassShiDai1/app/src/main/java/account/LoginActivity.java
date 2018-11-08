package account;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.uilistviewtest.R;
import com.example.wangluomokuai.Data;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fuwuqi.JSONParser;

/**
 * Created by shuangshuang on 2017/2/26.
 */
public class LoginActivity extends Activity {
    private Context mContext4;
    JSONParser jParser = new JSONParser();	//访问网络数据对象
    private static final String TAG_SUCCESS = "success";//json数据中访问是否成功回调
    private static final String TAG_ACCOUNTS = "account";//json数据中数组名称
    private static String url_account_details = "http://qq282844655.hk623-hl.6464.cn/yanjing/get_account_details.php";
    //访问MYSQL的php文件，获取json数据
  private EditText accountet,passwordet;
    private Button loginBT;
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext4=this;
        accountet=(EditText) findViewById(R.id.accountET);
        passwordet=(EditText) findViewById(R.id.passwordET);
        loginBT=(Button) findViewById(R.id.loginBT);
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(mContext4))//判断是否可以连接网络
                {
                    String accountString = accountet.getText().toString(),
                            passwordString = passwordet.getText().toString();
                    int success;
                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("account", accountString));
                        // getting account details by making HTTP request
                        // Note that account details url will use GET request
                        JSONObject json = jParser.makeHttpRequest(url_account_details, "GET", params);
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            JSONArray accountObj = json
                                    .getJSONArray(TAG_ACCOUNTS); // JSON Array
                            // get first product object from JSON Array
                            JSONObject account = accountObj.getJSONObject(0);

                            if (account.getString("password").equals(passwordString)) {
                                Toast.makeText(LoginActivity.this, "登录成功",
                                        Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE); //私有数据
                                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                                editor.putString("account", accountString);
                                editor.commit();//提交修改
                                finish();
                            } else
                                Toast.makeText(LoginActivity.this, "密码错误",
                                        Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else{
                    Toast.makeText(LoginActivity.this, "网络连接不可用",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
