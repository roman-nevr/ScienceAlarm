package org.berendeev.roma.sciencealarm;

import android.content.Context;

import org.berendeev.roma.sciencealarm.data.sqlite.AlarmDataSource;
import org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class SqliteTest {

    private AlarmDataSource dataSource;

    @Before
    public void before(){
        Context context = RuntimeEnvironment.application.getApplicationContext();
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(context);

        dataSource = new AlarmDataSource(openHelper);
    }

    @Test
    public void saveTest(){

        Alarm alarm = Alarm.create(1, "qwerty", 0, true);

        dataSource.saveAlarm(alarm);

        List<Alarm> allAlarm = dataSource.getAllAlarm();

        assertTrue(allAlarm.size() == 1);

        passed("saveTest");

        printAll();
    }

    @Test
    public void updateTest(){
        Alarm alarm = Alarm.create(1, "qwerty", 0, true);

        dataSource.saveAlarm(alarm);

        alarm = alarm.toBuilder().isStarted(false).build();

        dataSource.saveAlarm(alarm);

        Alarm savedAlarm = dataSource.getAlarm(1);

        assertTrue(alarm.equals(savedAlarm));

        passed("updateTest");

        printAll();
    }

    @Test
    public void getTest(){
        Alarm alarm = Alarm.create(1, "qwerty", 0, true);

        dataSource.saveAlarm(alarm);

        Alarm alarmEqual = dataSource.getAlarm(1);

        Assert.assertEquals(alarm, alarmEqual);

        passed("getTest");

        printAll();
    }

    @Test
    public void deleteTest(){
        Alarm alarm = Alarm.create(1, "qwerty", 0, true);

        dataSource.saveAlarm(alarm);

        alarm = alarm.toBuilder().id(2).card("ytrewq").build();

        dataSource.saveAlarm(alarm);

        dataSource.deleteAlarm(1);

        List<Alarm> allAlarm = dataSource.getAllAlarm();

        assertTrue(allAlarm.size() == 1);

        passed("deleteTest");

        printAll();
    }

    private void printAll(){

        System.out.println("All alarms: ");

        List<Alarm> alarms = dataSource.getAllAlarm();

        for (Alarm alarm : alarms) {
            System.out.println(alarm);
        }
    }

    private void passed(String s){
        System.out.println(s + " passed");
    }

}
