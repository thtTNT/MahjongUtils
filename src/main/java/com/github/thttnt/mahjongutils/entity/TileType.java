package com.github.thttnt.mahjongutils.entity;


import com.github.thttnt.mahjongutils.enums.TileColor;

/**
 * @author HaotianTu
 * create at 2023/2/9
 **/
public record TileType(TileColor color, int index) {

    public String toString() {
        char colorChar = switch (this.color()) {
            case WAN -> 'm';
            case TIAO -> 's';
            case TONG -> 'p';
            case ZI -> 'z';
        };
        return index + "" + colorChar;
    }
}
