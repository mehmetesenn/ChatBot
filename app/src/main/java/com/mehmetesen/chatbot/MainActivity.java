package com.mehmetesen.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView chatsRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY="bot";
    private final  String USER_KEY="user";
    private ArrayList<ChatsModal> chatsModalArrayList;
    private ChatRVAdapter chatRVAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdt=findViewById(R.id.idEdtMessage);
        sendMsgFAB=findViewById(R.id.idFABSend);
        chatsModalArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModalArrayList,this);
        LinearLayoutManager manager  = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);
        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your message", Toast.LENGTH_LONG).show();
                    return;

                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");

            }
        });



    }
    private void getResponse(String message){
        chatsModalArrayList.add(new ChatsModal(message,USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url= "http://api.brainshop.ai/get?bid=164969&key=k4zmExe0WzN6Y92Z&uid=[uid]&msg="+message;
        String BASE_URL="http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI  = retrofit.create(RetrofitAPI.class);
        Call<MSGModal> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MSGModal>() {
            @Override
            public void onResponse(Call<MSGModal> call, Response<MSGModal> response) {
                if(response.isSuccessful()){
                    MSGModal msgModal = response.body();
                    chatsModalArrayList.add(new ChatsModal(msgModal.getCnt(),BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<MSGModal> call, Throwable t) {
                chatsModalArrayList.add(new ChatsModal("Please revert your question ",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();

            }
        });


    }
}