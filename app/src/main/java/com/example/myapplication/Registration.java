package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registration extends AppCompatActivity {
    // обьявляем переменную для Binding
   private ActivityRegistrationBinding b;
    // Обявляем переменую для Firebase для связи с базой по аунтефикации
    private FirebaseAuth mAuth;

    // Обьявляе переменную для Firestore для записи данных в базу
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Обьявляем переменную для коллекции
    private CollectionReference base;

    // метод создания экрана
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        init();

    }

    // метод для инициализации
    private void init(){
        // подключаем метод для регистрации
        registration();
    }

    // метод регистрации
    private void registration(){
        // инициализируем переменную Firebase для связи с базой по аунтефикации
        mAuth = FirebaseAuth.getInstance();

        // подключаем слушатель на кнопку Регистрации
        b.buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // при срабатывания нажатия на кнопку, если поля логин и пароль не пустые/заполненны
                if (!TextUtils.isEmpty(b.edLogin.getText().toString()) && !TextUtils.isEmpty(b.edPassword.getText().toString())) {
                    // то записать данные по аунтефикации в базу
                    mAuth.createUserWithEmailAndPassword(b.edLogin.getText().toString(), b.edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // если задача по записи прошла успешно
                            if (task.isSuccessful()) {

                                // инициализиуем коллекцию для базы данных Firestore, имя коллекции присваиваем из логина пользователя
                                base = db.collection(b.edLogin.getText().toString() + "");

                                // записываем логин пароль и права доступа в переменные
                                String login = b.edLogin.getText().toString();
                                String password = b.edPassword.getText().toString();
                                String access = b.spinner.getSelectedItem().toString();

                                // вызываем конструктор
                                User user = new User(
                                        login, password,
                                        access);
                                // передаем на запись переменные с данными
                                base.add(user);

                                // сохраняем в настройки значения логина пользователя
                                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("login", b.edLogin.getText().toString());
                                editor.apply();

                                // переходим на экран заявок
                                Intent intent = new Intent(Registration.this, Order.class);
                                startActivity(intent);


                            } else {
                                // при неудачной аунтефикации показываем соообщение
                                Toast.makeText(Registration.this, "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // если поля не заполнены то выводим сообщение
                } else if (b.edLogin.getText().toString().equals("") || b.edPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}