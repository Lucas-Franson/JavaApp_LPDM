package com.uniso.lpdm.whatdoido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//integrantes:
//Lucas Franson 98407
//Matheus Goes 98398
//Guilherme Vanini 98519
//Bianca Campos 98059
//Julio Tsutsui 98139
//Lane Noriuki 98429


public class MainTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
    }

    //Botão que ao ser clicado leva a tela de criação de uma nova task.
    public void onClickNovaTarefa(View view){
        Intent intent = new Intent(this, CriarTaskActivity.class);
        startActivity(intent);
    }

    public void onClickTarefa(View view){
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }
}