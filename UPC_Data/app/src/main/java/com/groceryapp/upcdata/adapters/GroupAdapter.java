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

import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private final OnClickListener clickListener;
    private Context context;
    private List<Group> groupList;
    private RelativeLayout rl;

    public interface OnClickListener {
        void onItemClicked(int position);
    }
    public GroupAdapter(Context context, List<Group> groupList, GroupAdapter.OnClickListener clickListener){
        this.context = context;
        this.groupList = groupList;
        this.clickListener = clickListener;
    }
    public GroupAdapter(OnClickListener clickListener, Context context, List<Group> groupList){
        this.clickListener = clickListener;
        this.context = context;
        this.groupList = groupList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("FriendRequestAdapter", "OnBindViewHolder" + position);
        Group gr = groupList.get(position);
        holder.bind(gr);
    }

    public void clear(){
        groupList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Group> list){
        groupList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groupList.size();
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

        public void bind(Group gr){
            frUser.setText(gr.getGroupname());
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