package com.botdiril.userdata.item;

import com.botdiril.framework.command.context.CommandContext;

public interface IOpenable
{
    void open(CommandContext co, long amount);

    default boolean requiresKey()
    {
        return false;
    }
}
