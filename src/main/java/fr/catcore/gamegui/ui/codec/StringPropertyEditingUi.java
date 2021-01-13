package fr.catcore.gamegui.ui.codec;

import fr.catcore.gamegui.accessor.AnvilScreenHandlerAccessor;
import fr.catcore.gamegui.accessor.ForgingScreenHandlerAccessor;
import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import fr.catcore.gamegui.builder.gamebuilder.main.MainGuiEntry;
import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.inventory.codec.ConfigureGameMainInventory;
import fr.catcore.gamegui.inventory.codec.StringPropertyInventory;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringTag;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class StringPropertyEditingUi implements NamedScreenHandlerFactory {
    private final Text title;
    private final Consumer<CodecGuiBuilder> builder;

    StringPropertyEditingUi(Text title, Consumer<CodecGuiBuilder> builder) {
        this.title = title;
        this.builder = builder;
    }

    public static StringPropertyEditingUi create(Text title, Consumer<CodecGuiBuilder> builder) {
        return new StringPropertyEditingUi(title, builder);
    }

    public Text getDisplayName() {
        return this.title;
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        final ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        StringPropertyInventory inventory = new StringPropertyInventory(GameCreatorHelper.getEditingFieldValue(player.getUuid()).asString(), serverPlayer, this.builder);
//        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, inventory, 6) {
//            public ItemStack transferSlot(PlayerEntity player, int invSlot) {
//                this.resendInventory();
//                return ItemStack.EMPTY;
//            }
//
//            public ItemStack onSlotClick(int slot, int data, SlotActionType action, PlayerEntity player) {
//                if (action == SlotActionType.SWAP || action == SlotActionType.THROW || action == SlotActionType.CLONE) {
//                    this.resendInventory();
//                    return ItemStack.EMPTY;
//                }
//
//                return super.onSlotClick(slot, data, action, player);
//            }
//
//            private void resendInventory() {
//                serverPlayer.onHandlerRegistered(this, this.getStacks());
//            }
//        };

        AnvilScreenHandler anvilScreenHandler = new AnvilScreenHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY) {
            public ItemStack transferSlot(PlayerEntity player, int invSlot) {
                this.resendInventory();
                return ItemStack.EMPTY;
            }

            public ItemStack onSlotClick(int slot, int data, SlotActionType action, PlayerEntity player) {
                if (action == SlotActionType.SWAP || action == SlotActionType.THROW || action == SlotActionType.CLONE) {
                    this.resendInventory();
                    return ItemStack.EMPTY;
                }

                return super.onSlotClick(slot, data, action, player);
            }

            private void resendInventory() {
                serverPlayer.onHandlerRegistered(this, this.getStacks());
            }

            @Override
            protected ItemStack onTakeOutput(PlayerEntity player, ItemStack stack) {
                this.input.setStack(0, ItemStack.EMPTY);
                this.input.setStack(1, ItemStack.EMPTY);

                String newValue = stack.getName().asString();
                GameCreatorHelper.setEditingFieldValue(player.getUuid(), StringTag.of(newValue));
                if (GameCreatorHelper.getEditingField(player.getUuid()).equals("name")) {
                    player.openHandledScreen(ConfigureGameMainUi.create(new LiteralText("Configure Game: Main"), mainGuiEntryCodecGuiBuilder -> {
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createType());
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createName());
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createConfig());
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createLaunch());
                    }));
                }

                return ItemStack.EMPTY;
            }

            public void updateResult() {
                ItemStack itemStack = this.input.getStack(0);
                ((AnvilScreenHandlerAccessor)(Object)this).setLevelCost_server(0);
                int i = 0;
                int j = 0;
                int k = 0;
                if (itemStack.isEmpty()) {
                    this.output.setStack(0, ItemStack.EMPTY);
                } else {
                    ItemStack itemStack2 = itemStack.copy();
                    ItemStack itemStack3 = this.input.getStack(1);
                    Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack2);
                    j = j + itemStack.getRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getRepairCost());
                    ((AnvilScreenHandlerAccessor)(Object)this).setRepairItemUsage(0);
                    if (!itemStack3.isEmpty()) {
                        boolean bl = itemStack3.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantmentTag(itemStack3).isEmpty();
                        int o;
                        int p;
                        int q;
                        if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                            o = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                            if (o <= 0) {
                                this.output.setStack(0, ItemStack.EMPTY);
                                return;
                            }

                            for(p = 0; o > 0 && p < itemStack3.getCount(); ++p) {
                                q = itemStack2.getDamage() - o;
                                itemStack2.setDamage(q);
                                ++i;
                                o = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                            }

                            ((AnvilScreenHandlerAccessor)(Object)this).setRepairItemUsage(p);
                        } else {
                            if (!bl && (itemStack2.getItem() != itemStack3.getItem() || !itemStack2.isDamageable())) {
                                this.output.setStack(0, ItemStack.EMPTY);
                                return;
                            }

                            if (itemStack2.isDamageable() && !bl) {
                                o = itemStack.getMaxDamage() - itemStack.getDamage();
                                p = itemStack3.getMaxDamage() - itemStack3.getDamage();
                                q = p + itemStack2.getMaxDamage() * 12 / 100;
                                int r = o + q;
                                int s = itemStack2.getMaxDamage() - r;
                                if (s < 0) {
                                    s = 0;
                                }

                                if (s < itemStack2.getDamage()) {
                                    itemStack2.setDamage(s);
                                    i += 2;
                                }
                            }

                            Map<Enchantment, Integer> map2 = EnchantmentHelper.get(itemStack3);
                            boolean bl2 = false;
                            boolean bl3 = false;
                            Iterator var24 = map2.keySet().iterator();

                            label155:
                            while(true) {
                                Enchantment enchantment;
                                do {
                                    if (!var24.hasNext()) {
                                        if (bl3 && !bl2) {
                                            this.output.setStack(0, ItemStack.EMPTY);
                                            return;
                                        }
                                        break label155;
                                    }

                                    enchantment = (Enchantment)var24.next();
                                } while(enchantment == null);

                                int t = (Integer)map.getOrDefault(enchantment, 0);
                                int u = (Integer)map2.get(enchantment);
                                u = t == u ? u + 1 : Math.max(u, t);
                                boolean bl4 = enchantment.isAcceptableItem(itemStack);
                                if (this.player.abilities.creativeMode || itemStack.getItem() == Items.ENCHANTED_BOOK) {
                                    bl4 = true;
                                }

                                Iterator var17 = map.keySet().iterator();

                                while(var17.hasNext()) {
                                    Enchantment enchantment2 = (Enchantment)var17.next();
                                    if (enchantment2 != enchantment && !enchantment.canCombine(enchantment2)) {
                                        bl4 = false;
                                        ++i;
                                    }
                                }

                                if (!bl4) {
                                    bl3 = true;
                                } else {
                                    bl2 = true;
                                    if (u > enchantment.getMaxLevel()) {
                                        u = enchantment.getMaxLevel();
                                    }

                                    map.put(enchantment, u);
                                    int v = 0;
                                    switch(enchantment.getRarity()) {
                                        case COMMON:
                                            v = 1;
                                            break;
                                        case UNCOMMON:
                                            v = 2;
                                            break;
                                        case RARE:
                                            v = 4;
                                            break;
                                        case VERY_RARE:
                                            v = 8;
                                    }

                                    if (bl) {
                                        v = Math.max(1, v / 2);
                                    }

                                    i += v * u;
                                    if (itemStack.getCount() > 1) {
                                        i = 40;
                                    }
                                }
                            }
                        }
                    }

                    if (StringUtils.isBlank(((AnvilScreenHandlerAccessor)(Object)this).getNewItemName())) {
                        if (itemStack.hasCustomName()) {
                            k = 1;
                            i += k;
                            itemStack2.removeCustomName();
                        }
                    } else if (!((AnvilScreenHandlerAccessor)(Object)this).getNewItemName().equals(itemStack.getName().getString())) {
                        k = 1;
                        i += k;
                        itemStack2.setCustomName(new LiteralText(((AnvilScreenHandlerAccessor)(Object)this).getNewItemName()));
                    }

                    if (i <= 0) {
                        itemStack2 = ItemStack.EMPTY;
                    }

                    this.output.setStack(0, itemStack2);
                    this.sendContentUpdates();
                }
            }

            @Override
            protected boolean canTakeOutput(PlayerEntity player, boolean present) {
                return true;
            }
        };
        ((ForgingScreenHandlerAccessor)anvilScreenHandler).setInventory(inventory, (ServerPlayerEntity) player);
        return anvilScreenHandler;
    }
}
