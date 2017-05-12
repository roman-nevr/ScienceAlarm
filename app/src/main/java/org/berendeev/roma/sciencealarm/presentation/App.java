package org.berendeev.roma.sciencealarm.presentation;

import android.app.Application;

import org.berendeev.roma.sciencealarm.di.DaggerMainComponent;
import org.berendeev.roma.sciencealarm.di.MainComponent;
import org.berendeev.roma.sciencealarm.di.MainModule;


public class App extends Application {

    private static App instance;
    private MainComponent mainComponent;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        initDi();
    }

    private void initDi() {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
    }

    public static App getInstance() {
        return instance;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
