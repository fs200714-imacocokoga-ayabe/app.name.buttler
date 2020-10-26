package com.e.app_namebattler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyOpenHelper (context: Context?) : SQLiteOpenHelper(context, DBNmae,null, VERSION) {

    companion object {
        //データベースのバージョン
        const val VERSION = 1
        //データベース情報
        const val DBNmae = "Aya.db01"//データベース名
        const val TABLE_NAME = "CHARACTER"//テーブル名
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

        //テーブルの作成を行う変数
        //         * 引数1 ・・・ NAME:列名 , TEXT：文字型 , PRIMARY KEY：テーブル内の行で重複無し
        //         * 引数2 ・・・ JOB:列名 , INTEGER:数値型
        //         * 引数3 ・・・ HP:列名 , INTEGER:数値型
        //         * 引数4　・・・ MP:列名 , INTEGER:数値型
        //         * 引数5　・・・STR:列名 , INTEGER:数値型
        //         * 引数6　・・・DEF：列名 , INTEGER：数値型
        //         * 引数7　・・・AGI：列名 , INTEGER：数値型
        //         * 引数8 ・・・LUCK：列名 , INTEGER：数値型
        //         * 引数9 ・・・CREATE_AT , NULL:NULL値
        private const val SQL_CREATE_ENTRIES = ("CREATE TABLE " + TABLE_NAME + " (" +
       // private const val SQL_CREATE_ENTRIES = ("CREATE TABLE IF NOT EXISTS CHARACTER (" +
                "NAME TEXT(20) NOT NULL PRIMARY KEY," +
                "JOB TEXT NOT NULL," +
                "HP INTEGER NOT NULL," +
                "MP INTEGER NOT NULL," +
                "STR INTEGER NOT NULL," +
                "DEF INTEGER NOT NULL," +
                "AGI INTEGER NOT NULL," +
                "LUCK INTEGER NOT NULL," +
                "CREATE_AT NULL)")

        //テーブルの消去を行う変数
        //private const val SQL_DELETE_ENTRIES= "DROP TABLE IF EXISTS" + TABLE_NAME

    }

    /*テーブルが存在しないときに呼び出す
        * execSQLでクエリSQL文を実行しDB構造を決定する*/
    override  fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    //DBバージョンが上がったときの処理
    override fun onUpgrade(db:SQLiteDatabase, oldVersion: Int, newVersion :Int) {
        // テーブルを削除する
        db.execSQL("DROP TABLE IF EXISTS CHARACTER")
        // 新しくテーブルを作成する
        onCreate(db)
    }

    //DBバージョンが下がったときの処理
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onDowngrade(db, oldVersion, newVersion)
    }

    //カラムをテーブルに追加する
    fun saveData(db: SQLiteDatabase, name: String, job: String, hp: Int,mp: Int, str: Int, def: Int, agi: Int, luck: Int, create_at: String) {
   // fun saveData(db: SQLiteDatabase, name: String, job: String, hp: Int,mp: Int, str: Int, def: Int, agi: Int, luck: Int) {
        val values: ContentValues = ContentValues()
        values.put(NAME, name)
        values.put(JOB, job)
        values.put(HP, hp)
        values.put(MP, mp)
        values.put(STR, str)
        values.put(DEF, def)
        values.put(AGI, agi)
        values.put(LUCK, luck)
        values.put(CREATE_AT, create_at)
        db.insertOrThrow(TABLE_NAME, null, values)
    }

}

//    // データベースが開かれた時に実行される処理
//    override fun onOpen(db: SQLiteDatabase?) {
//        super.onOpen(db)
//    }

