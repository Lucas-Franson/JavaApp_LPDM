package com.uniso.lpdm.whatdoido;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* Para instanciar o banco:
*  SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
*  SQLiteDatabase = databaseHelper.getReadableDatabase();
*/
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "whatdoido";
    private static final int DB_VERSION = 1;

    DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        atualizarBanco(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        atualizarBanco(db, oldVersion, newVersion);
    }

    /* === TAREFAS === */

    /* create */
    public static void inserirTarefa(SQLiteDatabase db, String titulo, Integer id_usuario, String descricao, Integer tipo_tarefa, String prazo, String lembrete_semana){

        ContentValues tarefa = new ContentValues();

        tarefa.put("titulo", titulo);
        tarefa.put("id_usuario", id_usuario);
        tarefa.put("descricao", descricao);
        tarefa.put("tipo_tarefa", tipo_tarefa);
        tarefa.put("concluido", 0);
        tarefa.put("prazo", prazo);
        tarefa.put("lembrete_semana", lembrete_semana);

        long ret = db.insert("tarefa", null, tarefa);

        if(ret != -1 ) {
            Log.d("INSERIDO", "######################-------INSERIDO ####################---------------");
        }
        else{
            Log.d("ERRO", "######################-------ERRO AO INSERIR ####################---------------");
        }
    }

    /* delete */
    public static void deletarTarefa(SQLiteDatabase db, int id_tarefa ) {
        db.delete("tarefa", "_id=?", new String[]{id_tarefa + ""});
    }

    /* update */
    public static void modificarTarefa(SQLiteDatabase db, int id_tarefa, ContentValues tarefa ){

        db.update("tarefa", tarefa, "_id=?", new String[]{id_tarefa + ""});
    }

    /*
     * Para leitura dos dados utilizar SimpleCursorAdapter
     * exemplo:
     *    Cursor cursor = DatabaseHelper.buscarTarefas(db);
     *    CursorAdapter tarefasAdapter = new SimpleCursorAdapter(
     *            this,
     *            android.R.layout.simple_list_item_1,
     *            cursor,
     *            new String[] {"titulo"},
     *            new int[] {android.R.id.text1},
     *            0
     *    );
     * */
    /* get all */
    public static Cursor buscarTarefas(SQLiteDatabase db) {

        Cursor res = db.rawQuery( "select * from "+ "tarefa", null );

        return res;
    }

    /* get one */
    public static Cursor buscarTarefa(SQLiteDatabase db, int id) {

        Cursor res = db.rawQuery("select * from tarefa where _id = ?", new String[] { String.valueOf(id) });

        return res;
    }

    /* TIPO */
    public static void inserirTipo(SQLiteDatabase db, String _nome){
        ContentValues tipo = new ContentValues();
        tipo.put("nome_tipo", _nome);

        db.insert("tipo_tarefa", null, tipo);
    }

    /* USUARIO */
    public static void inserirUsuario(SQLiteDatabase db, String _usuario, String _senha){

        ContentValues usuario = new ContentValues();

        usuario.put("nome_usuario", _usuario);
        usuario.put("senha", _senha);

        db.insert("usuario", null, usuario);
    }

    private void atualizarBanco(SQLiteDatabase db, int oldVersion, int newVersion){

        String sql;
        Log.d("entrou", "######################------- ENTROU ####################---------------");

        /* cria o banco pela primeira vez */
        if(oldVersion < 1){

            /* tabela usuario */
            db.execSQL("create table usuario(" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  nome_usuario text NOT NULL," +
                    "  senha text NOT NULL" +
                    ");");

            /* tabela tarefa*/
            db.execSQL("CREATE table tarefa(" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  id_usuario INTEGER NOT NULL," +
                    "  titulo text NOT NULL," +
                    "  descricao text," +
                    "  tipo_tarefa INTEGER NOT NULL," +
                    "  concluido INTEGER NOT NULL," +
                    "  prazo text," +
                    "  lembrete_semana text," +
                    "  FOREIGN KEY(id_usuario)REFERENCES usuario(_id)," +
                    "  FOREIGN KEY(tipo_tarefa) REFERENCES tipo_tarefa(_id)" +
                    ");");

            /* tabela tipo */
            db.execSQL("create TABLE tipo_tarefa(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " nome_tipo text NOT NULL" +
                    ");");

            /* mock */
            inserirTipo(db, "Escolar");

            inserirUsuario(db,
                    "admin",
                    "1234"
            );

            inserirTarefa(db,
                    "primeira tarefa",
                    1,
                    "descricao da primeira tarefa",
                    1,
                    "20/11/2020",
                    "quarta-feira"
            );
        }

        /* atualizacoes do banco*/
        if (oldVersion < 2 ){
            // atualizar banco de dados v2
        }

    }
}
