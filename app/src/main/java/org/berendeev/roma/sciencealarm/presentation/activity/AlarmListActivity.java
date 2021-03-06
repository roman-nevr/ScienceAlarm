package org.berendeev.roma.sciencealarm.presentation.activity;

import android.app.PendingIntent;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import org.berendeev.roma.sciencealarm.R;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.AlarmListView.Router;
import org.berendeev.roma.sciencealarm.presentation.adapter.AlarmListAdapter;
import org.berendeev.roma.sciencealarm.presentation.AlarmListView;
import org.berendeev.roma.sciencealarm.presentation.App;
import org.berendeev.roma.sciencealarm.presentation.adapter.SwipeHelper;
import org.berendeev.roma.sciencealarm.presentation.presenter.AlarmListPresenter;
import org.berendeev.roma.sciencealarm.presentation.service.TimerService;
import org.berendeev.roma.sciencealarm.presentation.view.DurationPicker;
import org.berendeev.roma.sciencealarm.presentation.view.DurationPicker.TimeSetListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.ADD_NEW;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.COMMAND;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.REMOVE;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.TIME;
import static org.berendeev.roma.sciencealarm.presentation.service.TimerService.TOGGLE;

public class AlarmListActivity extends AppCompatActivity implements AlarmListView, Router {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Inject AlarmListPresenter presenter;

    private AlarmListAdapter adapter;
    private SwipeHelper swipeHelper;
    private BroadcastReceiver broadcastReceiver;
    private ServiceConnection serviceConnection;
    private TimerService timerService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            presenter.onFabClick();
        });
        initDi();
//        setupUi();
        registerTimerReceiver();
    }

    private void initDi() {
        App.getInstance().getMainComponent().inject(this);
        presenter.setView((AlarmListView)this);
        presenter.setRouter((Router)this);
        serviceIntent = new Intent(this, TimerService.class);
        presenter.setServiceIntent(serviceIntent);
        swipeHelper = new SwipeHelper(presenter);

    }

    private void setupUi(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override protected void onStart() {
        super.onStart();
        presenter.start();

    }

    @Override protected void onResume() {
        super.onResume();
        bindTimerService();
    }

    private void bindTimerService() {
        serviceConnection = new ServiceConnection() {
            @Override public void onServiceConnected(ComponentName name, IBinder service) {
                timerService = ((TimerService.MyBinder) service).getService();
                presenter.setService(timerService);
                setAlarms(timerService.getAlarms());
            }

            @Override public void onServiceDisconnected(ComponentName name) {
                //wtf
            }
        };
        bindService(serviceIntent, serviceConnection, 0);
    }

    private void registerTimerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                if(timerService != null){
                    setAlarms(timerService.getAlarms());
                }
            }
        };

        IntentFilter intFilt = new IntentFilter(TimerService.TIMER_SERVICE_BROADCAST_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intFilt);
    }

    @Override protected void onPause() {
        super.onPause();
        unbindTimerService();
    }

    private void unbindTimerService() {
        if(serviceConnection != null){
            unbindService(serviceConnection);
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_finish) {
            setAlarms(new ArrayList<>());
            stopService(serviceIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void setAlarms(List<Alarm> alarms) {
        if(adapter == null){
            adapter = new AlarmListAdapter(alarms, presenter, this);
            recyclerView.setAdapter(adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }else {
            adapter.update(alarms);
        }
    }

    @Override public void addNewAlarm(int time) {
        startService(addIntent(time));
    }

    @Override public void removeAlarm(long id) {
        startService(removeIntent(id));
    }

    @Override public void toggleAlarm(long id) {
        startService(toggleIntent(id));
    }

    @Override public void showNewAlarmDialog() {
        TimeSetListener listener = new TimeSetListener() {
            @Override public void onTimeSet(TimePicker view, int minute, int second) {
                presenter.addNewAlarm(minute, second);
            }
        };
        DurationPicker picker = DurationPicker.newInstance(this, listener, 0, 0);
        picker.show();
    }

    private Intent addIntent(int time){
//        Intent intent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(COMMAND, ADD_NEW);
        serviceIntent.putExtra(TIME, time);
        return serviceIntent;
    }

    private Intent removeIntent(long id){
//        Intent intent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(COMMAND, REMOVE);
        serviceIntent.putExtra(_ID, id);
        return serviceIntent;
    }

    private Intent toggleIntent(long id){
//        Intent intent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(COMMAND, TOGGLE);
        serviceIntent.putExtra(_ID, id);
        return serviceIntent;
    }
}
