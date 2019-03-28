package com.example.apatki.inclass08;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddExpense extends Fragment {

    Double amount1;
    String name1;

    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    public AddExpense() {
        // Required empty public constructor
    }

    Interface1 data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            data = (Interface1) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "should implement OnFragmentTextChange");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Add Expense");
        final Expense expense = new Expense();
        Spinner spinner = getActivity().findViewById(R.id.spinner);
        spinner.setPrompt("Select Category");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] category = getResources().getStringArray(R.array.categoryArray);
                expense.category = category[position];
                Log.d("demo", expense.category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });
        final EditText edit = getActivity().findViewById(R.id.editText);
        final EditText edit2 = getActivity().findViewById(R.id.editText2);

        Button add = getActivity().findViewById(R.id.button3);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1 = String.valueOf(edit.getText());
                Log.d("beforeClick", name1);
                String x = edit2.getText().toString();
                if (x.equals("")){
                    Log.d("demo", "amount is null");
                    edit2.setError("Please Enter a Value");
                }else{
                    amount1 = Double.parseDouble(x);
                }
                if (name1.equals("")){
                    Toast.makeText(getActivity(), "Please enter a value", Toast.LENGTH_SHORT).show();
                    edit.setError("Please Enter a Value");
                }else{
                    expense.name = name1;
                    Log.d("Expense Name", expense.name);
                    expense.amount = amount1;
                    Log.d("Expense amount", expense.amount.toString());
                    expense.date = Calendar.getInstance().getTime();
                    Log.d("Expense date", expense.date.toString());
                    Log.d("demo", String.valueOf(expense));

                    DatabaseReference databaseReference = myRef.child("expenses");
                    String key = databaseReference.push().getKey();
                    expense.setExpenseID(key);
                    databaseReference.child(key).setValue(expense);
                    data.onExpenseAdded(expense);
                }
            }
        });

        Button close = getActivity().findViewById(R.id.button4);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.closed();
            }
        });

    }
    public interface Interface1{
        void onExpenseAdded(Expense expense);
        void closed();
    }

}
