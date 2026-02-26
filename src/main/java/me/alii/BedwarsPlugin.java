package me.alii;

import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import lombok.Getter;
import me.alii.commands.BedwarsAdminCommand;
import me.alii.registry.GeneratorRegistry;
import me.alii.modules.GeneratorModule;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

@Getter
public class BedwarsPlugin extends JavaPlugin {
    @Getter
    private static BedwarsPlugin instance;

    public BedwarsPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        // Initialize everything
        CommandRegistry commandRegistry = this.getCommandRegistry();
        commandRegistry.registerCommand(new BedwarsAdminCommand(GeneratorRegistry.getInstance()));

        // Init modules
        new GeneratorModule(this);
    }
}