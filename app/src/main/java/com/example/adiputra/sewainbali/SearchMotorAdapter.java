package com.example.adiputra.sewainbali;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class SearchMotorAdapter extends RecyclerView.Adapter<SearchMotorAdapter.SearchMotorViewHolder> implements Filterable {
    private ArrayList<SearchMotor> dataList;
    private ArrayList<SearchMotor> dataListFull;
    private View view;

    public SearchMotorAdapter(ArrayList<SearchMotor> dataList) {
        this.dataList = dataList;
        dataListFull = new ArrayList<>(dataList);
    }

    @Override
    public SearchMotorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_search_motor, parent, false);
        return new SearchMotorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchMotorViewHolder holder, final int position) {
        double harga = Double.parseDouble(dataList.get(position).getHarga());

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Log.d("harga","harganya " + formatRupiah.format((double)harga));
        String price = formatRupiah.format((double)harga) + "/day";

        holder.idMotor.setText(dataList.get(position).getIdMotor());
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
                .load("https://kelompok23.000webhostapp.com/images/"+dataList.get(position).getGambar())
                .placeholder(jenis)
                .into(holder.imgMotor);

        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailMotorActivity.class);
//                intent.putExtra("NAMA",holder.txtNama.getText());
                intent.putExtra("IDMOTOR",holder.idMotor.getText().toString());
                intent.putExtra("GBRMOTOR",dataList.get(position).getGambar());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchMotor> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SearchMotor item : dataListFull) {
                    if (item.getNamaMotor().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    } else if (item.getPemilik().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList.clear();
            dataList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class SearchMotorViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtJenis, txtHarga, txtPemilik, idMotor;
        private ImageView imgMotor;
        View mView;

        public SearchMotorViewHolder(View itemView) {
            super(itemView);
            imgMotor = (ImageView) itemView.findViewById(R.id.img_motor);
            txtNama = (TextView) itemView.findViewById(R.id.tv_nama_motor);
            txtJenis = (TextView) itemView.findViewById(R.id.tv_jenis_motor);
            txtHarga = (TextView) itemView.findViewById(R.id.tv_harga_motor);
            txtPemilik = (TextView) itemView.findViewById(R.id.tv_pemilik);
            idMotor = (TextView) itemView.findViewById(R.id.tv_id_motor);

            mView = itemView;
        }
    }
}
