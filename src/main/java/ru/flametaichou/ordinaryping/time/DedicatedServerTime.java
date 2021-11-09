package ru.flametaichou.ordinaryping.time;

import org.swarg.mcforge.common.SysTimeHandler;

/**
 * 09-11-21
 * @author Swarg
 */
public class DedicatedServerTime extends ServerTime {
    private SysTimeHandler sth;

    public DedicatedServerTime() {
        /*One handler for many mods receiving system time at the beginning
        of the server tick. */
        sth = SysTimeHandler.instance();
        sth.register();
    }

    @Override
    public long get() {
        return sth.getNowTimeMillis();
    }

}
