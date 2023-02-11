package com.github.thttnt.mahjongutils.entity;


import com.github.thttnt.mahjongutils.enums.TileColor;

/**
 * @author HaotianTu
 * create at 2023/2/9
 **/
public record Tile(TileType type, boolean isDora) {
    public Tile(TileColor color, int index, boolean isDora) {
        this(new TileType(color, index), isDora);
    }

    @Override
    public String toString() {
        int strIndex = isDora ? 0 : type.index();
        char colorChar = switch (type.color()) {
            case WAN -> 'm';
            case TIAO -> 's';
            case TONG -> 'p';
            case ZI -> 'z';
        };
        return strIndex + "" + colorChar;
    }

    public static Tile parseString(String str) {
        if (str.length() != 2) {
            throw new IllegalArgumentException("Tile string must be 2 characters long");
        }
        char indexChar = str.charAt(0);
        char colorChar = str.charAt(1);
        if (indexChar > '9' || indexChar < '0') {
            throw new IllegalArgumentException("Tile index must be a number");
        }
        int index = indexChar - '0';

        boolean dora = index == 0;
        index = dora ? 5 : index;

        if (colorChar == 'z' && index > 7) {
            throw new IllegalArgumentException("Zi tile index must be between 1 and 7");
        }

        return switch (colorChar) {
            case 'm' -> new Tile(TileColor.WAN, index, dora);
            case 'p' -> new Tile(TileColor.TONG, index, dora);
            case 's' -> new Tile(TileColor.TIAO, index, dora);
            case 'z' -> new Tile(TileColor.ZI, index, dora);
            default -> throw new IllegalArgumentException("Tile color must be m, p, s or z");
        };
    }
}
