package com.groceryapp.upcdata.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private final FriendListAdapter.OnClickListener clickListener;
    private Context context;
    private List<Friend> friends;
    private RelativeLayout rl;

    public FriendListAdapter(Context context, List<Friend> friendRequestList, OnClickListener clickListener){
        this.context = context;
        this.friends = friendRequestList;
        this.clickListener = clickListener;
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
        View view = LayoutInflater.from(context).inflate(R.layout.friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("FriendRequestAdapter", "OnBindViewHolder" + position);
        Friend fr = friends.get(position);
        holder.bind(fr);
    }

    public void clear(){
        friends.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Friend> list){
        friends.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView frImage;
        private TextView frUser;
        private DBHelper dbHelper;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            frUser = itemView.findViewById(R.id.tvUser);

            frImage = itemView.findViewById(R.id.tvImage);
            //  item_grocery_container = itemView.findViewById(R.id.item_grocery_container);
            dbHelper = new DBHelper();
            rl = itemView.findViewById(R.id.post_container);




        }

        public void bind(Friend fr){
            frUser.setText(fr.getusername());
            //     User u = dbHelper.getUser(fr.getUid());
            //      frUser.setText(u.getUsername());
            //  tvItemName.setText(groceryPost.getGroceryItem().getTitle());
            //      Glide.with(context).load(groceryPost.getGroceryItem().getImageUrl()).into(ivGroceryItemImage);
            dbHelper.getUserFromUid(fr.getuserID(), new DBHelper.MyCallback() {
                @Override
                public void onCallback(String value) {
                    frUser.setText(value);
                }
            });

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}