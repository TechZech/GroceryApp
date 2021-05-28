package com.groceryapp.upcdata.adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DB.UserGroupItem.UserGroupItem;
import com.groceryapp.upcdata.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by User on 1/17/2018.
 */

public class UserGroupItemAdapter extends RecyclerView.Adapter<UserGroupItemAdapter.ViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";
    private User ugiaUser  = null;
    private Group ugiaGroup  = null;
    private GroceryPost ugiaGroceryPost  = null;
    private GroceryItem ugiaGroceryItem  = null;
    private UserGroupItem userGroupItem = null;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public UserGroupItemAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
    }
    public UserGroupItemAdapter(Context context, UserGroupItem userGroupItem) {
        this.userGroupItem = userGroupItem;
        if(userGroupItem.getUgiUser()!=null){
            this.ugiaUser=this.userGroupItem.getUgiUser();
        }
        else if(userGroupItem.getUgiGroup()!=null){
            this.ugiaGroup=this.userGroupItem.getUgiGroup();
        }
        else if(userGroupItem.getUgiGP()!=null){
            this.ugiaGroceryPost=this.userGroupItem.getUgiGP();
        }
        else if(userGroupItem.getUgiGI()!=null){
            this.ugiaGroceryItem=this.userGroupItem.getUgiGI();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        if(this.userGroupItem.getUgiUser()!=null){
            Glide.with(mContext)
                    .load(mImageUrls.get(position))
                    .apply(requestOptions)
                    .into(holder.image);

            holder.name.setText(mNames.get(position));
        }
        else if(this.userGroupItem.getUgiGroup()!=null){
            Glide.with(mContext)
                    .load(mImageUrls.get(position))
                    .apply(requestOptions)
                    .into(holder.image);

            holder.name.setText(mNames.get(position));
        }
        else if(this.userGroupItem.getUgiGP()!=null){
            Glide.with(mContext)
                    .load(mImageUrls.get(position))
                    .apply(requestOptions)
                    .into(holder.image);

            holder.name.setText(mNames.get(position));
        }
        else if(this.userGroupItem.getUgiGI()!=null){
            Glide.with(mContext)
                    .load(mImageUrls.get(position))
                    .apply(requestOptions)
                    .into(holder.image);

            holder.name.setText(mNames.get(position));
        }



/*
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.stImage);
            this.name = itemView.findViewById(R.id.stText);
        }
    }
}