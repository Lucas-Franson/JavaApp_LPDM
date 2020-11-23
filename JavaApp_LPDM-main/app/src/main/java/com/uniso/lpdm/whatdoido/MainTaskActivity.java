package com.uniso.lpdm.whatdoido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

//integrantes:
//Lucas Franson 98407
//Matheus Goes 98398
//Guilherme Vanini 98519
//Bianca Campos 98059
//Julio Tsutsui 98139
//Lane Noriuki 98429

public class MainTaskActivity extends AppCompatActivity implements RecycleViewAdapter.ItemClickListener {

    //Lista que recebe todos os usuários
    private ArrayList<Task> task_list = new ArrayList<>();

    //Variável de criação da recycler view
    private static RecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);

        GerarRecyclerView();
    }

    //Toda vez que volta a tela principal, o contéudo da lista é atualizado
    @Override
    protected void onRestart() {
        super.onRestart();
        task_list.clear();
        mAdapter.notifyDataSetChanged();

        GerarRecyclerView();
    }

    //Botão que ao ser clicado leva a tela de criação de uma nova task.
    public void onClickNovaTarefa(View view){
        Intent intent = new Intent(this, ItemTaskActivity.class);
        startActivity(intent);
    }

    //Ao dar um click em uma tarefa na recycler view, essa função é disparada e leva o usuário a tela de exibição
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("TASK_CODE_EXTRA",task_list.get(position).Codigo);
        startActivity(intent);
    }

    //Pega todos os dados do banco e armazena em uma array list
    private void storeDataInTaskList(){
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.GetAllTasks();
        if (cursor.getCount() == 0){
            Toast.makeText(this,"Não existe tarefas criadas.", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                boolean completed = cursor.getInt(6) > 0;
                task_list.add(new Task(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3),cursor.getString(4),"D,S",completed));
            }
        }
    }

    private void GerarRecyclerView(){
        //TextView da data de hoje
        TextView data = findViewById(R.id.data);
        //Cria um objeto de formatação de data
        DateFormat formatacao = new SimpleDateFormat("dd/MM/yyyy");
        Date dataAtual = new Date();

        //Armazena os dados do banco a lista de exibição
        storeDataInTaskList();

        //Aplica a formatação de data e inseri no TextView da data
        data.setText(formatacao.format(dataAtual));

        //Criação da lista de tasks
        RecyclerView taskList = findViewById(R.id.task_list);
        taskList.setHasFixedSize(true);

        //Layout Mananger
        layoutManager = new LinearLayoutManager(this);
        taskList.setLayoutManager(layoutManager);

        //Setar o adapter
        mAdapter = new RecycleViewAdapter(this,task_list);
        mAdapter.setClickListener(this);
        taskList.setAdapter(mAdapter);
    }

    public void Complete(int position, boolean completed){
        DatabaseHelper db = new DatabaseHelper(this);
        db.CompleteTask(task_list.get(position).Codigo, completed ? 1 : 0);
    }
}