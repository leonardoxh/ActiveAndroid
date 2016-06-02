package com.activeandroid;

/*
 * Copyright (C) 2010 Michael Pardo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Collection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.serializer.TypeSerializer;
import com.activeandroid.util.Log;

public final class Cache {

    //////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE MEMBERS
    //////////////////////////////////////////////////////////////////////////////////////

    private static Context sContext;

    private static ModelInfo sModelInfo;
    private static DatabaseHelper sDatabaseHelper;

    private static boolean sIsInitialized = false;

    //////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    //////////////////////////////////////////////////////////////////////////////////////

    private Cache() {
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    public static synchronized void initialize(Configuration configuration) {
        if (sIsInitialized) {
            Log.v("ActiveAndroid already initialized.");
            return;
        }

        sContext = configuration.getContext();
        sModelInfo = new ModelInfo(configuration);
        sDatabaseHelper = new DatabaseHelper(configuration);

        openDatabase();

        sIsInitialized = true;

        Log.v("ActiveAndroid initialized successfully.");
    }

    public static synchronized void dispose() {
        closeDatabase();

        sModelInfo = null;
        sDatabaseHelper = null;

        sIsInitialized = false;

        Log.v("ActiveAndroid disposed. Call initialize to use library.");
    }

    // Database access

    public static boolean isInitialized() {
        return sIsInitialized;
    }

    public static synchronized SQLiteDatabase openDatabase() {
        return sDatabaseHelper.getWritableDatabase();
    }

    public static synchronized void closeDatabase() {
        sDatabaseHelper.close();
    }

    // Context access

    public static Context getContext() {
        return sContext;
    }

    // Model cache
    public static synchronized Collection<TableInfo> getTableInfos() {
        return sModelInfo.getTableInfos();
    }

    public static synchronized TableInfo getTableInfo(Class<? extends Model> type) {
        return sModelInfo.getTableInfo(type);
    }

    public static synchronized TypeSerializer getParserForType(Class<?> type) {
        return sModelInfo.getTypeSerializer(type);
    }

    public static synchronized String getTableName(Class<? extends Model> type) {
        return sModelInfo.getTableInfo(type).getTableName();
    }
}
