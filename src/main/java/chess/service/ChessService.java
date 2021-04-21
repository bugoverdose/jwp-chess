package chess.service;

import chess.dao.BackupBoardDao;
import chess.dao.RoomDao;
import chess.domain.Game;
import chess.domain.Rooms;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.MovedInfoDto;
import chess.dto.RoomNameDto;
import chess.dto.SquareDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final RoomDao roomDao;
    private final BackupBoardDao backupBoardDao;
    private final Rooms rooms;

    public ChessService(RoomDao roomDao, BackupBoardDao backupBoardDao) {
        this.roomDao = roomDao;
        this.backupBoardDao = backupBoardDao;
        this.rooms = new Rooms();
    }

    public Game restartGame(String name) {
        Game currentGame = currentGame(name);
        currentGame.init();
        return currentGame;
    }

    public Game currentGame(String name) {
        Optional<Game> game = rooms.findGame(name);
        return game.orElseGet(() -> getGame(name));
    }

    private Game getGame(String name) {
        if (roomDao.existsRoom(name)) {
            Game findGame = Game.game(new Board(playingBoard(name)), roomDao.findRoomTurnColor(name));
            rooms.addRoom(name, findGame);
            return findGame;
        }

        Game newGame = Game.newGame();
        rooms.addRoom(name, newGame);
        return newGame;
    }

    private Map<Position, Piece> playingBoard(String name) {
        List<SquareDto> boardInfo = backupBoardDao.findPlayingBoardByRoom(name);

        return boardInfo.stream()
            .collect(Collectors.toMap(this::position, this::piece));
    }

    private Position position(SquareDto squareDto) {
        return Position.from(squareDto.getPosition());
    }

    private Piece piece(SquareDto squareDto) {
        List<String> pieceInfo = Arrays.asList(squareDto.getPiece().split(("_")));
        return new Piece(pieceInfo.get(1), pieceInfo.get(0));
    }

    public MovedInfoDto move(String name, String source, String target) {
        Game currentGame = currentGame(name);
        currentGame.move(source, target);
        if (currentGame.isEnd()) {
            deleteRoom(name);
            return new MovedInfoDto(source, target,
                currentGame.winnerColor().getSymbol());
        }

        return new MovedInfoDto(source, target,
            currentGame.turnColor().getName());
    }

    public void savePlayingBoard(String name) {
        Game currentGame = currentGame(name);
        backupBoardDao.deleteExistingBoard(name);
        roomDao.deleteRoom(name);
        roomDao.addRoom(name, currentGame.turnColor());
        backupBoardDao.addPlayingBoard(name, currentGame.getBoard());
    }

    public void deleteRoom(String roomName) {
        backupBoardDao.deleteExistingBoard(roomName);
        roomDao.deleteRoom(roomName);
        rooms.deleteRoom(roomName);
    }

    public List<RoomNameDto> roomNames() {
        return roomDao.findRoomNames();
    }
}