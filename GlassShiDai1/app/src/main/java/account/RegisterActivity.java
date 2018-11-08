package account;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.uilistviewtest.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import fuwuqi.JSONParser;
/**
 * Created by shuangshuang on 2017/2/26.
 */
public class RegisterActivity extends Activity {
private EditText accountet,passwordet1,passwordet2;
    private Button registerbt;
    private Context mContext4;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_account = "http://qq282844655.hk623-hl.6464.cn/yanjing/create_account.php";
    private static final String TAG_SUCCESS = "success";
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mContext4=this;
        accountet=(EditText) findViewById(R.id.accountET1);
        passwordet1=(EditText) findViewById(R.id.passwordET1);
        passwordet2=(EditText) findViewById(R.id.passwordET2);
        registerbt=(Button)findViewById(R.id.registerBT);


        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(mContext4))//判断是否可以连接网络
                {
                    String accountString = accountet.getText().toString(),
                            passwordString1 = passwordet1.getText().toString(),
                            passwordString2 = passwordet2.getText().toString();
                    if (accountString.length() > 0 && passwordString1.length() > 0 && passwordString2.length() > 0
                            && passwordString1.equals(passwordString2))
                        try {
                            // Building Parameters
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("account", URLEncoder.encode(accountString)));
                            params.add(new BasicNameValuePair("password", passwordString1));
                            // getting JSON Object
                            // Note that create product url accepts POST method
                            JSONObject json = jsonParser.makeHttpRequest(url_create_account,
                                    "POST", params);
                            // check log cat fro response
                            Log.d("Create Response", json.toString());
                            // check for success tag
                            int success = json.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                // successfully created product

                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                                // closing this scree
                            } else {
                                Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                // failed to create product
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    else if (accountString.length() == 0) {
                        Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (passwordString1.length() == 0 || passwordString2.length() == 0) {
                        Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else if (!passwordString1.equals(passwordString2)) {
                        Toast.makeText(RegisterActivity.this, "输入密码不相同", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "网络连接不可用",
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
