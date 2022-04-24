package chess.dto.response;

import java.util.Objects;

public class CreatedGameDto {

    private final int id;

    public CreatedGameDto(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreatedGameDto that = (CreatedGameDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CreateGameDto{" + "id=" + id + '}';
    }
}
