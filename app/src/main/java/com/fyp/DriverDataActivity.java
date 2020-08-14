package com.fyp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.TinyDB;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.PostInfoModel;

import java.util.ArrayList;
import java.util.List;

public class DriverDataActivity extends AppCompatActivity {
List<PostInfoModel> postInfoModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_data);
        TinyDB tinyDB=new TinyDB(this);
        tinyDB.getObject("hello",PostInfoModel.class);

    }
}
