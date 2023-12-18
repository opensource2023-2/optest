package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_page extends AppCompatActivity
{

    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증 처리
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtUsername;      //회원가입 입력필드
    private Button mBtnRegister;            //회원가입 입력버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");

        mEtEmail = findViewById(R.id.login_email);
        mEtPwd = findViewById(R.id.login_pwd);
        mEtUsername = findViewById(R.id.login_username);
        mBtnRegister = findViewById(R.id.login_signup);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strUsername = mEtUsername.getText().toString();

                //FirebaseAuth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Signup_page.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail()); //로그인은 firebaseuser 라는 객체로부터 가져옴
                            account.setPassword(strPwd);
                            account.setUsername(strUsername);
                            ((GlobalVars) getApplicationContext()).setUserID(strUsername);//사용자가 입력한거 그대로 가져옴


                            //setvalue : database 에 insert 삽입 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(Signup_page.this, "SignUp Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup_page.this, home_page.class); // NextActivity에는 넘어가고자 하는 다음 액티비티의 클래스명을 넣어야 합니다.
                            startActivity(intent);
                        }else {
                            Toast.makeText(Signup_page.this, "SignUp Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}