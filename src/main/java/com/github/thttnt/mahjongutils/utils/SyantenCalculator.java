package com.github.thttnt.mahjongutils.utils;

import com.github.thttnt.mahjongutils.entity.Tile;
import com.github.thttnt.mahjongutils.entity.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HaotianTu
 * create at 2023/2/11
 **/
public class SyantenCalculator {

    private class SearchResult implements Comparable<SearchResult> {
        public int mentsu;
        public int tatsu;
        public int alone;

        public SearchResult() {
            this(0, 0, 0);
        }

        public SearchResult(int mentsu, int tatsu, int alone) {
            this.mentsu = mentsu;
            this.tatsu = tatsu;
            this.alone = alone;
        }

        public void add(SearchResult other) {
            mentsu += other.mentsu;
            tatsu += other.tatsu;
            alone += other.alone;
        }

        @Override
        public int compareTo(SearchResult o) {
            if (mentsu != o.mentsu)
                return mentsu - o.mentsu;
            if (tatsu != o.tatsu)
                return tatsu - o.tatsu;
            if (alone != o.alone)
                return alone - o.alone;
            return 0;
        }

        public static SearchResult max(SearchResult a, SearchResult b) {
            return a.compareTo(b) > 0 ? a : b;
        }
    }

    private int[][] cloneArray(int[][] arr) {
        int[][] clone = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            clone[i] = arr[i].clone();
        }
        return clone;
    }

    private int calculateTileAmount(int[][] tiles) {
        int amount = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                amount += tiles[i][j];
            }
        }
        return amount;
    }

    private SearchResult searchHelper(int[] tiles, int index, boolean isJiHai, SearchResult result) {
        SearchResult tmp;
        SearchResult max = result;
        if (index == (isJiHai ? 7 : 9)) {
            return max;
        }
        if (tiles[index] == 0) {
            max = SearchResult.max(max, searchHelper(tiles, index + 1, isJiHai, result));
        }
        if (tiles[index] >= 3) {
            tiles[index] -= 3;
            tmp = searchHelper(tiles, index, isJiHai, new SearchResult(result.mentsu + 1, result.tatsu, result.alone));
            max = SearchResult.max(max, tmp);
            tiles[index] += 3;
        }
        if (tiles[index] >= 2) {
            tiles[index] -= 2;
            tmp = searchHelper(tiles, index, isJiHai, new SearchResult(result.mentsu, result.tatsu + 1, result.alone));
            max = SearchResult.max(max, tmp);
            tiles[index] += 2;
        }
        if (tiles[index] >= 1) {
            tiles[index] -= 1;
            tmp = searchHelper(tiles, index, isJiHai, new SearchResult(result.mentsu, result.tatsu, result.alone + 1));
            max = SearchResult.max(max, tmp);
            tiles[index] += 1;
        }
        if (!isJiHai) {
            if (index < 7 && tiles[index] > 0 && tiles[index + 1] > 0 && tiles[index + 2] > 0) {
                tiles[index]--;
                tiles[index + 1]--;
                tiles[index + 2]--;
                tmp = searchHelper(tiles, index, isJiHai, new SearchResult(result.mentsu + 1, result.tatsu, result.alone));
                max = SearchResult.max(max, tmp);
                tiles[index]++;
                tiles[index + 1]++;
                tiles[index + 2]++;
            }
            if (index < 8 && tiles[index] > 0 && tiles[index + 1] > 0) {
                tiles[index]--;
                tiles[index + 1]--;
                tmp = searchHelper(tiles, index, isJiHai, new SearchResult(result.mentsu, result.tatsu + 1, result.alone));
                max = SearchResult.max(max, tmp);
                tiles[index]++;
                tiles[index + 1]++;
            }
            if (index < 7 && tiles[index] > 0 && tiles[index + 2] > 0) {
                tiles[index]--;
                tiles[index + 2]--;
                tmp = searchHelper(tiles, index, isJiHai, new SearchResult(result.mentsu, result.tatsu + 1, result.alone));
                max = SearchResult.max(max, tmp);
                tiles[index]++;
                tiles[index + 2]++;
            }
        }
        return max;
    }

    private SearchResult search(int[][] tiles) {
        SearchResult result = new SearchResult();
        result.add(searchHelper(tiles[0], 0, false, new SearchResult()));
        result.add(searchHelper(tiles[1], 0, false, new SearchResult()));
        result.add(searchHelper(tiles[2], 0, false, new SearchResult()));
        result.add(searchHelper(tiles[3], 0, true, new SearchResult()));
        return result;
    }


    private int calculateSyanten(int[][] tiles) {
        int tileAmount = calculateTileAmount(tiles);
        int requiredMentsu = tileAmount / 3;
        int minSyanten = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                int[][] clonedTiles = cloneArray(tiles);
                clonedTiles[i][j] -= Math.min(clonedTiles[i][j], 2);
                SearchResult result = search(clonedTiles);
                if (tileAmount % 3 == 1) {
                    result.alone++;
                }

                int syanten = -1;
                while (result.mentsu < requiredMentsu) {
                    if (result.tatsu > 0 && result.alone > 0) {
                        result.tatsu--;
                        result.alone--;
                        result.mentsu++;
                        syanten++;
                        continue;
                    }
                    if (result.tatsu > 0 && result.alone == 0) {
                        result.tatsu -= 2;
                        result.alone++;
                        result.mentsu++;
                        syanten++;
                        continue;
                    }
                    if (result.tatsu == 0 && result.alone > 0) {
                        result.alone -= 2;
                        result.tatsu++;
                        syanten++;
                    }
                }
                if (result.alone > 0) {
                    syanten += result.alone;
                }
                minSyanten = Math.min(minSyanten, syanten);
            }
        }
        return minSyanten;
    }

    public int calculateSyanten(List<Tile> tiles) {
        int[][] tileArray = Tiles.toHotCode(tiles.stream().map(Tile::type).toList());
        return calculateSyanten(tileArray);
    }

    private List<TileType> calculateHikari13(int[][] tiles, int[] self, int originSyanten) {
        List<TileType> waitTile = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == 3 && j >= 7) {
                    continue;
                }
                if (i == self[0] && j == self[1]) {
                    continue;
                }
                if (i != 3 &&
                        (j < 2 || tiles[i][j - 2] == 0) &&
                        (j < 1 || tiles[i][j - 1] == 0) &&
                        (tiles[i][j] == 0) &&
                        (j > 7 || tiles[i][j + 1] == 0) &&
                        (j > 6 || tiles[i][j + 2] == 0)
                ) {
                    continue;
                }
                tiles[i][j]++;
                int syanten = calculateSyanten(tiles);
                if (syanten < originSyanten) {
                    waitTile.add(Tiles.parseId(i * 9 + j));
                }
                tiles[i][j]--;
            }
        }
        return waitTile;
    }

    public Map<TileType, List<TileType>> calculateHikari(List<Tile> tiles) {
        Map<TileType, List<TileType>> waitMap = new HashMap<>();
        int[][] tileArray = Tiles.toHotCode(tiles.stream().map(Tile::type).toList());
        int syanten = calculateSyanten(tileArray);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                if (tileArray[i][j] == 0) {
                    continue;
                }
                tileArray[i][j]--;
                if (calculateSyanten(tileArray) == syanten) {
                    waitMap.put(Tiles.parseId(i * 9 + j), calculateHikari13(tileArray, new int[]{i, j}, syanten));
                }
                tileArray[i][j]++;
            }
        }
        return waitMap;
    }
}
