package com.e.app_namebattler

class Ninja (name:String):Player(name){

    constructor(name: String,job: String,hp: Int,mp: Int,str: Int,def: Int,agi: Int,luck: Int): this(name)

    override fun makeCharacter(name: String) {
        // 忍者のパラメータを名前から生成する
        this.hp = getNumber(0, 100) + 70 // 70-170
        this.mp = getNumber(1, 20) + 10 // 10-30
        this.str = getNumber(2, 50) + 20 // 20-70
        this.def = getNumber(3, 49) + 1  // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 40 // 40-80
    }

}