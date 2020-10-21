package com.uniso.lpdm.whatdoido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}