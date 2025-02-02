package chess.domain.event;

import java.util.Objects;

public final class MoveEvent extends Event {

    private final MoveRoute moveRoute;

    public MoveEvent(MoveRoute moveRoute) {
        super(EventType.MOVE, moveRoute.toDescription());
        this.moveRoute = moveRoute;
    }

    public MoveEvent(String description) {
        this(MoveRoute.ofEventDescription(description));
    }

    public MoveRoute toMoveRoute() {
        return moveRoute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveEvent moveEvent = (MoveEvent) o;
        return Objects.equals(moveRoute, moveEvent.moveRoute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moveRoute);
    }

    @Override
    public String toString() {
        return "MoveEvent{" + "moveRoute=" + moveRoute + '}';
    }
}
