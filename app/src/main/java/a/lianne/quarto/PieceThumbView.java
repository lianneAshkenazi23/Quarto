package a.lianne.quarto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PieceThumbView extends View {

    public static final int PAD = 6; // px
    private final Paint piecePaint = new Paint();
    Piece p;


    public PieceThumbView(Context context) {
        super(context);
    }

    public PieceThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieceThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getResources().getDimensionPixelSize(R.dimen.piece_cell);
        setMeasuredDimension(size, size);
    }

    public void setPiece(Piece p) {
        this.p = p;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("QBView", "onDraw width=" + getWidth() + " height=" + getHeight());

        super.onDraw(canvas);
        int s = getWidth() - PAD * 2;
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int r = p.isTall ? s - 72 : (int) (s / 2.5);

        piecePaint.setStrokeWidth(36);

        if (p.isDark) {
//            piecePaint.setColor(Color.argb(255, 0, 255, 255));
            piecePaint.setColor(Color.WHITE);
        } else {
            piecePaint.setColor(Color.argb(255, 255, 0, 127));
        }

        piecePaint.setStyle(p.isHollow ? Paint.Style.STROKE : Paint.Style.FILL_AND_STROKE);


        if (p.isSquare) {
            canvas.drawRect(cx - r / 2, cy - r / 2, cx + r / 2, cy + r / 2, piecePaint);
        } else {
            canvas.drawCircle(cx, cy, r / 2, piecePaint);
        }
    }

}