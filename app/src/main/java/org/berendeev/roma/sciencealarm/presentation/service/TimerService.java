package org.berendeev.roma.sciencealarm.presentation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.berendeev.roma.sciencealarm.domain.AlarmRepository;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.App;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class TimerService extends Service implements BaseColumns {

    @Inject AlarmRepository alarmRepository;

    public static final int TIME_STEP = 500;
    public static final String COMMAND = "command";
    public static final String NEW_ALARMS = "new_alarms";
    public static final String TIME = "time";
    public static final String TIMER_SERVICE_BROADCAST_ACTION = "org.berendeev.roma.sciencealarm.presentation.service.alarms";

    public static final int ADD_NEW = 1;
    public static final int REMOVE = 2;
    public static final int TOGGLE = 3;
    public static final int STOP = 4;

    private MyBinder binder = new MyBinder();
    private List<Alarm> alarms;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Intent newAlarmsIntent;
    private Timer timer;

    @Override public void onCreate() {
        super.onCreate();
        initDi();
        createNewAlarmsIntent();
        subscribeOnAlarms();
        startTimer();
    }

    private void initDi() {
        App.getInstance().getMainComponent().inject(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override public void run() {
                for (int index = 0; index < alarms.size(); index++) {
                    Alarm alarm = alarms.get(index);
                    if (alarm.isStarted()){
                        alarm = alarm.toBuilder().time(alarm.time() + TIME_STEP).build();
                        alarms.set(index, alarm);
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, TIME_STEP, TIME_STEP);
    }

    private void createNewAlarmsIntent() {
        newAlarmsIntent = new Intent(TIMER_SERVICE_BROADCAST_ACTION);
        newAlarmsIntent.putExtra(NEW_ALARMS, 1);
    }

    private void subscribeOnAlarms() {
        compositeDisposable.add(alarmRepository
                .getAllAlarms()
                .subscribe(alarms -> {
                    this.alarms = alarms;
                    LocalBroadcastManager.getInstance(this).sendBroadcast(newAlarmsIntent);
                }));
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(COMMAND, -1);

        switch (command) {
            case ADD_NEW: {
                int time = intent.getIntExtra(TIME, -1);
                if (time != -1) {
                    Alarm alarm = Alarm.create(-1, "", time, true);
                    addNew(alarm);
                }
                break;
            }
            case REMOVE: {
                int id = intent.getIntExtra(_ID, -1);
                if (id != -1) {
                    remove(id);
                }
            }
            case TOGGLE:{
                int id = intent.getIntExtra(_ID, -1);
                for (int index = 0; index < alarms.size(); index++) {
                    if (alarms.get(index).id() == id){
                        Alarm alarm = alarms.get(index);
                        alarm = alarm.toBuilder().isStarted(!alarm.isStarted()).build();
                        alarms.set(index, alarm);
                        alarmRepository.saveAlarm(alarm);
                    }
                }
            }
        }

        return START_STICKY;
    }

    private void remove(int id) {
        alarmRepository.deleteAlarm(id).subscribe();
    }

    private void addNew(Alarm alarm) {
        alarmRepository.saveAlarm(alarm).subscribe();
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public class MyBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }
}