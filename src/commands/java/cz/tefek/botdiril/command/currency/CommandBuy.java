package cz.tefek.botdiril.command.currency;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "buy", aliases = { "b", "bi" }, category = CommandCategory.CURRENCY, description = "Buy items from the shop.", levelLock = 2)
public class CommandBuy
{
    @CmdInvoke
    public static void buy(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        if (!ShopEntries.canBeBought(item))
        {
            throw new CommandException("That item cannot be bought, sorry.");
        }

        if (ShopEntries.getCoinPrice(item) > co.ui.getCoins())
        {
            throw new CommandException("You can't afford that item, sorry.");
        }

        buy(co, item, 1);
    }

    @CmdInvoke
    public static void buy(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD_BUY_COINS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't buy zero items / cards.");

        var price = amount * ShopEntries.getCoinPrice(item);

        if (item instanceof Item)
        {
            co.ui.addItem((Item) item, amount);
        }
        else if (item instanceof Card)
        {
            co.ui.addCard((Card) item, amount);
        }

        co.ui.addCoins(-price);

        co.respond(String.format("You bought **%s %s** for **%s** %s.", BotdirilFmt.format(amount), item.inlineDescription(), BotdirilFmt.format(price), Icons.COIN));
    }
}
