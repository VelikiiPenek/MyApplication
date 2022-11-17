package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityNewOrderBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class NewOrder extends AppCompatActivity {
    private ActivityNewOrderBinding b;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference base;

    private StorageReference storageRef; // обьявляем базу для картинок

    private Uri upLoadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityNewOrderBinding.inflate(getLayoutInflater());
        View v = b.getRoot();

        setContentView(v);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        init();
    }


    private void init(){

        base = db.collection("request");

        storageRef = FirebaseStorage.getInstance().getReference("imageDB");

        initClicker();
    }

    private void initClicker() {
        b.newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        b.imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!b.editText3.getText().toString().equals("")&&!b.editText4.getText().toString().equals("")&&!b.editText5.getText().toString().equals("")&&!b.editText6.getText().toString().equals("")){

                    String name = b.editText3.getText().toString();
                    String telephone = b.editText4.getText().toString();
                    String image = upLoadUri.toString();
                    String nomination = b.editText5.getText().toString();
                    String description = b.editText6.getText().toString();

                    Request request = new Request(
                            name, telephone, image, nomination, description);
                    base.add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent intent = new Intent(NewOrder.this, Order.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(NewOrder.this, "Введите данные", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                b.imageView7.setImageURI(data.getData());
                b.newImage.setText("Изображение загружено");
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) b.imageView7.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        final StorageReference mRef = storageRef.child(System.currentTimeMillis() + "my_image");
        UploadTask up = mRef.putBytes(byteArray);

        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                // записываем ссылку на картирку в переменную
                upLoadUri = task.getResult();
            }
        });

    }

}