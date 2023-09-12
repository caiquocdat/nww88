package dat.mid.weather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dat.mid.weather.model.InfoResponse;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WeatherDatabase_1";
    private static final String TABLE_WEATHER = "weather";
    private static final int DATABASE_VERSION = 1;

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_TEMP_C = "temperatureCelsius";

    private static final String CREATE_TABLE_WEATHER = "CREATE TABLE " + TABLE_WEATHER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_COUNTRY + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_TEXT + " TEXT,"
            + COLUMN_TEMP_C + " REAL" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(db);
    }

    public boolean isNameDuplicate(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WEATHER,
                new String[]{COLUMN_ID},
                COLUMN_NAME + "=?",
                new String[]{name},
                null,
                null,
                null);

        int numRows = cursor.getCount();
        cursor.close();
        db.close();

        return numRows > 0;
    }
    public List<String> getAllNames() {
        List<String> namesList = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_NAME + " FROM " + TABLE_WEATHER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                namesList.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return namesList;
    }
    public void deleteWeather(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_WEATHER, COLUMN_NAME + "=?", new String[]{name});
        db.close();
    }

    public void insertWeather(InfoResponse infoLocationResponse) {
        if(!isNameDuplicate(infoLocationResponse.getName())) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_COUNTRY, infoLocationResponse.getCountry());
            values.put(COLUMN_NAME, infoLocationResponse.getName());
            values.put(COLUMN_TEXT, infoLocationResponse.getText());
            values.put(COLUMN_TEMP_C, infoLocationResponse.getTemperatureCelsius());

            db.insert(TABLE_WEATHER, null, values);
            db.close();
        } else {
            // Handle the duplicate text case here
        }
    }

    public List<InfoResponse> getAllWeather() {
        List<InfoResponse> weatherList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WEATHER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                InfoResponse infoLocationResponse = new InfoResponse();
                infoLocationResponse.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)));
                infoLocationResponse.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)));
                infoLocationResponse.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                infoLocationResponse.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT)));
                infoLocationResponse.setTemperatureCelsius(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TEMP_C)));

                weatherList.add(infoLocationResponse);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return weatherList;
    }
}
