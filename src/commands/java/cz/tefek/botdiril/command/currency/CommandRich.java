package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.api.EmbedBuilder;

import java.text.MessageFormat;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "rich", aliases = {
        "topcoins" }, category = CommandCategory.CURRENCY, description = "Shows the top 10 richest users.")
public class CommandRich
{
    public static final int LIMIT = 10;

    @CmdInvoke
    public static void show(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Richest users");
        eb.setDescription(MessageFormat.format("Showing max {0} users.", LIMIT));
        eb.setColor(0x008080);
        eb.setThumbnail(co.jda.getSelfUser().getEffectiveAvatarUrl());

        co.db.setAutocommit(true);
        co.db.exec("SELECT us_userid, us_coins FROM " + UserInventory.TABLE_USER + " WHERE us_userid<>? ORDER BY us_coins DESC LIMIT " + LIMIT, stat ->
        {
            var rs = stat.executeQuery();

            int i = 1;

            while (rs.next())
            {
                var us = co.jda.retrieveUserById(rs.getLong("us_userid")).complete();
                var userName = us == null ? "[Unknown user]" : us.getAsMention();
                var usn = String.format("**%d.** %s", i, userName);
                var row = String.format("%s with **%s** %s", usn, BotdirilFmt.format(rs.getLong("us_coins")), Icons.COIN);

                eb.addField("", row, false);

                i++;
            }

            return 0;
        }, Botdiril.AUTHOR_ID);
        co.db.setAutocommit(false);

        co.respond(eb.build());
    }
}
