package com.e.app_namebattler

class Priest (name:String):Player(name){

    constructor(name: String,job: String,hp: Int,mp: Int,str: Int,def: Int,agi: Int,luck: Int): this(name)

    override fun makeCharacter(name: String) {
        // 僧侶のパラメータを名前から生成する
        this.hp = getNumber(0, 120) + 80 // 80-200
        this.mp = getNumber(1, 30) + 20 // 20-50
        this.str = getNumber(2, 40) + 10 // 10-50
        this.def = getNumber(3, 60) + 10 // 10-70
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 20 // 20-60
    }
}