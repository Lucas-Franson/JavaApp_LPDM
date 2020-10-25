package com.uniso.lpdm.whatdoido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CriarTaskActivity extends AppCompatActivity {

    //Tela de Criação de uma Task
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_task);
    }
    public void onClickAdicionarTarefa(View view){
        Intent intent = new Intent(this, MainTaskActivity.class);
        startActivity(intent);

        Context context = getApplicationContext();
        CharSequence text = "Tarefa Criada!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}