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

import example.android.ubsmanaus.Adapter.Adapter;
import example.android.ubsmanaus.Model.Countries;
import example.android.ubsmanaus.Util.Http;
import example.android.ubsmanaus.Util.HttpRetro;
import example.android.ubsmanaus.dao.Repositorio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Adapter adapter;
    private List<Countries> contryList;
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

        contryList = new ArrayList<Countries>();

        adapter = new Adapter(this, contryList);

        db = new Repositorio(getBaseContext());
        getDataRetro();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hasPermission();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("ubs", contryList.get(position));
                startActivity(intent);
            }
        });
    }

    // chama AsyncTask para requisicao das ubs
    public void getDataHttp () {
        CountryTask mTask = new CountryTask();
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
        contryList.clear();
        contryList.addAll(db.listarPaises());
        adapter.notifyDataSetChanged();
    }

    public void getDataRetro() {

        swiperefresh.setRefreshing(true);

        // se tiver conexao faz get, senao pega do sqlite
        if (isConnected()) {
            HttpRetro.getCountryClient().getUbs().enqueue(new Callback<List<Countries>>() {
                public void onResponse(Call<List<Countries>> call, Response<List<Countries>> response) {
                    if (response.isSuccessful()) {
                        List<Countries> countryBody = response.body();
                        contryList.clear();

                        db.excluirAll();

                        for (Countries country : countryBody) {
                            contryList.add(country);
                            db.inserir(country);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.out.println(response.errorBody());
                    }
                    swiperefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Countries>> call, Throwable t) {
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

    class CountryTask extends AsyncTask<Void, Void, List<Countries>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swiperefresh.setRefreshing(true);
        }

        @Override
        protected List<Countries> doInBackground(Void... voids) {
            return Http.carregarCountryJson();
        }

        @Override
        protected void onPostExecute(List<Countries> ubs) {
            super.onPostExecute(ubs);
            if (ubs != null) {
                contryList.clear();
                contryList.addAll(ubs);
                adapter.notifyDataSetChanged();
            }
            swiperefresh.setRefreshing(false);
        }
    }
}


