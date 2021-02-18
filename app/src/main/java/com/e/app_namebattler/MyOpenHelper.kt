package com.e.app_namebattler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyOpenHelper (context: Context?) : SQLiteOpenHelper(context, DBNmae,null, VERSION) {

    companion object {
        //データベースのバージョン
        const val VERSION = 1
        //データベース情報
        const val DBNmae = "Aya.db01"//データベース名
        private const val TABLE_NAME = "CHARACTER"//テーブル名
        private const val ENEMY_TABLE_NAME = "ENEMY"
        //カラムの種類
        const val NAME = "name"
        const val JOB = "job"
        const val HP = "hp"
        const val MP = "mp"
        const val STR = "str"
        const val DEF = "def"
        const val AGI = "agi"
        const val LUCK = "luck"
        const val CREATE_AT = "create_at"

        private const val SQL_CREATE_CHARACTER = ("CREATE TABLE " + TABLE_NAME + " (" +
                "NAME TEXT(20) NOT NULL PRIMARY KEY," +
                "JOB INTEGER NOT NULL," +
                "HP INTEGER NOT NULL," +
                "MP INTEGER NOT NULL," +
                "STR INTEGER NOT NULL," +
                "DEF INTEGER NOT NULL," +
                "AGI INTEGER NOT NULL," +
                "LUCK INTEGER NOT NULL," +
                "CREATE_AT NULL)")

        private const val SQL_CREATE_ENEMY = ("CREATE TABLE " + ENEMY_TABLE_NAME + " (" +
                "NAME TEXT(20) NOT NULL PRIMARY KEY," +
                "JOB INTEGER NOT NULL," +
                "HP INTEGER NOT NULL," +
                "MP INTEGER NOT NULL," +
                "STR INTEGER NOT NULL," +
                "DEF INTEGER NOT NULL," +
                "AGI INTEGER NOT NULL," +
                "LUCK INTEGER NOT NULL," +
                "CREATE_AT NULL)")
    }

    /*テーブルが存在しないときに呼び出す
        * execSQLでクエリSQL文を実行しDB構造を決定する*/
    override  fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CHARACTER)
        db.execSQL(SQL_CREATE_ENEMY)
    }

    //DBバージョンが上がったときの処理
    override fun onUpgrade(db:SQLiteDatabase, oldVersion: Int, newVersion :Int) {
        // テーブルを削除する
        db.execSQL("DROP TABLE IF EXISTS CHARACTER")
        db.execSQL("DROP TABLE IF EXISTS ENEMY")
        // 新しくテーブルを作成する
        onCreate(db)
    }

    //DBバージョンが下がったときの処理
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onDowngrade(db, oldVersion, newVersion)
    }
}

