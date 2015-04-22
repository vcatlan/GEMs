package com.unccstudio.gems.gems;

import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;


public class TipsActivity extends ActionBarActivity {
    private ActionBar actionBar;
    private ArrayList<TIP> tips;
    private TextView title, desc, nr;
    private ImageView image;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.logo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        title = (TextView) findViewById(R.id.textViewTipTitle);
        desc = (TextView) findViewById(R.id.textViewTipDesc);
        image = (ImageView) findViewById(R.id.imageViewTip);
        nr = (TextView) findViewById(R.id.textViewTipNr);

        tips = new ArrayList<>();
        generateTips();

        showTip(counter);

        image.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if(counter == 0){
                    counter = tips.size()-1;
                    showTip(counter);
                } else {
                    counter--;
                    showTip(counter);
                }
            }
            public void onSwipeLeft() {
                if(counter == tips.size()-1){
                    counter = 0;
                    showTip(counter);
                } else {
                    counter++;
                    showTip(counter);
                }
            }
        });
    }

    public void showTip(int cnt){
        title.setText(tips.get(cnt).getTitle());
        //Picasso.with(this).load(tips.get(cnt).getImageURL()).into(image);
        image.setImageResource(tips.get(cnt).getResID());
        desc.setText(tips.get(cnt).getDescription());
        desc.setMovementMethod(new ScrollingMovementMethod());
        nr.setText(cnt+1 + "/" + tips.size());
    }

    public void generateTips(){
        TIP tip = new TIP("Meat, Poultry, and Fish", R.mipmap.meat ,"Keep all meat in the coldest part of the fridge (the bottom shelf), " +
                                                                    "fresh meat will only last for 1-2 days in the fridge, " +
                                                                    "cooked meat can last up to 4-5 days in the fridge");
        tips.add(tip);

        tip = new TIP("Egg Freshness Test", R.mipmap.egg_test ,"Drop one egg in a glass of water:\n" +
                                                                    "If the egg lays flat or at an angle at the bottom = FRESH\n" +
                                                                    "If it stands on the bottom = best used for baking and hard-boiled eggs\n" +
                                                                    "If it floats = EXPIRED\n");
        tips.add(tip);

        tip = new TIP("Flour", R.mipmap.flour ,"Keep in an airtight container in a cool, dark place. " +
                                             "Flours, especially flours with more oils and higher protein levels " +
                                             "(like whole wheat) last longer in the fridge or freezer\n");
        tips.add(tip);

        tip = new TIP("Crisper Tips", R.mipmap.vegetable_crisper ,"High humidity setting: \n" +
                                                    "Unripe bananas, " +
                                                    "Broccoli, " +
                                                    "Cabbage, " +
                                                    "Carrots, " +
                                                    "Cauliflower, " +
                                                    "Cucumbers, " +
                                                    "Green beans, " +
                                                    "Herbs, " +
                                                    "Leafy greens (kale, lettuce, spinach), " +
                                                    "Peppers, " +
                                                    "Strawberries, " +
                                                    "Summer squash, " +
                                                    "Watermelon.");
        tips.add(tip);

        tip = new TIP("Crisper Tips", R.mipmap.vegetable_crisper ,"Low humidity setting: \n" +
                                                    "Apples, " +
                                                    "Avocados, " +
                                                    "Ripe bananas, " +
                                                    "Cantaloupes, " +
                                                    "Figs, " +
                                                    "honeydew, " +
                                                    "Kiwi, " +
                                                    "Mangoes, " +
                                                    "Pears, " +
                                                    "Apricots, " +
                                                    "Nectarines, " +
                                                    "Peaches, " +
                                                    "Plums.");
        tips.add(tip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
