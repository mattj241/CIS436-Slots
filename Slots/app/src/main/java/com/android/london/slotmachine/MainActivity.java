package com.android.london.slotmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    String Toasted = "Must bet an amount between 100 and 500";
    View mainView;

    Button setValueBtn;
    Button pullLeverBtn;
    Button newGameBtn;
    EditText bettingInput;

    int currentBet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = getLayoutInflater().inflate(R.layout.activity_main, null, false);

        setValueBtn = findViewById(R.id.SetValueBtn);
        pullLeverBtn = findViewById(R.id.PullBtn);
        newGameBtn = findViewById(R.id.NewGameBtn);
        bettingInput = findViewById(R.id.BetAmount);

        setValueBtn.setOnClickListener(this);
        pullLeverBtn.setOnClickListener(this);
        newGameBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.SetValueBtn:
                int amount;
                String input = bettingInput.getText().toString();
                amount = (input.equals("")) ? 0 : Integer.parseInt(input);
                if (amount < 100 || amount > 500){
                    Toast.makeText(MainActivity.this, Toasted , Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, String.format("You just bet %d", amount) , Toast.LENGTH_LONG).show();
                    currentBet = amount;
                    setValueBtn.setActivated(false);
                    bettingInput.setActivated(false);
                }
        }
    }

}
