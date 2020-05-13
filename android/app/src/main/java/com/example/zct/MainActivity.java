package com.example.zct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MICROPHONE = 1;
    private TableLayout mTableLayout;
    private String l,c;
    private String[] nationsArray = {"es", "fr", "be","it","se","kr","ru","cn","hu","pl","gr","dk","nl","iq","jp","br","vn","sk","ie","il","cz","ro","id"};
    Context context = this;
    //static int REQUEST_MICROPHONE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTableLayout = findViewById(R.id.nationsTablelayout);
        loadNations();
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);

        }
    }



    public void loadNations(){
        for (int i=0; i<=nationsArray.length-1;i++){
            String word = nationsArray[i];
            addTableRow(i,localeToEmoji(stringToLocale(word.toUpperCase())) + "  " + stringToLocale(word.toUpperCase()).getDisplayCountry());
        }
    }
    public void addTableRow(int index, final String nationName){
        TableRow tr = new TableRow(this);
        tr.setTag(index);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView nationNameTextView = new TextView(this);
        nationNameTextView.setTextSize(20);
        nationNameTextView.setText(nationName);
        nationNameTextView.setTextColor(Color.parseColor("#000000"));
        tr.setPadding(10,20,20,10);
        tr.addView(nationNameTextView);
        //Click on nation in table
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int index = (int) v.getTag();
                Intent game = new Intent(context, GameActivity.class);
                game.putExtra("COUNTRY", nationsArray[index]);
                startActivity(game);
            }
        });
        mTableLayout.addView(tr);
    }
    //Get nation flag from locale
    public String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
    //Get locale from string
    public Locale stringToLocale(String s) {
        StringTokenizer tempStringTokenizer = new StringTokenizer(s,",");
        if(tempStringTokenizer.hasMoreTokens())
            l = tempStringTokenizer.nextElement().toString();
        if(tempStringTokenizer.hasMoreTokens())
            c = tempStringTokenizer.nextElement().toString();
        return new Locale(l,l.toLowerCase());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MICROPHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
}