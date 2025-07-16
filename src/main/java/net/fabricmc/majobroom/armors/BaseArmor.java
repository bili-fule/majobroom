package net.fabricmc.majobroom.armors;

import net.fabricmc.majobroom.MajoBroom;
import net.minecraft.block.DispenserBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;

public class BaseArmor extends ArmorItem {

    public BaseArmor(RegistryEntry<ArmorMaterial> material, ArmorItem.Type slot) {
        super(material, slot, new Item.Settings());
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);//发射器穿装备
    }

    public int getColor(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(-6265536, false)).rgb();
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(getDefaultColor(), false));
        return stack;
    }

    public int getDefaultColor() {
        return 14525383; // 您的默认颜色
    }
}