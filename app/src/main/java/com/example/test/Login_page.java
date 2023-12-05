package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_page extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증 처리
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;      //로그인 입력필드
    Button login_signup;
    Button login_success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");

        mEtEmail = findViewById(R.id.login_email); //R은 res 파일 . 은 하위 폴더 id 는 찾아줘의 의미
        mEtPwd = findViewById(R.id.login_pwd);

        login_success = findViewById(R.id.login_success);
        login_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Login_page.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //로그인 성공
                            Intent intent = new Intent(Login_page.this, Hello_page.class);
                            startActivity(intent);
                            finish(); //현재 엑티비티 파괴

                        }else {
                            Toast.makeText(Login_page.this,"Login Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        login_signup = findViewById(R.id.login_signup);

        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 화면 이동
                Intent intent= new Intent(getApplicationContext(), Signup_page.class);
                startActivity(intent);

            }
        });

    }
}