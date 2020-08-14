//package com.fyp.baigktk.cuifr.viewholder;
//
//import android.text.format.DateFormat;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.fyp.baigktk.cuifr.ChatActivity;
//import com.fyp.baigktk.cuifr.R;
//import com.fyp.baigktk.cuifr.models.ChatMessage;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//
//public class MessageAdapter extends FirebaseListAdapter<ChatMessage> {
//
//    private ChatActivity activity;
//
//    public MessageAdapter(ChatActivity activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref) {
//        super(activity,modelClass,modelLayout,ref);
//
//        // super(activity, modelClass, modelLayout, ref);
//        this.activity = activity;
//
//    }
//
//
//
//    @Override
//    protected void populateView(View v, ChatMessage model, int position) {
//        TextView messageText = (TextView) v.findViewById(R.id.message_text);
//        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
//        TextView messageTime = (TextView) v.findViewById(R.id.message_time);
//
//        messageText.setText(model.getMessageText());
//        messageUser.setText(model.getMessageUser());
//
//        // Format the date before showing it
//        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//    }
//    @Override
//    public View getView(int position, View view, ViewGroup viewGroup) {
//        ChatMessage chatMessage = getItem(position);
//        String myKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        if (chatMessage.getMessageUserId().equals(myKey))
//            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
//        else
//            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
//
//        //generating view
//        populateView(view, chatMessage, position);
//
//        return view;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        // return the total number of view types. this value should never change
//        // at runtime
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        // return a value between 0 and (getViewTypeCount - 1)
//        return position % 2;
//    }
//
//}
