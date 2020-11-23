package com.uniso.lpdm.whatdoido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ItemTaskActivity extends AppCompatActivity {

    private int Id;
    private EditText titulo;
    private Spinner tipo;
    private EditText descricao;
    private TextView lembrete;
    private Calendar c = Calendar.getInstance();

    //Tela de Criação e Edição de uma Task
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_task);

        //Preencher os tipos
        Spinner tipos = findViewById(R.id.tipo_tarefa_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_task, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipos.setAdapter(adapter);

        //Setar inputs
        titulo = findViewById(R.id.titulo);
        tipo = findViewById(R.id.tipo_tarefa_spinner);
        descricao = findViewById(R.id.descricao_tarefa);
        lembrete = findViewById(R.id.lembrete);

        //Verifica se existe um código de task
        this.Id = getIntent().getIntExtra("TASK_CODE_EXTRA", 0);

        //Se sim, altera o layout para edição, se não o layout fica como de criação
        if (this.Id != 0){
            Button btn = findViewById(R.id.button_add_tarefa);
            Button del = findViewById(R.id.button_delete);

            TextView txt = findViewById(R.id.txt_titulo);

            txt.setText("Editar Tarefa");
            btn.setText("Atualizar");
            del.setVisibility(View.VISIBLE);

            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.GetOneTask(this.Id);

            if (cursor.getCount() == 0){
                Toast.makeText(this,"Tarefa não encontrada", Toast.LENGTH_SHORT).show();
            }else{
                if (cursor.moveToFirst()){
                    Task tarefa = new Task(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3),cursor.getString(4),"D,S", cursor.getInt(6) > 0);

                    //Seta os dados para edição
                    titulo.setText(tarefa.Titulo);
                    tipo.setSelection(tarefa.Tipo);
                    descricao.setText(tarefa.Descricao);
                    lembrete.setText(tarefa.Lembrete);
                }
            }


        }

    }

    //Função de criação de task
    public void onClickAdicionarTarefa(View view) {
        DatabaseHelper mydb = new DatabaseHelper(this);

        //Pega os dados inseridos pelo usuário
        Task tarefa = new Task(this.Id, titulo.getText().toString(), tipo.getSelectedItemPosition(), descricao.getText().toString(), lembrete.getText().toString(), "D", false);
        if (this.Id != 0) {
            //Atualiza os dados e o alarme
            mydb.UpdateTask(tarefa);

            cancelNotification("Você tem uma tarefa!","Não se esqueça da tarefa " + tarefa.Titulo, this.Id);
            setNotification("Você tem uma tarefa!","Não se esqueça da tarefa " + tarefa.Titulo, c, this.Id);

            finish();

        } else {
            //Adiciona uma nova task ao banco, junto de seu lembrete
            this.Id = (int) mydb.AddTask(tarefa);

            setNotification("Você tem uma tarefa!","Não se esqueça da tarefa " + tarefa.Titulo, c, this.Id);
            finish();
        }
    }

    //Função de criação do time picker
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Armazena horário em uma variável global
    public void setLembrete(int hour, int minute) {
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

        String horario = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());

        TextView lembrete = findViewById(R.id.lembrete);
        lembrete.setText(horario);
    }

    //Exibe o dialog do time picker
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((ItemTaskActivity) this.getActivity()).setLembrete(hourOfDay, minute);
        }
    }

    //Função que cria um lembrete
    public void setNotification(String title, String text, Calendar c, int id){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("TITLE_EXTRA",title);
        intent.putExtra("DESC_EXTRA",text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }


    //Função de cancelamento de lembrete
    public void cancelNotification(String title, String text, int id){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("TITLE_EXTRA",title);
        intent.putExtra("DESC_EXTRA",text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id,intent,0);
        alarmManager.cancel(pendingIntent);

    }

    //Função de exclusão de task
    public void onClickDeleteTask(View view){
        DatabaseHelper db = new DatabaseHelper(this);
        cancelNotification(titulo.getText().toString(), descricao.getText().toString(), this.Id);

        db.DeleteTask(this.Id);

        Intent intent = new Intent(this, MainTaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}