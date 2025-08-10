package com.totemmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class TotemKeyMod implements ClientModInitializer {
    private static KeyBinding totemKey;

    @Override
    public void onInitializeClient() {
        totemKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.totemkey.put_totem",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.totemkey"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (totemKey.wasPressed() && client.player != null) {
                PlayerEntity player = client.player;

                // Already holding totem in offhand
                if (player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;

                // Search inventory and move to offhand
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack stack = player.getInventory().getStack(i);
                    if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                        player.getInventory().setStack(i, player.getOffHandStack());
                        player.setStackInHand(net.minecraft.util.Hand.OFF_HAND, stack);
                        break;
                    }
                }
            }
        });
    }
}
