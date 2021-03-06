package org.berendeev.roma.sciencealarm.presentation.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.App;
import org.berendeev.roma.sciencealarm.presentation.activity.AlarmListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;


public class TimerService extends Service implements BaseColumns {

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

    private MediaPlayer player;
    private List<Alarm> alarms;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Intent newAlarmsIntent;
    private Timer timer;
    private boolean isBinded;

    @Override public void onCreate() {
        super.onCreate();
//        initDi();
        initAlarms();
        createNewAlarmsIntent();
        startTimer();
    }

    private void initAlarms() {
        alarms = new ArrayList<>();
    }

    private void initDi() {
        App.getInstance().getMainComponent().inject(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
        if (player != null){
            player.release();
        }
    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override public void run() {
                for (int index = 0; index < alarms.size(); index++) {
                    decreaseTimer(index);
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, TIME_STEP, TIME_STEP);
    }

    private void decreaseTimer(int index){
        Alarm alarm = alarms.get(index);
        if (alarm.isStarted()){

            alarm = alarm.toBuilder().time(alarm.time() - TIME_STEP).build();
            alarms.set(index, alarm);

            if (alarm.time() <= 0){
                stopAlarmTimer(alarm, index);
                if (!isBinded){
                    startMyActivity();
                }
            }
        }
    }

    private void stopAlarmTimer(Alarm alarm, int index){
        if(alarm.isStarted()){
            soundOn();
        }
        alarm = alarm.toBuilder().isStarted(false).build();
        alarms.set(index, alarm);
    }

    private void startMyActivity(){
        Intent intent = new Intent(TimerService.this, AlarmListActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void createNewAlarmsIntent() {
        newAlarmsIntent = new Intent(TIMER_SERVICE_BROADCAST_ACTION);
        newAlarmsIntent.putExtra(NEW_ALARMS, 1);
    }

    private void notifyDataSetChanges() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(newAlarmsIntent);
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        isBinded = true;
        return binder;
    }

    @Override public boolean onUnbind(Intent intent) {
        isBinded = false;
        return super.onUnbind(intent);
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(COMMAND, -1);

        switch (command) {
            case ADD_NEW: {
                int time = intent.getIntExtra(TIME, -1);
                if (time != -1) {
                    long id = 0;
                    if(alarms.size() != 0){
                        id = alarms.get(alarms.size() - 1).id() + 1;
                    }
                    Alarm alarm = Alarm.create(id, "", time, true);
                    addNew(alarm);
                }
                break;
            }
            case REMOVE: {
                long id = intent.getLongExtra(_ID, -1);
                if (id != -1) {
                    remove(id);
                    if (isNeedToStopPlayer()){
                        soundOff();
                    }
                }
            }
            case TOGGLE:{
                long id = intent.getLongExtra(_ID, -1);
                for (int index = 0; index < alarms.size(); index++) {
                    if (alarms.get(index).id() == id){
                        Alarm alarm = alarms.get(index);
                        alarm = alarm.toBuilder().isStarted(!alarm.isStarted()).build();
                        alarms.set(index, alarm);
                    }
                }
            }
        }

        return START_STICKY;
    }

    private void remove(long id) {

        alarms.remove(indexOfAlarm(id));
        notifyDataSetChanges();
    }

    private void addNew(Alarm alarm) {
        alarms.add(alarm);
        notifyDataSetChanges();
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public class MyBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    private int indexOfAlarm(long id){
        for (int index = 0; index < alarms.size(); index++) {
            if(alarms.get(index).id() == id){
                return index;
            }
        }
        return -1;
    }

    private void soundOn(){
        if(player == null){
            player = MediaPlayer.create(this,
                    Settings.System.DEFAULT_RINGTONE_URI);
            player.start();
        }else if (!player.isPlaying()){
            player.start();
        }
    }

    private void soundOff(){
        player.pause();
        player.seekTo(0);
    }

    private boolean isNeedToStopPlayer(){
        if(player == null || !player.isPlaying()){
            return false;
        }
        boolean needToStop = true;
        for (Alarm alarm : alarms) {
            if (alarm.time() <= 0){
                needToStop = false;
            }
        }
        return needToStop;
    }
}
