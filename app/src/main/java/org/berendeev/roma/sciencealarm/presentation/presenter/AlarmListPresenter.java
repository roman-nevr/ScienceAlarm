package org.berendeev.roma.sciencealarm.presentation.presenter;

import android.content.Intent;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.AlarmListView;
import org.berendeev.roma.sciencealarm.presentation.activity.AlarmListActivity;
import org.berendeev.roma.sciencealarm.presentation.service.TimerService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static android.provider.BaseColumns._ID;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.ADD_NEW;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.COMMAND;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.REMOVE;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.TIME;

public class AlarmListPresenter {

    private final CompositeDisposable compositeDisposable;
    private AlarmListView view;
    private List<Alarm> alarms;
    private TimerService service;
    private AlarmListView.Router router;
    private Intent serviceIntent;

    @Inject
    public AlarmListPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    public void start(){
//        alarms = new ArrayList<>();
//        Alarm alarm = Alarm.create(1, "", 0, true);
//        Alarm alarm2 = Alarm.create(2, "", 610 * 1000, true);
//        Alarm alarm3 = Alarm.create(3, "", 610 * 1000, false);
//        alarms.add(alarm);
//        alarms.add(alarm2);
//        alarms.add(alarm3);
//        view.setAlarms(alarms);
        router.sendCommand(serviceIntent);
    }

    public void stop(){
        compositeDisposable.clear();
    }

    public boolean onLongClick(long id){
        return true;
    }

    public void onAlarmClick(long id){
//        alarm = alarm.toBuilder().isStarted(!alarm.isStarted()).build();
//        alarms.set(id, alarm);
//        view.setAlarms(alarms);
    }

    public void setView(AlarmListView view) {
        this.view = view;
    }

    public void onFabClick() {
//        alarms.add(createAlarm());
//        view.setAlarms(alarms);
        serviceIntent.putExtra(COMMAND, ADD_NEW);
        serviceIntent.putExtra(TIME, 10000);
        router.sendCommand(serviceIntent);
    }

    private Alarm createAlarm(){
        return Alarm.create(alarms.size(), "", 0, true);
    }

    public void onAlarmSwiped(long id) {
//        alarms.remove(adapterPosition);
//        view.setAlarms(alarms);
        serviceIntent.putExtra(COMMAND, REMOVE);
        serviceIntent.putExtra(_ID, id);
        router.sendCommand(serviceIntent);
    }

    public void setService(TimerService service) {
        this.service = service;
    }

    public void setRouter(AlarmListView.Router router) {
        this.router = router;
    }

    public void setServiceIntent(Intent serviceIntent) {
        this.serviceIntent = serviceIntent;
    }
}
