package com.botdiril.command;


import com.botdiril.Botdiril;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.util.CommandAssert;

@Command("prefix")
public class CommandPrefix
{
    @CmdInvoke
    public static void setPrefix(DiscordCommandContext co, @CmdPar("prefix") String prefix)
    {
        CommandAssert.stringNotTooLong(prefix, 8, "The prefix is too long.");

        if (prefix.contains("@"))
        {
            throw new CommandException("The prefix can't contain @.");
        }

        co.sc.setPrefix(co.db, prefix);

        co.guild.retrieveMember(co.bot).queue(member -> member.modifyNickname("[%s] %s".formatted(prefix, Botdiril.BRANDING)).complete());
        co.respond("Prefix set to: `%s`".formatted(prefix));
    }
}
