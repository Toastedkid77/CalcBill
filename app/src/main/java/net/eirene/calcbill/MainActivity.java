package net.eirene.calcbill;

//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;

import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//public class MainActivity extends AppCompatActivity {

//    private TextView TextViewTotal, TextViewFinalcost
//    private EditText editTextkwh, editTextumberrebate,
//    private Button buttonCalculate, buttonReset

//    private EditText editTextKwh, editTextNumberRebate;
//    private TextView textViewTotal, textViewFinalCost;
//    private Button buttonCalculate, buttonReset;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);

//        btnCalculate = findViewById(R.id.btnCalculate);
//        etNumber2 = findViewById(R.id.etNumber2);
//        atNumber1 = findViewById(R.id.etNumber1);
//        textview = findViewById(R.id.textview);
//        btnCalculate.setOnClickListener(this);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

//            textviewElectricCalculatorBill = findViewById(R.id.textViewElectricCalculatorBill);
//            TextViewTotal = findViewById(R.id.textViewTotal)
//            TextViewFinalcost = findViewById(R.id.textViewFinalcost)
//            editTextkwh = findViewById(R.id.editTextKwh)
//            editTextumberrebate = findViewById(R.id.editTextNumberRebate)
//            buttonCalculate = findViewById(R.id.buttonCalculate)
//            buttonReset = findViewById(R.id.buttonReset)

//    }


//     @Override
//     public boolean onCreateOptionsMenu(Menu menu) {
//            MenuInflater inflater = getMenuInflater();
//           inflater.inflate(R.menu.menu, menu);
//            return true;
//     }



//     @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int selected = item.getItemId();

//        if(selected == R.id.menuAbout) {
//            Toast.makeText( this, "about clicked",Toast.LENGTH_SHORT).show();
//            openAboutPage();
//            return true;

//        }
//        return true;
//    }

//    private void openAboutPage() {
//        Intent intent = new Intent(MainActivity.this, About.class);
//        startActivity(intent);

//    }
//    private void SHOW() {

//   }
//}
//}

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textViewTotal, textViewFinalCost;
    private EditText editTextKwh, editTextNumberRebate;
    private Button buttonCalculate, buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        textViewTotal = findViewById(R.id.textViewTotal);
        textViewFinalCost = findViewById(R.id.textViewFinalCost);
        editTextKwh = findViewById(R.id.editTextKwh);
        editTextNumberRebate = findViewById(R.id.editTextNumberRebate);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonReset = findViewById(R.id.buttonReset);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calculateBill();
                } catch (Exception e) {
                    Log.e(TAG, "Error calculating bill", e);
                    Toast.makeText(MainActivity.this, "An error occurred while calculating the bill.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    private void calculateBill() {
        if (!validateInput()) {
            return;
        }

        String unitsStr = editTextKwh.getText().toString();
        String rebateStr = editTextNumberRebate.getText().toString();

        int units = Integer.parseInt(unitsStr);
        double rebate = Double.parseDouble(rebateStr) / 100;

        double totalCharge = 0.0;

        if (units <= 200) {
            totalCharge = units * 0.218;
        } else if (units <= 300) {
            totalCharge = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totalCharge = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            totalCharge = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }

        double finalCost = totalCharge - (totalCharge * rebate);

        textViewTotal.setText(String.format("Total: RM %.2f", totalCharge));
        textViewFinalCost.setText(String.format("Final Cost after Rebate: RM %.2f", finalCost));
    }

    private boolean validateInput() {
        String unitsStr = editTextKwh.getText().toString();
        String rebateStr = editTextNumberRebate.getText().toString();

        if (unitsStr.isEmpty()) {
            editTextKwh.setError("Please enter the number of units");
            return false;
        }

        if (rebateStr.isEmpty()) {
            editTextNumberRebate.setError("Please enter the rebate percentage");
            return false;
        }

        double rebate = Double.parseDouble(rebateStr);
        if (rebate < 0 || rebate > 5) {
            editTextNumberRebate.setError("Rebate percentage must be between 0% and 5%");
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuAbout) {
            openAboutPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void openAboutPage() {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    private void resetFields() {
        editTextKwh.setText("");
        editTextNumberRebate.setText("");
        textViewTotal.setText("");
        textViewFinalCost.setText("");
    }
}
