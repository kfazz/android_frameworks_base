package android.hardware;

import android.os.*;
import android.graphics.*;
import android.view.*;

public final class EpdRegionParams implements Parcelable
{
    public static final Creator<EpdRegionParams> CREATOR;
    public static final int THRESH_BLACKOUT = 17;
    public static final int THRESH_DEFAULT = 0;
    public static final int THRESH_MAX = 15;
    public static final int THRESH_MIN = 1;
    public static final int THRESH_WHITEOUT = 16;
    public int ex;
    public int ey;
    public int sx;
    public int sy;
    public int thresh;
    public Wave wave;

    static {
        CREATOR = new Creator<EpdRegionParams>() {
            public EpdRegionParams createFromParcel(final Parcel parcel) {
                return new EpdRegionParams(parcel);
            }

            public EpdRegionParams[] newArray(final int n) {
                return new EpdRegionParams[n];
            }
        };
    }

    public EpdRegionParams(final int n, final int n2, final int n3, final int n4, final Wave wave) {
        this(n, n2, n3, n4, wave, 0);
    }

    public EpdRegionParams(final int sx, final int sy, final int ex, final int ey, final Wave wave, final int thresh) {
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
        this.wave = wave;
        this.thresh = thresh;
    }

    public EpdRegionParams(final Rect rect, final Wave wave) {
        this(rect.left, rect.top, rect.right, rect.bottom, wave, 0);
    }

    public EpdRegionParams(final Rect rect, final Wave wave, final int n) {
        this(rect.left, rect.top, rect.right, rect.bottom, wave, n);
    }

    private EpdRegionParams(final Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public EpdRegionParams(final View view, final Wave wave) {
        this(view, wave, 0);
    }

    public EpdRegionParams(final View view, final Wave wave, final int thresh) {
        final int[] array = new int[2];
        view.getLocationOnScreen(array);
        this.sx = array[0];
        this.sy = array[1];
        this.ex = array[0] + view.getWidth();
        this.ey = array[1] + view.getHeight();
        this.wave = wave;
        this.thresh = thresh;
    }

    private final void readFromParcel(final Parcel parcel) {
        this.sx = parcel.readInt();
        this.sy = parcel.readInt();
        this.ex = parcel.readInt();
        this.ey = parcel.readInt();
        this.wave = Wave.value(parcel.readInt());
        this.thresh = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.sx);
        parcel.writeInt(this.sy);
        parcel.writeInt(this.ex);
        parcel.writeInt(this.ey);
        parcel.writeInt(this.wave.ordinal());
        parcel.writeInt(this.thresh);
    }

    public enum Wave
    {
        A2(3),
        AUTO(256),
        DU(2),
        GC(0),
        GL16(4),
        GU(1);

        static final Wave[] VALUES;
        public int hwMode;

        static {
            VALUES = values();
        }

        private Wave(final int hwMode) {
            this.hwMode = hwMode;
        }

        public static Wave value(final int n) {
            if (n < 0 || n >= Wave.VALUES.length) {
                return Wave.AUTO;
            }
            return Wave.VALUES[n];
        }
    }
}
