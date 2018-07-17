package com.example.dim.tp14;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dim.tp14.utils.MainPagerAdapter;
import com.example.dim.tp14.utils.TabListActionInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements TabListActionInterface {

    // titles also used for view tags, important !
    private static final String ARG_MOTS_TITLE = "MOTS";
    private static final String ARG_DEFS_TITLE = "DEFS";

    private DrawerLayout drawerMenu;
    private HashMap<Integer, Fragment> fragments;
    private LinearLayout addDialogLayout;
    private EditText newWordEdit;
    private EditText newDefinitionEdit;
    private AlertDialog addDialog;

    private WordsFragment wordsFragment;
    private DefinitionsFragment definitionsFragment;
    private SelectedFragment selectedFragment;
    private File dataFile;
    private HashMap<String, String> dataMap;
    private MainPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private int curentPage;
    private String delKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // toolbar & menu
        Toolbar toolbar = initToolbar();
        initDrawerMenu(toolbar);

        if (addDialogLayout == null) {
            initSaveDialog();
        }

        initData();

        // fragments
        initFragments();

        // view pager
        initViewPager();
    }

    // setting view pager and view pager adapter
    private void initViewPager() {
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);

        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        // handle page title with title strip
        PagerTitleStrip titleStrip = findViewById(R.id.view_pager_strip);
        titleStrip.setTextColor(getResources().getColor(R.color.White));
        titleStrip.setBackgroundColor(getResources().getColor(R.color.myPrimaryDarkColor));
    }

    private void initFragments() {
        // init fragments
        wordsFragment = WordsFragment.newInstance(new ArrayList<String>(dataMap.keySet()), ARG_MOTS_TITLE);
        definitionsFragment = DefinitionsFragment.newInstance(new ArrayList<String>(dataMap.values()), ARG_DEFS_TITLE);
        selectedFragment = SelectedFragment.newInstance();

        // set fragments in map
        fragments = new HashMap<>();
        fragments.put(0, wordsFragment);
        fragments.put(1, definitionsFragment);
        fragments.put(2, selectedFragment);
    }

    private void initData() {
        dataMap = new HashMap<>();

        // if dataFile is null, load
        if (dataFile == null) {
            String filePath = "definitions.txt";
            dataFile = new File(getFilesDir().getAbsolutePath() + "/" + filePath);

            // create if not created already
            if (!dataFile.exists()) {
                try {
                    //Log.i("TEST", "initData: NEW FILE !!!!!!!!!!!!!!!!!!!!!!!!!");
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // read file
            Scanner scan;
            try {
                scan = new Scanner(dataFile);

                while (scan.hasNextLine()) {
                    String line = scan.nextLine();

                    // add to data map
                    if (!line.trim().isEmpty()) {
                        String[] lineValues = line.split("=", 2);

                        dataMap.put(lineValues[0].trim(), lineValues[1].trim());
                    }
                }

                scan.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDrawerMenu(Toolbar toolbar) {
        drawerMenu = findViewById(R.id.drawer_layout);

        // set drawer menu toggle listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerMenu, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerMenu.addDrawerListener(toggle);
        toggle.syncState(); // use to sync if toggled or not

        // handle navigation
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                item.setChecked(true); // for visual effect
                drawerMenu.closeDrawers(); // close menu

                // add option
                // TODO: set focus on word field
                if (item.getItemId() == R.id.nav_ajouter) {
                    if (addDialog == null) {
                        // init AlertDialog with custom view
                        addDialog = makeAddDialog();
                        // onDismiss & onShow implementations
                        initAddDialog();
                    }

                    addDialog.show();
                    return true;
                }

                if (item.getItemId() == R.id.nav_effacer) {
                    pagerAdapter.setDelMod(true);
                    pagerAdapter.notifyDataSetChanged();
                    getSupportActionBar().setTitle(R.string.deletion);
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * Needed to detect page change on swipe back
     */
    private SimpleOnPageChangeListener pageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            curentPage = position;
        }
    };

    @Override
    public void onBackPressed() {
        if (pagerAdapter.isDelMod()) {
            pagerAdapter.setDelMod(false);
            pagerAdapter.notifyDataSetChanged();
            getSupportActionBar().setTitle(R.string.dictionnary);
        } else {
            // TODO: handle tabs
            if (curentPage > 0) {
                curentPage -=1;
                mViewPager.setCurrentItem(curentPage);
                pagerAdapter.notifyDataSetChanged();
            } else {
                AlertDialog dialog = makeQuitDialog();
            }
            //super.onBackPressed();
        }

    }

    private void initAddDialog() {
        addDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetDialogContent();
                addDialog.dismiss();
            }
        });

        addDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = (addDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //call to return closes dialog
                        boolean invalid = false;

                        if (newWordEdit.getText().toString().isEmpty()
                                || newDefinitionEdit.getText().toString().isEmpty()) {

                            Toast.makeText(MainActivity.this,
                                    R.string.all_fields_required,
                                    Toast.LENGTH_SHORT).show();
                            invalid = true;
                        }

                        if (dataMap.keySet().contains(newWordEdit.getText().toString())) {
                            Toast.makeText(MainActivity.this,
                                    R.string.already_exists,
                                    Toast.LENGTH_SHORT).show();
                            invalid = true;
                        }

                        // saveDefinition
                        if (!invalid) {
                            saveDefinition(newWordEdit.getText().toString(),
                                    newDefinitionEdit.getText().toString());
                            //Log.i("TEST", "onClick: INVALID ADD " + invalid);
                            addDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private AlertDialog makeAddDialog() {
        return new Builder(new ContextThemeWrapper(MainActivity.this,
                R.style.AppTheme_Dialog))
                .setTitle(R.string.dialog_title_add)
                .setMessage(R.string.dialog_message_save)
                .setView(addDialogLayout)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, null).create();
    }

    private AlertDialog makeQuitDialog() {
        return new Builder(new ContextThemeWrapper(MainActivity.this,
                R.style.AppTheme_Dialog))
                .setTitle(R.string.dialog_title_quit)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // start clean activity
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // clean quit
                        finish();
                        System.exit(0);
                    }
                }).create();
    }

    private AlertDialog makeDeleteDialog() {
        String delMessage = getResources().getString(R.string.dialog_message_del)
                + delKey + " ?";
        return new Builder(new ContextThemeWrapper(MainActivity.this,
                R.style.AppTheme_Dialog))
                .setTitle(R.string.dialog_title_del)
                .setMessage(delMessage)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // update data
                        String def = dataMap.get(delKey);
                        dataMap.remove(delKey);

                        // prep for file update
                        StringBuilder sb = new StringBuilder();
                        for (Entry<String, String> entry : dataMap.entrySet()) {
                            sb.append(entry.getKey());
                            sb.append("=");
                            sb.append(entry.getValue());
                            sb.append("\r\n");

                        }
                        // update file - rewrite actually
                        try {
                            FileWriter fileWriter = new FileWriter(dataFile, false);
                            fileWriter.write(sb.toString());

                            // ending
                            fileWriter.flush();
                            fileWriter.close();
                        }catch (IOException e){
                            Log.e("IOEXCEPTION", "onClick: ", e);
                        }

                        //update view
                        wordsFragment.updateTab(delKey, true);
                        definitionsFragment.updateTab(def, true);
                    }
                }).create();
    }



    private Toolbar initToolbar() {
        // set toolbar as actionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init toolbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);
        actionbar.setTitle(R.string.dictionnary);
        return toolbar;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Called when an item of a tab is clicked.</br>
     * <p>
     * The tag is defined as defined :</br>
     * <b>title</b>_<b>pos</b>
     * <p>
     * where <b>title</b> is the Fragment name </br>
     * and <b>pos</b> the position of the item in the list </br>
     *
     * @param item String - Tag of the view
     */
    @Override
    public void onItemClick(String item) {
        String[] selectInfos = item.split("_");

        if (selectInfos[0].equalsIgnoreCase(ARG_MOTS_TITLE)) {
            if (pagerAdapter.isDelMod()){
                delKey = selectInfos[1];
                makeDeleteDialog().show();
            } else {
                mViewPager.setCurrentItem(2);
                selectedFragment.updateFragment(dataMap.get(selectInfos[1]), false);
                pagerAdapter.notifyDataSetChanged();
            }

        }

        if (selectInfos[0].equalsIgnoreCase(ARG_DEFS_TITLE)) {
            //Log.i("TEST", "onItemClick: DEFINITION !"+selectInfos[1]);
            mViewPager.setCurrentItem(2);
            String key = "";
            for (Entry<String, String> entry : dataMap.entrySet()) {
                if (entry.getValue().equalsIgnoreCase(selectInfos[1])) {
                    key = entry.getKey();
                }
            }
            selectedFragment.updateFragment(key, true);
            pagerAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    private void initSaveDialog() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addDialogLayout = new LinearLayout(getApplicationContext());
        addDialogLayout.setOrientation(LinearLayout.VERTICAL);
        addDialogLayout.setLayoutParams(layoutParams);
        View view = getLayoutInflater().inflate(R.layout.dialog_save, addDialogLayout);

        newWordEdit = view.findViewById(R.id.edit_new_word);
        newDefinitionEdit = view.findViewById(R.id.edit_new_definition);
    }

    private void resetDialogContent() {
        newWordEdit.setText("");
        newDefinitionEdit.setText("");
    }

    private void saveDefinition(String word, String definition) {
        try {

            FileWriter fileWriter = new FileWriter(dataFile, true);
            fileWriter.append(word).append("=").append(definition).append("\r\n");
            // flush and close
            fileWriter.flush();
            fileWriter.close();

            // update data
            dataMap.put(word, definition);

            // update view
            wordsFragment.updateTab(word, false);
            definitionsFragment.updateTab(definition, false);
            mViewPager.setCurrentItem(0);

        } catch (IOException e) {
            Log.e("EXCEPTION", "saveDefinition: ", e);
        }
    }
}
