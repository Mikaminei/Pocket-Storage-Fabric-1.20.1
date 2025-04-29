package de.mikaminei.pocketstorage.item;

import de.mikaminei.pocketstorage.PocketStorage;
import de.mikaminei.pocketstorage.item.custom.ChestLinkItem;
import de.mikaminei.pocketstorage.item.custom.CraftingLinkItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item CHEST_LINK = registerItem("chest_link",
            new ChestLinkItem(new FabricItemSettings()));
    public static final Item CRAFTING_LINK = registerItem("crafting_link",
            new CraftingLinkItem(new FabricItemSettings()));

    private static void addItemsToToolsItemGroup(FabricItemGroupEntries entries) {
        entries.add(CHEST_LINK);
        entries.add(CRAFTING_LINK);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(PocketStorage.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PocketStorage.LOGGER.debug("Registering mod items for {}", PocketStorage.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsItemGroup);
    }
}