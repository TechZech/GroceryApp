package com.groceryapp.upcdata.DB.UserGroupItem;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;

public class UserGroupItem {
    private Group ugiGroup = null;
    private User ugiUser = null;

    public Group getUgiGroup() {
        return ugiGroup;
    }

    public void setUgiGroup(Group ugiGroup) {
        this.ugiGroup = ugiGroup;
    }

    public User getUgiUser() {
        return ugiUser;
    }

    public void setUgiUser(User ugiUser) {
        this.ugiUser = ugiUser;
    }

    public GroceryPost getUgiGP() {
        return ugiGP;
    }

    public void setUgiGP(GroceryPost ugiGP) {
        this.ugiGP = ugiGP;
    }

    public GroceryItem getUgiGI() {
        return ugiGI;
    }

    public void setUgiGI(GroceryItem ugiGI) {
        this.ugiGI = ugiGI;
    }

    private GroceryPost ugiGP = null;
    private GroceryItem ugiGI = null;
}
