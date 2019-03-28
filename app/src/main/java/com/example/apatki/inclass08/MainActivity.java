package com.example.apatki.inclass08;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ExpenseApp.OnAddButtonPressed, ShowExpense.Interface2, AddExpense.Interface1, EditExpense.Interface3 {

    ArrayList<Expense> expenses = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("expenses/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new ExpenseApp(), "tag_ExpenseApp").commit();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Expense expense = ds.getValue(Expense.class);
                    Log.d(TAG, expense.toString());
                    expenses.add(expense);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.toString());
            }
        });
    }

    @Override
    public void onAddButtonClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddExpense(), "tag_AddExpense").commit();
    }

    @Override
    public void itemClicked(int position) {
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment1 = new ShowExpense();
        ft1.replace(R.id.container, fragment1);
        ft1.commit();
        ((ShowExpense) fragment1).onExpenseSelected(expenses.get(position));
    }

    @Override
    public void itemLongClicked(ArrayList<Expense> expenses2) {
        expenses = expenses2;
    }

    @Override
    public void onExpenseAdded(Expense expense) {
//        expenses.add(expense);
//        Log.d("ArrayList",expenses.toString());
//        ArrayList<Expense> expenses3;
//        expenses3 = expenses;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ExpenseApp();
        ft.replace(R.id.container, fragment);
        ft.commit();
//        ((ExpenseApp) fragment).expensesChanged(expenses3);
    }

    @Override
    public void onExpenseChanged(Expense expense) {
//        expenses.add(expense);
//        Log.d("ArrayList",expenses.toString());
//        ArrayList<Expense> expenses3;
//        expenses3 = expenses;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ExpenseApp();
        ft.replace(R.id.container, fragment);
        ft.commit();
//        ((ExpenseApp) fragment).expensesChanged(expenses3);
    }

    @Override
    public void closed() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ExpenseApp();
        ft.replace(R.id.container, fragment);
        ft.commit();
//        ((ExpenseApp) fragment).expensesChanged(expenses);
    }

    @Override
    public void onClosed() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ExpenseApp();
        ft.replace(R.id.container, fragment);
        ft.commit();
//        ((ExpenseApp) fragment).expensesChanged(expenses);
    }

    @Override
    public void onEdit(Expense expense) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new EditExpense();
        ft.replace(R.id.container, fragment);
        ft.commit();
        ((EditExpense) fragment).expenseToEdit(expense);
    }

}

/*
    Part A: ExpenseApp screen(30 points)
    The main activity should start by displaying the ExpenseApp screen, with the following
    requirements:
        1. When the app first starts, there should be no expenses added in the list. So it should
            display the ExpenseApp screen with a message, “There is no expense to show,
            Please add your expenses from the menu.” (Figure 1(a)).
        2. The list of expenses should be stored in Firebase realtime database.
        3. The ExpenseApp screen should use a ListView/RecyclerView to display the list of
            expenses as shown in (Figure 1(b)).
            a) Long press on an item should delete the expense from the list. It should update
            Firebase, and refresh the ListView to indicate this change. A Toast should be
            displayed having the message, “Expense Deleted” (Figure 1(c, d)).
            b) Clicking on an expense item should display the ShowExpense screen, you
            should push the ExpenseApp screen on the screen stack.
        4. Clicking on the add (+) icon should start the AddExpense screen.

    Part B: AddExpense screen (30 points)
    This screen should enable user to add a new expense. You should complete the
    following tasks:
        1. The user should be able to enter the expense name, category and amount. The app
            should take the current date as the expense date. This information should be stored
            in an Expense Object.
        2. The categories should be in a selection pane as in the Figure 2(d). The categories
            you should include are: Groceries, Invoice, Transportation, Shopping, Rent, Trips,
            Utilities and Other.
        3. Clicking on “Add Expense” button should validate the user’s input and ensure that
            all the fields are provided. If any field is missing, display a Toast to indicate the
            missing field. If all the fields are provided correctly, save the fields as an Expense
            object, and add the new expense to Firebase. Then display the main activity with the
            added expense, as shown in Figure 2(f).


    Part C: ShowExpenses screen (15 Points):
    Implement the following requirements:
        1. When the user clicks on an expense item in the ExpenseApp screen, the
            ShowExpenses screen should be started to show the details of selected expense
            item. as shown in Figure 3(b).
        2. If the user clicks Edit Expense button, it should start EditExpense screen with
            preloaded values.
        3. Upon clicking Close button, the screen should be closed and should navigate back
            to the ExpenseApp screen.

    Part D: EditExpense screen (25 points)
    Implement the following requirements:
        1. It is identical to the Add Expense screen with preloaded values for the particular
            expense.
        2. If the user makes changes to the values, and clicks on Save button, it should update
            the corresponding values to Firebase, and get back to the main screen with updated
            values.

 */