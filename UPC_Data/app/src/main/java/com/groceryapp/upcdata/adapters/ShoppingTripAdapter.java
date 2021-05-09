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
import com.groceryapp.upcdata.DB.ShoppingTrip.ShoppingTrip;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class ShoppingTripAdapter extends RecyclerView.Adapter<ShoppingTripAdapter.ViewHolder>{

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    ShoppingTripAdapter.OnClickListener clickListener;
    private Context context;
    private List<ShoppingTrip> shoppingTrips;

    public ShoppingTripAdapter(Context context, List<ShoppingTrip> shoppingTrips, ShoppingTripAdapter.OnClickListener clickListener){
        this.context = context;
        this.shoppingTrips = shoppingTrips;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingTripAdapter.ViewHolder holder, int position) {
        ShoppingTrip shoppingTrip = shoppingTrips.get(position);
        holder.bind(shoppingTrip);
    }

    @Override
    public int getItemCount() {
        return shoppingTrips.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout item_trip_container;
        private TextView tvPrice;
        private TextView tvDate;
        private TextView tvCost;

        private DBHelper dbHelper;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            item_trip_container = itemView.findViewById(R.id.item_trip_container);

            dbHelper = new DBHelper();
            tvDate = itemView.findViewById(R.id.tvShoppingTripDate);
            tvCost = itemView.findViewById(R.id.tvShoppingTripCost);
        }

        public void bind(ShoppingTrip shoppingTrip){
            String modifiedDate = shoppingTrip.getDate().toString().substring(0, 10) + ", " + shoppingTrip.getDate().toString().substring(24);
            tvDate.setText(modifiedDate);
            tvCost.setText("$" + String.valueOf(shoppingTrip.getTotalPrice()).substring(0, 5));

            item_trip_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

        }
    }
}
