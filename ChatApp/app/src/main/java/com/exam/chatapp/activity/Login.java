package com.exam.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.AuthenticationPhone;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.UserClientService;
import com.exam.chatapp.services.firebase.UserFirebaseClientService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    private EditText edtPhone;
    private EditText edtPassword;
    private TextView tvForgotPassword;
    private TextView tvRegister;
    private Button btnSubmit;
    private ProgressBar progressBarLoggin;
    private SessionManager sessionManager;
    private TextView tvWarningPhone;
    private TextView tvWarningPassword;
    private ImageView imgViewShowPasword;
    private ImageView imgViewCancelShowPasword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mapping();
        goToHome();

        onClickLogin();
        onClickChangeToForgotPassword();
        onClickChangeToRegister();
        onClickShowPassword();
        onClickCancelShowPassword();
    }

    private void goToHome() {
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(Login.this, BottomNavigation.class);
            startActivity(intent);
        }
    }

    private void onClickShowPassword() {
        imgViewShowPasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewShowPasword.setVisibility(View.INVISIBLE);
                imgViewCancelShowPasword.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });
    }

    private void onClickCancelShowPassword() {
        imgViewCancelShowPasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewCancelShowPasword.setVisibility(View.INVISIBLE);
                imgViewShowPasword.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });
    }

    private boolean validLogin(String phone, String password) {
        if (phone.isEmpty()) {
            tvWarningPhone.setText("Nhập số điện thoại");
            return false;
        } else {
            tvWarningPhone.setText("");
        }

        if (password.isEmpty()) {
            tvWarningPassword.setText("Nhập mật khẩu");
            return false;
        } else {
            tvWarningPassword.setText("");
        }

        return true;
    }

    private void onClickLogin() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoading(true);
                String phone = edtPhone.getText().toString();
                String password = edtPassword.getText().toString();

                if (!validLogin(phone, password)) {
                    isLoading(false);
                    return;
                }
                validUser(phone, password);
            }
        });
    }

    private void validUser(String phone, String password) {
        UserClientService.getInstance(Login.this).checkUser(phone, password, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                try {
                    JSONObject userJson = result.getJSONObject("data");
                    if (userJson != null) {
                        User user = new User();
                        String id = userJson.getString("id");
                        user.setId(id);
                        user.setPhone(userJson.getString("phone"));
                        user.setEncodedImage(userJson.getString("avatar"));
                        user.setFullname(userJson.getString("fullname"));
                        isLoading(false);
                        checkOnlyDevice(user);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                isLoading(false);
                Toast.makeText(Login.this, "Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkOnlyDevice(User user) {
        UserFirebaseClientService.getInstance(Login.this).getUser(user.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject userJson = result.getJSONObject("data");
                boolean isLogin = userJson.getBoolean("login");
                if (!isLogin) {
                    sessionManager.saveUser(user);
                    //cap nhap firebase
                    updateFcm(user.getId());
                    updateIsLogin(user.getId());
                    updateAvaibility(user.getId());

                    Intent intent = new Intent(Login.this, BottomNavigation.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Hãy đăng xuất trên thiết bị khác", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
        isLoading(false);
    }

    private void updateAvaibility(String id) {
        UserFirebaseClientService.getInstance(this).updateAvaibility(id, 1);
    }

    private void updateIsLogin(String id) {
        UserFirebaseClientService.getInstance(Login.this).updateIsLogin(id, true);
    }

    private void updateFcm(String id) {
        UserFirebaseClientService.getInstance(this).updateFcm(id);
    }

    private void isLoading(boolean loading) {
        if (loading) {
            progressBarLoggin.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.INVISIBLE);
        } else {
            progressBarLoggin.setVisibility(View.INVISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void onClickChangeToRegister() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void onClickChangeToForgotPassword() {
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void mapping() {
        sessionManager = new SessionManager(this);
        edtPhone = findViewById(R.id.editTextPhoneLogin);
        edtPassword = findViewById(R.id.editTextPasswordLogin);
        tvForgotPassword = findViewById(R.id.textViewForgotPassword);
        tvRegister = findViewById(R.id.textViewRegister);
        btnSubmit = findViewById(R.id.buttonLogin);
        progressBarLoggin = findViewById(R.id.progressBarLogin);
        tvWarningPhone = findViewById(R.id.textViewWarningPhoneLogin);
        tvWarningPassword = findViewById(R.id.textViewWarningPasswordLogin);
        imgViewShowPasword = findViewById(R.id.imageViewShowPassword);
        imgViewCancelShowPasword = findViewById(R.id.imageViewCancelShowPassword);
    }
}