package com.example.adiputra.sewainbali;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class MostSearchedAdapter extends RecyclerView.Adapter<MostSearchedAdapter.MostSearchedViewHolder> {
    private ArrayList<MostSearched> dataList;
    private View view;

    public MostSearchedAdapter(ArrayList<MostSearched> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MostSearchedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_most_searched, parent, false);
        return new MostSearchedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MostSearchedViewHolder holder, final int position) {
//        holder.imgMotor.setImageResource(dataList.get(position).getGambarMotor());
        double harga = Double.parseDouble(dataList.get(position).getHarga());

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Log.d("harga","harganya " + formatRupiah.format((double)harga));
        String price = formatRupiah.format((double)harga) + "/day";

        holder.idMotor = dataList.get(position).getIdMotor();
        holder.txtNama.setText(dataList.get(position).getNamaMotor());
        holder.txtJenis.setText(dataList.get(position).getJenis());
        holder.txtHarga.setText(price);
        holder.txtPemilik.setText(dataList.get(position).getPemilik());

        Drawable jenis;
        if (holder.txtJenis.getText().toString().equals("Matic")) jenis = view.getResources().getDrawable(R.drawable.jenis_matic_2);
        else if (holder.txtJenis.getText().toString().equals("Standard")) jenis = view.getResources().getDrawable(R.drawable.jenis_standard);
        else if (holder.txtJenis.getText().toString().equals("Sport")) jenis = view.getResources().getDrawable(R.drawable.jenis_sport);
        else if (holder.txtJenis.getText().toString().equals("Trail")) jenis = view.getResources().getDrawable(R.drawable.jenis_trail);
        else  jenis = view.getResources().getDrawable(R.drawable.jenis_matic);
        
        Glide.with(view.getContext())
                .load("https://kelompok23.000webhostapp.com/images/"+dataList.get(position).getGambarMotor())
                .placeholder(jenis)
                .into(holder.imgMotor);

        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailMotorActivity.class);
//                intent.putExtra("NAMA",holder.txtNama.getText());
                intent.putExtra("IDMOTOR",holder.idMotor);
                intent.putExtra("GBRMOTOR",dataList.get(position).getGambarMotor());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MostSearchedViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtJenis, txtHarga, txtPemilik;
        private ImageView imgMotor;
        String idMotor;
        View mView;

        public MostSearchedViewHolder(View itemView) {
            super(itemView);
            imgMotor = (ImageView) itemView.findViewById(R.id.img_motor);
            txtNama = (TextView) itemView.findViewById(R.id.tv_nama_motor);
            txtJenis = (TextView) itemView.findViewById(R.id.tv_jenis_motor);
            txtHarga = (TextView) itemView.findViewById(R.id.tv_harga_motor);
            txtPemilik = (TextView) itemView.findViewById(R.id.tv_pemilik);

            mView = itemView;
        }
    }
}
