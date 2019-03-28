package com.example.apatki.inclass08;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpenseApp extends Fragment {

    private static ArrayList<Expense> expenses1 = new ArrayList<>();
    TextView textView;
    ListView listView;
    private static final String KEY = "key";
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("expenses/");
//    public void expensesChanged(ArrayList<Expense> expenses){
//        expenses1 = expenses;
//    }

    public ExpenseApp() {
        // Required empty public constructor
    }
    OnAddButtonPressed mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddButtonPressed) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "should implement OnFragmentTextChange");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_expense_app, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Expense App");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses1.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Expense expense = ds.getValue(Expense.class);
                    expenses1.add(expense);
                }
                try {
                    if (expenses1.size() > 0) {
                        listView = getActivity().findViewById(R.id.listView);
                        listView.setVisibility(View.VISIBLE);
                        ExpenseAdapter adapter = new ExpenseAdapter(getActivity(), R.layout.expense_item, expenses1);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        });

                        textView = getActivity().findViewById(R.id.textView2);
                        textView.setVisibility(View.INVISIBLE);
//                        listView.setVisibility(View.VISIBLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mListener.itemClicked(position);
                            }
                        });
                    } else if (expenses1.isEmpty()) {
                        listView = getActivity().findViewById(R.id.listView);
//                        ExpenseAdapter adapter = new ExpenseAdapter(getActivity(), R.layout.expense_item, expenses1);
//                        listView.setAdapter(adapter);
                        listView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    expenses1.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView = getActivity().findViewById(R.id.listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myRef.child(expenses1.get(position).getExpenseID()).removeValue();
                expenses1.remove(position);
                listView = getActivity().findViewById(R.id.listView);
                ExpenseAdapter adapter = new ExpenseAdapter(getActivity(), R.layout.expense_item, expenses1);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("demo", "Clicked Item " + position);
                    }
                });
                mListener.itemLongClicked(expenses1);
                expenses1.clear();
                return true;
            }
        });

        ImageView button = getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddButtonClicked();
            }
        });

    }

    public interface OnAddButtonPressed{
        void onAddButtonClicked();
        void itemClicked(int position);
        void itemLongClicked(ArrayList<Expense> expenses2);
    }

}
