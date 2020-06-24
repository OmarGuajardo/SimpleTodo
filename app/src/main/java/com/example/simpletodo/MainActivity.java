package com.example.simpletodo;

import androidx.annotation.Nullable;
import  androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Dialog.DialogListener{

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    //Declaring global variables
    List<String> items;
    FloatingActionButton floatingActionBtn;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionBtn = findViewById(R.id.floatingActionButton);
        rvItems = findViewById(R.id.rvItems);

        //calling 'loadItems' at the beginning of the rendering so that 'items'
        //can be populated and used to populate the ListView
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item Removed Successfully!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };



       ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
           @Override
           public void onItemClicked(int position) {

               //create the new activity
                Intent i = new Intent(MainActivity.this,EditActivity.class);

                //pass the data being edited
               i.putExtra(KEY_ITEM_TEXT, items.get(position));
               i.putExtra(KEY_ITEM_POSITION,position);

               //display the activity
               startActivityForResult(i,EDIT_TEXT_CODE);

           }
       };

        //
        itemsAdapter = new ItemsAdapter(items,onLongClickListener,onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));


        //When the FAB is clicked it will call 'openDialog' which will PopUp the
        // 'Add a new Tod0' screen
        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Dialog
                openDialog();

            }
        });
    }

    //handle the result of the Edit Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("MainActivity","This is the requestCode " + requestCode + " and this is the resultCode " + resultCode);
        Log.i("MainActivity","This is what it should be requestCode " + RESULT_OK + " and this is the resultCode " + EDIT_TEXT_CODE);
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //Retrieve the updated text val
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract original position of the edited item
            int itemPosition = data.getExtras().getInt(KEY_ITEM_POSITION);



            //update the model at the right position
            items.set(itemPosition,itemText);

            //notify the adapter
            itemsAdapter.notifyItemChanged(itemPosition);
            //save the changes
            saveItems();
            Toast.makeText(getApplicationContext(),"Item updated successuflly!", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.w("MainActivity","Unknown call to onActivityResult");
        }
    }

    //BELOW IS A PLACE WHERE METHODS ARE DEFINED

    //This method will return the file data.txt
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    //This method will load items by reading every line of data.txt file
    private void loadItems(){
        //Note that readLines is part of the Apache Commons library that returns every line of the .txt file as an Array of Strings
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){

            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>();
        }

    }

    //This method will saves items by writing them into data file
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(),items);
        }
        catch (IOException e){
            Log.e("MainActivity","Error reading items",e);
        }

    }

    //This method will create a new instance of the Class Dialog
    //When the class is constructed it will generated a new View that will be a PopUp Dialog
    public void openDialog(){
            Dialog newDialog = new Dialog();
            newDialog.show(getSupportFragmentManager(),"Dialog");
    }

    //This method will receive information from the Dialog and pass it back up
    //to MainActivity were then the 'newItem' can be inserted into the 'items' array
    @Override
    public void applyText(String newItem) {

        //add new Item to the List
        items.add(newItem);

        //Notify the adapter that an item has been inserted
        itemsAdapter.notifyItemInserted(items.size() - 1);
        Toast.makeText(getApplicationContext(),"Item Added!", Toast.LENGTH_SHORT).show();
        saveItems();
    }
}
