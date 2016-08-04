package com.mobile.fuhome.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobile.fuhome.app.application.BaseActivity;
import com.mobile.fuhome.app.application.Constants;
import com.mobile.fuhome.app.utils.HttpUtils;
import com.mobile.fuhome.app.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class RegistererActivity extends BaseActivity {

    private static final String TAG = "ryan";
    private EditText mPhone, mPassword, mPasswordConfirm;
    private View mProgressView;
    private View mRegistererFormView;
    private ProgressBar mPhoneCheckProgress;
    private ImageView mCorrectView, mRejectView;
    private String encode="";
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerer);

        mRegistererFormView = findViewById(R.id.registerer_form);
        mProgressView = findViewById(R.id.registerer_progress);
        mPhoneCheckProgress = (ProgressBar) findViewById(R.id.registerer_phone_progress);
        mCorrectView = (ImageView) findViewById(R.id.registerer_phone_correct);
        mRejectView = (ImageView) findViewById(R.id.registerer_phone_wrong);

        mPhone = (EditText) findViewById(R.id.registerer_phone);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    if (TextUtil.isMobileNO(s.toString())) {
                        checkPhnoe(s.toString());
                    } else {
                        Toast.makeText(RegistererActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    hideCheckPhoneIcon();
                }
            }
        });

        mPassword = (EditText) findViewById(R.id.password);
        mPasswordConfirm = (EditText) findViewById(R.id.password_confirm);

        mButton = (Button) findViewById(R.id.registerer_button);
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataUsable()){
                    register();
                }
            }
        });

    }

    private void register() {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.put("username", mPhone.getText().toString());
        map.put("encode",encode);
        map.put("f", "1");
        HttpUtils.addCommValue(this, map);
        HttpUtils.requestPost(Constants.HOST, map, new HttpUtils.ResultCallback() {
            @Override
            public void onResponse(Call call, String response) {
                try {
                    showProgress(false);
                    JSONObject object = new JSONObject(response);
                    String log = object.getString("log");
                    if (TextUtils.equals(log,"ok")){
                        Toast.makeText(RegistererActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegistererActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                showProgress(false);
                Toast.makeText(RegistererActivity.this, "请求失败,请检查网络~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isDataUsable() {
        boolean usable = true;
        if(!TextUtil.isMobileNO(mPhone.getText().toString())){
            Toast.makeText(RegistererActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            usable = false;
        }
        if (mPassword.getText().toString().trim().length()<6){
            Toast.makeText(RegistererActivity.this, "密码必须大于6位", Toast.LENGTH_SHORT).show();
            usable = false;
        }
        if (!TextUtils.equals(mPassword.getText().toString().trim(),mPasswordConfirm.getText().toString().trim())){
            Toast.makeText(RegistererActivity.this, "两次输入密码不一样", Toast.LENGTH_SHORT).show();
            usable = false;
        }
        return usable;
    }

    private void checkPhnoe(String phone) {
        //TODO checking the phone that is usable?
        showCheckPhoneIcon();
        Map<String, String> map = new HashMap<>();
        map.put("phonenum", phone);
        map.put("f", "0");
        HttpUtils.addCommValue(this, map);
        HttpUtils.requestPost(Constants.HOST, map, new HttpUtils.ResultCallback() {
            @Override
            public void onResponse(Call call, String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String log = object.getString("log");
                    if (TextUtils.equals(log,"ok")){
                        showCheckPhoneIcon(true);
                        encode = object.getString("encode");
                        mButton.setEnabled(true);
                    }else {
                        showCheckPhoneIcon(false);
                        Toast.makeText(RegistererActivity.this, "该电话号码已经被注册~", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                hideCheckPhoneIcon();
                Toast.makeText(RegistererActivity.this, "请求失败,请检查网络~", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showCheckPhoneIcon() {
        mPhoneCheckProgress.setVisibility(View.VISIBLE);
        mRejectView.setVisibility(View.GONE);
        mCorrectView.setVisibility(View.GONE);
    }

    private void hideCheckPhoneIcon(){
        encode = "";
        mButton.setEnabled(false);
        mPhoneCheckProgress.setVisibility(View.GONE);
        mRejectView.setVisibility(View.GONE);
        mCorrectView.setVisibility(View.GONE);
    }

    private void showCheckPhoneIcon(boolean isCorrect) {
        mPhoneCheckProgress.setVisibility(View.GONE);
        if (isCorrect) {
            mCorrectView.setVisibility(View.VISIBLE);
            mRejectView.setVisibility(View.GONE);
        } else {
            mRejectView.setVisibility(View.VISIBLE);
            mCorrectView.setVisibility(View.GONE);
        }
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

            mRegistererFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistererFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistererFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegistererFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
