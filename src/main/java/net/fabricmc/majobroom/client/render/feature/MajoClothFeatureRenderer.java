package net.fabricmc.majobroom.client.render.feature;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.majobroom.MajoBroom;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.fabricmc.majobroom.armors.MajoWearableModel;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class MajoClothFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends ArmorFeatureRenderer<T, M,A> {
    private static MajoWearableModel hat = null;
    private static MajoWearableModel cloth = null;
    private static MajoWearableModel foot = null;

    public MajoClothFeatureRenderer(FeatureRendererContext<T, M> context, A leggingsModel, A bodyModel, BakedModelManager modelManager) {
        super(context, leggingsModel, bodyModel, modelManager);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof BaseArmor) {
            if (hat == null){
                hat = new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(256, 256), "majo_hat.json", MajoWearableModel.ModelType.HEAD);
            }
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, light, (A) hat);
        }
        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BaseArmor){
            if (cloth == null){
                cloth = new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(256, 256), "majo_cloth.json", MajoWearableModel.ModelType.UPPER_BODY);
            }
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.CHEST, light, (A) cloth);
        }

        if (livingEntity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof BaseArmor){
            if (foot == null){
                foot = new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(64, 64), "stocking.json", MajoWearableModel.ModelType.FOOT);
            }
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.FEET, light, (A) foot);
        }
    }

    private boolean usesSecondLayer(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS;
    }

    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if (itemStack.getItem() instanceof BaseArmor) {
            ArmorItem armorItem = (ArmorItem)itemStack.getItem();
            if (armorItem.getSlotType() == armorSlot) {
                this.getContextModel().copyBipedStateTo(model);
                this.setVisible(model, armorSlot);
                boolean bl = this.usesSecondLayer(armorSlot);

                // 在1.21中，染色功能现在通过DataComponents处理
                int i = getArmorColor(itemStack);
                float f = (float)(i >> 16 & 255) / 255.0F;
                float g = (float)(i >> 8 & 255) / 255.0F;
                float h = (float)(i & 255) / 255.0F;

                this.renderArmorParts(matrices, vertexConsumers, light, armorItem, false, model, bl, f, g, h, (String)null);
                this.renderArmorParts(matrices, vertexConsumers, light, armorItem, false, model, bl, 1.0F, 1.0F, 1.0F, "overlay");
            }
        }
    }

    // 新的方法来获取盔甲颜色 - 使用1.21的DataComponents系统
// 修复 getArmorColor 方法
    private int getArmorColor(ItemStack itemStack) {
        // 检查是否为可染色盔甲
        if (itemStack.getItem() instanceof ArmorItem armorItem) {
            ArmorMaterial material = armorItem.getMaterial().value();
            // 在1.21中，直接检查是否有染色组件，而不是检查layer.dyeable()
            DyedColorComponent dyedColor = itemStack.get(DataComponentTypes.DYED_COLOR);
            if (dyedColor != null) {
                return dyedColor.rgb();
            }
            // 如果没有染色组件但是皮革材料，返回默认皮革颜色
            return 0xA06540; // 默认皮革颜色
        }
        return 0xFFFFFF; // 白色（无染色）
    }

    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, boolean legs, float red, float green, float blue, @Nullable String overlay) {
        RenderLayer layer = RenderLayer.getEntityTranslucent(this.getArmorTexture(item, legs, overlay));
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, layer, false);
        // 在1.21中，model.render() 只需要4个参数
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }

    private Identifier getArmorTexture(ArmorItem item, boolean legs, String overlay) {
        if (item instanceof BaseArmor){
            switch (item.toString()){
                case "majo_hat":
                    return Identifier.of(MajoBroom.MODID,"jsonmodels/textures/"+item.toString()+(overlay == null ? "" : "_" + overlay) + ".png");
                case "majo_cloth":
                case "stocking":
                    return Identifier.of(MajoBroom.MODID,"jsonmodels/textures/"+item.toString() + ".png");
            }
        }
        return Identifier.of(MajoBroom.MODID,"empty.png");
    }
}

class MajoStockRenderLayer{

}