package cz.tefek.botdiril.userdata.timers;

import cz.tefek.botdiril.userdata.ItemLookup;

public class Timer
{
    private final int id;
    private final String name;
    private final String localizedName;
    private final long timeMs;

    public Timer(String name, String niceName, long timeMs)
    {
        this.name = "timer_" + name;
        this.timeMs = timeMs;
        this.id = ItemLookup.make(this.name);
        this.localizedName = niceName;

        Timers.allTimers.add(this);
    }

    public int getID()
    {
        return this.id;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }

    public String getName()
    {
        return this.name;
    }

    public long getTimeOffset()
    {
        return this.timeMs;
    }
}
