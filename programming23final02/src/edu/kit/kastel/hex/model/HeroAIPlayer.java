package edu.kit.kastel.hex.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static edu.kit.kastel.hex.model.AIEntry.PLACE_INFO;

/**
 * A Hero-AI-player.
 *
 * @author uqeoc
 */
public class HeroAIPlayer extends ArtificialPlayer {
    private static final String NAME = "HeroAI";
    private final List<BoardCoordinate> extraFreeHexagon;

    HeroAIPlayer() {
        super(NAME);
        extraFreeHexagon = new ArrayList<>();
    }

    @Override
    public String nextAction(Hex hex) {

        //If HeroAI can win in one move, place the game piece accordingly to win.
        BoardCoordinate placeCoordinate = hex.getWinCoordinate(hex.getCurrentEntry());
        if (placeCoordinate != null) {
            return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
        }

        //If the opponent can win in one move, place the game piece accordingly to block the opponent. If there
        //are multiple moves to block, choose the move with the lowest x-coordinate. (If this is still ambiguous,
        //choose the move with the lowest y-coordinate.)
        placeCoordinate = hex.getWinCoordinate(hex.getCurrentEntry() == Entry.O ? Entry.X : Entry.O);
        if (placeCoordinate != null) {
            return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
        }

        //If there is no own game piece on the board yet, place the piece on the free hexagon with the
        //lowest x-coordinate (and if ambiguous, with the lowest y-coordinate).
        placeCoordinate = hex.getBoard().getTheMinimumAvailableCoordinateForHero();
        if (hex.getTurnCount() == 1) {
            return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
        }

        //If there exists exactly one path of free hexagons, place a game piece at the westernmost point of the path.
        // If there are multiple paths of free hexagons, place a game piece at the westernmost (and if still
        // ambiguous, northernmost) point on one of these paths.
        //If there is no path of free hexagons, recursively backtrack the placed game pieces,
        //considering the current piece as free. Execute the algorithm again
        placeCoordinate = getMostWestAvailableTile(hex.getCurrentEntry(), hex);
        if (placeCoordinate != null) {
            return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
        }

        //If there is no own game piece on the board yet, place the piece on the free hexagon with the
        //lowest x-coordinate (and if ambiguous, with the lowest y-coordinate).
        placeCoordinate = hex.getBoard().getTheMinimumAvailableCoordinateForHero();
        return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
    }

    /**
     * Determine the shortest path (number of hexagons) from the position of your own last game piece to
     * the eastern edge of the playing field.
     * If there exists exactly one path of free hexagons, return the tile at the westernmost point of the path.
     * If there are multiple paths of free hexagons, return the tile at the westernmost (and if still ambiguous,
     * northernmost) point on one of these paths.
     *
     * @param entry Entry for token.
     * @return a tile if it meets the requirement. Otherwise, return null.
     */
    private BoardCoordinate getMostWestAvailableTile(Entry entry, Hex hex) {
        if (hex.getHistories().size() <= 1) {
            return null;
        }

        int startIndex = hex.getHistories().size() - 2;
        BoardCoordinate tile = getMostWestAvailableTileFromOneTile(entry,
                hex.getHistories().get(startIndex).getCoordinate(), hex);
        this.extraFreeHexagon.add(hex.getHistories().get(startIndex).getCoordinate());

        // check every history for player from end to start.
        while (tile == null && startIndex > 1) {
            startIndex = startIndex - 2;
            tile = getMostWestAvailableTileFromOneTile(entry, hex.getHistories().get(startIndex).getCoordinate(), hex);
            extraFreeHexagon.add(hex.getHistories().get(startIndex).getCoordinate());
        }
        extraFreeHexagon.clear();
        return tile;
    }

