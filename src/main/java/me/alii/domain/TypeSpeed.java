package me.alii.domain;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class TypeSpeed {
    public static final BuilderCodec<TypeSpeed> CODEC;

    private ItemStack item = new ItemStack("Soil_Dirt");
    private long genSpeed = 1;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private transient ScheduledFuture<?> spawningTask = null;

    public TypeSpeed() {

    }

    public TypeSpeed(ItemStack item, long genSpeed, TimeUnit timeUnit) {
        this.item = item;
        this.genSpeed = genSpeed;
        this.timeUnit = timeUnit;
    }

    public void startGenerating(Vector3d spawnPosition, World world) {
        if (this.spawningTask != null) return;
        spawningTask = HytaleServer.SCHEDULED_EXECUTOR
                .scheduleAtFixedRate(() -> world.execute(() -> {
                    Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
                    TransformComponent transformComponent = new TransformComponent(spawnPosition, Vector3f.ZERO);
                    ItemComponent itemComponent = new ItemComponent(item);
                    holder.addComponent(TransformComponent.getComponentType(), transformComponent);
                    holder.addComponent(ItemComponent.getComponentType(), itemComponent);
                }), 0, genSpeed, timeUnit);
    }

    public void stopGenerating() {
        if (this.spawningTask == null) return;
        spawningTask.cancel(true);
        this.spawningTask = null;
    }

    static {
        CODEC = BuilderCodec.builder(TypeSpeed.class, TypeSpeed::new)
                .append(new KeyedCodec<>("Item", ItemStack.CODEC),
                        (type, value) -> type.item = value,
                        (type) -> type.item).add()
                .append(new KeyedCodec<>("GenSpeed", Codec.LONG),
                        (type, value) -> type.genSpeed = value,
                        (type) -> type.genSpeed).add()
                .append(new KeyedCodec<>("TimeUnit", Codec.STRING),
                        (type, value) -> type.timeUnit = TimeUnit.valueOf(value),
                        (type) -> type.timeUnit.name()).add()
                .build();
    }
}