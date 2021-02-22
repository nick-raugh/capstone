// Group member: Khoa Tiet, Avani Sonawane, Nick Raugh

package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Spinner mySpinner;
    EditText inputEmail;
    EditText inputSubject;
    FloatingActionButton send;
    ArrayAdapter<String> myAdapter;
    TextView getToResult;
    TextView getSubResult;
    String dataStructure;
    TextView displayOp;
    TextView displayTime;
    StringBuffer bigO;
    CheckBox getMin;
    CheckBox insert;
    CheckBox search;

    String email;
    String subject;
    String caseType;
    String getMinTime;
    String insertTime;
    String searchTime;
    String bigO_time=" ";

    boolean two_threeTree, bst, hashTable, linkedlist, minHeap = false;

    private Menu optionsMenu;
    MenuItem item1;

    int click = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        item1 = menu.findItem(R.id.compose);

        if (click % 2 == 0)
        {

            item1.setIcon(R.drawable.ic_pencil);
        }
        else
        {
            item1.setIcon(R.drawable.ic_email);
        }

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.compose:
                click++;

                if (click % 2 == 0)
                {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("Mail to"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {inputEmail.getText().toString()});
                    item.setIcon(R.drawable.ic_pencil);
                }
                else
                {
                    fillInfo();
                    item.setIcon(R.drawable.ic_email);
                }

                if (click == 100) {click = 0;}

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fillInfo()
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        email = inputEmail.getText().toString();
        subject = inputSubject.getText().toString();
        caseType = radioButton.getText().toString();

        if (two_threeTree && caseType.equals("Average Case"))
        {
            getMinTime = "O(log n)";
            insertTime = "O(log n)";
            searchTime = "O(log n)";
        }
        else if (two_threeTree && caseType.equals("Worst Case"))
        {
            getMinTime = "O(log n)";
            insertTime = "O(log n)";
            searchTime = "O(log n)";
        }
        else if (bst && caseType.equals("Average Case"))
        {
            getMinTime = "O(log n)";
            insertTime = "O(log n)";
            searchTime = "O(log n)";
        }
        else if (bst && caseType.equals("Worst Case"))
        {
            getMinTime = "O(n)";
            insertTime = "O(n)";
            searchTime = "O(n)";
        }
        else if (hashTable && caseType.equals("Average Case"))
        {
            getMinTime = "O(1)";
            insertTime = "O(1)";
            searchTime = "O(1)";
        }
        else if (hashTable && caseType.equals("Worst Case"))
        {
            getMinTime = "O(n)";
            insertTime = "O(n)";
            searchTime = "O(n)";
        }
        else if (linkedlist && caseType.equals("Average Case"))
        {
            getMinTime = "O(n)";
            insertTime = "O(1)";
            searchTime = "O(n)";
        }
        else if (linkedlist && caseType.equals("Worst Case"))
        {
            getMinTime = "O(n)";
            insertTime = "O(1)";
            searchTime = "O(n)";
        }
        else if (minHeap && caseType.equals("Average Case"))
        {
            getMinTime = "O(1)";
            insertTime = "O(1)";
            searchTime = "O(n)";
        }
        else if (minHeap && caseType.equals("Worst Case"))
        {
            getMinTime = "O(1)";
            insertTime = "O(log n)";
            searchTime = "O(n)";
        }


        if (getMin.isChecked())
        {
            bigO_time += "Get Minimum: " + getMinTime + "\n";
        }
        if (insert.isChecked())
        {
            bigO_time += "Insert: " + insertTime + "\n";
        }
        if (search.isChecked())
        {
            bigO_time += "Search: " + searchTime + "\n";
        }

        getToResult.setText(email);
        getSubResult.setText(subject);
        displayOp.setText(caseType + " Time Complexity for " + dataStructure + ":");
        displayTime.setText(bigO_time);
        //bigO_time = "hah";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mySpinner = (Spinner) findViewById(R.id.dataStructSpinner);

        myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.dataStruct));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        radioGroup = findViewById(R.id.radioGroup);

        inputEmail = findViewById(R.id.emailAddrTextbox);
        inputSubject = findViewById(R.id.emailSubTextbox);

        getToResult = findViewById(R.id.toResult);
        getSubResult = findViewById(R.id.subjectResult);
        displayOp = findViewById(R.id.displayOperations);
        displayTime = findViewById(R.id.displayTime);
        getMin = findViewById(R.id.getMinCheckbox);
        insert = findViewById(R.id.insertCheckbox);
        search = findViewById(R.id.searchCheckbox);
        
        bigO = new StringBuffer();

        if (savedInstanceState != null)
        {
            email = savedInstanceState.getString("email");
            subject = savedInstanceState.getString("subject");
            caseType = savedInstanceState.getString("type");
            click = savedInstanceState.getInt("count");
            dataStructure = savedInstanceState.getString("struct");
            bigO_time = savedInstanceState.getString("time");

            System.out.println(bigO_time);

            getToResult.setText(email);
            getSubResult.setText(subject);
            displayOp.setText(caseType + " Time Complexity for " + dataStructure + ":");
            displayTime.setText(bigO_time);


        }

        // set up spinner
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch(position)
                {
                    case 0:
                        dataStructure = "Binary Search Tree";
                        bst = true;
                        break;
                    case 1:
                        dataStructure = "2-3 Tree";
                        two_threeTree = true;
                        break;
                    case 2:
                        dataStructure = "Hash Table";
                        hashTable = true;
                        break;
                    case 3:
                        dataStructure = "Linked List";
                        linkedlist = true;
                        break;
                    case 4:
                        dataStructure = "Min Heap";
                        minHeap = true;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    public void checkButton(View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    @Override
    protected void onPause() {
        //Log.d("life_cycle", "onPause invoked");
        super.onPause();
    }

    @Override
    protected void onStop() {
        //Log.d("life_cycle", "onStop invoked");
        super.onStop();
    }

    public void onRestorelnstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Log.d("life_cycle", "onRestoreInstanceState invoked");

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("email", email);
        outState.putString("subject", subject);
        outState.putString("type", caseType);
        outState.putString("struct", dataStructure);
        outState.putString("time", bigO_time);
        outState.putInt("count", click);

    }

}