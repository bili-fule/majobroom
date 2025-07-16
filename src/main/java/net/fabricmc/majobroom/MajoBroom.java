package net.fabricmc.majobroom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.fabricmc.majobroom.config.MajoBroomConfig;
import net.fabricmc.majobroom.entity.BroomEntity;
import net.fabricmc.majobroom.items.BroomItem;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MajoBroom implements ModInitializer {
	public static final String MODID = "majobroom";

	// 创建ItemGroup的RegistryKey
	public static final RegistryKey<ItemGroup> MAJO_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MODID, "majo_group"));

	// 盔甲材料注册
	public static final RegistryEntry<ArmorMaterial> FABRIC_ARMOR = registerArmorMaterial("fabric",
			Map.of(
					ArmorItem.Type.HELMET, 3,
					ArmorItem.Type.CHESTPLATE, 8,
					ArmorItem.Type.LEGGINGS, 6,
					ArmorItem.Type.BOOTS, 3
			),
			5,
			SoundEvents.ITEM_ARMOR_EQUIP_IRON,
			() -> Ingredient.ofItems(Items.IRON_INGOT),
			0.0F,
			0.0F,
			false
	);

	public static final Item broomItem = new BroomItem(new Item.Settings().maxCount(1));
	public static final Item majoCloth = new BaseArmor(FABRIC_ARMOR, ArmorItem.Type.CHESTPLATE);
	public static final Item majoHat = new BaseArmor(FABRIC_ARMOR, ArmorItem.Type.HELMET);

	// 使用新的FabricItemGroup.builder()方法
	public static final ItemGroup majoGroup = FabricItemGroup.builder()
			.icon(() -> new ItemStack(MajoBroom.broomItem))
			.displayName(Text.translatable("itemGroup.majobroom.majo_group"))
			.build();

	// 盔甲材料注册方法
	private static RegistryEntry<ArmorMaterial> registerArmorMaterial(String id, Map<ArmorItem.Type, Integer> defensePoints,
																	  int enchantability, RegistryEntry<net.minecraft.sound.SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier,
																	  float toughness, float knockbackResistance, boolean dyeable) {
		List<ArmorMaterial.Layer> layers = List.of(
				new ArmorMaterial.Layer(Identifier.of(MODID, id), "", dyeable)
		);

		ArmorMaterial material = new ArmorMaterial(defensePoints, enchantability, equipSound,
				repairIngredientSupplier, layers, toughness, knockbackResistance);
		material = Registry.register(Registries.ARMOR_MATERIAL, Identifier.of(MODID, id), material);

		return RegistryEntry.of(material);
	}

	// ItemGroup注册
	static {
		Registry.register(Registries.ITEM_GROUP, MAJO_GROUP_KEY, majoGroup);
		ItemGroupEvents.modifyEntriesEvent(MAJO_GROUP_KEY).register(MajoBroom::setItemGroup);
	}

	protected static void setItemGroup(FabricItemGroupEntries entries) {
		entries.add(broomItem);
		entries.add(majoHat);
		entries.add(majoCloth);
	}

	// 实体注册
	public static final EntityType<BroomEntity> BROOM_ENTITY_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of(MODID, "majo_broom"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BroomEntity::new)
					.dimensions(EntityDimensions.fixed(0.75f, 0.75f))
					.build()
	);

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, Identifier.of(MODID, "broom_item"), broomItem);
		Registry.register(Registries.ITEM, Identifier.of(MODID, "majo_cloth"), majoCloth);
		Registry.register(Registries.ITEM, Identifier.of(MODID, "majo_hat"), majoHat);

		MajoBroomConfig.getInstance();
	}

	public static void main(String[] args) {
		System.out.println("sda");
	}
}