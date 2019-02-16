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
import java.util.Random;

/*
* Author: Matthew London
* Professor: John P. Baugh
* Class: CIS 436
* Project: Slot Machine - Project 1
* */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    //Global variables
    static String Toasted = "You Must bet an amount between 100 and 500!";
    static String OutOfBank = "You ran out of money, you are not lucky.";
    static String NewGame = "New Game Started.";
    static int newGameBankAmount = 0;
    static int randomBound = 8;
    static int smallPrize = 10;
    static int midPrize = 40;
    static int bigPrize = 100;
    static int grandPrize = 1000;
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

        //Creating identifiers for changing UI components
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
        pullLeverBtn.setClickable(false);
    }

    @Override
    public void onClick(View v){

        //Switch case that calls the handler of the button caller
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

    //Pre: The "Set Value" button must have been pressed by its onClick listener
    //Post: Rejects the user if the initial value is within bounds,
    //      otherwise posts the input to the bank and allows the lever button to be pulled
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
            pullLeverBtn.setClickable(true);
            setValueAmount.setEnabled(false);
        }
    }

    //Pre: The "New Game" button must have been pressed by its onClick listener,
    //     or the user's bank falls below 0 or above 1000
    //Post: Disables the pull lever btn and re-enables the bank input dialogue
    private void newGameHandler(boolean isOutOfBank){
        if (isOutOfBank) {
            Toast.makeText(MainActivity.this, OutOfBank , Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, NewGame , Toast.LENGTH_LONG).show();
        }
        setValueAmount.setText("");
        currentBank = newGameBankAmount;
        updateBankText(currentBank);
        setValueBtn.setClickable(true);
        pullLeverBtn.setClickable(false);
        setValueAmount.setEnabled(true);
    }

    //Pre: The "Pull Lever" button must have been pressed by its onClick listener
    //Post: An iteration of the slot machine game is completed,
    //      -5 from the bank per pull and rewards are calculated in another function
    private void slotsHandler(){
        int numOne, numTwo, numThree;
        Random rnd = new Random();
        numOne = rnd.nextInt(randomBound);
        numOne++;
        numTwo = rnd.nextInt(randomBound);
        numTwo++;
        numThree = rnd.nextInt(randomBound);
        numThree++;

        slot1.setText(String.format("%s", numOne));
        slot2.setText(String.format("%s", numTwo));
        slot3.setText(String.format("%s", numThree));

        currentBank -= 5;
        currentBank = checkForMatches(numOne, numTwo, numThree, currentBank);
        updateBankText(currentBank);

        //New Game logic implementation check after every pull
        if (currentBank == 0){
            newGameHandler(true);
        }
        else if (currentBank >= 1000){
            Toast.makeText(MainActivity.this, "Machine is cleared out!" , Toast.LENGTH_SHORT).show();
            newGameHandler(false);
        }
    }

    //Pre: A decent programmer (haha) placed these function calls after all executions that require an update to the bank status
    //     Examples include: Set Value button press, new game button press, and pull lever button press
    //Post: Bank status updated
    private void updateBankText(int amount){
        bankAmount.setText(String.format("$%s", amount));
    }

    //Pre: Pull lever button was pulled and this function was called by the main slot machine handler
    //Post: All logic is sorted and rewards the user based on number of random matches in the slot machine
    private int checkForMatches(int slot1, int slot2, int slot3, int inputBank){
        int highestMatchCounter = 0;
        int tempCounter = 0;
        ArrayList<Integer> matchList = new ArrayList<Integer>();

        //add all slots to a list for easier iteration
        matchList.add(slot1);
        matchList.add(slot2);
        matchList.add(slot3);

        //Check all number slots for identical matches in other slots...doesn't seem efficient, thoughts on how to improve?
        for (int i = 0; i < matchList.size(); i++){
            for (int j = 0; j < matchList.size(); j++){
                if (matchList.get(i) == matchList.get(j)){
                    tempCounter++;
                    highestMatchCounter = (tempCounter > highestMatchCounter) ? tempCounter : highestMatchCounter;
                }
            }
            tempCounter = 0;
        }


        //If-blocks below decide on the prize money based of the highest match
        if (highestMatchCounter == 2){
            //Toast.makeText(MainActivity.this, "Small Prize Awarded!" , Toast.LENGTH_SHORT).show();
            return inputBank += smallPrize;
        }

        else if (highestMatchCounter == 3){
            if (matchList.get(0) < 5){
                Toast.makeText(MainActivity.this, "Mid Prize Awarded!" , Toast.LENGTH_SHORT).show();
                return inputBank += midPrize;
            }
            else if (matchList.get(0) >= 5 && matchList.get(0) <= 8){
                Toast.makeText(MainActivity.this, "Big Prize Awarded!" , Toast.LENGTH_SHORT).show();
                return inputBank += bigPrize;
            }
            else {
                Toast.makeText(MainActivity.this, "Grand Prize Awarded!" , Toast.LENGTH_SHORT).show();
                return inputBank += grandPrize;
            }
        }

        else {
            return inputBank;
        }
    }


}
