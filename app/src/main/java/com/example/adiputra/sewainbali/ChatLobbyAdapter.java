package com.example.adiputra.sewainbali;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ChatLobbyAdapter extends RecyclerView.Adapter<ChatLobbyAdapter.ChatLobbyViewHolder> {
    private ArrayList<ChatLobby> dataList;
    private View view;

    public ChatLobbyAdapter(ArrayList<ChatLobby> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ChatLobbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_chat_lobby, parent, false);
        return new ChatLobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatLobbyViewHolder holder, int position) {
        Glide.with(view.getContext()).load("https://kelompok23.000webhostapp.com/images/"+dataList.get(position).getGambar()).placeholder(R.drawable.user).into(holder.imgProfile);
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.email = dataList.get(position).getEmail();
        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatRoomActivity.class);
//                intent.putExtra("NAMA",holder.txtNama.getText());
                intent.putExtra("NAMA",holder.txtNama.getText().toString());
                intent.putExtra("EMAIL",holder.email);
                view.getContext().startActivity(intent);
//                Log.d("debug","nama : " + holder.txtNama.getText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ChatLobbyViewHolder extends RecyclerView.ViewHolder{
        TextView txtNama, txtPesan;
        ImageView imgProfile;
        String email;
        View mView;

        public ChatLobbyViewHolder(View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_chat_lobby);
            txtNama = (TextView) itemView.findViewById(R.id.tv_name_chat_lobby);
            txtPesan = (TextView) itemView.findViewById(R.id.tv_msg_chat_lobby);

            mView = itemView;
        }
    }
}
