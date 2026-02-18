package com.example.parchismania.ui.widgets

import com.example.parchismania.engine.PlayerColor

data class BoardPos(val x: Float, val y: Float)

object BoardPositions {

    val homeBases: Map<PlayerColor, List<BoardPos>> = mapOf(
        PlayerColor.YELLOW to listOf(
            BoardPos(1.5f, 1.5f), BoardPos(4f, 1.5f), BoardPos(1.5f, 4f), BoardPos(4f, 4f)
        ),
        PlayerColor.BLUE to listOf(
            BoardPos(9.5f, 1.5f), BoardPos(12f, 1.5f), BoardPos(9.5f, 4f), BoardPos(12f, 4f)
        ),
        PlayerColor.RED to listOf(
            BoardPos(9.5f, 9.5f), BoardPos(12f, 9.5f), BoardPos(9.5f, 12f), BoardPos(12f, 12f)
        ),
        PlayerColor.GREEN to listOf(
            BoardPos(1.5f, 9.5f), BoardPos(4f, 9.5f), BoardPos(1.5f, 12f), BoardPos(4f, 12f)
        ),
    )

    val homeStretch: Map<PlayerColor, List<BoardPos>> = mapOf(
        PlayerColor.YELLOW to listOf(
            BoardPos(1f, 7f), BoardPos(2f, 7f), BoardPos(3f, 7f), BoardPos(4f, 7f), BoardPos(5f, 7f), BoardPos(6f, 7f), BoardPos(7f, 7f)
        ),
        PlayerColor.BLUE to listOf(
            BoardPos(7f, 1f), BoardPos(7f, 2f), BoardPos(7f, 3f), BoardPos(7f, 4f), BoardPos(7f, 5f), BoardPos(7f, 6f), BoardPos(7f, 7f)
        ),
        PlayerColor.RED to listOf(
            BoardPos(13f, 7f), BoardPos(12f, 7f), BoardPos(11f, 7f), BoardPos(10f, 7f), BoardPos(9f, 7f), BoardPos(8f, 7f), BoardPos(7f, 7f)
        ),
        PlayerColor.GREEN to listOf(
            BoardPos(7f, 13f), BoardPos(7f, 12f), BoardPos(7f, 11f), BoardPos(7f, 10f), BoardPos(7f, 9f), BoardPos(7f, 8f), BoardPos(7f, 7f)
        ),
    )

    val mainPath: Map<Int, BoardPos> = buildMap {
        val pathPositions = listOf(
            1 to 6, 2 to 6, 3 to 6, 4 to 6, 5 to 6,
            6 to 5, 6 to 4, 6 to 3, 6 to 2, 6 to 1, 6 to 0,
            7 to 0, 8 to 0,
            8 to 1, 8 to 2, 8 to 3, 8 to 4, 8 to 5,
            9 to 6, 10 to 6, 11 to 6, 12 to 6, 13 to 6,
            14 to 6,
            14 to 7, 14 to 8,
            13 to 8, 12 to 8, 11 to 8, 10 to 8, 9 to 8,
            8 to 9, 8 to 10, 8 to 11, 8 to 12, 8 to 13, 8 to 14,
            7 to 14, 6 to 14,
            6 to 13, 6 to 12, 6 to 11, 6 to 10, 6 to 9,
            5 to 8, 4 to 8, 3 to 8, 2 to 8, 1 to 8,
            0 to 8, 0 to 7, 0 to 6,
            1 to 6, 2 to 6, 3 to 6, 4 to 6, 5 to 6,
            6 to 5, 6 to 4, 6 to 3, 6 to 2, 6 to 1, 6 to 0,
            7 to 0, 8 to 0,
            8 to 1, 8 to 2, 8 to 3, 8 to 4, 8 to 5,
        )
        for (i in 1..68) {
            val idx = (i - 1) % pathPositions.size
            val (c, r) = pathPositions[idx]
            put(i, BoardPos(c.toFloat(), r.toFloat()))
        }
    }
}
