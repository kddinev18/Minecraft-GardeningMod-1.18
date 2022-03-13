package com.sAnimVsa.gardening_mod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModTab {
    public static final CreativeModeTab GARDENINGMOD_TAB = new CreativeModeTab("gardening_mod_creative_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.POOP.get());
        }
    };
}
