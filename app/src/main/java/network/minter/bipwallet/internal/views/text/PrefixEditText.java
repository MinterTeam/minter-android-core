package network.minter.bipwallet.internal.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import network.minter.bipwallet.R;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class PrefixEditText extends TextInputEditText {
    private float mOriginalLeftPadding = -1;
    private String mPrefix;
    private boolean mPrefixOnEmptyText = true;

    public PrefixEditText(Context context) {
        super(context);
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PrefixEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getText().length() == 0 && !mPrefixOnEmptyText) {
            return;
        }
        canvas.drawText(
                mPrefix,
                mOriginalLeftPadding,
                getLineBounds(0, null),
                getPaint()
        );
    }

    public void setPrefix(String prefix) {
        mPrefix = prefix;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePrefix();
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.PrefixEditText, defStyleAttr, 0);
        mPrefix = a.getString(R.styleable.PrefixEditText_prefix);
        mPrefixOnEmptyText = a.getBoolean(R.styleable.PrefixEditText_prefixOnEmpty, true);

        a.recycle();
    }

    private void calculatePrefix() {
        if (mPrefix == null) {
            return;
        }

        if(getText().length() == 0 && !mPrefixOnEmptyText) {
            mOriginalLeftPadding = -1;
            return;
        }


        if (mOriginalLeftPadding == -1) {
            float[] widths = new float[mPrefix.length()];
            getPaint().getTextWidths(mPrefix, widths);
            float textWidth = 0;
            for (float w : widths) {
                textWidth += w;
            }
            mOriginalLeftPadding = getCompoundPaddingLeft();
            setPadding((int) (textWidth + mOriginalLeftPadding),
                       getPaddingRight(), getPaddingTop(),
                       getPaddingBottom());
        }
    }
}
