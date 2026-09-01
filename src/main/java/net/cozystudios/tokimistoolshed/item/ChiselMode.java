package net.cozystudios.tokimistoolshed.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public enum ChiselMode {
    COPY("Copy"),
    SHUFFLE("Shuffle");

    private static final String TAG = "ChiselMode";
    private static final ChiselMode[] VALUES = values();

    private final String displayName;

    ChiselMode(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public ChiselMode next() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }

    public ChiselMode prev() {
        return VALUES[(ordinal() - 1 + VALUES.length) % VALUES.length];
    }

    public static ChiselMode fromOrdinal(int i) {
        if (i < 0 || i >= VALUES.length) return COPY;
        return VALUES[i];
    }

    public static ChiselMode read(ItemStack stack) {
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) return COPY;
        CompoundTag tag = component.copyTag();
        return fromOrdinal(tag.getInt(TAG).orElse(0));
    }

    public static void write(ItemStack stack, ChiselMode mode) {
        CompoundTag tag = new CompoundTag();
        CustomData existing = stack.get(DataComponents.CUSTOM_DATA);
        if (existing != null) {
            tag = existing.copyTag();
        }
        tag.putInt(TAG, mode.ordinal());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
