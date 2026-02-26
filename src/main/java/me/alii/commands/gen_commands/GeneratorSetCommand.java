package me.alii.commands.gen_commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import me.alii.components.GeneratorComponent;
import me.alii.registry.GeneratorRegistry;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.Optional;

public class GeneratorSetCommand extends CommandBase {
    private final GeneratorRegistry generatorRegistry;
    private final RequiredArg<String> idArg;

    public GeneratorSetCommand(GeneratorRegistry generatorRegistry) {
        super("gen-set", "Bedwars admin command.");
        this.generatorRegistry = generatorRegistry;
        this.idArg = withRequiredArg("genId", "The id of the bedwars generator you'd like to create.", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        if (!(commandContext.sender() instanceof Player player)) return;
        World world = player.getWorld();
        if (world == null) return;

        Store<EntityStore> store = world.getEntityStore()
                .getStore();
        Ref<EntityStore> ref = player.getReference();
        if (ref == null) return;

        String id = idArg.get(commandContext);
        Optional<GeneratorComponent> generatorComponent = generatorRegistry.getComponent(id);
        generatorComponent.ifPresentOrElse(component ->
                world.execute(() -> {
                    Vector3i targetBlock = TargetUtil.getTargetBlock(ref, 6, store);
                    if (targetBlock == null) return;

                    ChunkStore chunkStore = world.getChunkStore();

                    long chunkIndex = ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z);
                    Ref<ChunkStore> chunkRef = chunkStore.getChunkReference(chunkIndex);
                    if (chunkRef == null) return;

                    Store<ChunkStore> storeChunk = chunkStore.getStore();
                    if (storeChunk.getComponent(chunkRef, GeneratorComponent.getComponentType()) != null) return;

                    // Get a centered location that doesn't spawn inside a block
                    Vector3d vector3d = targetBlock.toVector3d()
                            .clone().add(0.5, 0.5, 0.5);

                    GeneratorComponent clonedComponent = (GeneratorComponent) component.clone();
                    if (clonedComponent == null) return;

                    clonedComponent.setSpawnPosition(vector3d);
                    storeChunk.addComponent(chunkRef, GeneratorComponent.getComponentType(),
                            clonedComponent);

                    player.sendMessage(Message.raw("Generator Initialized!").color(Color.GREEN));
                }), () -> player.sendMessage(Message.raw(("No generator with ID: %s\n" +
                "Please create one with /bws gen-create --help").formatted(id))));
    }
}