package org.berendeev.roma.sciencealarm.di;

import android.content.Context;

import org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public DatabaseOpenHelper provideDatabaseOpenHelper(Context context){
        return new DatabaseOpenHelper(context);
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }
}
