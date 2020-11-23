package com.uniso.lpdm.whatdoido;

public class Task {
    public int Codigo;
    public String Titulo;
    public int Tipo;
    public String Descricao;
    public String Lembrete;
    public String DiaSemana;
    public boolean isCompleted;

    public Task(int codigo, String titulo, int tipo, String descricao, String lembrete, String diaSemana, boolean completed) {
        Codigo = codigo;
        Titulo = titulo;
        Tipo = tipo;
        Descricao = descricao;
        Lembrete = lembrete;
        DiaSemana = diaSemana;
        isCompleted = completed;
    }

}
