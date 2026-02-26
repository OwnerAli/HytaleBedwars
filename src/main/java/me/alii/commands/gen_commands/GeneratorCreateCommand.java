package me.alii.commands.gen_commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.ParseResult;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.*;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import me.alii.components.GeneratorComponent;
import me.alii.registry.GeneratorRegistry;
import me.alii.domain.GeneratorConfig;
import me.alii.domain.TypeSpeed;
import me.alii.utils.TimeUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GeneratorCreateCommand extends CommandBase {
    private static final ArgumentType<List<TypeSpeed>> listArgumentType;

    private final RequiredArg<String> idArg;
    private final OptionalArg<List<TypeSpeed>> typeSpeedsArg;
    private final GeneratorRegistry generatorRegistry;

    public GeneratorCreateCommand(GeneratorRegistry generatorRegistry) {
        super("gen-create", "Bedwars generator create command.");
        this.generatorRegistry = generatorRegistry;
        this.idArg = withRequiredArg("id", "", ArgTypes.STRING);
        this.typeSpeedsArg = withOptionalArg("TypeSpeeds", "", listArgumentType);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        List<TypeSpeed> typeSpeeds = typeSpeedsArg.get(commandContext);
        Set<TypeSpeed> typeSpeedSet = new HashSet<>(typeSpeeds);

        // Create generator config object with command arguments
        String id = idArg.get(commandContext);
        generatorRegistry.getComponent(id).ifPresentOrElse(_ ->
                        commandContext.sendMessage(Message.raw("There's already a config with that name!").color(Color.RED)),
                () -> {
                    GeneratorComponent generatorComponent = new GeneratorComponent();
                    generatorComponent.setGeneratorConfig(new GeneratorConfig(id, typeSpeedSet));
                    generatorRegistry.registerComponent(generatorComponent);

                    // Send player confirmation message
                    commandContext.sendMessage(Message.raw("Successfully created a new generator config with id %s!".formatted(id)));
                });
    }

    static {
        MultiArgumentType<TypeSpeed> multiArgumentType = new MultiArgumentType<>("TypeSpeeds", "TypeSpeeds",
                "Soil_Dirt 1 1s", "Soil_Dirt 2 30s,Soil_Grass 2 30s", "Arg 1 = BlockType to Drop (e.g. Soil_Grass), " +
                "Arg 2 = Amount to Drop at Once (e.g. 2), Arg 3 = How Often to Drop (e.g. 30s, 10s, 1m, 1h)") {
            private final WrappedArgumentType<String> blockType;
            private final WrappedArgumentType<String> amount;
            private final WrappedArgumentType<String> duration;

            {
                this.blockType = this.withParameter("blockType", "typespeed.blocktype.usage", ArgTypes.STRING);
                this.amount = this.withParameter("amount", "typespeed.amount.usage", ArgTypes.STRING);
                this.duration = this.withParameter("duration", "typespeed.duration.usage", ArgTypes.STRING);
            }

            @Override
            public TypeSpeed parse(@NonNullDecl MultiArgumentContext context, @NonNullDecl ParseResult parseResult) {
                String blockTypeVal = context.get(this.blockType);

                if ((blockTypeVal == null || blockTypeVal.isEmpty())) {
                    parseResult.fail(Message.raw("Invalid Item Id!").color(Color.RED));
                    return null;
                }

                String amountVal = context.get(this.amount);
                if (amountVal == null || amountVal.isEmpty()) {
                    parseResult.fail(Message.raw("Invalid amount!").color(Color.RED));
                    return null;
                }

                String durationVal = context.get(this.duration);
                if (durationVal == null || durationVal.isEmpty()) {
                    parseResult.fail(Message.raw("Invalid duration!").color(Color.RED));
                    return null;
                }

                return parseTypeSpeed(blockTypeVal, amountVal, durationVal);
            }

            private TypeSpeed parseTypeSpeed(String blockType, String amount, String duration) {
                int itemAmount = TimeUtil.parseIntOrDefault(amount, 1);
                TimeUnit timeUnit = TimeUtil.parseTimeUnit(duration.trim());
                long durationVal = TimeUtil.extractDuration(duration.trim());

                return new TypeSpeed(new ItemStack(blockType.trim(), itemAmount), durationVal, timeUnit);
            }
        };

        listArgumentType = new ListArgumentType<>(multiArgumentType);
    }
}