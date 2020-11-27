package com.e.app_namebattler

class Wizard (name:String):Player(name){

    constructor(name: String,job: String,hp: Int,mp: Int,str: Int,def: Int,agi: Int,luck: Int): this(name)

    override fun makeCharacter(name: String) {
        // 魔法使いのパラメータを名前から生成する
        this.hp = getNumber(0, 100) + 50 // 50-150
       this. mp = getNumber(1, 50) + 30 // 30-80
        this.str = getNumber(2, 49) + 1 // 1-50
        this.def = getNumber(3, 49) + 1 // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 20 // 20-60
    }
}