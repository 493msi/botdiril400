package cz.tefek.botdiril.gamelogic.mine;

import java.util.EnumMap;

import cz.tefek.botdiril.userdata.item.ItemDrops;

public class MineResult
{
    private final long xp;
    private final ItemDrops lostItems;

    private final boolean miningWithoutRepairKit;
    private final EnumMap<EnumMineMultiplier, Double> multipliers;

    private final boolean instantlyRefreshed;

    public MineResult(long xp, ItemDrops lostItems, boolean miningWithoutRepairKit, EnumMap<EnumMineMultiplier, Double> multipliers, boolean instantlyRefreshed)
    {
        this.xp = xp;
        this.lostItems = lostItems;
        this.miningWithoutRepairKit = miningWithoutRepairKit;
        this.multipliers = multipliers;
        this.instantlyRefreshed = instantlyRefreshed;
    }

    public long getXP()
    {
        return this.xp;
    }

    public ItemDrops getLostItems()
    {
        return this.lostItems;
    }

    public boolean isMiningWithoutRepairKit()
    {
        return this.miningWithoutRepairKit;
    }

    public EnumMap<EnumMineMultiplier, Double> getMultipliers()
    {
        return this.multipliers;
    }

    public boolean isInstantlyRefreshed()
    {
        return this.instantlyRefreshed;
    }
}
