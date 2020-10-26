package com.e.app_namebattler

class Fighter(name:String):Player(name) {

    override fun makeCharacter(name: String) {
        // 戦士のパラメータを名前から生成する
        this.hp = getNumber(0, 200) + 100 // 100-300
        this.mp = getNumber(1, 0) // 0
        this.str = getNumber(2, 70) + 30 // 30-100
        this.def = getNumber(3, 70) + 30 // 30-100
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 49) + 1 // 1-50
    }
}