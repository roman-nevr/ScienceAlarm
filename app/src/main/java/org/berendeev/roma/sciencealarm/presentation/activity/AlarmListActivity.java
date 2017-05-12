package org.berendeev.roma.sciencealarm.presentation.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import org.berendeev.roma.sciencealarm.R;
import org.berendeev.roma.sciencealarm.domain.entity.Alarm;
import org.berendeev.roma.sciencealarm.presentation.adapter.AlarmListAdapter;
import org.berendeev.roma.sciencealarm.presentation.AlarmListView;
import org.berendeev.roma.sciencealarm.presentation.App;
import org.berendeev.roma.sciencealarm.presentation.adapter.SwipeHelper;
import org.berendeev.roma.sciencealarm.presentation.presenter.AlarmListPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmListActivity extends AppCompatActivity implements AlarmListView {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Inject AlarmListPresenter presenter;

    private AlarmListAdapter adapter;
    private SwipeHelper swipeHelper;

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
    }

    private void initDi() {
        App.getInstance().getMainComponent().inject(this);
        presenter.setView(this);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
}
