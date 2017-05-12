package org.berendeev.roma.sciencealarm;


import android.content.Context;

import org.berendeev.roma.sciencealarm.data.AlarmRepositoryImpl;
import org.berendeev.roma.sciencealarm.data.sqlite.AlarmDataSource;
import org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.sciencealarm.domain.AlarmRepository;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class AlarmRepoTest {

    private AlarmRepository repository;

    @Before
    public void before(){
        Context context = RuntimeEnvironment.application.getApplicationContext();
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(context);

        repository = new AlarmRepositoryImpl(openHelper);
    }

    @Test
    public void subscriptionTest(){
        System.out.println("started");
        repository
                .subscribeOnAlarms()
                .subscribe(alarms -> {
                    print(alarms);
        });

        Alarm alarm = Alarm.create(-1, "", 0, true);

        System.out.println("add 1 items");
        repository.saveAlarm(alarm).subscribe();
        System.out.println("add 1 items");
        repository.saveAlarm(alarm).subscribe();


        alarm = alarm.toBuilder().id(5).time(10).build();

        System.out.println("add 1 item");
        repository.saveAlarm(alarm).subscribe();

        alarm = alarm.toBuilder().isStarted(false).build();

        System.out.println("update item");
        repository.saveAlarm(alarm).subscribe();

        System.out.println("delte item");
        repository.deleteAlarm(alarm.id()).subscribe();
    }

    private void print(List<Alarm> alarms){

        System.out.println("Alarms: ");

        alarms.forEach(System.out::println);
    }

    private void passed(String s){
        System.out.println(s + " passed");
    }

}
