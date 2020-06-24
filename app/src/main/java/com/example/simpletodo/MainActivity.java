package com.example.simpletodo;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
//import android.os.FileUtils;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //place to declare variables
    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);




        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item Removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items,onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newToDo = etItem.getText().toString();

                //Add Item to Model
                items.add(newToDo);

                //Notify the adapter that an item has been inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(),"Item Added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    //place to define methods
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    //This function will load items by reading every line of data.txt file
    private void loadItems(){
        try{
//            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){

            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>();
        }

    }

    //This function will saves items by writing them into data file
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(),items);
        }
        catch (IOException e){
            Log.e("MainActivity","Error reading items",e);
        }

    }
}
