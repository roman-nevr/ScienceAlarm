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
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.TOGGLE;

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

        router.toggleAlarm(id);
    }

    public void setView(AlarmListView view) {
        this.view = view;
    }

    public void onFabClick() {
//        alarms.add(createAlarm());
//        view.setAlarms(alarms);
        serviceIntent.putExtra(COMMAND, ADD_NEW);
        serviceIntent.putExtra(TIME, 3000);
        router.addNewAlarm(3000);
    }

    private Alarm createAlarm(){
        return Alarm.create(alarms.size(), "", 0, true);
    }

    public void onAlarmSwiped(long id) {
        router.removeAlarm(id);
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
