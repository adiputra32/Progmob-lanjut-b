package com.example.adiputra.sewainbali;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<History> dataList;
    private View view;

    public HistoryAdapter(ArrayList<History> dataList) {
        this.dataList = dataList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {
        holder.idSewa = dataList.get(position).getId();
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtPemilik.setText(dataList.get(position).getPemilik());
        holder.txtJenis.setText(dataList.get(position).getJenis());
        holder.txtStatus.setText(dataList.get(position).getStatus());

        Drawable jenis;
        if (holder.txtJenis.getText().toString().equals("Matic")) jenis = view.getResources().getDrawable(R.drawable.jenis_matic_2);
        else if (holder.txtJenis.getText().toString().equals("Standard")) jenis = view.getResources().getDrawable(R.drawable.jenis_standard);
        else if (holder.txtJenis.getText().toString().equals("Sport")) jenis = view.getResources().getDrawable(R.drawable.jenis_sport);
        else if (holder.txtJenis.getText().toString().equals("Trail")) jenis = view.getResources().getDrawable(R.drawable.jenis_trail);
        else  jenis = view.getResources().getDrawable(R.drawable.jenis_matic);

        Glide.with(view.getContext())
                .load("https://kelompok23.000webhostapp.com/images/"+dataList.get(position).getGambar())
                .placeholder(jenis)
                .into(holder.imgMotor);

        if (holder.txtStatus.getText().toString().equals("Success")){
            holder.txtStatus.setTextColor(Color.parseColor("#8BC34A"));
        } else {
            holder.txtStatus.setTextColor(Color.parseColor("#cf3e3e"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailHistoryActivity.class);
//                intent.putExtra("NAMA",holder.txtNama.getText());
                intent.putExtra("IDSEWA2",holder.idSewa);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtPemilik, txtJenis, txtStatus;
        private ImageView imgMotor;
        String idSewa;
        View mView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            imgMotor = (ImageView) itemView.findViewById(R.id.img_history_motor);
            txtNama = (TextView) itemView.findViewById(R.id.tv_history_nama);
            txtPemilik = (TextView) itemView.findViewById(R.id.tv_history_pemilik);
            txtJenis = (TextView) itemView.findViewById(R.id.tv_history_jenis);
            txtStatus = (TextView) itemView.findViewById(R.id.tv_history_status);

            mView = itemView;
        }
    }
}
