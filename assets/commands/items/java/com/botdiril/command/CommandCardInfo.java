package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.EnumCurrency;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.EnumCardModifier;
import com.botdiril.userdata.icon.IconUtil;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

import java.util.stream.Collectors;

@Command("cardinfo")
public class CommandCardInfo
{
    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("card") Card card)
    {
        var eb = new ResponseEmbed();
        eb.setDescription(card.getDescription());
        eb.setColor(0x008080);

        if (co instanceof DiscordCommandContext dcc)
            eb.setThumbnail(IconUtil.urlFromIcon(dcc.jda, card));

        eb.setDescription(card.getDescription());

        eb.addField("ID:", card.getName(), true);

        eb.addField("Set:", card.getCardSet().getSetLocalizedName(), true);

        eb.addField("Rarity:", card.getCardRarity().getCardIcon() + " " + card.getCardRarity().getRarityName(), true);

        boolean hasCard = co.inventory.howManyOf(card) > 0;

        if (hasCard)
        {
            var level = co.inventory.getCardLevel(card);
            var tier = EnumCardModifier.getByLevel(level);
            assert tier != null;
            eb.setTitle("%s %s".formatted(tier.getLocalizedName(), card.getLocalizedName()));
            eb.addField("Level:", BotdirilFmt.format(level), true);

            var xpForLevelUp = tier.getXPForLevelUp();

            if (xpForLevelUp == Long.MAX_VALUE)
                eb.addField("XP:", BotdirilFmt.format(co.inventory.getCardXP(card)), true);
            else
                eb.addField("XP:", "%s / %s".formatted(BotdirilFmt.format(co.inventory.getCardXP(card)), BotdirilFmt.format(xpForLevelUp)), true);

            eb.addField("Sells for:", BotdirilFmt.amountOf(Card.getPrice(card, level), EnumCurrency.COINS.getIcon()), true);
        }
        else
        {
            eb.setTitle(card.getLocalizedName());

            eb.addField("XP / Level", "Obtain this card to level it up!" , true);

            eb.addField("Sells for:", BotdirilFmt.amountOf(Card.getPrice(card, 0), EnumCurrency.COINS.getIcon()), true);
        }

        if (card.hasCollection())
        {
            eb.addField("Collection:", card.getCollectionName(), true);
        }

        var recipe = CraftingEntries.search(card);

        if (recipe != null)
        {
            var components = recipe.components();
            var recipeParts = components.stream().map(ItemPair::toString).collect(Collectors.joining(" + "));

            eb.addField("Crafts from", """
            **%s**
            Recipe yields %s item(s).
            """.formatted(recipeParts, BotdirilFmt.format(recipe.amount())), false);
        }

        if (ShopEntries.canBeDisenchanted(card))
        {
            eb.addField("Disenchants for:", BotdirilFmt.amountOf(ShopEntries.getDustForDisenchanting(card), EnumCurrency.DUST.getIcon()), true);
        }

        eb.setFooter(card.getFootnote(co), null);

        if (card.hasCustomImage())
        {
            eb.setImageURL(card.getCustomImage());
        }

        co.respond(eb);
    }
}
