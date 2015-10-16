package ca.uwaterloo.sh6choi.czshopper.bus;

import com.squareup.otto.Bus;

/**
 * Created by Samson on 2015-10-15.
 */
public class BusProvider {
    private static Bus sDatabaseBus = new Bus();

    public static Bus getDatabaseBus() {
        return sDatabaseBus;
    }

}
