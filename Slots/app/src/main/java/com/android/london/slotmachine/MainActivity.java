package com.android.london.slotmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    //Global variables
    static String Toasted = "You Must bet an amount between 100 and 500!";
    static String OutOfBank = "You ran out of money, you are not lucky.";
    static String NewGame = "New Game Started.";
    static int newGameBankAmount = 0;
    static int bound = 8;
    int currentBank;

    //Widgets
    Button setValueBtn;
    Button pullLeverBtn;
    Button newGameBtn;
    EditText setValueAmount;
    TextView bankAmount;
    TextView slot1, slot2, slot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setValueBtn = findViewById(R.id.SetValueBtn);
        pullLeverBtn = findViewById(R.id.PullBtn);
        newGameBtn = findViewById(R.id.NewGameBtn);
        setValueAmount = findViewById(R.id.SetValueAmount);
        bankAmount = findViewById(R.id.BankAmount);
        slot1 = findViewById(R.id.Slot1);
        slot2 = findViewById(R.id.Slot2);
        slot3 = findViewById(R.id.Slot3);

        setValueBtn.setOnClickListener(this);
        pullLeverBtn.setOnClickListener(this);
        newGameBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.SetValueBtn:
                setValueHandler();
                break;
            case R.id.NewGameBtn:
                newGameHandler(false);
                break;
            case R.id.PullBtn:
                slotsHandler();
                break;
            default:
                Toast.makeText(MainActivity.this, "Invalid Button Press..." , Toast.LENGTH_SHORT).show();
        }
    }

    private void setValueHandler(){
        int amount;
        String input = setValueAmount.getText().toString();
        amount = (input.equals("")) ? 0 : Integer.parseInt(input);
        if (amount < 100 || amount > 500){
            Toast.makeText(MainActivity.this, Toasted , Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this,
                    String.format("You just bet $%s!", amount) , Toast.LENGTH_SHORT).show();
            currentBank = amount;
            updateBankText(currentBank);
            setValueBtn.setClickable(false);
            setValueAmount.setEnabled(false);
        }
    }

    private void newGameHandler(boolean isOutOfBank){
        if (isOutOfBank) {
            Toast.makeText(MainActivity.this, OutOfBank , Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, NewGame , Toast.LENGTH_LONG).show();
        }
        currentBank = newGameBankAmount;
        setValueBtn.setClickable(true);
        setValueAmount.setEnabled(true);
    }

    private void slotsHandler(){
        int numOne, numTwo, numThree;
        Random rnd = new Random();
        numOne = rnd.nextInt(bound);
        numOne++;
        numTwo = rnd.nextInt(bound);
        numTwo++;
        numThree = rnd.nextInt(bound);
        numThree++;

        currentBank -= 5;
        currentBank = checkForMatches(numOne, numTwo, numThree, currentBank);
        updateBankText(currentBank);
        if (currentBank == 0){
            newGameHandler(true);
        }


        slot1.setText(String.format("%s", numOne));
        slot2.setText(String.format("%s", numTwo));
        slot3.setText(String.format("%s", numThree));
    }

    private void updateBankText(int amount){
        bankAmount.setText(String.format("%s", amount));
    }

    private int checkForMatches(int slot1, int slot2, int slot3, int inputBank){
        int matchCounter = 0;
        ArrayList<Integer> matchList = new ArrayList<Integer>();

        matchList.add(slot1);
        matchList.add(slot2);
        matchList.add(slot3);
    }


}
