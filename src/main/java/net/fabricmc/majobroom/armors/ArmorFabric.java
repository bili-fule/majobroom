package net.fabricmc.majobroom.armors;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.item.Items;

public class ArmorFabric {
    public static final int DURABILITY_MULTIPLIER = 25;

    public static final RegistryEntry<ArmorMaterial> MAJOWEARABLE = registerMaterial("majowearable",
            // 防护值映射 - 按照头盔、胸甲、护腿、靴子的顺序
            Map.of(
                    ArmorItem.Type.HELMET, 2,
                    ArmorItem.Type.CHESTPLATE, 6,
                    ArmorItem.Type.LEGGINGS, 5,
                    ArmorItem.Type.BOOTS, 3
            ),
            // 附魔等级
            100,
            // 装备声音
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            // 修复材料
            () -> Ingredient.ofItems(Items.PURPLE_WOOL),
            // 韧性
            10.0F,
            // 击退抗性
            0.0F,
            // 是否可染色
            false);

    public static RegistryEntry<ArmorMaterial> registerMaterial(String id, Map<ArmorItem.Type, Integer> defensePoints,
                                                                int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier,
                                                                float toughness, float knockbackResistance, boolean dyeable) {

        // 创建装备层
        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(Identifier.of("majobroom", id), "", dyeable)
        );

        ArmorMaterial material = new ArmorMaterial(defensePoints, enchantability, equipSound,
                repairIngredientSupplier, layers, toughness, knockbackResistance);

        // 注册材料
        material = Registry.register(Registries.ARMOR_MATERIAL, Identifier.of("majobroom", id), material);

        return RegistryEntry.of(material);
    }

    public static void initialize() {
        // 初始化方法，确保类被加载
    }
}