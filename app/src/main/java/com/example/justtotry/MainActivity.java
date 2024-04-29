package com.example.justtotry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //public static String TAG = "UPConvTemp"; // Identifiant pour les messages de log
    private EditText editInputTemp; // Boite de saisie de la température

   // private RadioButton rbCelsius; // Bouton radio indiquant si la saisie est en Celsius
   // private RadioButton rbFahrenheit; // Bouton radio indiquant si la saisie est en Fahrenheit
    private TextView dispResult; // Le TextView qui affichera le résultat

    private Spinner spinner;

    private Spinner spinner2;

    private Switch switchAutoMode;
    private boolean isAutoMode = false;

    private ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editInputTemp = findViewById(R.id.editTextNumber);
        //rbCelsius = findViewById(R.id.radioButton6);
        //rbFahrenheit = findViewById(R.id.radioButton7);
        spinner = findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        dispResult = findViewById(R.id.textView6);
        Button buttonConvertAndVibrate = findViewById(R.id.button2);
        switchAutoMode = findViewById(R.id.switch1);
        dataList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        buttonConvertAndVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action_convert(v);
                convert_take(v);
                vibrate(100);
            }
        });

        editInputTemp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (switchAutoMode.isChecked()) {
                    action_convert(v);
                }
                return false;
            }
        });

        switchAutoMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAutoMode = isChecked;
            }
        });



    }

    public void convert_take (android.view.View v) {
        String newItem = dispResult.getText().toString();
        String newItem2 = editInputTemp.getText().toString() +" " + spinner.getSelectedItem().toString() ;
        String result = newItem2 + " = " + newItem;
        if (!result.isEmpty()) {
            dataList.add(result);
            adapter.notifyDataSetChanged();
        }
    }


    public void action_convert(android.view.View v) {
        String inputText = editInputTemp.getText().toString();
        if (inputText.isEmpty()) {
            dispResult.setText("Erreur : Aucune valeur saisie.");
            return;
        }
        double inputValue = Double.parseDouble(inputText);

        String fromUnit = spinner.getSelectedItem().toString();
        String toUnit = spinner2.getSelectedItem().toString();

        double convertedValue;

        switch (fromUnit) {
            case "◦C":
                switch (toUnit) {
                    case "◦K":
                        convertedValue = inputValue + 273.15;
                        break;
                    case "◦F":
                        convertedValue = (inputValue * 9/5) + 32;
                        break;
                    default:
                        dispResult.setText("Erreur : Conversion non supportée.");
                        return;
                }
                break;
            case "◦K":
                switch (toUnit) {
                    case "◦C":
                        convertedValue = inputValue - 273.15;
                        break;
                    case "◦F":
                        convertedValue = inputValue * 1.8 - 459.67;
                        break;
                    default:
                        dispResult.setText("Erreur : Conversion non supportée.");
                        return;
                }
                break;
            case "◦F":
                switch (toUnit) {
                    case "◦K":
                        convertedValue = (inputValue + 459.67) / 1.8;
                        break;
                    case "◦C":
                        convertedValue = (inputValue -32) * 5/9;
                        break;
                    default:
                        dispResult.setText("Erreur : Conversion non supportée.");
                        return;
                };
                break;
            default:
                dispResult.setText("Erreur : Conversion non supportée.");
                return;
        }

        dispResult.setText(String.format("%.2f %s", convertedValue, toUnit));

    }
    public void vibrate(int duration) {
        duration = Math.max(duration, 1);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }


}