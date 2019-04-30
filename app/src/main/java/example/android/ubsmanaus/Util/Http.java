package example.android.ubsmanaus.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.android.ubsmanaus.Model.Ubs;


public class Http {
    public static final String UBS_URL_JSON = "https://api.myjson.com/bins/13hcng";

    private static HttpURLConnection connectar(String urlArquivo) throws IOException{
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection)url.openConnection();
        conexao.setReadTimeout(10 * SEGUNDOS);
        conexao.setConnectTimeout(15 * SEGUNDOS);
        conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        conexao.setDoOutput(false);
        conexao.connect();
        return conexao;
    }

    public static List<Ubs> carregarUbsJson() {
        try {
            HttpURLConnection conexao = connectar(UBS_URL_JSON);
            int resposta = conexao.getResponseCode();
            if (resposta ==  HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                JSONArray json = new JSONArray(bytesParaString(is));
                return lerJsonUbs(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Ubs> lerJsonUbs(JSONArray json) throws JSONException {

        List<Ubs> listaDeUbs = new ArrayList<Ubs>();
        //JSONArray jsonUbs = json.getJSONArray("ubs");
        for (int j = 0; j < json.length(); j++) {
            JSONObject unidade = json.getJSONObject(j);
            Ubs ubs = new Ubs(
                    unidade.getLong("id"),
                    unidade.getString("nome"),
                    unidade.getString("endereco"),
                    unidade.getString("bairro"),
                    unidade.getString("latitude"),
                    unidade.getString("longitude"),
                    unidade.getString("servicos"),
                    unidade.getString("url_foto"),
                    unidade.getString("zona")

            );
            listaDeUbs.add(ubs);
        }

        return listaDeUbs;
    }

    private static String bytesParaString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        // O bufferzao vai armazenar todos os bytes lidos
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        // precisamos saber quantos bytes foram lidos
        int bytesLidos;
        // Vamos lendo de 1KB por vez...
        while ((bytesLidos = is.read(buffer)) != -1) {
            // copiando a quantidade de bytes lidos do buffer para o bufferzão
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }
}