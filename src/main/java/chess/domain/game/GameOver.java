package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.piece.Color;
import chess.domain.event.Event;
import chess.domain.event.EventType;
import chess.domain.game.statistics.GameResult;

final class GameOver extends Started {

    private static final String GAME_NOT_RUNNING_EXCEPTION_MESSAGE = "이미 종료된 게임입니다.";

    GameOver(Board board) {
        super(GameState.OVER, board);
    }

    @Override
    public Game play(Event event) {
        if (event.hasTypeOf(EventType.INIT)) {
            return new WhiteTurn(board);
        }
        throw new UnsupportedOperationException(GAME_NOT_RUNNING_EXCEPTION_MESSAGE);
    }

    @Override
    public boolean isValidTurn(Color playerColor) {
        throw new UnsupportedOperationException(GAME_NOT_RUNNING_EXCEPTION_MESSAGE);
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public GameResult getResult() {
        return new GameResult(board.toMap());
    }
}
