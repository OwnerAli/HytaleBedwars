package me.alii.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import me.alii.commands.gen_commands.GeneratorCreateCommand;
import me.alii.commands.gen_commands.GeneratorSetCommand;
import me.alii.commands.gen_commands.GeneratorUnSetCommand;
import me.alii.registry.GeneratorRegistry;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BedwarsAdminCommand extends CommandBase {

    public BedwarsAdminCommand(GeneratorRegistry generatorRegistry) {
        super("bws", "Bedwars admin command.");
        this.addSubCommand(new GeneratorCreateCommand(generatorRegistry));
        this.addSubCommand(new GeneratorSetCommand(generatorRegistry));
        this.addSubCommand(new GeneratorUnSetCommand());
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
    }
}
