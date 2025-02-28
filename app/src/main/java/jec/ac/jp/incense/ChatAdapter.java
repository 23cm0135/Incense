package jec.ac.jp.incense;

import android.view.LayoutInflater;
import android.view.View;
import jec.ac.jp.incense.ChatMessage;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.messageTextView.setText(chatMessage.getMessage());
        if (chatMessage.getIsAi()) {
            holder.messageTextView.setBackgroundColor(0xFFE0E0E0);
        } else {
            holder.messageTextView.setBackgroundColor(0xFFFFFFFF);
        }
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}

