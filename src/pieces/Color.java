package pieces;

public enum Color {
    WHITE, BLACK;

    public Color opponent() {
        return this == WHITE ? BLACK : WHITE;
    }

    @Override
    public String toString() {
        return name();
    }
}
