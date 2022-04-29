package chess.service;

import chess.dao.EventDao;
import chess.dao.GameDao;
import chess.domain.auth.EncryptedAuthCredentials;
import chess.domain.event.Event;
import chess.domain.event.InitEvent;
import chess.domain.game.Game;
import chess.domain.game.NewGame;
import chess.dto.response.CreatedGameDto;
import chess.dto.response.SearchResultDto;
import chess.dto.view.FullGameDto;
import chess.dto.view.GameCountDto;
import chess.dto.view.GameOverviewDto;
import chess.dto.view.GameResultDto;
import chess.entity.GameEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private static final String GAME_NOT_OVER_EXCEPTION_MESSAGE = "아직 게임 결과가 산출되지 않았습니다.";

    private final GameDao gameDao;
    private final EventDao eventDao;

    public ChessService(GameDao gameDao, EventDao eventDao) {
        this.gameDao = gameDao;
        this.eventDao = eventDao;
    }

    public List<GameOverviewDto> findGames() {
        return gameDao.findAll()
                .stream()
                .map(GameEntity::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public FullGameDto findGame(int gameId) {
        GameEntity gameEntity = gameDao.findById(gameId);
        Game game = currentSnapShotOf(gameId);
        return FullGameDto.of(gameEntity, game);
    }

    public GameResultDto findGameResult(int gameId) {
        GameEntity gameEntity = gameDao.findById(gameId);
        Game game = currentSnapShotOf(gameId);
        validateGameOver(game);
        return GameResultDto.of(gameEntity, game);
    }

    public SearchResultDto searchGame(int gameId) {
        return new SearchResultDto(gameId, gameDao.checkById(gameId));
    }

    public GameCountDto countGames() {
        int totalCount = gameDao.countAll();
        int runningCount = gameDao.countRunningGames();

        return new GameCountDto(totalCount, runningCount);
    }

    @Transactional
    public CreatedGameDto initGame(EncryptedAuthCredentials authCredentials) {
        int gameId = gameDao.saveAndGetGeneratedId(authCredentials);
        eventDao.save(gameId, new InitEvent());
        return CreatedGameDto.of(gameId);
    }

    @Transactional
    public void playGame(int gameId, Event moveEvent) {
        Game game = currentSnapShotOf(gameId).play(moveEvent);

        eventDao.save(gameId, moveEvent);
        finishGameOnEnd(gameId, game);
    }

    private void finishGameOnEnd(int gameId, Game game) {
        if (game.isEnd()) {
            gameDao.finishGame(gameId);
        }
    }

    @Transactional
    public void deleteFinishedGame(int gameId, EncryptedAuthCredentials authCredentials) {
        gameDao.deleteGame(authCredentials);
        eventDao.deleteAllByGameId(gameId);
    }

    private Game currentSnapShotOf(int gameId) {
        List<Event> events = eventDao.findAllByGameId(gameId);
        Game game = new NewGame();
        for (Event event : events) {
            game = game.play(event);
        }
        return game;
    }

    private void validateGameOver(Game game) {
        if (!game.isEnd()) {
            throw new IllegalArgumentException(GAME_NOT_OVER_EXCEPTION_MESSAGE);
        }
    }
}
