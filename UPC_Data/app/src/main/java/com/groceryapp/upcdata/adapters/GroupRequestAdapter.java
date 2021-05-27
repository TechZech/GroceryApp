package com.groceryapp.upcdata.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class GroupRequestAdapter extends RecyclerView.Adapter<GroupRequestAdapter.ViewHolder> {

    private Context context;
    private List<Group> groupRequestList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User grUser = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public GroupRequestAdapter(Context context, List<Group> groupRequestList){
        this.context = context;
        this.groupRequestList = groupRequestList;
    }
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("GroupRequestAdapter", "OnBindViewHolder" + position);
        Group gr = groupRequestList.get(position);
        holder.bind(gr);
    }

    public void clear(){
        groupRequestList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Group> list){
        groupRequestList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groupRequestList.size();
    }
    public void removeAt(int position) {
        groupRequestList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, groupRequestList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView frImage;
        private TextView frUser;
        private Button acceptButton;
        private DBHelper dbHelper;
        private Button declineButton;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            frUser = itemView.findViewById(R.id.tvUser);

            frImage = itemView.findViewById(R.id.imageView);
            //  item_grocery_container = itemView.findViewById(R.id.item_grocery_container);
            dbHelper = new DBHelper();
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.addGroup(groupRequestList.get(getAdapterPosition()).getGid(), grUser);
                 //   dbHelper.acceptFriend(friendRequestList.get(getAdapterPosition()).getuserID());
                    removeAt(getAdapterPosition());
                }
            });
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.declineFriend(groupRequestList.get(getAdapterPosition()).getGid());
                    removeAt(getAdapterPosition());
                }
            });

        }

        public void bind(Group gr){
            frUser.setText(gr.getGroupname());
            //     User u = dbHelper.getUser(fr.getUid());
            //      frUser.setText(u.getUsername());
            //  tvItemName.setText(groceryPost.getGroceryItem().getTitle());
            //      Glide.with(context).load(groceryPost.getGroceryItem().getImageUrl()).into(ivGroceryItemImage);
        }
    }
}