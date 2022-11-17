package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityOrderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity implements RecyclerAdapterInterface {
    // обьявляем переменную для Binding
    private ActivityOrderBinding b;

    // обьявляем и инициализируем переменную для FirebaseFirestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // обьявляем коллекцию
    private CollectionReference base;

    // обьявляем переменные для RecyclerView и RecyclerAdapter
    // необходимы для выгрузки заданий в список и перезагрузке при изменений
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    // создаем листы для данных , Название и Описание задания , айди
    List<String> orderNameList;
    List<String> orderList;
    List<String> idList;


    // создаем представление
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityOrderBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);

        // принудительно отключаем темную тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // подключаем метод подгрузки прав пользователя ( Заказчик или Исполнитель)
        getId();

        // инициализируем списки
        orderNameList = new ArrayList<>();
        orderList = new ArrayList<>();
        idList = new ArrayList<>();

        // инициализируем recyclerView
        recyclerView = findViewById(R.id.recycler);
        // заполняем адаптеп из списков
        recyclerAdapter = new RecyclerAdapter(orderNameList, orderList, idList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // заполняем данные из адаптера в ресайкл
        recyclerView.setAdapter(recyclerAdapter);

        // подключаем метод для подгрузки данных из базы FireStore
        getBase();

        // подключаем слушатели
        initClicker();

    }

    private void initClicker() {
        // подключаем слушатель на кнопку создать задание
        b.textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // при срабатывании нажатия переходим на экран создания заявки
                Intent intent = new Intent(Order.this, NewOrder.class);
                startActivity(intent);
            }
        });
    }

    // метод подгрузки данных из базы
    private void getBase() {
        // инициализируем переменную для базы с именем request
        // в него будут записываться все задания
        base = db.collection("request");

        // подключаемся к базе
        base.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // при успешном подключении
                // проверяем списки на заполненость , если они не пустые то очищаем списки
                if (orderNameList.size() > 0) orderNameList.clear();
                if (orderList.size() > 0) orderList.clear();
                if (idList.size() > 0) idList.clear();

                // создаем переменные для записи данных
                String orderNameText = "";
                String orderText = "";

                // загружаем данные из базы QueryDocumentSnapshot
                // выгрыжаем данные через цикл
                for (QueryDocumentSnapshot document : task.getResult()) {

                    // заисываем данные в переменныю по тегам
                    orderNameText = document.getString("nomination");
                    orderText = document.getString("description");

                    // заполняем списки переменными
                    orderNameList.add(orderNameText);
                    orderList.add(orderText);
                    // записываем айди в список
                    idList.add(document.getId());
                }
                // так как данные обновились то перезагружаем адаптер
                recyclerAdapter.notifyDataSetChanged();
            }
        });

    }

    //метод подгрузки прав пользователя ( Заказчик или Исполнитель)
    private void getId() {
        // запрашиваем сохраненые настройки по ключу login
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");

        // инициализируем имя коолекции базы данных по логину полученному из настроек
        base = db.collection(login);

        // соединяемся с базой
        base.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // при успешном соединении
                // создаем переменную для данных
                String data = "";

                // загружаем данные в QueryDocumentSnapshot
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    // подлючаем класс с конструктором и через getter выгружаем данные в переменую
                    // в итоге получаем права пользователя, Исполнитель или Заказчик

                    User user = documentSnapshot.toObject(User.class);
                    String access = user.getAccess();
                    data = access;
                }

                // проверяем права
                // если Заказчик,
                if (data.equals("Заказчик")) {
                    // то показываем кнопку Создать задание
                    b.textView5.setVisibility(View.VISIBLE);
                    b.plus.setVisibility(View.VISIBLE);
                } else if (data.equals("Исполнитель")) {
                    // если Исполнитель то прячем кнопку
                    b.textView5.setVisibility(View.GONE);
                    b.plus.setVisibility(View.GONE);
                }
            }
        });
    }

    // подключаем слушатель на нажатие с ресайкл вью
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Order.this, Respond.class);
        intent.putExtra("id", idList.get(position));
        startActivity(intent);
    }
}