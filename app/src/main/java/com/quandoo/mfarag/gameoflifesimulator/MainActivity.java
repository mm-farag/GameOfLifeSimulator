package com.quandoo.mfarag.gameoflifesimulator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.quandoo.mfarag.gameoflifesimulator.util.LogUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements View.OnClickListener {

    // Constants
    private final int numberOfRows = 20;
    private final int numberOfColumns = 20;
    private final int ITERATION_TIME = 1000;
    //private int numberOfItems;

    // Objects
    private Timer incrementTimer;
    private ArrayList<Integer> livingList;
    private ArrayList<Integer> dyingList;
    private long iterationTimeSum ;
    private int iterationNumber ;

    // Views
    private TableLayout tableLayout;
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

        //numberOfItems = numberOfColumns * numberOfRows;

        LogUtil.debug("init");

        iterationTimeSum = 0 ;
        iterationNumber = 1 ;

        livingList = new ArrayList();
        dyingList = new ArrayList();

    }

    /**
     * initializeScreenViews method initializes the screen view, sets the views
     * listeners and setup all necessary texts for the screen.
     */
    private void initializeScreenViews() {


        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        for (int row = 0; row < numberOfRows; row++) {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            tableRow.setLayoutParams(params);

            for (int col = 0; col < numberOfColumns; col++) {

                final View squareCell = getLayoutInflater().inflate(R.layout.square_cell, null);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(30, 30);
                layoutParams.setMargins(2, 2, 2, 2);
                squareCell.setLayoutParams(layoutParams);

                squareCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ColorDrawable drawable = (ColorDrawable) squareCell.getBackground();
                        if (drawable.getColor() == Color.BLACK) {
                            squareCell.setBackgroundColor(Color.WHITE);
                        } else {
                            squareCell.setBackgroundColor(Color.BLACK);
                        }
                    }
                });

                tableRow.addView(squareCell);
            }
            tableLayout.addView(tableRow);
        }

        /*
        tableLayout.setAdapter(new ImageAdapter(this, numberOfItems));
        tableLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        */

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

            int startIndex = 4;
            TableRow row = (TableRow) tableLayout.getChildAt(9);

            for (int i = startIndex; i < 10 + startIndex; i++) {
                ImageView imageView = (ImageView) row.getChildAt(i);
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

                iterationTimeSum = 0 ;
                iterationNumber = 1 ;

                TimerTask incrementTask = new TimerTask() {
                    @Override
                    public void run() {

                        LogUtil.debug("Running Iteration : " + iterationNumber );
                        long startTime = System.nanoTime();

                        // run/call the method
                        performStep();
                        updateTheGrid();

                        long endTime = System.nanoTime();
                        long diff = endTime - startTime;
                        LogUtil.debug("Elapsed milliseconds: " + diff / 1000000);

                        iterationTimeSum += diff / 1000000 ;

                        long avg = iterationTimeSum / iterationNumber++;
                        LogUtil.debug("Avg elapsed milliseconds: " + avg );

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

            TableRow row = (TableRow) tableLayout.getChildAt(i);

            for (int j = 0; j < numberOfColumns; j++) {

                ImageView imageView = (ImageView) row.getChildAt(j);
                imageView.setBackgroundColor(Color.WHITE);
            }
        }
    }


    private boolean isLiveCell(TableRow row, int index) {

        boolean liveCell = false;

        ImageView imageView = (ImageView) row.getChildAt(index);

        ColorDrawable drawable = (ColorDrawable) imageView.getBackground();
        if (drawable.getColor() == Color.BLACK) {
            liveCell = true;
        }

        return liveCell;
    }

    private void checkCell(boolean liveCell, int i, int j) {

        // search for live neighbours
        int liveNeighbours = 0;

        for (int k = i - 1; k < i + 2 && k >= 0 && k < numberOfRows; k++) {

            TableRow row = (TableRow) tableLayout.getChildAt(k);

            for (int m = j - 1; m < j + 2 && m >= 0 && m < numberOfColumns ; m++) {

                if ( k == i && m == j ) {
                    continue;
                } else {

                    if (isLiveCell(row, m)) {
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

    private void performStep() {

        for (int i = 0; i < numberOfRows; i++) {

            TableRow row = (TableRow) tableLayout.getChildAt(i);

            for (int j = 0; j < numberOfColumns; j++) {

                boolean liveCell = isLiveCell(row, j);
                checkCell(liveCell, i, j);
            }
        }
    }

    private void updateTheGrid() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int index : dyingList) {

                    int rowIndex = index / numberOfColumns;
                    int columnIndex = index % numberOfColumns;

                    TableRow row = (TableRow) tableLayout.getChildAt(rowIndex);
                    ImageView imageView = (ImageView) row.getChildAt(columnIndex);

                    imageView.setBackgroundColor(Color.WHITE);
                }


                for (int index : livingList) {

                    int rowIndex = index / numberOfColumns;
                    int columnIndex = index % numberOfColumns;

                    TableRow row = (TableRow) tableLayout.getChildAt(rowIndex);
                    ImageView imageView = (ImageView) row.getChildAt(columnIndex);


                    imageView.setBackgroundColor(Color.BLACK);
                }

                dyingList.clear();
                livingList.clear();
            }
        });


        //tableLayout.invalidate();
    }
}
