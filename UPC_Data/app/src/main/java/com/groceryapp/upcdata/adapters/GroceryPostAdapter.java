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
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class GroceryPostAdapter extends RecyclerView.Adapter<GroceryPostAdapter.ViewHolder> {

    private Context context;
    private List<GroceryPost> groceryPosts;

    public GroceryPostAdapter(Context context, List<GroceryPost> groceryPosts){
        this.context = context;
        this.groceryPosts = groceryPosts;
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

        private ImageView ivGroceryItemImage;
        private RelativeLayout item_grocery_container;
        private DBHelper dbHelper;
        private TextView tvUser;
        private TextView tvListName;
        private TextView tvItemName;
        private Button addButton;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvListName = itemView.findViewById(R.id.tvListName);
            tvItemName = itemView.findViewById(R.id.tvItemName);

            ivGroceryItemImage = itemView.findViewById(R.id.tvImage);
            item_grocery_container = itemView.findViewById(R.id.item_grocery_container);
            dbHelper = new DBHelper();
            addButton = itemView.findViewById(R.id.acceptButton);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    groceryPosts.get(getAdapterPosition()).getGroceryItem().setQuantity(1);
                    dbHelper.addGroceryItem(groceryPosts.get(getAdapterPosition()).getGroceryItem());
                }
            });

        }

        public void bind(GroceryPost groceryPost){
            tvUser.setText(groceryPost.user.getUsername());
            tvItemName.setText(groceryPost.getGroceryItem().getTitle());
            if (groceryPost.getWhichList())
                tvListName.setText("Inventory");
            else
                tvListName.setText("Grocery List");
            Glide.with(context).load(groceryPost.getGroceryItem().getImageUrl()).into(ivGroceryItemImage);
        }
    }
}