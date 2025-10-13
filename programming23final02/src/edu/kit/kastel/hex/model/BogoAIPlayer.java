package edu.kit.kastel.hex.model;

import static edu.kit.kastel.hex.model.AIEntry.PLACE_INFO;
import static edu.kit.kastel.hex.model.AIEntry.SWAP_INFO;

/**
 * Simulate Bogo-AI-player.
 *
 * @author uqeoc
 */
public class BogoAIPlayer extends ArtificialPlayer {

    private static final String NAME = "BogoAI";

    /**
     * Create a new BogoAI.
     */
    BogoAIPlayer() {
        super(NAME);
    }

    @Override
    public String nextAction(Hex hex) {

        //If BogoAI can win in one move, place the game piece accordingly to win.
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

        //If BogoAI is in the first move, switch colors if (𝑡𝑥 + 𝑡𝑦) mod 2 ≡ 0 mod 2.
        BoardCoordinate lastPlaceCoordinate = getLastHistoryCoordinate(hex);
        if (hex.getTurnCount() == 1
                && (lastPlaceCoordinate.getRow() + lastPlaceCoordinate.getColumn()) % 2 == 0) {
            return SWAP_INFO;
        }

        //Place a stone at the position (𝑡𝑥, 𝑡𝑦) symmetric to (𝑛−1, 𝑛−1).
        placeCoordinate = getPointSymmetryCoordinate(lastPlaceCoordinate, hex);
        if (placeCoordinate != null) {
            return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
        }

        //place the stone in the first available position along the rows of hexagons.
        placeCoordinate = hex.getBoard().getTheMinimumAvailableCoordinateForBogo();
        return String.format(PLACE_INFO, placeCoordinate.getColumn(), placeCoordinate.getRow());
    }

    private BoardCoordinate getPointSymmetryCoordinate(BoardCoordinate coordinate, Hex hex) {
        int boardSideLength = hex.getBoard().getSideLength();
        int rowSymmetry = boardSideLength / 2 + (boardSideLength / 2 - coordinate.getRow());
        int columnSymmetry = boardSideLength / 2 + (boardSideLength / 2 - coordinate.getColumn());
        if (hex.getBoard().getTile(rowSymmetry, columnSymmetry).getToken() == Entry.EMPTY.getToken()) {
            return hex.getBoard().getTile(rowSymmetry, columnSymmetry);
        } else {
            return null;
        }
    }

    private BoardCoordinate getLastHistoryCoordinate(Hex hex) {
        return hex.getHistories().get(hex.getHistories().size() - 1).getCoordinate();
    }
}
