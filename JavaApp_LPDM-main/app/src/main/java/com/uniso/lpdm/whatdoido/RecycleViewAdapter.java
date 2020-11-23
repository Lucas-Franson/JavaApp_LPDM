package com.uniso.lpdm.whatdoido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Adaptador customizado para a RecyclerView
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private ArrayList<Task> taskList;
    private LayoutInflater mInflater;
    private Context mContext;
    private ItemClickListener mClickListener;

    RecycleViewAdapter(Context context, ArrayList<Task> data){
        this.mInflater = LayoutInflater.from(context);
        this.taskList = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    //Função que cria a view dentro do recycler view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Cria as view com o mesmo layout
        View view = mInflater.inflate(R.layout.recycleview_row, parent,false);
        return new ViewHolder(view);
    }


    @Override
    //Função que seta os dados na view criados pelo OnCreateViewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Após a view ser criada, os dados de titulo precisam 
        Task task = taskList.get(position);
        holder.task.setText(task.Titulo);

        if (task.isCompleted)
            holder.check.setChecked(true);

        //Insere um listener no checkbox para quando o botão muda de estado
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainTaskActivity tsk = ((MainTaskActivity)mContext);
                if (isChecked){
                    tsk.Complete(position,true);
                }else{
                    tsk.Complete(position,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    //Adiciona o listener para quando um item da recycler view é selecionado
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView task;
        CheckBox check;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task_details);
            check = itemView.findViewById(R.id.task_chkbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null){
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    String getItem(int id){
        return taskList.get(id).Titulo;
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

}
