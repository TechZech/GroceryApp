package com.groceryapp.upcdata.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.groceryapp.upcdata.DB.Friend.Comment;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private Context context;
    private List<Comment> commentsList;

    public CommentsAdapter(Context context, List<Comment> commentsList){
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Log.d("CommentsAdapter", "OnBindViewHolder" + position);
        Comment comment = commentsList.get(position);
        holder.bind(comment);
    }

    public void clear(){
        commentsList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Comment> list){
        commentsList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView commentTextView;
        private CircleImageView ivUserCommentPic;

    public ViewHolder(@NonNull View itemView){
        super(itemView);

        commentTextView = itemView.findViewById(R.id.tvCommentText);
        ivUserCommentPic = itemView.findViewById(R.id.ivCommentUserPic);
    }

    public void bind(Comment comment){
        commentTextView.setText(comment.getCommentText());
        Glide.with(context).load(comment.getUser().getProfilePhotoURL()).into(ivUserCommentPic);

        ivUserCommentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    }
}
