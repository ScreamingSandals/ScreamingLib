package org.screamingsandals.lib.material.builder;

import lombok.SneakyThrows;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.ConfigurateUtils;
import org.screamingsandals.lib.utils.ConsumerExecutor;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ItemFactory {

    private static ItemFactory factory;
    private static final Function<ConfigurationNode, Item> CONFIGURATE_RESOLVER = node -> {
        var type = node.node("type");

        var optionalItem = readStack(type.getString());
        if (optionalItem.isEmpty()) {
            return null;
        }
        var item = optionalItem.get();

        var amount = node.node("amount");
        if (!amount.empty()) {
            item.setAmount(amount.getInt(1));
        }

        var damage = node.node("damage");
        if (!damage.empty()) {
            item.setMaterial(item.getMaterial().newDurability(damage.getInt(0)));
        }
        var durability = node.node("durability");
        if (!durability.empty()) {
            item.setMaterial(item.getMaterial().newDurability(durability.getInt(0)));
        }

        var displayName = node.node("display-name");
        if (!displayName.empty()) {
            item.setDisplayName(displayName.getString());
        }
        var locName = node.node("loc-name");
        if (!locName.empty()) {
            item.setLocalizedName(locName.getString());
        }
        var customModelData = node.node("custom-model-data");
        if (!customModelData.empty()) {
            item.setCustomModelData(locName.getInt(0));
        }
        var repairCost = node.node("repair-cost");
        if (!repairCost.empty()) {
            item.setRepair(repairCost.getInt());
        }
        var itemFlags = node.node("ItemFlags");
        if (!itemFlags.empty()) {
            if (itemFlags.isList()) {
                try {
                    item.setItemFlags(itemFlags.getList(String.class));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            } else {
                //noinspection ConstantConditions
                item.setItemFlags(List.of(itemFlags.getString()));
            }
        }
        var unbreakable = node.node("Unbreakable");
        if (!unbreakable.empty()) {
            item.setUnbreakable(unbreakable.getBoolean(false));
        }
        var lore = node.node("lore");
        if (!lore.empty()) {
            if (lore.isList()) {
                try {
                    item.setLore(lore.getList(String.class));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            } else {
                //noinspection ConstantConditions
                item.setLore(List.of(lore.getString()));
            }
        }
        var enchants = node.node("enchants");
        if (!enchants.empty()) {
            if (enchants.isMap()) {
                enchants.childrenMap().entrySet().stream()
                        .map(EnchantmentMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(item.getEnchantments()::add);
            } else if (enchants.isList()) {
                try {
                    //noinspection ConstantConditions
                    enchants.getList(Object.class).stream()
                            .map(EnchantmentMapping::resolve)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(item.getEnchantments()::add);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    EnchantmentMapping.resolve(enchants.get(Object.class)).ifPresent(item.getEnchantments()::add);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            }
        }
        var potionType = node.node("potion-type");
        if (!potionType.empty()) {
            try {
                PotionMapping.resolve(potionType.get(Object.class)).ifPresent(item::setPotion);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }
        var potionEffects = node.node("effects");
        if (!potionEffects.empty()) {
            if (potionEffects.isList()) {
                item.setPotionEffects(potionEffects.childrenList().stream()
                        .map(PotionEffectMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
            } else {
                item.setPotionEffects(new ArrayList<>());
                PotionEffectMapping.resolve(potionEffects).ifPresent(item.getPotionEffects()::add);
            }
        }

        var meta = node.node("meta");
        if (!meta.empty()) {
            item.setPlatformMeta(ConfigurateUtils.toMap(meta));
        }

        return item;
    };
    protected BidirectionalConverter<Item> itemConverter = BidirectionalConverter.<Item>build()
            .registerW2P(String.class, item -> item.getMaterial().getPlatformName())
            .registerW2P(MaterialHolder.class, Item::getMaterial)
            .registerP2W(ConfigurationNode.class, CONFIGURATE_RESOLVER)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_RESOLVER.apply(BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .registerP2W(Item.class, Item::clone);

    private static final Pattern SHORT_STACK_PATTERN = Pattern.compile("^(?<material>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(\\\\*)?(;(?<amount>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)?(\\\\*)?(;(?<name>(\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+|(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+))?(\\\\*)?(;(?<lore>.*))?)?)?$");
    private static final Pattern LORE_SPLIT = Pattern.compile("((\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+\")|((?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(?=($|;))");

    @SneakyThrows
    public static void init(Supplier<ItemFactory> factoryClass) {
        if (factory != null) {
            throw new UnsupportedOperationException("ItemFactory is already initialized.");
        }

        factory = factoryClass.get();

        assert MaterialMapping.isInitialized();
        assert PotionMapping.isInitialized();
        assert EnchantmentMapping.isInitialized();
        assert PotionEffectMapping.isInitialized();

        factory.itemConverter.finish();
    }

    public static ItemBuilder builder() {
        return new ItemBuilder(new Item());
    }

    public static Optional<Item> build(Object stack) {
        return readStack(stack);
    }

    public static Optional<Item> build(Consumer<ItemBuilder> builder) {
        var item = new Item();

        if (builder != null) {
            ConsumerExecutor.execute(builder, new ItemBuilder(item));
        }

        if (item.getMaterial() != null) {
            return Optional.of(item);
        }

        return Optional.empty();
    }

    public static Optional<Item> build(Object shortStack, Consumer<ItemBuilder> builder) {
        var item = readStack(shortStack);
        if (item.isEmpty()) {
            return Optional.empty();
        }

        if (builder != null) {
            ConsumerExecutor.execute(builder, new ItemBuilder(item.get()));
        }

        return item;
    }

    public static Optional<Item> readStack(Object stackObject) {
        var it = factory.itemConverter.convertOptional(stackObject);
        if (it.isPresent()) {
            return it;
        }
        return readShortStack(new Item(), stackObject);
    }

    public static Optional<Item> readShortStack(Item item, Object shortStackObject) {
        if (shortStackObject instanceof ConfigurationNode) {
            shortStackObject = ((ConfigurationNode) shortStackObject).getString();
        }
        if (!(shortStackObject instanceof String)) {
            var opt = MaterialMapping.resolve(shortStackObject);
            if (opt.isPresent()) {
                item.setMaterial(opt.get());
                return Optional.of(item);
            }
        }
        if (shortStackObject == null) {
            return Optional.empty();
        }

        var shortStack = shortStackObject.toString().trim();
        if (shortStack.startsWith("(cast to ItemStack)")) {
            shortStack = shortStack.substring(19).trim();
        }

        var matcher = SHORT_STACK_PATTERN.matcher(shortStack);

        if (!matcher.matches() || matcher.group("material") == null) {
            return Optional.empty();
        }

        var material = matcher.group("material");
        var amount = matcher.group("amount");
        var name = matcher.group("name");
        if (name != null && name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        var lore_string = matcher.group("lore");
        var lore = new ArrayList<String>();
        if (lore_string != null) {
            Matcher loreMatcher = LORE_SPLIT.matcher(lore_string);
            while (loreMatcher.find()) {
                lore.add(loreMatcher.group());
            }
        }

        var materialHolder = MaterialMapping.resolve(material);
        if (materialHolder.isEmpty()) {
            return Optional.empty();
        }
        item.setMaterial(materialHolder.get());
        try {
            if (amount != null && !amount.trim().isEmpty()) {
                item.setAmount(Integer.parseInt(amount.trim()));
            }
        } catch (NumberFormatException ignored) {
        }
        if (name != null && !name.trim().isEmpty()) {
            item.setDisplayName(name.trim());
        }
        item.setLore(lore);

        return Optional.of(item);
    }

    public static List<Item> buildAll(List<Object> objects) {
        return objects.stream().map(o -> build(o).orElse(ItemFactory.getAir())).collect(Collectors.toList());
    }

    private static Item cachedAir;

    public static Item getAir() {
        if (cachedAir == null) {
            cachedAir = build("AIR").orElseThrow();
        }
        return cachedAir.clone();
    }

    public static <T> T convertItem(Item item, Class<T> newType) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.itemConverter.convert(item, newType);
    }

    public static Optional<Container> wrapContainer(Object container) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.wrapContainer0(container);
    }

    public abstract Optional<Container> wrapContainer0(Object container);

    public static boolean isInitialized() {
        return factory != null;
    }
}
