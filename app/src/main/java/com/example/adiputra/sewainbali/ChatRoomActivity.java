package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;
    private String user_name ,room_name, email, email_tujuan;
    private DatabaseReference root;
    private String temp_key;
    private ChatRoomAdapter adapter;
    private ArrayList<ChatRoom> chatRoomArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        chatRoomArrayList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_send_msg = (Button)findViewById(R.id.button);
        input_msg = (EditText)findViewById(R.id.editText);
//        chat_conversation = (TextView)findViewById(R.id.textView);
//        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getStringExtra("NAMA");
        email_tujuan = getIntent().getStringExtra("EMAIL");
        email_tujuan = email_tujuan.replace("@","");
        email_tujuan = email_tujuan.replace(".","");
        email = Preferences.getLoggedInUser(this);
        email = email.replace("@","");
        email = email.replace(".","");
        user_name = Preferences.getKeyNameTeregister(this);

//        room_name = "Adi Putra";
        setTitle(room_name);
        Log.d("chatnya",chat_email+" | "+email+" | "+email_tujuan);
        Log.d("chatnya","before : "+chat_msg+" | "+chat_user_name+" | "+chat_email+" | "+email+" | "+email_tujuan);

        root = FirebaseDatabase.getInstance().getReference().child(email_tujuan+email);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ChatRoomAdapter(chatRoomArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("email",email);
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());

                message_root.updateChildren(map2);

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversatin(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversatin(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private String chat_msg, chat_user_name, chat_email;

    private void append_chat_conversatin(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            chat_email = (String) ((DataSnapshot)i.next()).getValue();
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            chat_email = chat_email.replace("@","");
            chat_email = chat_email.replace(".","");

            Log.d("chatnya","after : "+chat_msg+" | "+chat_user_name+" | "+chat_email+" | "+email+" | "+email_tujuan);
//            if (chat_email.equals(email) || chat_email.equals(email_tujuan)){
//                addData(chat_user_name,chat_msg,chat_email,email);
//            }
            addData(chat_user_name,chat_msg,chat_email,email);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                if (getParentActivityIntent() == null){
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void addData(String chat_user_name, String chat_msg, String chat_email, String email){
        chatRoomArrayList.add(new ChatRoom(chat_user_name,chat_msg, email, chat_email));
        adapter.notifyDataSetChanged();
    }
}
