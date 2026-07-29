package net.cozystudios.tokimistoolshed.compat;

import net.cozystudios.tokimistoolshed.TokimisToolshed;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class JobsPlusCompat {

    private static boolean initialized = false;
    private static boolean available = false;

    private static Constructor<?> actionDataBuilderCtor;
    private static Method withDataMethod;
    private static Method buildMethod;
    private static Method sendToActionMethod;

    private static Object breakBlockActionType;
    private static Object blockStateDataType;
    private static Object blockPositionDataType;
    private static Object worldDataType;
    private static Object itemStackDataType;
    private static Object itemDataType;

    private JobsPlusCompat() {
    }

    public static boolean isAvailable() {
        ensureInit();
        return available;
    }

    public static void fireBreakBlockEvent(ServerPlayer player, ServerLevel world, BlockPos pos, BlockState state, ItemStack stack) {
        ensureInit();
        if (!available) return;

        try {
            Object builder = actionDataBuilderCtor.newInstance(player, breakBlockActionType);
            builder = withDataMethod.invoke(builder, blockStateDataType, state);
            builder = withDataMethod.invoke(builder, blockPositionDataType, pos);
            builder = withDataMethod.invoke(builder, worldDataType, world);
            if (itemStackDataType != null) {
                builder = withDataMethod.invoke(builder, itemStackDataType, stack);
            }
            if (itemDataType != null) {
                builder = withDataMethod.invoke(builder, itemDataType, stack.getItem());
            }
            Object actionData = buildMethod.invoke(builder);
            sendToActionMethod.invoke(actionData);
        } catch (Throwable ignored) {
        }
    }

    private static synchronized void ensureInit() {
        if (initialized) return;
        initialized = true;

        FabricLoader loader = FabricLoader.getInstance();
        if (!loader.isModLoaded("jobsplus") && !loader.isModLoaded("arc")) return;

        try {
            Class<?> playerClass = Class.forName("com.daqem.arc.api.player.ArcPlayer");
            Class<?> builderClass = Class.forName("com.daqem.arc.api.action.data.ActionDataBuilder");
            Class<?> actionTypeClass = tryClass(
                    "com.daqem.arc.api.action.IActionType",
                    "com.daqem.arc.api.action.type.ActionType"
            );
            Class<?> dataTypeClass = tryClass(
                    "com.daqem.arc.api.action.data.IActionDataType",
                    "com.daqem.arc.api.action.data.type.ActionDataType"
            );

            actionDataBuilderCtor = builderClass.getConstructor(playerClass, actionTypeClass);
            withDataMethod = builderClass.getMethod("withData", dataTypeClass, Object.class);
            buildMethod = builderClass.getMethod("build");

            Class<?> actionDataClass = buildMethod.getReturnType();
            sendToActionMethod = actionDataClass.getMethod("sendToAction");

            breakBlockActionType = actionTypeClass.getField("BREAK_BLOCK").get(null);
            blockStateDataType = dataTypeClass.getField("BLOCK_STATE").get(null);
            blockPositionDataType = dataTypeClass.getField("BLOCK_POSITION").get(null);
            worldDataType = dataTypeClass.getField("WORLD").get(null);
            itemStackDataType = tryStaticField(dataTypeClass, "ITEM_STACK");
            itemDataType = tryStaticField(dataTypeClass, "ITEM");

            available = true;
            TokimisToolshed.LOGGER.info("Jobs+/ARC detected — lumber axe will grant XP for full trees.");
        } catch (Throwable t) {
            available = false;
            TokimisToolshed.LOGGER.warn("Jobs+ compat unavailable: {}", t.toString());
        }
    }

    private static Class<?> tryClass(String... names) throws ClassNotFoundException {
        for (String name : names) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException(String.join(" | ", names));
    }

    private static Object tryStaticField(Class<?> owner, String name) {
        try {
            return owner.getField(name).get(null);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
