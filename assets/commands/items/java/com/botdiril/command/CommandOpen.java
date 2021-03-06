package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.IOpenable;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command("open")
public class CommandOpen
{
    @CmdInvoke
    public static void open(CommandContext co, @CmdPar("what to open") Item item)
    {
        open(co, item, 1);
    }

    @CmdInvoke
    public static void open(CommandContext co, @CmdPar("what to open") Item item, @CmdPar(value = "how many to open", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.assertTrue(item instanceof IOpenable, "*This item cannot be opened.*");

        var openable = (IOpenable) item;

        final long limit = 64;
        CommandAssert.numberInBoundsInclusiveL(amount, 1, limit, "*You can open **%d %s** at most at once and one at least (obviously).*".formatted(limit, item.getInlineDescription()));

        CommandAssert.numberNotBelowL(co.inventory.howManyOf(item), amount, "You don't have %ss...".formatted(BotdirilFmt.amountOfMD(amount, item.getInlineDescription())));

        if (openable.requiresKey())
        {
            ItemAssert.consumeItems(co.inventory, "open %s".formatted(BotdirilFmt.amountOfMD(amount, item.getInlineDescription())), ItemPair.of(Items.keys, amount));

            if (Curser.isBlessed(co, EnumBlessing.CHANCE_NOT_TO_CONSUME_KEY))
            {
                long keysBack = 0;

                for (int i = 0; i < amount; i++)
                    if (BotdirilRnd.rollChance(co.rdg, 0.25))
                        keysBack++;

                co.inventory.addKeys(keysBack);
            }
        }

        openable.open(co, amount);
        co.inventory.addItem(item, -amount);
    }
}
