package com.example.apatki.inclass08;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ShowExpense extends Fragment {
    Expense expense1;
    public void onExpenseSelected(Expense expense){
        expense1 = expense;
    }
    Interface2 data1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("demo", "AFragment: onAttach");
        try {
            data1 = (Interface2) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "should implement OnFragmentTextChange");
        }
    }

    public ShowExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_expense, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Show Expense");
        TextView name = getActivity().findViewById(R.id.textView11);
        TextView category = getActivity().findViewById(R.id.textView12);
        TextView amount = getActivity().findViewById(R.id.textView13);
        TextView date = getActivity().findViewById(R.id.textView14);
        name.setText(expense1.name);
        category.setText(expense1.category);
        amount.setText(String.valueOf(expense1.amount));
        date.setText(String.valueOf(expense1.date));

        final Button button = (Button) getActivity().findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1.onClosed();
            }
        });

        final Button button1 = (Button) getActivity().findViewById(R.id.button5);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1.onEdit(expense1);
            }
        });
    }

    public interface Interface2{
        void onClosed();
        void onEdit(Expense expense);
    }
}
