package demo.cxl.com.voicedemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserInfo;

/**
 * Created by cxl 2018/9/25.
 */

public class LoginActivity extends Activity {

    private EditText mEt_login;
    private EditText mEt_password;
    private Button mBt_login;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        mEt_login = findViewById(R.id.et_login);
        mEt_password = findViewById(R.id.et_password);
        mBt_login = findViewById(R.id.bt_login);
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private void init() {
        mBt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
    }

    private void login() {
        String phone = mEt_login.getText().toString().trim();
        String password = mEt_password.getText().toString().trim();
        if(phone.isEmpty()||phone.length()<0){
            Toast.makeText(mContext,"请输入账号",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.isEmpty()||password.length()<0){
            Toast.makeText(mContext,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }
        AC.accountMgr().login(phone, password, new PayloadCallback<ACUserInfo>() {
            @Override
            public void success(ACUserInfo acUserInfo) {
                finish();
            }

            @Override
            public void error(ACException e) {
                Toast.makeText(mContext,"检查账户和密码",Toast.LENGTH_LONG).show();
            }
        });
    }
}
