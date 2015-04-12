package android.hardware;

import android.content.*;
import android.util.*;
import android.os.*;
import android.view.*;


public class EpdController
{
    private static final String TAG = "EpdController";
    private final IEpdService epdService;

    public EpdController(final Context context) {
        this.epdService = IEpdService.Stub.asInterface(ServiceManager.getService("epd"));
        //this.epdService = new EpdService(context);
    }

    public void disableEpd(final String s, final int n) {
        try {
            this.epdService.disableEpd(s, n);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "disableEpd() failed!", ex);
        }
    }

    public void epdRefresh(final String s, final Refresh refresh) {
        try {
            this.epdService.epdRefresh(s, refresh.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "epdRefresh() failed!", ex);
        }
    }

    public void fillRegion(final String s, final EpdRegionParams epdRegionParams) {
        try {
            this.epdService.fillRegion(s, epdRegionParams);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "fillRegion() failed!", ex);
        }
    }

    public boolean isInVuMode() {
        try {
            return this.epdService.isInVuMode();
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "isInVuMode() failed!", ex);
            return false;
        }
    }

    public void resetRegion(final String s, final HwRegion hwRegion) {
        try {
            this.epdService.resetRegion(s, hwRegion.regionIndex);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "resetRegion() failed!", ex);
        }
    }

    public void resetRegion(final String s, final Region region) {
        try {
            this.epdService.resetRegion(s, region.regionIndex);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "resetRegion() failed!", ex);
        }
    }

    public void resetVuMode(final String s) {
        try {
            this.epdService.resetVuMode(s);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "resetVuMode() failed!", ex);
        }
    }

    public void setRegion(final String s, final HwRegion hwRegion, final EpdRegionParams epdRegionParams) {
        try {
            this.epdService.configureRegion(s, hwRegion.regionIndex, epdRegionParams, Mode.ACTIVE.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final HwRegion hwRegion, final EpdRegionParams epdRegionParams, final Mode mode) {
        try {
            this.epdService.configureRegion(s, hwRegion.regionIndex, epdRegionParams, mode.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final HwRegion hwRegion, final View view, final EpdRegionParams.Wave wave) {
        try {
            this.epdService.configureRegion(s, hwRegion.regionIndex, new EpdRegionParams(view, wave), Mode.ACTIVE.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final HwRegion hwRegion, final View view, final EpdRegionParams.Wave wave, final Mode mode) {
        try {
            this.epdService.configureRegion(s, hwRegion.regionIndex, new EpdRegionParams(view, wave), mode.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final Region region, final EpdRegionParams epdRegionParams) {
        try {
            this.epdService.configureRegion(s, region.regionIndex, epdRegionParams, Mode.ACTIVE.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final Region region, final EpdRegionParams epdRegionParams, final Mode mode) {
        try {
            this.epdService.configureRegion(s, region.regionIndex, epdRegionParams, mode.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final Region region, final View view, final EpdRegionParams.Wave wave) {
        try {
            this.epdService.configureRegion(s, region.regionIndex, new EpdRegionParams(view, wave), Mode.ACTIVE.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setRegion(final String s, final Region region, final View view, final EpdRegionParams.Wave wave, final Mode mode) {
        try {
            this.epdService.configureRegion(s, region.regionIndex, new EpdRegionParams(view, wave), mode.hwMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setRegion() failed!", ex);
        }
    }

    public void setVuMode(final String vuMode) {
        try {
            this.epdService.setVuMode(vuMode);
        }
        catch (RemoteException ex) {
            Log.e("EpdController", "setVuMode() failed!", ex);
        }
    }

    public enum HwRegion
    {
        APP_1(4),
        APP_2(5),
        APP_3(6),
        APP_4(7),
        DIALOG(2),
        KBD(1),
        OVERLAY(3),
        TOAST(0);

        public int regionIndex;

        private HwRegion(final int regionIndex) {
            this.regionIndex = regionIndex;
        }
    }

    public enum Mode
    {
        ACTIVE(1),
        ACTIVE_ALL(9),
        CLEAR(4),
        CLEAR_ALL(12),
        INACTIVE(0),
        ONESHOT(2),
        ONESHOT_ALL(10);

        public int hwMode;

        private Mode(final int hwMode) {
            this.hwMode = hwMode;
        }
    }

    public enum Refresh
    {
        GC_ALL(1),
        REFRESH(0);

        int hwMode;

        private Refresh(final int hwMode) {
            this.hwMode = hwMode;
        }
    }

    public enum Region
    {
        APP_1(4),
        APP_2(5),
        APP_3(6),
        APP_4(7);

        public int regionIndex;

        private Region(final int regionIndex) {
            this.regionIndex = regionIndex;
        }
    }
}
