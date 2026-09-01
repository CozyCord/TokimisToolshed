package net.cozystudios.tokimistoolshed.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
//? if >=1.21 {
/*import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
*///?}

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
        //? if <1.21 {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(TAG)) return COPY;
        return fromOrdinal(nbt.getInt(TAG));
        //?} elif <1.21.5 {
        /*NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component == null) return COPY;
        NbtCompound nbt = component.copyNbt();
        if (!nbt.contains(TAG)) return COPY;
        return fromOrdinal(nbt.getInt(TAG));
        *///?} else {
        /*NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component == null) return COPY;
        NbtCompound nbt = component.copyNbt();
        return fromOrdinal(nbt.getInt(TAG).orElse(0));
        *///?}
    }

    public static void write(ItemStack stack, ChiselMode mode) {
        //? if <1.21 {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(TAG, mode.ordinal());
        //?} else {
        /*NbtCompound nbt = new NbtCompound();
        NbtComponent existing = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (existing != null) {
            nbt = existing.copyNbt();
        }
        nbt.putInt(TAG, mode.ordinal());
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        *///?}
    }
}
