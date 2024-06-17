package com.exam.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.services.UserClientService;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {
    private EditText edtPhone;
    private EditText edtNewPass;
    private EditText edtReNewPass;
    private TextView tvLogin;
    private Button btnGetPass;
    private ImageView imgViewShowPassword1;
    private ImageView imgViewShowPassword2;
    private ImageView imgViewCancelShowPassword1;
    private ImageView imgViewCancelShowPassword2;
    private TextView tvPhone;
    private TextView tvNewPass;
    private TextView tvReNewPass;
    private ProgressBar loadingForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        onClickChangePassword();

        onClickChangeToLogin();
        onClickCancelShowPassword1();
        onClickCancelShowPassword2();
        onClickShowPassword1();
        onClickShowPassword2();
    }

    private void onClickChangePassword() {
        btnGetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoading(true);
                String phone = edtPhone.getText().toString().trim();
                String newPass = edtNewPass.getText().toString().trim();
                String reNewPass = edtReNewPass.getText().toString().trim();

                if (!validForgotPass(phone, newPass, reNewPass)) {
                    isLoading(false);
                    return;
                }

                changePassword(phone, newPass, reNewPass);
            }
        });
    }

    private void changePassword(String phone, String newPass, String reNewPass) {
        UserClientService.getInstance(ForgotPassword.this).changePassword(phone, newPass, reNewPass, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);

                isLoading(false);
            }

            @Override
            public void onError(VolleyError error) {
                isLoading(false);
            }
        });
    }

    private boolean validForgotPass(String phone, String newPass, String reNewPass) {
        Log.d("validForgotPass", "validForgotPass: " + newPass + " " + reNewPass);
        Log.d("validForgotPass", "validForgotPass: " + (newPass != reNewPass));
        if (phone.isEmpty()) {
            tvPhone.setText("Nhập số điện thoại");
            return false;
        } else if (phone.length() != 10) {
            tvPhone.setText("Số điện thoại 10 chữ số");
            return false;
        } else {
            tvPhone.setText("");
        }

        if (newPass.isEmpty()) {
            tvNewPass.setText("Nhập mật khẩu mới");
            return false;
        } else if (!newPass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            tvNewPass.setText("Mật khẩu phải lớn hơn 8 ký tự, bao gồm số, chữ thường, chữ hoa và ký tự đặc biệt");
            return false;
        } else {
            tvNewPass.setText("");
        }

        if (reNewPass.isEmpty()) {
            tvReNewPass.setText("Nhập lại mật khẩu mới");
            return false;
        } else if (!reNewPass.equals(newPass)) {
            tvReNewPass.setText("Không khớp mật khẩu mới");
            return false;
        } else {
            tvReNewPass.setText("");
        }

        return true;
    }

    private void onClickChangeToLogin() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void onClickShowPassword1() {
        imgViewShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewShowPassword1.setVisibility(View.INVISIBLE);
                imgViewCancelShowPassword1.setVisibility(View.VISIBLE);
                edtNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtNewPass.setSelection(edtNewPass.getText().length());
            }
        });
    }

    private void onClickCancelShowPassword1() {
        imgViewCancelShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewCancelShowPassword1.setVisibility(View.INVISIBLE);
                imgViewShowPassword1.setVisibility(View.VISIBLE);
                edtNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtNewPass.setSelection(edtNewPass.getText().length());
            }
        });
    }

    private void onClickShowPassword2() {
        imgViewShowPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewShowPassword2.setVisibility(View.INVISIBLE);
                imgViewCancelShowPassword2.setVisibility(View.VISIBLE);
                edtReNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtReNewPass.setSelection(edtReNewPass.getText().length());
            }
        });
    }

    private void onClickCancelShowPassword2() {
        imgViewCancelShowPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewCancelShowPassword2.setVisibility(View.INVISIBLE);
                imgViewShowPassword2.setVisibility(View.VISIBLE);
                edtReNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtReNewPass.setSelection(edtReNewPass.getText().length());
            }
        });
    }

    private void isLoading(boolean loading) {
        if (loading) {
            loadingForgotPassword.setVisibility(View.VISIBLE);
            btnGetPass.setVisibility(View.INVISIBLE);
        } else {
            loadingForgotPassword.setVisibility(View.INVISIBLE);
            btnGetPass.setVisibility(View.VISIBLE);
        }
    }

    private void mapping() {
        edtPhone = findViewById(R.id.editTextPhoneRegister);
        edtNewPass = findViewById(R.id.editTextPasswordRegister);
        edtReNewPass = findViewById(R.id.editTextRepassRegister);
        tvLogin = findViewById(R.id.textViewLoginForgot);
        btnGetPass = findViewById(R.id.buttonRegister);
        imgViewShowPassword1 = findViewById(R.id.imageViewShowPasswordForgotPassword1);
        imgViewCancelShowPassword1 = findViewById(R.id.imageViewCancelShowPasswordForgotPassword1);

        imgViewShowPassword2 = findViewById(R.id.imageViewShowPasswordForgotPassword2);
        imgViewCancelShowPassword2 = findViewById(R.id.imageViewCancelShowPasswordForgotPassword2);
        tvPhone = findViewById(R.id.textViewPhoneForgot);
        tvNewPass = findViewById(R.id.textViewNewPassForgot);
        tvReNewPass = findViewById(R.id.textViewReNewPassForgot);
        loadingForgotPassword = findViewById(R.id.progressBarForgotPassword);
    }
}