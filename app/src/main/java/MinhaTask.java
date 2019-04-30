import android.os.AsyncTask;

import java.util.List;

class MinhaTask extends AsyncTask<String, Void, List<String>> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // ex.: Barra de progresso
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        // ex.: Buscar na internet
        return null;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        // ex.: Processamento dos dados vindos do Servidor
    }
}