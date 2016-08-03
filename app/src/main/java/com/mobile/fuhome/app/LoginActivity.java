package com.mobile.fuhome.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.fuhome.app.application.BaseActivity;
import com.mobile.fuhome.app.application.Constants;
import com.mobile.fuhome.app.utils.HttpUtils;
import com.mobile.fuhome.app.utils.MD5;
import com.mobile.fuhome.app.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        TextView signUp = (TextView) findViewById(R.id.sign_up_tv);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(RegistererActivity.class);
//                Toast.makeText(LoginActivity.this, "暂时未开放改功能", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin() {
        //记录下账号和密码
        String str_name = mEmailView.getText().toString(); //这是要传输的数据
        String str_psws = mPasswordView.getText().toString();
        str_psws = MD5.GetMD5Code(str_psws);
        SharedPreferenceUtils.setStringData(this, "username", str_name);
        SharedPreferenceUtils.setStringData(this, "psw", str_psws);

        if (!str_name.equals("") && !str_psws.equals("")) {
            requestInternet(str_name, str_psws);
        } else {
            Toast.makeText(LoginActivity.this, "用户和密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestInternet(String str_name, String str_psws) {
        showProgress(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("openid", Constants.STR_OPENID);
        params.put("username", str_name);
        params.put("psw", str_psws);
        params.put("f", "2");
        HttpUtils.requestPost(Constants.HOST, params, new HttpUtils.ResultCallback() {

            @Override
            public void onResponse(Call call, String response) {
                try {
                    showProgress(false);
                    JSONObject obj = new JSONObject(response);
                    String log = obj.getString("log");
                    if (log.equals("ok")) {
                        String userid = obj.getString("userid");
                        SharedPreferenceUtils.setBooleanData(LoginActivity.this, "isLogined", true);
                        SharedPreferenceUtils.setStringData(LoginActivity.this, "userid", userid);
                        jumpToActivity(SblistActivity.class);
                        LoginActivity.this.finish();
                    } else {
                        SharedPreferenceUtils.setBooleanData(LoginActivity.this, "isLogined", false);
                        SharedPreferenceUtils.setStringData(LoginActivity.this, "userid", "");
                        SharedPreferenceUtils.setStringData(LoginActivity.this, "username", "");
                        SharedPreferenceUtils.setStringData(LoginActivity.this, "psw", "");
                        String res = "账号或者密码错误";
                        Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                showProgress(false);
                SharedPreferenceUtils.setBooleanData(LoginActivity.this, "isLogined", false);
                SharedPreferenceUtils.setStringData(LoginActivity.this, "userid", "");
                SharedPreferenceUtils.setStringData(LoginActivity.this, "username", "");
                SharedPreferenceUtils.setStringData(LoginActivity.this, "psw", "");
                String res = "登录出错,请检查网络~";
                Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent i= new Intent(Intent.ACTION_MAIN);  //主启动，不期望接收数据

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       //新的activity栈中开启，或者已经存在就调到栈前

            i.addCategory(Intent.CATEGORY_HOME);            //添加种类，为设备首次启动显示的页面

            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}

