package com.example.mura.criminalntentd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mura.criminalntentd.database.CrimeBaseHelper;
import com.example.mura.criminalntentd.database.CrimeCursorWrapper;
import com.example.mura.criminalntentd.database.CrimeDbShema;
import com.example.mura.criminalntentd.database.CrimeDbShema.CrimeTable.Cols;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    //возврашение списка преступлений
    public List<Crime> getmCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }

    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeDbShema.CrimeTable.NAME,null,values);
    }

    //запись и обновление бд
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(Cols.UUID,crime.getmId().toString());
        values.put(Cols.TITLE,crime.getmTitle());
        values.put(Cols.DATE,crime.getmDate().getTime());
        values.put(Cols.SOLVED,crime.ismSolved() ? 1 : 0);
        values.put(Cols.SUSPECT,crime.getmSuspect());
        return values;
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbShema.CrimeTable.NAME,values, Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    //чтенеие данных
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeDbShema.CrimeTable.NAME,
                null, //все столбы
                whereClause,
                whereArgs,
                null,null,null
        );
        return new CrimeCursorWrapper(cursor);
    }

}
