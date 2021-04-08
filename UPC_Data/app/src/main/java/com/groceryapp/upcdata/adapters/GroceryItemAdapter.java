package com.groceryapp.upcdata.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.R;

import java.util.List;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    private Context context;
    private List<GroceryItem> groceryItems;

    public GroceryItemAdapter(Context context, List<GroceryItem> groceryItems){
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grocery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("GroceryItemAdapter", "OnBindViewHolder" + position);
        GroceryItem groceryItem = groceryItems.get(position);
        holder.bind(groceryItem);
    }

    public void clear(){
        groceryItems.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<GroceryItem> list){
        groceryItems.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUPC;
        private TextView tvTitle;
        private ImageView ivGroceryItemImage;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvUPC = itemView.findViewById(R.id.tvUPCcode);
            tvTitle = itemView.findViewById(R.id.tvGroceryName);
            ivGroceryItemImage = itemView.findViewById(R.id.ivGroceryItemImage);
        }

        public void bind(GroceryItem groceryItem){
            tvUPC.setText(groceryItem.getUpc());
            tvTitle.setText(groceryItem.getTitle());
        }
    }
}
