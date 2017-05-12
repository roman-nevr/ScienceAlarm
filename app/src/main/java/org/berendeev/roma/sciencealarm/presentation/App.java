package org.berendeev.roma.sciencealarm.presentation;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.berendeev.roma.sciencealarm.BuildConfig;
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
        initStetho();
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

    private void initStetho(){
        if(!BuildConfig.DEBUG){
            return;
        }
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }
}
