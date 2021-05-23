package com.groceryapp.upcdata.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class GroceryPostAdapter extends RecyclerView.Adapter<GroceryPostAdapter.ViewHolder> {

    private Context context;
    private List<GroceryPost> groceryPosts;
    private RelativeLayout rl;
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }
    private final OnClickListener onClickListener;


    public GroceryPostAdapter(Context context, List<GroceryPost> groceryPosts, OnClickListener onClickListener){
        this.context = context;
        this.groceryPosts = groceryPosts;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("GroceryItemAdapter", "OnBindViewHolder" + position);
        GroceryPost groceryPost = groceryPosts.get(position);
        holder.bind(groceryPost);
    }

    public void clear(){
        groceryPosts.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<GroceryPost> list){
        groceryPosts.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groceryPosts.size();
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
            rl = itemView.findViewById(R.id.post_container);
            frImage = itemView.findViewById(R.id.tvImage);
            //  item_grocery_container = itemView.findViewById(R.id.item_grocery_container);
            dbHelper = new DBHelper();



        }

        public void bind(GroceryPost groceryPost){
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(getAdapterPosition());
                }
            });
            //     User u = dbHelper.getUser(fr.getUid());
            //      frUser.setText(u.getUsername());
            //  tvItemName.setText(groceryPost.getGroceryItem().getTitle());
            //      Glide.with(context).load(groceryPost.getGroceryItem().getImageUrl()).into(ivGroceryItemImage);
        }
    }
}