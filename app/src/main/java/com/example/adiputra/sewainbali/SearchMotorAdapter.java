package com.example.adiputra.sewainbali;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(SearchMotorViewHolder holder, int position) {
        holder.imgMotor.setImageResource(dataList.get(position).getGambar());
        holder.txtNama.setText(dataList.get(position).getNamaMotor());
        holder.txtJenis.setText(dataList.get(position).getJenis());
        holder.txtHarga.setText(dataList.get(position).getHarga());
        holder.txtPemilik.setText(dataList.get(position).getPemilik());
        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailMotorActivity.class);
//                intent.putExtra("NAMA",holder.txtNama.getText());
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
        private TextView txtNama, txtJenis, txtHarga, txtPemilik;
        private ImageView imgMotor;
        View mView;

        public SearchMotorViewHolder(View itemView) {
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
