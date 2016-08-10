package com.mywings.justolm.LocalUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mywings.justolm.Model.Area;
import com.mywings.justolm.Model.City;
import com.mywings.justolm.Model.Country;
import com.mywings.justolm.Model.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public class JustOLMHelper extends SQLiteOpenHelper {


    private static JustOLMHelper ourInstance;

    private SQLiteDatabase database;

    private String CREATE_COUNTRY = "CREATE TABLE IF NOT EXISTS Country(ID INTEGER, Name TEXT)";
    private String CREATE_STATE = "CREATE TABLE IF NOT EXISTS State (ID INTEGER, Name TEXT, CountryId INTEGER)";
    private String CREATE_CITY = "CREATE TABLE IF NOT EXISTS City (ID INTEGER, Name TEXT, StateId INTEGER)";
    private String CREATE_AREA = "CREATE TABLE IF NOT EXISTS Area (ID INTEGER, Name TEXT, CityId INTEGER)";


    private JustOLMHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        database = getReadableDatabase();
    }

    public static synchronized JustOLMHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if (null == ourInstance) {
            ourInstance = new JustOLMHelper(context, name, factory, version);
        }
        return ourInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUNTRY);
        db.execSQL(CREATE_STATE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_AREA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();
        Cursor cursor = database.query("Country", null, null, null, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    Country country = new Country();
                    country.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    country.setName(cursor.getString(cursor.getColumnIndex("Name")));
                    countries.add(country);
                } while (cursor.moveToNext());
            }
        }
        return countries;
    }

    public String getCountryId(String id) {
        Cursor cursor = database.query("Country", null, "ID=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("Name"));
        } else return "";
    }


    public String getStateId(String id) {
        Cursor cursor = database.query("State", null, "ID=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("Name"));
        } else return "";
    }


    public String getCityId(String id) {
        Cursor cursor = database.query("City", null, "ID=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("Name"));
        } else return "";
    }

    public String getAreaId(String id) {
        Cursor cursor = database.query("Area", null, "ID=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("Name"));
        } else return "";
    }


    public String getCountry(String id) {
        Cursor cursor = database.query("Country", null, "Name=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("ID"));
        } else return "";
    }


    public String getState(String id) {
        Cursor cursor = database.query("State", null, "Name=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("ID"));
        } else return "";
    }


    public String getCity(String id) {
        Cursor cursor = database.query("City", null, "Name=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("ID"));
        } else return "";
    }

    public String getArea(String id) {
        Cursor cursor = database.query("Area", null, "Name=?", new String[]{id.trim()}, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) return "";
            return cursor.getString(cursor.getColumnIndex("ID"));
        } else return "";
    }


    public synchronized List<State> getStates(int id) {
        List<State> states = new ArrayList<State>();
        Cursor cursor = database.query("State", null, "CountryId=?", new String[]{"" + id}, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    State state = State.getInstance();
                    state.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    state.setName(cursor.getString(cursor.getColumnIndex("Name")));
                    state.setCountryId(cursor.getInt(cursor.getColumnIndex("CountryId")));
                    states.add(state);
                } while (cursor.moveToNext());
            }
        }
        return states;
    }

    public synchronized List<City> getCities(int id) {
        List<City> cities = new ArrayList<City>();
        Cursor cursor = database.query("City", null, "StateId=?", new String[]{"" + id}, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    City city = City.getInstance();
                    city.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    city.setName(cursor.getString(cursor.getColumnIndex("Name")));
                    city.setStateId(cursor.getInt(cursor.getColumnIndex("StateId")));
                    cities.add(city);
                } while (cursor.moveToNext());
            }
        }
        return cities;
    }

    public synchronized List<Area> getAreas(int id) {
        List<Area> areas = new ArrayList<Area>();
        Cursor cursor = database.query("Area", null, "CityId=?", new String[]{"" + id}, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    Area area = Area.getInstance();
                    area.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    area.setName(cursor.getString(cursor.getColumnIndex("Name")));
                    area.setCityId(cursor.getInt(cursor.getColumnIndex("CityId")));
                    areas.add(area);
                } while (cursor.moveToNext());
            }
        }
        return areas;
    }

    public long countries(List<Country> countries) {
        long count = -1;
        for (int i = 0; i < countries.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", countries.get(i).getId());
            contentValues.put("Name", countries.get(i).getName());
            count = database.insertOrThrow("Country", null, contentValues);
        }
        return count;
    }

    public long states(List<State> states) {
        long count = -1;
        for (int i = 0; i < states.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", states.get(i).getId());
            contentValues.put("Name", states.get(i).getName());
            contentValues.put("CountryId", states.get(i).getCountryId());
            count = database.insertOrThrow("State", null, contentValues);
        }
        return count;
    }


    public long cities(List<City> cities) {
        long count = -1;
        for (int i = 0; i < cities.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", cities.get(i).getId());
            contentValues.put("Name", cities.get(i).getName());
            contentValues.put("StateId", cities.get(i).getStateId());
            count = database.insertOrThrow("City", null, contentValues);
        }
        return count;
    }

    public long areas(List<Area> areas) {
        long count = -1;
        for (int i = 0; i < areas.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", areas.get(i).getId());
            contentValues.put("Name", areas.get(i).getName());
            contentValues.put("CityId", areas.get(i).getCityId());
            count = database.insertOrThrow("Area", null, contentValues);
        }
        return count;
    }
}
