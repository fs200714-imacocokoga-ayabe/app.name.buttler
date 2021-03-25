package com.e.app_namebattler.view.party.party

import com.e.app_namebattler.view.party.player.Player


class Party {

    private var members: MutableList<Player> = ArrayList() // プレイヤーの入れ物
    private var party1: MutableList<Player> = ArrayList() // パーティ1の入れ物
    private var party2: MutableList<Player> = ArrayList() // パーティ2の入れ物
    lateinit var player: Player

    /**
     * パーティーメンバーをList で取得する
     */
    fun getMembers(): List<Player> {
        return members
    }

    /**
     * パーティー1,パーティー2にプレイヤーを追加する
     */
    fun appendPlayer(
        enemy01: Player,
        enemy02: Player,
        enemy03: Player,
        ally01: Player,
        ally02: Player,
        ally03: Player
    ) {

        val partyList: MutableList<Player> = mutableListOf(ally01,
            ally02,
            ally03,
            enemy01,
            enemy02,
            enemy03)
        for (player in partyList) {

            if (player.isMark) {
                party1.add(player) // playerがtrueならparty1に入れる
            } else {
                party2.add(player) // playerがfalseならparty2に入れる
            }
        }
    }

    /**
     * パーティーからプレイヤーを離脱させる
     * @param player : 離脱させるプレイヤー
     */
    fun removePlayer(player: Player) {

        if (player.isMark) {
            party1.remove(player)
        } else {
            party2.remove(player)
        }
    }

    /**
     * membersにplayerをセットする
     * @param player : プレイヤー
     */
    fun setMembers(player: Player) {
        members.add(player)
    }

    /**
     * membersにplayerを削除する
     * @param player : プレイヤー
     */
    fun removeMembers(player: Player) {
        members.remove(player)
    }

    /**
     * パーティ1を返す
     * @return party1 : パーティ１
     */
    fun getParty1(): List<Player> {
        return party1
    }

    /**
     * パーティ2を返す
     * @return party2 : パーティ2
     */
    fun getParty2(): List<Player> {
        return party2
    }

    fun selectMember(id: Int): Player? {
        for (i in members.indices) {
            if (members[i].getIdNumber() == id) {
                return members[i]
            }
        }
        return null
    }
}