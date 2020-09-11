package com.e.app_namebattler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build

class MyOpenHelper (context: Context?) : SQLiteOpenHelper(context,DBName,null,VERSION) {
    //  データベースが作成された時に実行される処理
    override  fun onCreate(db: SQLiteDatabase){
        /**
         * テーブルを作成する
         * execSQLメソッドにCREATET TABLE命令を文字列として渡すことで実行される
         * 引数1 ・・・ NAME:列名 , TEXT：文字型 , PRIMATY KEY：テーブル内の行で重複無し
         * 引数2 ・・・ JOB:列名 , INTEGER:数値型
         * 引数3 ・・・ HP:列名 , INTEGER:数値型
         * 引数4　・・・ MP:列名 , INTEGER:数値型
         * 引数5　・・・STR:列名 , INTEGER:数値型
         * 引数6　・・・DEF：列名 , INTEGER：数値型
         * 引数7　・・・AGI：列名 , INTEGER：数値型
         * 引数8 ・・・LUCK：列名 , INTEGER：数値型
         * 引数9 ・・・CREATE_AT , NULL:NULL値
         */
        db.execSQL("CREATE TABLE CHARACTER (" +
                "NAME TEXT(20) PRIMARY KEY NOT NULL," +
                "JOB INTEGER NOT NULL," +
                "HP INTEGER NOT NULL," +
                "MP INTEGER NOT NULL," +
                "STR INTEGER NOT NULL," +
                "DEF INTEGER NOT NULL," +
                "AGI INTEGER NOT NULL," +
                "LUCK INTEGER NOT NULL," +
                "CREATE_AT BLOB)")

    }

    // データベースをバージョンアップした時に実行される処理
    override fun onUpgrade(db:SQLiteDatabase, oldVersion: Int, newVersion :Int){
    // テーブルを削除する
        db.execSQL(("DROP TABLE IF EXISTS CHARACTER"))
    // 新しくテーブルを作成する
        onCreate(db)
    }
    // データベースが開かれた時に実行される処理
    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    companion object{
        // データベース自体の名前(テーブル名ではない)
        private const val DBName = "aya_00"
        // データベースのバージョン(2,3と挙げていくとonUpgradeメソッドが実行される)
        private const val VERSION = 1
    }


}