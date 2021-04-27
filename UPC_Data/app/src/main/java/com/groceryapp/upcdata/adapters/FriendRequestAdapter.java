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

import com.groceryapp.upcdata.DB.User.FriendRequest;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private Context context;
    private List<FriendRequest> friendRequestList;

    public FriendRequestAdapter(Context context, List<FriendRequest> friendRequestList){
        this.context = context;
        this.friendRequestList = friendRequestList;
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
        Log.d("FriendRequestAdapter", "OnBindViewHolder" + position);
        FriendRequest fr = friendRequestList.get(position);
        holder.bind(fr);
    }

    public void clear(){
        friendRequestList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<FriendRequest> list){
        friendRequestList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView frImage;
        private TextView frUser;
        private Button acceptButton;
        private DBHelper dbHelper;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            frUser = itemView.findViewById(R.id.tvUser);

            frImage = itemView.findViewById(R.id.tvImage);
          //  item_grocery_container = itemView.findViewById(R.id.item_grocery_container);
            dbHelper = new DBHelper();
            acceptButton = itemView.findViewById(R.id.acceptButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    dbHelper.acceptFriendRequest(friendRequestList.get(getAdapterPosition()).getFriendItem());
                }
            });

        }

        public void bind(FriendRequest fr){
            frUser.setText(fr.getUserName());
          //  tvItemName.setText(groceryPost.getGroceryItem().getTitle());
      //      Glide.with(context).load(groceryPost.getGroceryItem().getImageUrl()).into(ivGroceryItemImage);
        }
    }
}