package org.berendeev.roma.sciencealarm.di;

import android.content.Context;

import org.berendeev.roma.sciencealarm.data.AlarmRepositoryImpl;
import org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.sciencealarm.domain.AlarmRepository;

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

    @Singleton
    @Provides
    public AlarmRepository provideAlarmRepository(DatabaseOpenHelper openHelper){
        return new AlarmRepositoryImpl(openHelper);
    }
}
