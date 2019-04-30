package example.android.ubsmanaus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "dbUbs";
    private static final int VERSAO_BANCO = 1;
    public static final String TABELA_UBS = "ubs_tabela";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_ENDERECO = "endereco";
    public static final String COLUNA_BAIRRO = "bairro";
    public static final String COLUNA_LATITUDE = "latitude";
    public static final String COLUNA_LONGITUDE = "longitude";
    public static final String COLUNA_SERVICOS = "servicos";
    public static final String COLUNA_URL_FOTO = "url_foto";
    public static final String COLUNA_ZONA = "zona";


    public SQLHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABELA_UBS + " ( " +
                        COLUNA_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        COLUNA_NOME + " TEXT, " +
                        COLUNA_ENDERECO + " TEXT, " +
                        COLUNA_BAIRRO + " TEXT, " +
                        COLUNA_LATITUDE + " TEXT, " +
                        COLUNA_LONGITUDE + " TEXT, " +
                        COLUNA_SERVICOS + " TEXT, " +
                        COLUNA_URL_FOTO + " TEXT, " +
                        COLUNA_ZONA + " TEXT)"
        );

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // para as próximas versões
    }

}
