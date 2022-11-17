package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    // обьявляем переменную для Binding
    private ActivityLoginBinding b;
    // обьявляем переменную для базы Firebase для проверки авторизации
    private FirebaseAuth mAuth;

    // метод создания экрана
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        View v = b.getRoot();

        setContentView(v);

        // подключаем метод инициализации
        init();
    }

    //  метод инициализации
    private void init(){
        // инициализируем переменную для аунтификации
        mAuth = FirebaseAuth.getInstance();

        // подключаем слушатель на кнопку Авторизации
        b.authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // если поля логин и пароль не пустые / заполнены
                if(!b.edLogin.getText().toString().equals("")&&!b.edPassword.getText().toString().equals("")){
                    if (!TextUtils.isEmpty(b.edLogin.getText().toString()) && !TextUtils.isEmpty(b.edPassword.getText().toString())) {
                        // то тогда проверить совпадения логина и пороля в базе
                        mAuth.signInWithEmailAndPassword(b.edLogin.getText().toString(), b.edPassword.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // если задача успешна
                                if (task.isSuccessful()) {

                                    // сохраняем в настройки логин/почта пользователя
                                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("login", b.edLogin.getText().toString());
                                    editor.apply();

                                    // переходим на экран отображения списка Заявок
                                    Intent intent = new Intent(Login.this, Order.class);
                                    startActivity(intent);


                                } else {
                                    // при не совпадении данных аутефикации выводим сообщение
                                    Toast.makeText(getApplicationContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                } else {
                    // если логин и пароль не заполнены просим ввести данные
                    Toast.makeText(Login.this, "Введите данные", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}