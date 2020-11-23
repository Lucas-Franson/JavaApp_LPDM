package com.uniso.lpdm.whatdoido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.zip.Inflater;

//Essa tela é bem semelhante a ItemTaskActivity, porém os dados da task só serão setados para exibição
public class TaskActivity extends AppCompatActivity {

    private int Id;
    private TextView titulo, tipo, descricao, lembrete;
    private boolean isCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        this.Id = getIntent().getIntExtra("TASK_CODE_EXTRA", 0);

        titulo = findViewById(R.id.txt_titulo2);
        tipo = findViewById(R.id.txt_tipo);
        descricao = findViewById(R.id.txt_desc);
        lembrete = findViewById(R.id.txt_lembrete);

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.GetOneTask(this.Id);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Tarefa não encontrada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (cursor.moveToFirst()) {
                Task tarefa = new Task(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), "D,S", cursor.getInt(6) > 0);
                String tipoText = getResources().getStringArray(R.array.tipos_task)[tarefa.Tipo];
                titulo.setText(tarefa.Titulo);
                tipo.setText(tipoText);
                descricao.setText(tarefa.Descricao);
                lembrete.setText(tarefa.Lembrete);
                isCompleted = tarefa.isCompleted;
            }
        }
    }

    //Função que altera o layout do menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Função que compartilha task com outros aplicativos
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //É disparada quando um item do menu é selecionado
        if (item.getItemId() == R.id.share){
            //se existe o botão de compartilhar, uma intenção é criada levando o usuário a escolher o app desejado para o compartilhamento
            String message;
            if (isCompleted)
                message = "Completei a tarefa \"" + titulo.getText().toString() + "\"";
            else
                message = "Estou fazendo a tarefa \""+ titulo.getText().toString() + "\"";

            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }catch(Exception error){
                Toast.makeText(this, "Ocorreu um erro, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickEditTask(View view) {
        Intent intent = new Intent(this, ItemTaskActivity.class);
        intent.putExtra("TASK_CODE_EXTRA",this.Id);
        startActivity(intent);
    }
}