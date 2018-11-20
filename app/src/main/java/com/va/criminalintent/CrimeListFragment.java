package com.va.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitletextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;

//        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.list_item_crime, parent, false));
//            itemView.setOnClickListener(this);
//
//            mTitletextView = itemView.findViewById(R.id.crime_title);
//            mDateTextView = itemView.findViewById(R.id.crime_date);
//        }

        public CrimeHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);

            mTitletextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitletextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    private class CrimeHolderReqPolice extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitletextView;
        private TextView mDateTextView;
        private Crime mCrime;
        
        public CrimeHolderReqPolice(View view) {
            super(view);
            itemView.setOnClickListener(this);

            mTitletextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitletextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View view;

            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_crime, parent, false);
                    viewHolder = new CrimeHolder(view);
                    break;
                case 1:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_crime_req_police, parent, false);
                    viewHolder = new CrimeHolderReqPolice(view);
                    break;
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime;

            switch (this.getItemViewType(position)) {
                case 0:
                    crime = mCrimes.get(position);
                    CrimeHolder crimeHolder = (CrimeHolder) holder;
                    crimeHolder.bind(crime);
                    break;
                case 1:
                    crime = mCrimes.get(position);
                    CrimeHolderReqPolice crimeHolderReqPolice = (CrimeHolderReqPolice) holder;
                    crimeHolderReqPolice.bind(crime);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if (crime.ismRequiresPolice())
                return 0;
            else return 1;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
