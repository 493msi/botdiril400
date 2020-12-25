package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.pluto.chrono.MiniTime;

@Command(value = "timers", aliases = {
        "cooldowns", "cooldown", "timer" }, category = CommandCategory.GENERAL, description = "Check user's timers.")
public class CommandTimers
{
    @CmdInvoke
    public static void check(CallObj co, @CmdPar("user") User user)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Timers");
        eb.setDescription(user.getAsMention() + "'s active timers / cooldowns.");

        final var ui = new UserInventory(co.db, user.getIdLong());

        Timers.allTimers.forEach(t ->
        {
            var remaining = ui.checkTimer(t);

            if (remaining < 0)
            {
                return;
            }

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), false);
        });

        eb.setColor(0x008080);
        eb.setThumbnail(user.getEffectiveAvatarUrl());

        co.respond(eb.build());
    }

    @CmdInvoke
    public static void checkSelf(CallObj co)
    {
        check(co, co.caller);
    }
}
