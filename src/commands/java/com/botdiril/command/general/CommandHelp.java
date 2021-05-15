package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandStorage;
import com.botdiril.framework.command.GenUsage;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Command(value = "help", aliases = {
        "usage", "commands", "command" }, category = CommandCategory.GENERAL, description = "General help command.")
public class CommandHelp
{
    @CmdInvoke
    public static void show(ChatCommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setColor(Color.CYAN.getRGB());
        eb.setTitle("Stuck? Here is your help:");

        Arrays.stream(CommandCategory.values())
            .forEach(cat -> eb.addField(cat.getName() + " [" + CommandStorage.commandCountInCategory(cat) + "]",
                "Type ``" + co.usedPrefix + co.usedAlias + " " + cat.toString().toLowerCase() + "``", false));

        long cmdCnt = CommandStorage.commandCount();
        int catCnt = CommandCategory.values().length;

        eb.setDescription("There are " + cmdCnt + " commands in " + catCnt + " categories total.");

        co.respond(eb);
    }

    @CmdInvoke
    public static void show(ChatCommandContext co, @CmdPar("category or command") String tbp)
    {
        try
        {
            var command = CommandAssert.parseCommand(tbp);
            var sb = new StringBuilder("**Command `" + command.value() + "`**:");
            if (command.aliases().length != 0)
            {
                sb.append("\n**Aliases:** ");
                sb.append(Arrays.stream(command.aliases()).map(a -> "`" + a + "`").collect(Collectors.joining(", ")));
            }
            sb.append("\n**Description:** ");
            sb.append(command.description());
            sb.append("\n**Power level required:** ");
            sb.append(command.powerLevel());
            sb.append("\n**Available from level:** ");
            sb.append(command.levelLock() == 0 ? "Always available" : command.levelLock());
            sb.append("\n**Usage:**\n");
            sb.append(GenUsage.usage(co.usedPrefix, tbp, command));

            co.respond(sb.toString());
        }
        catch (CommandException e)
        {
            var found = CommandAssert.parseCommandGroup(tbp);

            var eb = new ResponseEmbed();
            eb.setColor(Color.CYAN.getRGB());
            eb.setTitle("Help for the " + found.getName());

            CommandStorage.getCommandsByCategory(found).forEach(comm -> eb.addField(comm.value(), comm.description(), false));

            eb.setDescription("Type ``" + co.usedPrefix + co.usedAlias + " <command>`` to show more information for each command.");

            co.respond(eb);
        }
    }
}