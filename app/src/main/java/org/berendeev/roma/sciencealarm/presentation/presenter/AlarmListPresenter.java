package org.berendeev.roma.sciencealarm.presentation.presenter;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.activity.AlarmListActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class AlarmListPresenter {

    private final CompositeDisposable compositeDisposable;
    private AlarmListActivity view;
    private List<Alarm> alarms;

    @Inject
    public AlarmListPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    public void start(){
        alarms = new ArrayList<>();
        Alarm alarm = Alarm.create(1, "", 0, true);
        Alarm alarm2 = Alarm.create(2, "", 610 * 1000, true);
        Alarm alarm3 = Alarm.create(3, "", 610 * 1000, false);
        alarms.add(alarm);
        alarms.add(alarm2);
        alarms.add(alarm3);
        view.setAlarms(alarms);
    }

    public void stop(){
        compositeDisposable.clear();
    }

    public boolean onLongClick(){
        return true;
    }

    public void onAlarmClick(int id){
        Alarm alarm = alarms.get(id);
        alarm = alarm.toBuilder().isStarted(!alarm.isStarted()).build();
        alarms.set(id, alarm);
        view.setAlarms(alarms);
    }

    public void setView(AlarmListActivity view) {
        this.view = view;
    }

    public void onFabClick() {
        alarms.add(createAlarm());
        view.setAlarms(alarms);
    }

    private Alarm createAlarm(){
        return Alarm.create(alarms.size(), "", 0, true);
    }

    public void onAlarmSwiped(int adapterPosition) {
        alarms.remove(adapterPosition);
        view.setAlarms(alarms);
    }
}
