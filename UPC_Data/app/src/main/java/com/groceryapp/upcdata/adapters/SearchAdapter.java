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

import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final OnClickListener clickListener;
    private Context context;
    private List<Object> searchResultList;
    private RelativeLayout rl;

    public SearchAdapter(Context context, List<Object> searchResultList, OnClickListener clickListener){
        this.context = context;
        this.searchResultList = searchResultList;
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
        rl = view.findViewById(R.id.post_container);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Object obj = searchResultList.get(position);
        if(obj.getClass().equals("User")){
            //got user in search
            holder.bind((User) obj);
        }
        else if(obj.getClass().equals("Group")){
            holder.bind((Group) obj);
        }
    }


    public void clear(){
        searchResultList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Object> list){
        searchResultList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
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


        public void bind(Object fr){
            Log.d("BIND","BIND");


            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            //     User u = dbHelper.getUser(fr.getUid());
            //      frUser.setText(u.getUsername());
            //  tvItemName.setText(groceryPost.getGroceryItem().getTitle());
            //      Glide.with(context).load(groceryPost.getGroceryItem().getImageUrl()).into(ivGroceryItemImage);
        }
    }
}