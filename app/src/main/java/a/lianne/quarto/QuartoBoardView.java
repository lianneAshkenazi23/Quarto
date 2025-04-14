package a.lianne.quarto;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class QuartoBoardView extends View {

    public static final int PAD = 24; // px

    private QuartoBoard board;

    private final Paint gridPaint = new Paint();
    private final Paint piecePaint = new Paint();
    private int cell;

    private OnCellClickListener listener;
    public interface OnCellClickListener {
        void onCellClicked(int row, int col);
    }
    public void setOnCellClickListener(OnCellClickListener _listener) {
        listener = _listener;
    }

    public QuartoBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(12);
        gridPaint.setStyle(Paint.Style.STROKE);
        piecePaint.setStrokeWidth(36);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cell = (Math.min(w, h) - 2 * PAD) / 4;
    }

    public void setBoard(QuartoBoard board) {
        this.board = board;
        invalidate();
    }

    public void drawPiece(Canvas canvas, int row, int col, Piece p) {
        int cx = PAD + col * cell + cell / 2;
        int cy = PAD + row * cell + cell / 2;
        int size = p.isTall ? cell - 72 : (int) (cell / 2.5);
        if (p.isDark) {
//            piecePaint.setColor(Color.argb(255, 0, 255, 255));
            piecePaint.setColor(Color.WHITE);
        } else {
            piecePaint.setColor(Color.argb(255, 255, 0, 127));
        }

        piecePaint.setStyle(p.isHollow ? Paint.Style.STROKE : Paint.Style.FILL_AND_STROKE);


        if (p.isSquare) {
            canvas.drawRect(cx - size / 2, cy - size / 2, cx + size / 2, cy + size / 2, piecePaint);
        } else {
            canvas.drawCircle(cx, cy, size / 2, piecePaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("QBView", "onDraw width=" + getWidth() + " height=" + getHeight());

        super.onDraw(canvas);

        for (int i = 0; i <= 4; i++) {
            int j = PAD + i * cell;
            // Vertical line
            canvas.drawLine(j, PAD, j, PAD + 4 * cell, gridPaint);
            // Horizontal line
            canvas.drawLine(PAD, j, PAD + 4 * cell, j, gridPaint);
        }

        // Draw pieces
        if(board == null) return;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.getPiece(i, j) != null) {
                    drawPiece(canvas, i, j, board.getPiece(i, j));
                    Log.d("QuartoBoardView", "Drawing piece at (" + i + ", " + j + ")");
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int row = (int) ((event.getY() - PAD) / cell);
            int col = (int) ((event.getX() - PAD) / cell);
            if (row >= 0 && row < 4 && col >= 0 && col < 4) {
                listener.onCellClicked(row, col);
            }
        }
        return true;
    }
}