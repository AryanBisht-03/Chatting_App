package com.example.tryingwhatsapp.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tryingwhatsapp.Models.MessagesModel;
import com.example.tryingwhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{


    ArrayList<MessagesModel> list;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE =2;
    public ChatAdapter(ArrayList<MessagesModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        if(list.get(position).getId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return  RECEIVER_VIEW_TYPE;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new senderViewHolder(view);
        }
        else {

            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new recieverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel =  list.get(position);

        if(holder.getClass() == senderViewHolder.class)
        {
            ((senderViewHolder)holder).senderMessage.setText(messagesModel.getMessage());

            String time = DateUtils.formatDateTime(context, messagesModel.getTime(), DateUtils.FORMAT_SHOW_TIME);

            ((senderViewHolder)holder).senderTime.setText(time);
        }
        else
        {
            ((recieverViewHolder)holder).recieverMessage.setText(messagesModel.getMessage());

            String time = DateUtils.formatDateTime(context, messagesModel.getTime(), DateUtils.FORMAT_SHOW_TIME);

            ((recieverViewHolder)holder).recieverTime.setText(time);
        }

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class recieverViewHolder extends RecyclerView.ViewHolder
    {

        TextView recieverMessage, recieverTime;
        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);

            recieverMessage = itemView.findViewById(R.id.recieverText);
            recieverTime = itemView.findViewById(R.id.recieverTime);

        }
    }

    public class senderViewHolder extends RecyclerView.ViewHolder{


        TextView senderMessage , senderTime;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }
}
