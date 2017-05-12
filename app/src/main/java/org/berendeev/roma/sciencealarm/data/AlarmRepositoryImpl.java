package org.berendeev.roma.sciencealarm.data;

import org.berendeev.roma.sciencealarm.data.sqlite.AlarmDataSource;
import org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper;
import org.berendeev.roma.sciencealarm.domain.AlarmRepository;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;


public class AlarmRepositoryImpl implements AlarmRepository {

    private DatabaseOpenHelper openHelper;
    private AlarmDataSource alarmDataSource;

    private BehaviorSubject<List<Alarm>> alarmsSubject;

    public AlarmRepositoryImpl(DatabaseOpenHelper openHelper) {
        this.openHelper = openHelper;
        alarmDataSource = new AlarmDataSource(openHelper);
        alarmsSubject = BehaviorSubject.create();
        updateAlarms();
    }

    @Override public Completable saveAlarm(Alarm alarm) {
        return Completable.fromAction(() -> {
            alarmDataSource.saveAlarm(alarm);
            updateAlarms();
        });
    }

    @Override public Observable<Alarm> getAlarm(long id) {
        return Observable.fromCallable(() -> {
            return alarmDataSource.getAlarm(id);
        });
    }

    @Override public Completable deleteAlarm(long id) {
        return Completable.fromAction(() -> {
            alarmDataSource.deleteAlarm(id);
            updateAlarms();
        });
    }

    @Override public Single<List<Alarm>> getAllAlarms() {
        return Single.fromCallable(() -> {
            return alarmDataSource.getAllAlarm();
        });
    }

    @Override public Observable<List<Alarm>> subscribeOnAlarms() {
        return alarmsSubject;
    }

    private void updateAlarms(){
        Completable.fromAction(() -> {
            List<Alarm> alarms = alarmDataSource.getAllAlarm();
            alarmsSubject.onNext(alarms);
        }).subscribe();
    }
}
