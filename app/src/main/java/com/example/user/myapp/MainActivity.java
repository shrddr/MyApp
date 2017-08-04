package com.example.user.myapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScreenTitles = getResources().getStringArray(R.array.screens_array);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
        mDrawerList.setOnItemClickListener(this);

        onItemClick(null, null, 0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new TodayFragment();
                break;
            case 1:
                fragment = new StatsFragment();
                break;
            default:
                return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mScreenTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public static class TodayFragment extends Fragment implements View.OnClickListener {

        private Calendar currentDay;
        private String currentDayString;
        private MySQLiteOpenHelper mDbHelper;

        public TodayFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_today, container, false);

            v.findViewById(R.id.buttonLeft).setOnClickListener(this);
            v.findViewById(R.id.buttonRight).setOnClickListener(this);
            v.findViewById(R.id.buttonAdd).setOnClickListener(this);
            v.findViewById(R.id.buttonClear).setOnClickListener(this);

            mDbHelper = new MySQLiteOpenHelper(getActivity());

            currentDay = Calendar.getInstance();
            currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());

            refreshView(v);

            EditText editTextDate = (EditText)v.findViewById(R.id.editTextDate);
            new SetDate(editTextDate, getActivity());

            return v;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonLeft:
                    currentDay.add(Calendar.DATE, -1);
                    currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
                    refreshView(getView());
                    break;
                case R.id.buttonRight:
                    currentDay.add(Calendar.DATE, 1);
                    currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
                    refreshView(getView());
                    break;
                case R.id.buttonAdd:
                    Intent intent = new Intent(getActivity(), MealEditorActivity.class);
                    intent.putExtra(MealEditorActivity.MESSAGE_DATE, currentDayString);
                    startActivity(intent);
                    break;
                case R.id.buttonClear:
                    mDbHelper.cleanDB();
                    refreshView(getView());
                    break;
            }
        }

        private void refreshView(View v) {
            EditText editTextDate = (EditText)v.findViewById(R.id.editTextDate);
            editTextDate.setTag(currentDay.getTime().getTime()/1000);
            editTextDate.setText(DateFormat.getDateFormat(getActivity()).format(currentDay.getTime()));

            List<Meal> meals = mDbHelper.getMeals(currentDayString);

            ListView listView = (ListView)v.findViewById(R.id.listView);
            listView.setAdapter(
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, meals));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Meal m = (Meal)parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), MealEditorActivity.class);
                    intent.putExtra(MealEditorActivity.MESSAGE_ID, m.id);
                    intent.putExtra(MealEditorActivity.MESSAGE_DATE, currentDayString);
                    startActivity(intent);
                }
            });
        }
    }

    public static class StatsFragment extends Fragment implements View.OnClickListener {

        private MySQLiteOpenHelper mDbHelper;

        public StatsFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_stats, container, false);

            GraphView graph = (GraphView) v.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, 1),
                    new DataPoint(1, 5),
                    new DataPoint(2, 3),
                    new DataPoint(3, 2),
                    new DataPoint(4, 6)
            });
            graph.addSeries(series);

            return v;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }

    }
}
