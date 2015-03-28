package com.unccstudio.gems.gems;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Administrator on 3/25/2015.
 */
public class ParseApplication extends Application {
    static final String APPLICATION_ID = "Djwu1TRmCeQfU2vUV5yaFedwTzmyZMoxWSo3yO4i";
    static final String CLIENT_KEY = "rbm6j1oVXujyfCmGdq3hCJLOdWhuTqRuBgbiZrNS";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
//        ParseObject.registerSubclass(HistoryParser.class);
//        ParseObject.registerSubclass(TodoListParser.class);
//        ParseObject.registerSubclass(FriendListParser.class);
//        ParseObject.registerSubclass(SharedListParser.class);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }
}

