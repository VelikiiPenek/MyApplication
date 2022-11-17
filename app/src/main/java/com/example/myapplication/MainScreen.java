package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainScreen extends AppCompatActivity {
    // создаем переменную для Binding
    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // инициализируем Binding
        b = ActivityMainBinding.inflate(getLayoutInflater());
        View v = b.getRoot();

        // собираем представление xml файла
        setContentView(v);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // подключаем слушатель на кнопку регистрации
        b.registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // при срабатывании нажатия происходит переход на экран Регистрации
                Intent intent=new Intent(MainScreen.this,Registration.class);
                startActivity(intent);
            }
        });

        // подключаем слушатель на кнопку Авторизации
        b.authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // при срабатывании нажатия происходит переход на экран Авторизации
                Intent intent=new Intent(MainScreen.this,Login.class);
                startActivity(intent);
            }
        });
    }
}