
package com.android.server;

        import android.content.*;
        import android.util.*;
        import android.hardware.*;
        import java.io.FileWriter;
        import java.io.IOException;

class EpdService extends IEpdService.Stub
{
    private static final boolean LOG_CHANGES = true;
    private static final String PRE_TAG = "EPD#";
    private static final String SYSFS_DISABLE = "/sys/class/graphics/fb0/epd_disable";
    private static final String SYSFS_REFRESH = "/sys/class/graphics/fb0/epd_refresh";
    private static final String TAG = "EPD";
    private static final EpdController.HwRegion VU_REGION;
    private static final int WAVE_VU = 128;
    private boolean inVuMode;
    private final Object sSync;

    static {
       // System.loadLibrary("epd");
        nativeClassInit();
        VU_REGION = EpdController.HwRegion.APP_1;
    }

    public EpdService(final Context context) {
        this.sSync = new Object();
        this.inVuMode = false;
        Log.d("EPD", "EpdService startup");
    }

    private static native int fillEpdRegionNative(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);

    private static native NativeParams getEpdRegionNative(final int p0);

    private static final void log(final String str, final String s) {
        Log.d("EPD#" + str, s);
    }

    private static final void log(final String str, final String format, final Object... args) {
        Log.d("EPD#" + str, String.format(format, args));
    }

    private static native void nativeClassInit();

    private static native int resetEpdRegionNative(final int p0);

    private static native int setEpdRegionNative(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);

    private static void sysWrite(String file, String string) {
        FileWriter mFileWriter = null;
        try {
            mFileWriter = new FileWriter(file);
            mFileWriter.write(string);
            mFileWriter.close();
        } catch (IOException e) {
            Log.e((String) "EPD", (String) ("Failed to write to " + file));
            return;
        }
    }


    public void configureRegion(final String s, final int n, final EpdRegionParams epdRegionParams, final int n2) {
        synchronized (this.sSync) {
            if (this.inVuMode && n == EpdService.VU_REGION.regionIndex) {
                log(s, "IGNORE set region %d %s/%s: in VU mode", n, n2, epdRegionParams.wave);
            }
            else {
                log(s, "set region %d %s = %d,%d -> %d,%d = %s", n, n2, epdRegionParams.sx, epdRegionParams.sy, epdRegionParams.ex, epdRegionParams.ey, epdRegionParams.wave);
                setEpdRegionNative(n, epdRegionParams.sx, epdRegionParams.sy, epdRegionParams.ex, epdRegionParams.ey, epdRegionParams.wave.hwMode, epdRegionParams.thresh, n2);
            }
        }
    }

    public void disableEpd(final String s, final int n) {
        synchronized (this.sSync) {
            if (this.inVuMode) {
                log(s, "IGNORE disableEpd: in VU mode");
            }
            else {
                log(s, "Disable EPD for " + n + "ms!!!!!!!!");
                sysWrite("/sys/class/graphics/fb0/epd_disable", String.format("%d", n));
            }
        }
    }

    public void epdRefresh(final String s, final int n) {
        synchronized (this.sSync) {
            log(s, "Refresh: " + n);
            sysWrite("/sys/class/graphics/fb0/epd_refresh", String.format("%d", n));
        }
    }

    public void fillRegion(final String s, final EpdRegionParams obj) {
        log(s, "fillRegion " + obj);
        fillEpdRegionNative(obj.sx, obj.sy, obj.ex, obj.ey, obj.wave.hwMode, obj.thresh);
    }

    public boolean isInVuMode() {
        return this.inVuMode;
    }

    public void resetRegion(final String s, final int n) {
        synchronized (this.sSync) {
            if (this.inVuMode && n == EpdService.VU_REGION.regionIndex) {
                log(s, "IGNORE reset region %d: in VU mode", n);
            }
            else {
                log(s, "resetRegion " + n);
                resetEpdRegionNative(1 << n);
            }
        }
    }

    public void resetVuMode(final String s) {
        synchronized (this.sSync) {
            log(s, "Leave VU Mode");
            resetEpdRegionNative(1 << EpdService.VU_REGION.regionIndex);
            fillEpdRegionNative(0, 0, 600, 800, EpdRegionParams.Wave.DU.hwMode, 16);
            this.inVuMode = false;
        }
    }

    public void setVuMode(final String s) {
        synchronized (this.sSync) {
            log(s, "Enter VU Mode");
            fillEpdRegionNative(0, 0, 600, 800, EpdRegionParams.Wave.DU.hwMode, 16);
            setEpdRegionNative(EpdService.VU_REGION.regionIndex, 0, 0, 600, 800, 128, 0, EpdController.Mode.ACTIVE.hwMode);
            this.inVuMode = true;
        }
    }

    private static final class NativeParams
    {
        public final int ex;
        public final int ey;
        public final int flags;
        public final int sx;
        public final int sy;
        public final int thresh;
        public final int wvfid;

        public NativeParams(final int sx, final int sy, final int ex, final int ey, final int wvfid, final int thresh, final int flags) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
            this.wvfid = wvfid;
            this.thresh = thresh;
            this.flags = flags;
        }
    }
}
