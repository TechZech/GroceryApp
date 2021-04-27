package com.groceryapp.upcdata.DB.User;


public class FriendRequest {
    private String uid;


        public FriendRequest(){
        }

        public FriendRequest(String uid) {
            this.uid = uid;
        }

        @Override
        public String toString() {
            return "FriendRequest{" +
                    ", uid='" + uid + '\'' +
                    '}';
        }


    public String getUserName() {
        return uid;
    }

    public void setUserName(String userName) {
        this.uid = uid;
    }
}
