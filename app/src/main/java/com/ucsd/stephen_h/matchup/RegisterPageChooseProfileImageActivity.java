package com.ucsd.stephen_h.matchup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;


public class RegisterPageChooseProfileImageActivity extends AppCompatActivity {
    Integer[] imageIDs = {
            R.drawable.profle1,
            R.drawable.anchor,
            R.drawable.bikewheel,
            R.drawable.blimp,
            R.drawable.bolt,
            R.drawable.flame,
            R.drawable.flower,
            R.drawable.security,
            R.drawable.parachute
    };
    Integer profileId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page_choose_profile_image);

        GridView gridView = (GridView) findViewById(R.id.RegisterPageGridView);
        gridView.setAdapter(new ImageAdapter(this));

        Button setProfileImageButton = (Button) findViewById(R.id.RegisterPageSetProfileImageButton);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Toast.makeText(getBaseContext(),
                        "pic " + (position + 1) + " selected",
                        Toast.LENGTH_SHORT).show();
                profileId = imageIDs[(int) id];
            }
        });

        setProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileId != 0) {

                    Intent profileImageIntent = new Intent();
                    profileImageIntent.putExtra("profileImageID", profileId.intValue());
                    setResult(RESULT_OK,profileImageIntent);
                    finish();
                }
            }
        });
    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return imageIDs.length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
                Point size = new Point();
                RegisterPageChooseProfileImageActivity.this.getWindowManager().getDefaultDisplay().getSize(size);


                int imageMaxSize = size.y / 13;

                imageView.setMaxHeight(imageMaxSize);
                imageView.setMaxWidth(imageMaxSize);

            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(imageIDs[position]);
            return imageView;
        }
    }
}
