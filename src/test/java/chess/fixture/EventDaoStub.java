package chess.fixture;

import chess.dao.EventDao;
import chess.domain.event.Event;
import chess.domain.event.InitEvent;
import chess.domain.event.MoveEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDaoStub extends EventDao {

    private final Map<Integer, List<Event>> repository = new HashMap<>() {{
        put(1, new ArrayList<>(List.of(new InitEvent(), new MoveEvent("e2 e4"),
                new MoveEvent("d7 d5"), new MoveEvent("f1 b5"))));
        put(2, new ArrayList<>(List.of(new InitEvent(),
                new MoveEvent("e2 e4"), new MoveEvent("d7 d5"),
                new MoveEvent("f1 b5"), new MoveEvent("a7 a5"))));
        put(3, new ArrayList<>(List.of(new InitEvent(), new MoveEvent("e2 e4"),
                new MoveEvent("d7 d5"), new MoveEvent("f1 b5"),
                new MoveEvent("a7 a5"), new MoveEvent("b5 e8"))));
    }};

    public EventDaoStub() {
        super(null);
    }

    @Override
    public List<Event> findAllByGameId(int gameId) {
        if (repository.containsKey(gameId)) {
            return repository.get(gameId);
        }
        return List.of();
    }

    @Override
    public void save(int gameId, Event event) {
        if (repository.containsKey(gameId)) {
            repository.get(gameId).add(event);
            return;
        }
        repository.put(gameId, new ArrayList<>(List.of(event)));
    }

    @Override
    public void deleteAllByGameId(int gameId) {
        if (!repository.containsKey(gameId)) {
            throw new IllegalArgumentException("해당되는 이벤트가 없습니다!");
        }
        repository.remove(gameId);
    }
}
