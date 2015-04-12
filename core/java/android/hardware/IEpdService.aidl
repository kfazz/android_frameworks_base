// IEpdService.aidl
package android.hardware;

// Declare any non-default types here with import statements
import android.hardware.EpdRegionParams;

interface IEpdService {
      void configureRegion(String p0, int p1,in EpdRegionParams p2, int p3);
      void resetRegion(String p0, int p1);
      void fillRegion(String p0,in EpdRegionParams p1);

      void setVuMode(String p0);
      void resetVuMode(String p0);
      boolean isInVuMode();

      void epdRefresh(String p0, int p1);
      void disableEpd(String p0, int p1);

}
