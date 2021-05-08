package com.groceryapp.upcdata.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

public class FriendDetailFragment extends Fragment {

    public static final String TAG = "FriendDetailFragment";
    ImageView ivFriendPic;
    TextView tvFriendName, tvFriendEmail;
    Button btnRemoveFriend;
    User user = new User();
    DBHelper dbHelper = new DBHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivFriendPic = view.findViewById(R.id.ivFriendPic);
        tvFriendEmail = view.findViewById(R.id.tvFriendEmail);
        tvFriendName = view.findViewById(R.id.tvFriendName);
        btnRemoveFriend = view.findViewById(R.id.btnRemoveFriend);

        unpackBundle();

        tvFriendName.setText(user.getUsername());
        tvFriendEmail.setText(user.getEmail());

        btnRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteFriend(user.getUserID());
            }
        });

    }

    private void unpackBundle(){
        Bundle Args = getArguments();
        user.setEmail(Args.getString("email"));
        user.setUserID(Args.getString("userID"));
        user.setUsername(Args.getString("username"));
    }
}
