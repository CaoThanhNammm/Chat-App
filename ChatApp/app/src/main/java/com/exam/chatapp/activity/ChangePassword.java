package com.exam.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.UserClientService;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {
    private EditText edtPhoneCP;
    private EditText edtNewPassCP;
    private EditText edtNewRepassCP;
    private Button btnCP;
    private ImageView imgViewShowPassword1;
    private ImageView imgViewCancelShowPassword1;
    private ImageView imgViewShowPassword2;
    private ImageView imgViewCancelShowPassword2;
    private TextView textViewLoginCP;
    private ProgressBar loadingChangePassword;
    private TextView tvWarningPhone;
    private TextView tvWarningNewPass;
    private TextView tvWarningReNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        onClickChangePassword();

        onClickShowPassword1();
        onClickCancelShowPassword1();
        onClickShowPassword2();
        onClickCancelShowPassword2();
        onClickChangeToLogin();
    }

    private void onClickCancelShowPassword2() {
        imgViewCancelShowPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewCancelShowPassword2.setVisibility(View.INVISIBLE);
                imgViewShowPassword2.setVisibility(View.VISIBLE);
                edtNewRepassCP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtNewRepassCP.setSelection(edtNewRepassCP.getText().length());
            }
        });
    }

    private void onClickShowPassword2() {
        imgViewShowPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewShowPassword2.setVisibility(View.INVISIBLE);
                imgViewCancelShowPassword2.setVisibility(View.VISIBLE);
                edtNewRepassCP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtNewRepassCP.setSelection(edtNewRepassCP.getText().length());
            }
        });
    }

    private void onClickCancelShowPassword1() {
        imgViewCancelShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewCancelShowPassword1.setVisibility(View.INVISIBLE);
                imgViewShowPassword1.setVisibility(View.VISIBLE);
                edtNewPassCP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtNewPassCP.setSelection(edtNewPassCP.getText().length());
            }
        });
    }

    private void onClickShowPassword1() {
        imgViewShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgViewShowPassword1.setVisibility(View.INVISIBLE);
                imgViewCancelShowPassword1.setVisibility(View.VISIBLE);
                edtNewPassCP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtNewPassCP.setSelection(edtNewPassCP.getText().length());
            }
        });
    }

    private void onClickChangePassword() {
        btnCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoading(true);
                String phone = edtPhoneCP.getText().toString().trim();
                String newPass = edtNewPassCP.getText().toString().trim();
                String reNewPass = edtNewRepassCP.getText().toString().trim();
                if (!validChangePassword(phone, newPass, reNewPass)) {
                    isLoading(false);
                    return;
                }

                changePassword(phone, newPass, reNewPass);
            }
        });
    }

    private void changePassword(String phone, String newPass, String reNewPass) {
        UserClientService.getInstance(ChangePassword.this).changePassword(phone, newPass, reNewPass, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Toast.makeText(ChangePassword.this, "Cập nhập mật khẩu thành công", Toast.LENGTH_SHORT).show();

                SessionManager sessionManager = new SessionManager(ChangePassword.this);
                sessionManager.removeSession();
                Intent intent = new Intent(ChangePassword.this, Login.class);
                startActivity(intent);

                isLoading(false);
            }

            @Override
            public void onError(VolleyError error) {
                isLoading(false);
            }
        });
    }

    private boolean validChangePassword(String phone, String newPass, String reNewPass) {
        if (phone.isEmpty()) {
            tvWarningPhone.setText("Nhập số điện thoại");
            return false;
        } else if (phone.length() != 10) {
            tvWarningPhone.setText("Số điện thoại 10 chữ số");
            return false;
        } else {
            tvWarningPhone.setText("");
        }

        if (newPass.isEmpty()) {
            tvWarningNewPass.setText("Nhập mật khẩu mới");
            return false;
        } else {
            tvWarningNewPass.setText("");
        }

        if (reNewPass.isEmpty()) {
            tvWarningReNewPass.setText("Nhập lại mật khẩu mới");
            return false;
        } else if (!newPass.equals(reNewPass)) {
            tvWarningReNewPass.setText("Không khớp mật khẩu mới");
            return false;
        } else {
            tvWarningReNewPass.setText("");
        }

        return true;
    }

    private void onClickChangeToLogin() {
        textViewLoginCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void isLoading(boolean loading) {
        if (loading) {
            loadingChangePassword.setVisibility(View.VISIBLE);
            btnCP.setVisibility(View.INVISIBLE);
        } else {
            btnCP.setVisibility(View.VISIBLE);
            loadingChangePassword.setVisibility(View.INVISIBLE);
        }
    }

    private void mapping() {
        edtPhoneCP = findViewById(R.id.editTextPhoneCP);
        edtNewPassCP = findViewById(R.id.editTextPasswordCP);
        edtNewRepassCP = findViewById(R.id.editTextRepassCP);
        btnCP = findViewById(R.id.buttonCP);
        imgViewShowPassword1 = findViewById(R.id.imageViewShowPasswordChangePassword1);
        imgViewCancelShowPassword1 = findViewById(R.id.imageViewCancelShowPasswordChangePassword1);
        imgViewShowPassword2 = findViewById(R.id.imageViewShowPasswordChangePassword2);
        imgViewCancelShowPassword2 = findViewById(R.id.imageViewCancelShowPasswordChangePassword2);
        textViewLoginCP = findViewById(R.id.textViewLoginCP);
        loadingChangePassword = findViewById(R.id.progressBarChangePassword);

        tvWarningPhone = findViewById(R.id.textViewWarningPhoneCP);
        tvWarningNewPass = findViewById(R.id.textViewNewPassCP);
        tvWarningReNewPass = findViewById(R.id.textViewReNewPassCP);
    }
}