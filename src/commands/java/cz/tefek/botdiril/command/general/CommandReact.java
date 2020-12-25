package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.entities.Emote;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.EnumSpecialCommandProperty;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;

@Command(value = "react", description = "Makes the bot react with an emote.",
    category = CommandCategory.GENERAL, special = EnumSpecialCommandProperty.ALLOW_LOCK_BYPASS)
public class CommandReact
{
    @CmdInvoke
    public static void react(CallObj co, @CmdPar("emote") Emote emote)
    {
        co.message.addReaction(emote).submit();
    }
}
