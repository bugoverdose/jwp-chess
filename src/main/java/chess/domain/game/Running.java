package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.piece.Color;
import chess.domain.board.piece.PieceType;
import chess.domain.board.position.Position;
import chess.domain.event.Event;
import chess.domain.event.EventType;
import chess.domain.event.MoveRoute;
import chess.domain.game.statistics.GameResult;

abstract class Running extends Started {

    private static final int ONGOING_GAME_KING_COUNT = 2;
    private static final String GAME_NOT_OVER_EXCEPTION_MESSAGE = "아직 종료되지 않은 게임입니다.";
    private static final String NOT_YET_IMPLEMENTED_EXCEPTION_MESSAGE = "아직 구현되지 않은 기능입니다.";

    protected Running(GameState state, Board board) {
        super(state, board);
    }

    @Override
    public Game play(Event event) {
        if (event.hasTypeOf(EventType.MOVE)) {
            return moveChessmen(event.toMoveRoute());
        }
        if (event.hasTypeOf(EventType.INIT)) {
            return new WhiteTurn(board);
        }
        throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED_EXCEPTION_MESSAGE);
    }

    private Game moveChessmen(MoveRoute moveRoute) {
        Position from = moveRoute.getSource();
        Position to = moveRoute.getTarget();
        Color currentTurnColor = state.toColor();

        board.movePiece(from, to, currentTurnColor);
        return moveResult();
    }

    private Game moveResult() {
        if (board.countByType(PieceType.KING) < ONGOING_GAME_KING_COUNT) {
            return new GameOver(board);
        }
        return continueGame();
    }

    protected abstract Game continueGame();

    @Override
    public final boolean isEnd() {
        return false;
    }

    @Override
    public final GameResult getResult() {
        throw new UnsupportedOperationException(GAME_NOT_OVER_EXCEPTION_MESSAGE);
    }
}
