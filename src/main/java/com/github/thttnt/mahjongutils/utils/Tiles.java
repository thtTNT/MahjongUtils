package com.github.thttnt.mahjongutils.utils;


import com.github.thttnt.mahjongutils.entity.Tile;
import com.github.thttnt.mahjongutils.entity.TileType;
import com.github.thttnt.mahjongutils.enums.TileColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HaotianTu
 * create at 2023/2/9
 **/
public class Tiles {
    public static final List<TileType> TYPES = List.of(
            new TileType(TileColor.WAN, 1),
            new TileType(TileColor.WAN, 2),
            new TileType(TileColor.WAN, 3),
            new TileType(TileColor.WAN, 4),
            new TileType(TileColor.WAN, 5),
            new TileType(TileColor.WAN, 6),
            new TileType(TileColor.WAN, 7),
            new TileType(TileColor.WAN, 8),
            new TileType(TileColor.WAN, 9),
            new TileType(TileColor.TIAO, 1),
            new TileType(TileColor.TIAO, 2),
            new TileType(TileColor.TIAO, 3),
            new TileType(TileColor.TIAO, 4),
            new TileType(TileColor.TIAO, 5),
            new TileType(TileColor.TIAO, 6),
            new TileType(TileColor.TIAO, 7),
            new TileType(TileColor.TIAO, 8),
            new TileType(TileColor.TIAO, 9),
            new TileType(TileColor.TONG, 1),
            new TileType(TileColor.TONG, 2),
            new TileType(TileColor.TONG, 3),
            new TileType(TileColor.TONG, 4),
            new TileType(TileColor.TONG, 5),
            new TileType(TileColor.TONG, 6),
            new TileType(TileColor.TONG, 7),
            new TileType(TileColor.TONG, 8),
            new TileType(TileColor.TONG, 9),
            new TileType(TileColor.ZI, 1),
            new TileType(TileColor.ZI, 2),
            new TileType(TileColor.ZI, 3),
            new TileType(TileColor.ZI, 4),
            new TileType(TileColor.ZI, 5),
            new TileType(TileColor.ZI, 6),
            new TileType(TileColor.ZI, 7)
    );

    public static int toId(TileType tileType) {
        return TYPES.indexOf(tileType);
    }

    public static TileType parseId(int id) {
        return TYPES.get(id);
    }

    public static int[][] toHotCode(List<TileType> tileTypes) {
        int[][] hotCode = new int[4][9];
        for (TileType tileType : tileTypes) {
            int id = toId(tileType);
            hotCode[id / 9][id % 9]++;
        }
        return hotCode;
    }

    public static List<Tile> parseTilesFromString(String str) {
        List<Integer> buffer = new ArrayList<>();
        List<Tile> result = new ArrayList<>();
        for (char chr : str.toCharArray()) {
            if (chr >= '0' && chr <= '9') {
                buffer.add(chr - '0');
            } else {
                for (int i : buffer) {
                    result.add(Tile.parseString(i + "" + chr));
                }
                buffer.clear();
            }
        }
        return result;
    }

}
