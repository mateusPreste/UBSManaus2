package example.android.ubsmanaus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import example.android.ubsmanaus.Model.Ubs;

public class Repositorio {

    private SQLHelper helper;
    private SQLiteDatabase db;

    public Repositorio(Context ctx){
        helper = new SQLHelper(ctx);
    }

    public long inserir(Ubs ubs){
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLHelper.COLUNA_NOME, ubs.nome);
        cv.put(SQLHelper.COLUNA_ENDERECO, ubs.endereco);
        cv.put(SQLHelper.COLUNA_BAIRRO, ubs.bairro);
        cv.put(SQLHelper.COLUNA_LATITUDE, ubs.latitude);
        cv.put(SQLHelper.COLUNA_LONGITUDE, ubs.longitude);
        cv.put(SQLHelper.COLUNA_SERVICOS, ubs.servicos);
        cv.put(SQLHelper.COLUNA_URL_FOTO, ubs.url_foto);
        cv.put(SQLHelper.COLUNA_ZONA, ubs.zona);

        long id = db.insert(SQLHelper.TABELA_UBS, null, cv);

        if(id != -1){
            ubs.id = id;
        }
        db.close();
        return id;
    }

    public void excluirAll(){
        db = helper.getWritableDatabase();
        db.delete(SQLHelper.TABELA_UBS, null, null);
        db.close();
    }

    public List<Ubs> listarUbs() {
        String sql = "SELECT * FROM " + SQLHelper.TABELA_UBS;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Ubs> list = new ArrayList();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(
                    cursor.getColumnIndex(SQLHelper.COLUNA_ID)
            );
            String nome = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_NOME)
            );
            String endereco = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_ENDERECO)
            );
            String bairro = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_BAIRRO)
            );
            String latitude = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_LATITUDE)
            );
            String longitude = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_LONGITUDE)
            );
            String servicos = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_SERVICOS)
            );
            String url_foto = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.COLUNA_URL_FOTO)
            );
            String zona = cursor.getString(
                    cursor.getColumnIndex(
                            SQLHelper.COLUNA_ZONA)
            );

            Ubs ubs = new Ubs(id, nome, endereco, bairro, latitude, longitude, servicos, url_foto, zona);
            list.add(ubs);
        }
        cursor.close();
        return list;
    }

}