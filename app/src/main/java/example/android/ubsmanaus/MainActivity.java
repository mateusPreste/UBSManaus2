package example.android.ubsmanaus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import example.android.ubsmanaus.Adapter.Adapter;
import example.android.ubsmanaus.Model.Ubs;
import example.android.ubsmanaus.Util.Http;
import example.android.ubsmanaus.Util.HttpRetro;
import example.android.ubsmanaus.dao.Repositorio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Adapter adapter;
    private List<Ubs> ubsList;
    private ListView listView;
    private SwipeRefreshLayout swiperefresh;
    Repositorio db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swiperefresh = (SwipeRefreshLayout) findViewById((R.id.swiperefresh));
        //seta cores
        swiperefresh.setColorScheme(R.color.colorPrimary, R.color.colorAccent);
        swiperefresh.setOnRefreshListener(this);

        listView = (ListView) findViewById(R.id.listView);

        ubsList = new ArrayList<Ubs>();

        adapter = new Adapter(this, ubsList);

        db = new Repositorio(getBaseContext());
        getDataRetro();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hasPermission();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("ubs",ubsList.get(position));
                startActivity(intent);
            }
        });
    }

    // chama AsyncTask para requisicao das ubs
    public void getDataHttp () {
        UbsTask mTask = new UbsTask();
        mTask.execute();
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        return false;
    }


    private void getDataSqlite() {
        ubsList.clear();
        ubsList.addAll(db.listarUbs());
        adapter.notifyDataSetChanged();
    }

    public void getDataRetro() {

        swiperefresh.setRefreshing(true);

        // se tiver conexao faz get, senao pega do sqlite
        if (isConnected()) {
            HttpRetro.getUbsClient().getUbs().enqueue(new Callback<List<Ubs>>() {
                public void onResponse(Call<List<Ubs>> call, Response<List<Ubs>> response) {
                    if (response.isSuccessful()) {
                        List<Ubs> ubsBody = response.body();
                        ubsList.clear();

                        db.excluirAll();

                        for (Ubs ubs : ubsBody) {
                            ubsList.add(ubs);
                            db.inserir(ubs);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.out.println(response.errorBody());
                    }
                    swiperefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Ubs>> call, Throwable t) {
                    t.printStackTrace();

                }

            });

        }else {
            swiperefresh.setRefreshing(false);
            Toast.makeText(this,"Sem Conexão, listando Ubs do banco...",Toast.LENGTH_SHORT).show();
            getDataSqlite();
        }

    }



    void hasPermission(){
        //pede permissao de localizacao
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // ja pediu permissao?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // solicita permissao de localizacao
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onRefresh() {
        getDataRetro();
    }

    class UbsTask extends AsyncTask<Void, Void, List<Ubs>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swiperefresh.setRefreshing(true);
        }

        @Override
        protected List<Ubs> doInBackground(Void... voids) {
            return Http.carregarUbsJson();
        }

        @Override
        protected void onPostExecute(List<Ubs> ubs) {
            super.onPostExecute(ubs);
            if (ubs != null) {
                ubsList.clear();
                ubsList.addAll(ubs);
                adapter.notifyDataSetChanged();
            }
            swiperefresh.setRefreshing(false);
        }
    }
}


