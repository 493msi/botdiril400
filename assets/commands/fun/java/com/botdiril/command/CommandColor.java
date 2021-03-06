package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;

@Command("color")
public class CommandColor
{
    @CmdInvoke
    public static void choose(CommandContext co)
    {
        var col = co.rdg.nextInt(0x000000, 0xffffff);

        var eb = new ResponseEmbed();
        eb.setTitle("Here's your random color.");
        eb.setColor(col);
        var hexInt = Integer.toHexString(col);
        var hex = "000000".substring(hexInt.length()) + hexInt;
        eb.setDescription("#" + hex);
        co.respond(eb);
    }
}
