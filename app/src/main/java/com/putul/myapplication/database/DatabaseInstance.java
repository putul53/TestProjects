package com.putul.myapplication.database;

import static com.putul.myapplication.database.DatabaseKeys.database_name;

import android.content.Context;
import androidx.room.Room;

public class DatabaseInstance {
        private Context mCtx;
        private static DatabaseInstance mInstance;

        //our app database object
        private AppDatabase appDatabase;

        private DatabaseInstance(Context mCtx) {
            this.mCtx = mCtx;

            //creating the app database with Room database builder
            appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, database_name)
                    .build();//.addMigrations(MIGRATION)
        }

        public static synchronized DatabaseInstance getInstance(Context mCtx) {
            if (mInstance == null) {
                mInstance = new DatabaseInstance(mCtx);
            }
            return mInstance;
        }

        public AppDatabase getAppDatabase() {
            return appDatabase;
        }

}
