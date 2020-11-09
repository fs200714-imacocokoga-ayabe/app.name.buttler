
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class chenge_kotlin {
    /** 年齢が一致するデータを検索  */
    private fun searchByAge(db: SQLiteDatabase, age: Int): String? {
        // Cursorを確実にcloseするために、try{}～finally{}にする
        var cursor: Cursor? = null
        return try {
            // name_book_tableからnameとageのセットを検索する
            // ageが指定の値であるものを検索
            cursor = db.query(
                "name_book_table", arrayOf("name", "age"),
                "age = ?", arrayOf("" + age),
                null, null, null
            )

            // 検索結果をcursorから読み込んで返す
            readCursor(cursor)
        } finally {
            // Cursorを忘れずにcloseする
            cursor?.close()
        }
    }


    /** 検索結果の読み込み  */
    private fun readCursor(cursor: Cursor): String? {
        var result = ""

        // まず、Cursorからnameカラムとageカラムを
        // 取り出すためのインデクス値を確認しておく
        val indexName = cursor.getColumnIndex("name")
        val indexAge = cursor.getColumnIndex("age")

        // ↓のようにすると、検索結果の件数分だけ繰り返される
        while (cursor.moveToNext()) {
            // 検索結果をCursorから取り出す
            val name = cursor.getString(indexName)
            val age = cursor.getInt(indexAge)
            result += "$name さん($age 歳)\n"
        }
        return result
    }
   }