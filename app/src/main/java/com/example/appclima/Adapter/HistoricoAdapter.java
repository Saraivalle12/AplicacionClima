package com.example.appclima.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appclima.Database.DatabaseHelper;
import com.example.appclima.Dto.Historico;
import com.example.appclima.R;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.MyViewHolder> {
    private Context context;
    private List<Historico> notesList;
    private List<Historico> movieListFiltered;
    private DatabaseHelper mDatabase;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView temperatura;
        public TextView summary;
        public TextView time;
        public TextView location;
        public TextView coordenadas;
        public ImageView imagen;
        public LinearLayout linear1;

        public MyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.id);
            temperatura = view.findViewById(R.id.temperature);
            summary = view.findViewById(R.id.summary);
            time = view.findViewById(R.id.time);
            location = view.findViewById(R.id.location);
            coordenadas = view.findViewById(R.id.coordenadas);
            linear1 = view.findViewById(R.id.rowFG);
            imagen = view.findViewById(R.id.img_user);
        }
    }


    public HistoricoAdapter(Context context, List<Historico> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
        mDatabase = new DatabaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historico_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Historico note = notesList.get(position);
        holder.id.setText("Id: "+note.getId());
        holder.temperatura.setText(""+note.getTemperatura());
        holder.summary.setText(""+note.getSummary());
        holder.time.setText(""+note.getTime());
        holder.location.setText(""+note.getLocation());
        holder.coordenadas.setText(""+note.getCoordenadas());
        switch (note.getIcono()) {
            case "clear-day":
                holder.imagen.setImageResource(R.drawable.ic_clear_day);
                break;

            case "clear-night":
                holder.imagen.setImageResource(R.drawable.ic_clear_night);
                break;

            case "rain":
                holder.imagen.setImageResource(R.drawable.ic_rain);
                break;

            case "snow":
                holder.imagen.setImageResource(R.drawable.ic_snow);
                break;

            case "sleet":
                holder.imagen.setImageResource(R.drawable.ic_sleet);
                break;

            case "wind":
                holder.imagen.setImageResource(R.drawable.ic_wind);
                break;

            case "fog":
                holder.imagen.setImageResource(R.drawable.ic_fog);
                break;

            case "cloudy":
                holder.imagen.setImageResource(R.drawable.ic_cloudy);
                break;

           case "partly-cloudy-day":
               // Dia parcialmente nublado
                holder.imagen.setImageResource(R.drawable.ic_cloudy_day);
                break;

            case "partly-cloudy-night":
                // Noche parcialmente nublada
                holder.imagen.setImageResource(R.drawable.ic_cloudy_night);
                break;

            case "hail":
                // Granizo
                holder.imagen.setImageResource(R.drawable.ic_hail);
                break;

            case "thunderstorm":
                // Tormenta
                holder.imagen.setImageResource(R.drawable.ic_thunderstorm);
                break;

            case "tornado":
                 // Tornado
                holder.imagen.setImageResource(R.drawable.ic_tornado);
                break;
        }

        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Deseas eliminar este registro ?");
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EliminarHistorico(note.getId());
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        });
    }


    private void EliminarHistorico(int id) {
        mDatabase.deleteItem(id);
    }




    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
