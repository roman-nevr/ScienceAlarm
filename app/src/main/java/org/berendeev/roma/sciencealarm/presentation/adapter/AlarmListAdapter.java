package org.berendeev.roma.sciencealarm.presentation.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.berendeev.roma.sciencealarm.R;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.entity.AlarmViewModel;
import org.berendeev.roma.sciencealarm.presentation.presenter.AlarmListPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.NO_POSITION;


public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmHolder> {

    public static final int TIME_STEP = 500;
    private List<Alarm> alarms;
    private AlarmListPresenter presenter;
    private Activity activity;
    private int normalColor;
    private int flashColor;


    public AlarmListAdapter(List<Alarm> alarms, AlarmListPresenter presenter, Activity activity) {
        this.alarms = (alarms);
        this.presenter = presenter;
        this.activity = activity;
        normalColor = ContextCompat.getColor(activity.getApplicationContext(), android.R.color.tertiary_text_dark);
        flashColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.flashColor);
    }

    @Override public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new AlarmHolder(view);
    }

    @Override public void onBindViewHolder(AlarmHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        Pair<String, String> pair = parseTime(alarm.time());
        holder.minutes.setText(pair.first);
        holder.seconds.setText(pair.second);
        holder.id = alarm.id();
        holder.startTimer();
    }

    @Override public int getItemCount() {
        return alarms.size();
    }

    public void update(List<Alarm> newAlarms) {
        alarms = newAlarms;
        notifyDataSetChanged();
    }

    private Pair<String, String> parseTime(int time) {

        String secondsBuilder = String.valueOf(time % 60000 / 10000) +
                time % 60000 % 10000 / 1000;

        String minutesBuilder = String.valueOf(time / 60000 / 10) +
                time / 60000 % 10;

        return new Pair<>(minutesBuilder, secondsBuilder);
    }

    class AlarmHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.minutes) TextView minutes;
        @BindView(R.id.seconds) TextView seconds;
        @BindView(R.id.item) ConstraintLayout item;
        long id;
        private Timer timer;
        private ObjectAnimator minutesAnimator;
        private ObjectAnimator secondsAnimator;

        public AlarmHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            item.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    presenter.onAlarmClick(id);
                }
            });
            item.setOnLongClickListener(v -> {

                int adapterPosition = getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    return presenter.onLongClick(id);
                } else {
                    return false;
                }
            });
        }

        public void cancelTimer() {
            if (timer != null) {
                timer.cancel();
                timer = null;
                stopBlink();
            }
        }

        public void startTimer() {
            if (timer != null) {
//                timer.cancel();
//                timer = null;
                cancelTimer();
            }
            timer = new Timer();
            timer.schedule(createTask(), TIME_STEP, TIME_STEP);
        }

        private TimerTask createTask() {
            return new TimerTask() {
                @Override public void run() {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition == NO_POSITION) {
                        return;
                    }
                    Alarm alarm = getAlarm(id);
                    if (alarm.time() <= 0) {
                        startBlink();
                    }else {
                        stopBlink();
                    }
                    if (!alarm.isStarted()) {
                        return;
                    }
                    Pair<String, String> pair = parseTime(alarm.time());
                    activity.runOnUiThread(() -> {
                        minutes.setText(pair.first);
                        seconds.setText(pair.second);
                    });
                }
            };
        }


        private void stopBlink() {
            activity.runOnUiThread(() -> {
                if (minutesAnimator != null){
                    minutesAnimator.cancel();
//                    minutesAnimator.
                    secondsAnimator.cancel();
                    minutes.setTextColor(normalColor);
                    seconds.setTextColor(normalColor);
                }
            });


        }

        private void startBlink() {
            activity.runOnUiThread(new Runnable() {
                @Override public void run() {
                    if (minutesAnimator == null){
                        minutesAnimator = getAnimator(minutes);
                        secondsAnimator = getAnimator(seconds);

                    }else {
                        if(!minutesAnimator.isStarted()){
                            minutesAnimator.start();
                            secondsAnimator.start();
                        }
                    }
                }
            });

        }

        private ObjectAnimator getAnimator(TextView view){
            ObjectAnimator animator = ObjectAnimator.ofObject(view,
                    "textColor", new ArgbEvaluator(), normalColor, flashColor);
            animator.setDuration(300);
            animator.start();
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            return animator;
        }

    }

    private Alarm getAlarm(long id) {
        for (Alarm alarm : alarms) {
            if (alarm.id() == id) {
                return alarm;
            }
        }
        return null;
    }
}