    private BoardCoordinate getMostWestAvailableTileFromOneTile(Entry entry,
                                                                BoardCoordinate startCoordinate, Hex hex) {
        Queue<BoardCoordinate> neighbors = new LinkedList<>();
        if (startCoordinate == null || startCoordinate.getColumn() == hex.getBoard().getSideLength() - 1) {
            hex.getBoard().reset();
            return null;
        }

        //use BFS to search the shortest routes.
        neighbors.add(startCoordinate);
        startCoordinate.setSearchedLevel(0);
        boolean isSearching = true;
        while (!neighbors.isEmpty() && isSearching) {
            BoardCoordinate startCoordinateTemp = neighbors.poll();
            hex.getBoard().addNeighborsToQueue(Entry.EMPTY.getToken(), entry.getToken(), startCoordinateTemp,
                    neighbors, List.copyOf(this.extraFreeHexagon));
            if (hex.getBoard().isEastSideReached() && neighbors.peek() != null
                    && startCoordinateTemp.getSearchedLevel()
                    != Objects.requireNonNull(neighbors.peek()).getSearchedLevel()) {
                isSearching = false;
            }
        }

        // if there is no shortest route to east-side, then return null.
        if (hex.getBoard().getTileSearchedOnEastSide().isEmpty()) {
            hex.getBoard().reset();
            return null;
        }

        List<BoardCoordinate> endTiles = new LinkedList<>(hex.getBoard().getTileSearchedOnEastSide());

        //find the first empty start-tile of every route.
        List<List<BoardCoordinate>> allRoutes = searchTheWholeRoutesFromEndTiles(endTiles);
        if (allRoutes == null) {
            hex.getBoard().reset();
            return null;
        }
        List<BoardCoordinate> startTiles = new ArrayList<>();
        for (List<BoardCoordinate> route : allRoutes) {
            boolean firstEmptyStartTileHasFound = false;
            for (int index = 0; index < route.size() && !firstEmptyStartTileHasFound; index++) {
                if (route.get(index).getToken() == Entry.EMPTY.getToken()) {
                    startTiles.add(route.get(index));
                    firstEmptyStartTileHasFound = true;
                }
            }
        }

        if (startTiles.isEmpty()) {
            hex.getBoard().reset();
            return null;
        }
        startTiles.sort(Comparator.comparingInt(BoardCoordinate::getColumn)
                .thenComparingInt(BoardCoordinate::getRow));
        //reset all marked-info from BFS.
        hex.getBoard().reset();
        return startTiles.get(0);
    }

    private List<List<BoardCoordinate>> searchTheWholeRoutesFromEndTiles(List<BoardCoordinate> endTiles) {
        if (endTiles == null || endTiles.isEmpty()) {
            return null;
        }

        //if start-coordinate is added, then stop this recursion
        List<List<BoardCoordinate>> routes = new LinkedList<>();
        BoardCoordinate startTile = endTiles.get(0);
        if (startTile.getPrev().isEmpty()) {
            for (BoardCoordinate tile : endTiles) {
                List<BoardCoordinate> route = new LinkedList<>();
                route.add(tile);
                routes.add(route);
            }
            return routes;
        }

        // get all prev-tiles of end-tiles
        List<BoardCoordinate> prevTiles = new LinkedList<>();
        for (BoardCoordinate tile : endTiles) {
            prevTiles.addAll(tile.getPrev());
        }

        // create list for every prev-tile of end-tile and add the current end-tile at the end of it's prev-tile-list.
        List<BoardCoordinate> copyOfEndTiles = new LinkedList<>(endTiles);
        List<List<BoardCoordinate>> frontTilesOfRoutes = searchTheWholeRoutesFromEndTiles(prevTiles);
        for (List<BoardCoordinate> route : frontTilesOfRoutes) {
            boolean nextTilesHasAdded = false;
            for (int i = 0; i < copyOfEndTiles.size() && !nextTilesHasAdded; i++) {
                if (copyOfEndTiles.get(i).getPrev().contains(route.get(route.size() - 1))) {
                    route.add(copyOfEndTiles.get(i));
                    copyOfEndTiles.remove(copyOfEndTiles.get(i));
                    nextTilesHasAdded = true;
                }
            }
        }
        return frontTilesOfRoutes;
    }
}
