package com.exam.chatapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.model.firebase.FirebaseUser;
import com.exam.chatapp.services.UserClientService;
import com.exam.chatapp.services.firebase.UserFirebaseClientService;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private EditText edtPhone;
    private EditText edtPassword;
    private EditText edtFullname;
    private TextView tvLogin;
    private EditText edtDob;
    private Calendar calendar;
    private String encodeImage;
    private Button btnRegister;
    private ImageView imageViewAvatar;
    private TextView tvWarningPhone;
    private TextView tvWarningPassword;
    private TextView tvWarningFullname;
    private TextView tvWarningDob;
    private TextView tvAvatarRegister;
    private ProgressBar progressBar;
    private ImageView imgShowPassword;
    private ImageView imgCancelShowPassword;
    private FirebaseAuth auth;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        onClickSubmit();
        onClickChangeToLogin();
        createDatePicker();
        onClickChooseAvatar();
        onClickShowPassword();
        onClickCancelShowPassword();
    }

    private void onClickChooseAvatar() {
        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });
    }

    // firebase
    private void addUserInFirebase(User user) {
        FirebaseUser fbUser = new FirebaseUser();
        fbUser.setId(user.getId());
        fbUser.setAvaibility(0);
        fbUser.setFcm("");
        fbUser.setLogin(false);

        UserFirebaseClientService.getInstance(this).add(fbUser);
    }

    private void onClickSubmit() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();
                String password = edtPassword.getText().toString();
                String fullname = edtFullname.getText().toString();
                String dob = edtDob.getText().toString();

                if (!validRegister(phone, password, fullname, dob)) {
                    return;
                }

                if (encodeImage == null) {
                    Drawable avatarDefault = AppCompatResources.getDrawable(Register.this, R.drawable.avatar_default);
                    Bitmap bitmap = ((BitmapDrawable) avatarDefault).getBitmap();
                    encodeImage = bitMapToString(bitmap);
                }
                checkExistPhone(phone, password, fullname, dob);
            }
        });
    }

    public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.NO_WRAP);
        return temp;
    }

    private void addUser(User user) {
        UserClientService.getInstance(this).add(user, new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                isLoading(true);
                JSONObject userJson = result.getJSONObject("data");

                User user = new User();
                user.setId(userJson.getString("id"));
                user.setEncodedImage(encodeImage);
                user.setPhone(userJson.getString("phone"));
                user.setFullname(userJson.getString("fullname"));
                user.setPassword(userJson.getString("password"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dobFor = LocalDate.parse(userJson.getString("dob"), formatter);
                LocalDate createFor = LocalDate.parse(userJson.getString("createdAt"), formatter);

                user.setDob(dobFor);
                user.setCreatedAt(createFor);

                addUserInFirebase(user);

                Intent intent = new Intent(Register.this, ConfirmAccount.class);
                startActivity(intent);
            }

            @Override
            public void onError(VolleyError error) {
                isLoading(false);
            }
        });
    }

    private void sendOtp(User user) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(user.getPhone())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                addUser(user);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Register.this, "otp failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Toast.makeText(Register.this, "otp success", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void isLoading(boolean loading) {
        if (loading) {
            btnRegister.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
        }
    }

    private void checkExistPhone(String phone, String password, String fullname, String dob) {
        UserClientService.getInstance(this).isExistPhone(phone, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                String notify = result.getString("data");
                tvWarningPhone.setText(notify);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onError(VolleyError error) {
                User user = new User();
                user.setPhone(phone);
                user.setPassword(password);
                user.setFullname(fullname);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dobFor = LocalDate.parse(dob, formatter);

                user.setDob(dobFor);
                user.setCreatedAt(LocalDate.now());

                user.setEncodedImage(encodeImage);

                // gui ma otp
                addUser(user);
                sendOtp(user);
            }
        });
    }

    public boolean validRegister(String phone, String password, String fullname, String dob) {
        if (phone.isEmpty()) {
            tvWarningPhone.setText("Nhập số điện thoại");
            return false;
        } else if (phone.length() != 10) {
            tvWarningPhone.setText("Số điện thoại 10 chữ số");
            return false;
        } else {
            tvWarningPhone.setText("");
        }

        if (password.isEmpty()) {
            tvWarningPassword.setText("Nhập mật khẩu");
            return false;
        } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            tvWarningPassword.setText("Mật khẩu phải lớn hơn 8 ký tự, bao gồm số, chữ thường, chữ hoa và ký tự đặc biệt");
            return false;
        } else {
            tvWarningPassword.setText("");
        }

        if (fullname.isEmpty()) {
            tvWarningFullname.setText("Nhập họ và tên");
            return false;
        } else {
            tvWarningFullname.setText("");
        }

        if (dob.isEmpty()) {
            tvWarningDob.setText("Chọn ngày sinh");
            return false;
        } else {
            tvWarningDob.setText("");
        }

        return true;
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                encodeImage = encodeImage(bitmap);
                                imageViewAvatar.setImageBitmap(bitmap);
                                tvAvatarRegister.setVisibility(View.GONE);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }
            }
    );

    private void onClickChangeToLogin() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void createDatePicker() {
        edtDob.setOnClickListener(view -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(Register.this, dateSetListener, year, month, day).show();
        });
    }

    // Lắng nghe sự kiện khi người dùng chọn ngày trong DatePickerDialog
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Cập nhật ngày trong Calendar
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Định dạng và cập nhật ngày trong EditText
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            edtDob.setText(dateFormat.format(calendar.getTime()));
        }
    };

    private void onClickShowPassword() {
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgShowPassword.setVisibility(View.INVISIBLE);
                imgCancelShowPassword.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });
    }

    private void onClickCancelShowPassword() {
        imgCancelShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCancelShowPassword.setVisibility(View.INVISIBLE);
                imgShowPassword.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });
    }

    private void mapping() {
        edtPhone = findViewById(R.id.editTextPhoneRegister);
        edtPassword = findViewById(R.id.editTextPasswordRegister);
        edtFullname = findViewById(R.id.editTextRepassRegister);
        edtDob = findViewById(R.id.editTextDobRegister);
        tvLogin = findViewById(R.id.textViewLogin);
        btnRegister = findViewById(R.id.buttonRegister);
        calendar = Calendar.getInstance();
        imageViewAvatar = findViewById(R.id.imageViewLogoRegister);
        tvWarningPhone = findViewById(R.id.textViewWarningPhoneRegister);
        tvWarningPassword = findViewById(R.id.textViewWarningPasswordRegister);
        tvWarningFullname = findViewById(R.id.textViewWarningFullnameRegister);
        tvWarningDob = findViewById(R.id.textViewWarningDobRegster);
        tvAvatarRegister = findViewById(R.id.textViewAvatarRegister);
        progressBar = findViewById(R.id.progressBarRegister);
        imgShowPassword = findViewById(R.id.imageViewShowPasswordRegister);
        imgCancelShowPassword = findViewById(R.id.imageViewCancelShowPasswordRegister);
        auth = FirebaseAuth.getInstance();
    }
}