package example.android.ubsmanaus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import example.android.ubsmanaus.Model.Ubs;
import example.android.ubsmanaus.R;

public class MapsAdapter extends ArrayAdapter<Ubs> {

    public MapsAdapter(Context context, List<Ubs> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ubs ubs = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_maps, parent, false);
        }

        TextView tvNome = (TextView) convertView.findViewById(R.id.nome);
        TextView tvBairro = (TextView) convertView.findViewById(R.id.bairro);

        tvNome.setText(ubs.nome);
        tvBairro.setText(ubs.bairro);

        return convertView;
    }
}
