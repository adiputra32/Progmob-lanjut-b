package com.example.adiputra.sewainbali;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {
    private ArrayList<ChatRoom> dataList;
    private View view;

    public ChatRoomAdapter(ArrayList<ChatRoom> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ChatRoomAdapter.ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_chat_room, parent, false);
        return new ChatRoomAdapter.ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatRoomAdapter.ChatRoomViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtPesan.setText(dataList.get(position).getPesan());
        String email_asal = dataList.get(position).getEmail_asal();
        String email_tujuan = dataList.get(position).getEmail_tujuan();
        String email = Preferences.getLoggedInUser(view.getContext());
        Log.d("chatnya",email_asal +" | "+ email + " |" + email_tujuan);
        if (!email_asal.equals(email_tujuan)){
            holder.lyUser.setGravity(Gravity.LEFT);
            holder.lyUser2.setGravity(Gravity.LEFT);
        } else {
            holder.lyUser.setGravity(Gravity.RIGHT);
            holder.lyUser2.setGravity(Gravity.RIGHT);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        TextView txtNama, txtPesan;
        LinearLayout lyUser,lyUser2;
        View mView;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.tv_name_chat_room);
            txtPesan = (TextView) itemView.findViewById(R.id.tv_msg_chat_room);
            lyUser = (LinearLayout) itemView.findViewById(R.id.ly_user);
            lyUser2 = (LinearLayout) itemView.findViewById(R.id.ly_user2);

            mView = itemView;
        }
    }
}
