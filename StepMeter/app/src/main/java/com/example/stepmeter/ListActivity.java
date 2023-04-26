package com.example.stepmeter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ListActivity extends AppCompatActivity {

    private StepsCountViewModel mStepCountViewModel;
    private StepsCountListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.stepsCountList);
        mAdapter = new StepsCountListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStepCountViewModel = new ViewModelProvider(this)
                .get(StepsCountViewModel.class);

        mStepCountViewModel.getAllElements().observe(this,
                elements -> mAdapter.setElementList(elements));

        swipedItemDelete(recyclerView);
    }

    private void swipedItemDelete(RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback itemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int swipedPosition = viewHolder.getAdapterPosition();
                        mStepCountViewModel.delete(mAdapter.getStepsCount(swipedPosition));
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.clear){
            mStepCountViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}