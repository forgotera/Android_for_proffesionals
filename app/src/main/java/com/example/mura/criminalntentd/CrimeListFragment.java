package com.example.mura.criminalntentd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.UUID;

public class CrimeListFragment extends Fragment {
 private RecyclerView mCrimeRecyclerView;
 private CrimeAdapter mAdapter;
 private ImageView mSolvedImageView;
 private int position;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState){
   View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
   mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
   mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

   updateUI();
   return view;
  }


 private void updateUI() {
   CrimeLab crimeLab = CrimeLab.get(getActivity());
   List<Crime> crimes = crimeLab.getmCrimes();

   if(mAdapter == null) {
       mAdapter = new CrimeAdapter(crimes);
       mCrimeRecyclerView.setAdapter(mAdapter);
   }else{
       mAdapter.notifyItemChanged(position);
   }
 }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

 //Holder созадет элементы textView и добавляет их на экран
  private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
      private TextView mTitleTextView;
      private TextView mDateTextView;
      private Crime mCrime;

      public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
          super(inflater.inflate(R.layout.list_item_crime,parent,false));
          mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
          mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
          mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
          itemView.setOnClickListener(this);
   }

   @Override
   public void onClick(View view){
       position = getAdapterPosition();
       Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getmId());
       startActivity(intent);
   }

   public void bind(Crime crime){
         mCrime = crime;
         mTitleTextView.setText(mCrime.getmTitle());
         mDateTextView.setText(mCrime.getmDate().toString());
         mSolvedImageView.setVisibility(crime.ismSolved()? View.VISIBLE : View.INVISIBLE );

     }

  }

  //создание нового объекта ViewHolder
  private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
   private List<Crime> mCrimes;

   public CrimeAdapter(List<Crime> crimes){
    mCrimes = crimes;
   }

   @NonNull
   @Override
   //вызывается когда требуется новое представление для отображеня
   public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    return new CrimeHolder(layoutInflater,parent);
   }

   //вызывается при выставлении элемента на экран
   @Override
   public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
       Crime crime = mCrimes.get(position);
       holder.bind(crime);
   }

   @Override
   public int getItemCount() {
    return mCrimes.size();
   }

  }

}
