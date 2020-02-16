package com.example.afreecar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class RequirementsActivity extends AppCompatActivity {

    ListView requirementsList;
    TextView kitIdView;
    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requirements_activity);

        // RETRIEVING INFO
        Intent intent = getIntent();
        String kitId = intent.getStringExtra("kitId");         // retrieving the kit ID from the MainActivity.

        String[] kitRequirements = intent.getStringArrayExtra("kitRequirements");
        ArrayList<String> kitRequirementsList = new ArrayList<>(Arrays.asList(kitRequirements));    // retrieving the kit requirements
                                                                                                    // and converting to an ArrayList to set in
                                                                                                    // the ListView.
        // SETTING INFO
        kitIdView = findViewById(R.id.kitId);
        kitIdView.setText(kitId); // setting the kit ID text with the info from MainActivity.

        requirementsList = findViewById(R.id.requirements_list);
        requirementsList.setAdapter(new RequirementsAdapter(kitRequirementsList, // populating list view with requirements
                getApplicationContext()) );

        final Intent assembleIntent = new Intent(RequirementsActivity.this, MainActivity.class);

        requirementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                final String stepValue =(String) parent.getItemAtPosition(position);

                assembleIntent.putExtra("isAssembling", true);
                assembleIntent.putExtra("stepValue", stepValue);

                scanButton = findViewById(R.id.scanButton);
                scanButton.setVisibility(View.VISIBLE);
                scanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequirementsActivity.this.startActivity(assembleIntent);
                    }
                });
            }
        });
    }
}
