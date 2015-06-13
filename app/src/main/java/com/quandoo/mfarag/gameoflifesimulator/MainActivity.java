package com.quandoo.mfarag.gameoflifesimulator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.quandoo.mfarag.gameoflifesimulator.adapters.ImageAdapter;
import com.quandoo.mfarag.gameoflifesimulator.util.LogUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements View.OnClickListener {

    // Constants
    private final int numberOfRows = 20;
    private final int numberOfColumns = 20;
    private final int ITERATION_TIME = 1000;
    private int numberOfItems;

    // Objects
    private Timer incrementTimer;
    private ArrayList<Integer> livingList;
    private ArrayList<Integer> dyingList;

    // Views
    private GridView gridView;
    private Button startButton;
    private Button clearButton;
    private Button performStepButton;
    private Button tenCellPatternButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeObjects();
        this.initializeScreenViews();
    }


    /**
     * initializeObjects method initializes the screen different objects.
     */
    private void initializeObjects() {

        numberOfItems = numberOfColumns * numberOfRows;

        livingList = new ArrayList();
        dyingList = new ArrayList();

    }

    /**
     * initializeScreenViews method initializes the screen view, sets the views
     * listeners and setup all necessary texts for the screen.
     */
    private void initializeScreenViews() {


        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(numberOfColumns);

        gridView.setAdapter(new ImageAdapter(this, numberOfItems));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ColorDrawable drawable = (ColorDrawable) view.getBackground();
                if (drawable.getColor() == Color.BLACK) {
                    view.setBackgroundColor(Color.WHITE);
                } else {
                    view.setBackgroundColor(Color.BLACK);
                }
            }
        });

        performStepButton = (Button) findViewById(R.id.performStepButton);
        performStepButton.setOnClickListener(this);

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);

        tenCellPatternButton = (Button) findViewById(R.id.tenCellPatternButton);
        tenCellPatternButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == tenCellPatternButton) {

            clearTheGrid();

            int startIndex = 185;
            for (int i = startIndex; i < 10 + startIndex; i++) {
                ImageView imageView = (ImageView) gridView.getChildAt(i);
                imageView.setBackgroundColor(Color.BLACK);
            }

        } else if (view == clearButton) {

            clearTheGrid();

        } else if (view == performStepButton) {

            performStep();
            updateTheGrid();

        } else if (view == startButton) {

            if (startButton.getText().toString().equals("Start")) {
                startButton.setText("Stop");
                performStepButton.setEnabled(false);
                clearButton.setEnabled(false);
                tenCellPatternButton.setEnabled(false);

                incrementTimer = new Timer();

                TimerTask incrementTask = new TimerTask() {
                    @Override
                    public void run() {

                        LogUtil.debug("Running Iteration...");
                        performStep();
                        updateTheGrid();

                    }
                };

                incrementTimer.schedule(incrementTask, 0, ITERATION_TIME);


            } else {
                startButton.setText("Start");
                performStepButton.setEnabled(true);
                clearButton.setEnabled(true);
                tenCellPatternButton.setEnabled(true);

                incrementTimer.cancel();
            }
        }
    }


    private void clearTheGrid() {

        for (int i = 0; i < numberOfRows; i++) {

            for (int j = 0; j < numberOfColumns; j++) {

                clearCell(i * numberOfColumns + j);
            }
        }
    }

    private void clearCell(int index) {

        ImageView imageView = (ImageView) gridView.getChildAt(index);
        imageView.setBackgroundColor(Color.WHITE);
    }

    private boolean isLiveCell(int index) {

        boolean liveCell = false;

        ImageView imageView = (ImageView) gridView.getChildAt(index);
        ColorDrawable drawable = (ColorDrawable) imageView.getBackground();
        if (drawable.getColor() == Color.BLACK) {
            liveCell = true;
        }

        return liveCell;
    }


    private void performStep() {

        for (int i = 0; i < numberOfRows; i++) {

            for (int j = 0; j < numberOfColumns; j++) {

                boolean liveCell = isLiveCell(i * numberOfColumns + j);

                // search for live neighbours
                int liveNeighbours = 0;

                for (int k = i - 1; k < i + 2; k++) {
                    for (int m = j - 1; m < j + 2; m++) {

                        if (k < 0 || m < 0 || k >= numberOfRows || m >= numberOfColumns || (k == i && m == j)) {
                            continue;
                        } else {

                            if (isLiveCell(k * numberOfColumns + m)) {
                                liveNeighbours++;
                            }
                        }
                    }
                }

                //LogUtil.debug("item index : " + (i * numberOfColumns + j));
                //LogUtil.debug("liveCell : " + liveCell);
                //LogUtil.debug("liveNeighbours : " + liveNeighbours);

                if (liveCell) {
                    if (liveNeighbours < 2 || liveNeighbours > 3) {
                        dyingList.add(i * numberOfColumns + j);
                        //LogUtil.debug("added to dyingList");
                    }
                } else {
                    if (liveNeighbours == 3) {
                        livingList.add(i * numberOfColumns + j);
                        //LogUtil.debug("added to livingList");
                    }
                }
            }
        }
    }

    private void updateTheGrid() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i : dyingList) {

                    ImageView imageView = (ImageView) gridView.getChildAt(i);
                    imageView.setBackgroundColor(Color.WHITE);
                }


                for (int i : livingList) {

                    ImageView imageView = (ImageView) gridView.getChildAt(i);
                    imageView.setBackgroundColor(Color.BLACK);
                }

                dyingList.clear();
                livingList.clear();
            }
        });


        //gridView.invalidate();
    }
}
