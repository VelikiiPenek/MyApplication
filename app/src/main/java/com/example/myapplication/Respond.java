package com.example.myapplication;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityOrderBinding;
import com.example.myapplication.databinding.ActivityRespondBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Respond extends AppCompatActivity {
    // обьявляем переменную для Binding
    private ActivityRespondBinding b;

    // обьявляем переменную для FirebaseFirestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // обьявляем переменную для коллекции FirebaseFirestore
    private CollectionReference base;

    // обьявляем переменую для записи айди
    private String id = "";

    // метод создания экрана , главный метод
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRespondBinding.inflate(getLayoutInflater());
        View v = b.getRoot();

        setContentView(v);

        // принудительно отключаем темную тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // подключаем метод инициализации
        init();
    }

    private void init() {
        // получение айди задания с экрана заявок
        getId();
        // получение прав полтзователя из настроек а затем из базы данных по айди
        getIdrights();
        // инициализация слушателей
        initClicker();
    }

    // слушатели
    private void initClicker() {
        // подключаем слушатель на кнопку Откликнуться
        b.textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // если нажатие сработало
                // то показываем телефон и имя
                // и убираем кнопку
                b.textView11.setVisibility(View.VISIBLE);
                b.textView10.setVisibility(View.VISIBLE);
                b.imageButton4.setVisibility(GONE);
                b.textView9.setVisibility(GONE);
            }
        });


    }

    // получение прав полтзователя из настроек а затем из базы данных по айди
    private void getIdrights() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");

        base = db.collection(login);

        base.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String data = "";


                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);

                    String access = user.getAccess();
                    data = access;
                }

                if (data.equals("Заказчик")) {
                    b.imageButton4.setVisibility(GONE);
                    b.textView9.setVisibility(GONE);
                    b.textView11.setVisibility(View.VISIBLE);
                    b.textView10.setVisibility(View.VISIBLE);
                } else if (data.equals("Исполнитель")) {
                    b.imageButton4.setVisibility(View.VISIBLE);
                    b.textView9.setVisibility(View.VISIBLE);
                    b.textView11.setVisibility(GONE);
                    b.textView10.setVisibility(GONE);
                }
            }
        });
    }

    // проверка получаемых аргументов, если аргументы существуют
    private void getId() {
        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            id = arguments.get("id").toString();
            getOrder();
        }
    }

    private void getOrder() {
        base = db.collection("request");

        base.document(id + "").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            String name = document.getString("name");
                            String telephone = document.getString("telephone");
                            String nomination = document.getString("nomination");
                            String description = document.getString("description");
                            String image = document.getString("image");

                            b.textView6.setText(nomination);
                            b.description.setText(description);
                            b.textView10.setText(name);
                            b.textView11.setText(telephone);

                            if (!image.equals("")) {
                                Picasso.get().load(image).into(b.imageView8, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        b.progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                            } else {
                                b.progressBar.setVisibility(GONE);
                            }
                        }
                    }
                });
    }
}