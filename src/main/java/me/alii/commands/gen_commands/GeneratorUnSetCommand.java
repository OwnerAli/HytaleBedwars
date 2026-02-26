package me.alii.commands.gen_commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import me.alii.components.GeneratorComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;

public class GeneratorUnSetCommand extends CommandBase {

    public GeneratorUnSetCommand() {
        super("gen-unset", "Bedwars admin command.");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        if (!(commandContext.sender() instanceof Player player)) return;
        World world = player.getWorld();
        if (world == null) return;

        Store<EntityStore> entityStore = world.getEntityStore()
                .getStore();
        Ref<EntityStore> ref = player.getReference();
        if (ref == null) return;


        world.execute(() -> {
            Vector3i targetBlock = TargetUtil.getTargetBlock(ref, 6, entityStore);
            if (targetBlock == null) return;

            ChunkStore chunkStore = world.getChunkStore();

            long chunkIndex = ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z);
            Ref<ChunkStore> chunkRef = chunkStore.getChunkReference(chunkIndex);
            if (chunkRef == null) return;

            Store<ChunkStore> store = chunkStore.getStore();
            boolean result = store.removeComponentIfExists(chunkRef, GeneratorComponent.getComponentType());
            if (result) {
                player.sendMessage(Message.raw("Generator unset!").color(Color.GREEN));
                return;
            }
            player.sendMessage(Message.raw("There is no generator at that location!").color(Color.RED));
        });
    }
}