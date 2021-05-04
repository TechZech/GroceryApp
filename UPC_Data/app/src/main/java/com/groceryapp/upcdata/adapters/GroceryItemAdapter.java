package com.groceryapp.upcdata.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    private Context context;
    private List<GroceryItem> groceryItems;

    public GroceryItemAdapter(Context context, List<GroceryItem> groceryItems, OnLongClickListener longClickListener, OnClickListener clickListener){
        this.context = context;
        this.groceryItems = groceryItems;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
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

        private TextView tvQuantity;
        private TextView tvTitle;
        private ImageView ivGroceryItemImage;
        private RelativeLayout item_grocery_container;
        private TextView tvPrice;

        private ImageView QuantityAdd;
        private ImageView QuantitySubtract;
        private DBHelper dbHelper;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTitle = itemView.findViewById(R.id.tvGroceryName);
            ivGroceryItemImage = itemView.findViewById(R.id.tvImage);
            ViewCompat.setTransitionName(ivGroceryItemImage, "grocery_item_image");
            item_grocery_container = itemView.findViewById(R.id.item_grocery_container);
            QuantityAdd = itemView.findViewById(R.id.ivPlusSign);
            QuantitySubtract = itemView.findViewById(R.id.ivMinusSign);
            dbHelper = new DBHelper();
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }

        public void bind(GroceryItem groceryItem){
            String strQuantity = "";
            strQuantity = strQuantity.valueOf(groceryItem.getQuantity());

            tvTitle.setText(groceryItem.getTitle());
            tvQuantity.setText(strQuantity);
            tvPrice.setText(groceryItem.getPrice());

            Glide.with(context).load(groceryItem.getImageUrl()).into(ivGroceryItemImage);

            item_grocery_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "OnLongClick", Toast.LENGTH_SHORT);
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

            item_grocery_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            QuantityAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    groceryItem.setQuantity(groceryItem.getQuantity()+1);
                    dbHelper.UpdateGroceryListQuantity(groceryItem);
                    dbHelper.UpdateInventoryQuantity(groceryItem);
                    notifyDataSetChanged();
                }
            });

            QuantitySubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (groceryItem.getQuantity() > 0){
                        groceryItem.setQuantity(groceryItem.getQuantity()-1);
                        dbHelper.UpdateGroceryListQuantity(groceryItem);
                        dbHelper.UpdateInventoryQuantity(groceryItem);
                        notifyDataSetChanged();
                        }
                    if (groceryItem.getQuantity() == 0){
                        dbHelper.removeInventoryItem(groceryItem);
                        dbHelper.addGroceryItem(groceryItem);
                        notifyDataSetChanged();

                    }

                }
            });
        }
    }
}
