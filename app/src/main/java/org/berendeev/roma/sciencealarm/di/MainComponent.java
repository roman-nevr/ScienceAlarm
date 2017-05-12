package org.berendeev.roma.sciencealarm.di;

import org.berendeev.roma.sciencealarm.presentation.activity.AlarmListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(AlarmListActivity alarmListActivity);
}
