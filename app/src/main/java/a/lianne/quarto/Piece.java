package a.lianne.quarto;

public class Piece {
    public final boolean isTall;
    public final boolean isDark;
    public final boolean isHollow;
    public final boolean isSquare;

    public Piece(boolean isTall, boolean isDark, boolean isHollow, boolean isSquare)
    {
        this.isTall = isTall;
        this.isDark = isDark;
        this.isHollow = isHollow;
        this.isSquare = isSquare;
    }
}
