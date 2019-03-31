package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rustam.apirecipes.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;

import Adapters.PagerAdapter;
import Classes.FavouriteListener;
import Saving.SaveImage;
import Saving.SaveObject;

public class Details extends AppCompatActivity {

    private float offsetSlide;
    private float offsetSlide2;
    private Boolean preventDuplicateLock = false;
    private Boolean delay = true;
    private FavouriteListener listener;
    private ArrayList<File> currentFiles = new ArrayList<>();

    public void setListener(FavouriteListener listener)
    {
        this.listener = listener ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListener(PagerAdapter.m_favourite);
        setContentView(R.layout.activity_details);

        ListView list_items = findViewById(R.id.l_foodDetails);
        ImageView img_details = findViewById(R.id.im_foodDetails);
        TextView label = findViewById(R.id.t_dishNameDetails);
        ImageButton b_expandCollapse = findViewById(R.id.b_expandCollapse);
        ImageButton b_favourite = findViewById(R.id.b_favourite);

        Intent intent =  getIntent();
        ArrayList<String> foodList = intent.getStringArrayListExtra("List");
        Bitmap image = intent.getParcelableExtra("Image");
        String dishName = intent.getStringExtra("DishName");
        String link = intent.getStringExtra("link");
        list_items.setAdapter(new ArrayAdapter<>(this, R.layout.listview_style, R.id.t_listSyle, foodList));
        img_details.setImageBitmap(image);
        label.setText(dishName);
        img_details.setOnClickListener(v -> {
            Uri uri = Uri.parse(link);
            Intent intentlink = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intentlink);
        });

        SlidingUpPanelLayout layout = findViewById(R.id.sliding_layout);
        layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                offsetSlide = slideOffset;
                if(!delay) {
                    delay = true;
                    offsetSlide2 = slideOffset;
                }else{
                    delay = false;
                }

                if(offsetSlide2 > slideOffset ){
                    b_expandCollapse.setImageResource(R.drawable.ic_baseline_expand_more_24px);
                }else if(offsetSlide2 < slideOffset) {
                    b_expandCollapse.setImageResource(R.drawable.ic_baseline_expand_less_24px);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });

        b_expandCollapse.setOnClickListener(v -> {
            if(offsetSlide == 1){
                try {
                    layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }catch (Exception ignored){

                }
            }else if(offsetSlide == 0) {
                try {
                    layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }catch (Exception ignored){

                }
            }
        });

        File file = getApplicationContext().getFilesDir();
        File[] files = file.listFiles();
        int x = files.length / 4;
        for(File member: files){
            String temp = member.getName();
            String name = temp.substring(0, temp.indexOf(","));
            if(name.equals(dishName)){
                currentFiles.add(member);
                preventDuplicateLock = true;
                b_favourite.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
            }
        }

        b_favourite.setOnClickListener(v -> {
            if(!preventDuplicateLock) {
                Snackbar snackbar = Snackbar.make(v, "Added To Favourites", Snackbar.LENGTH_LONG);
                snackbar.show();
                b_favourite.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
                String ser = SaveObject.objectToString(dishName);
                if (ser != null && !ser.equalsIgnoreCase("")) {
                    SaveObject.WriteSettings(getApplicationContext(), ser, dishName + "," + "DishName,"  + x);
                } else {
                    SaveObject.WriteSettings(getApplicationContext(), "", dishName + "," + "DishName," + x);
                }

                String temp = SaveObject.objectToString(foodList);
                if (temp != null && !temp.equalsIgnoreCase("")) {
                    SaveObject.WriteSettings(getApplicationContext(), temp, dishName + "," + "ArrayList, " + x);
                } else {
                    SaveObject.WriteSettings(getApplicationContext(), "", dishName + "," + "ArrayList, " + x);
                }

                String temp1 = SaveObject.objectToString(link);
                if (temp1 != null && !temp1.equalsIgnoreCase("")) {
                    SaveObject.WriteSettings(getApplicationContext(), temp1, dishName + "," + "Link, " + x);
                } else {
                    SaveObject.WriteSettings(getApplicationContext(), "", dishName + "," + "Link, " + x);
                }

                new SaveImage().saveToInternalStorage(getApplicationContext(), image,dishName + "," + "Image, " + x );

                File file1 = getApplicationContext().getFilesDir();
                File[] files1 = file1.listFiles();
                for(File member: files1){
                    String temp2 = member.getName();
                    String name1 = temp2.substring(0, temp2.indexOf(","));
                    if(name1.equals(dishName)){
                        currentFiles.add(member);
                        preventDuplicateLock = true;
                        b_favourite.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
                    }
                }

            }else {
                for(File member: currentFiles){
                    System.out.println(member.delete());
                }
                preventDuplicateLock = false;
                b_favourite.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
                Snackbar snackbar = Snackbar.make(v, "Removed From Favourites", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            listener.onFavouriteSaved();
        });
    }
}
