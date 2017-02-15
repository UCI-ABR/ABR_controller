package abr.main;

import ioio.lib.util.BaseIOIOLooper;

/**
 * Created by tiffany on 2/15/17.
 */
public abstract class IOIO_thread extends BaseIOIOLooper {
    public abstract void move(int value);
    public abstract void turn(int value);
}
