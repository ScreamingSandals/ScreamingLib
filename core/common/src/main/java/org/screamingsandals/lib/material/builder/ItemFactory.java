package org.screamingsandals.lib.material.builder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.material.data.ItemData;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class ItemFactory {

    private static ItemFactory factory;
    @SuppressWarnings("deprecation")
    private static final Function<ConfigurationNode, Item> CONFIGURATE_RESOLVER = node -> {
        if (!node.isMap()) {
            return readStack(node.getString()).orElse(null);
        }

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
            item.setDisplayName(AdventureHelper.toComponent(Objects.requireNonNull(displayName.getString())));
        }
        var locName = node.node("loc-name");
        if (!locName.empty()) {
            item.setLocalizedName(AdventureHelper.toComponent(Objects.requireNonNull(locName.getString())));
        }
        var customModelData = node.node("custom-model-data");
        if (!customModelData.empty()) {
            try {
                item.setCustomModelData(locName.get(Integer.class));
            } catch (SerializationException ignored) {
            }
        }
        var repairCost = node.node("repair-cost");
        if (!repairCost.empty()) {
            item.setRepair(repairCost.getInt());
        }
        var itemFlags = node.node("ItemFlags");
        if (!itemFlags.empty()) {
            if (itemFlags.isList()) {
                try {
                    item.getItemFlags()
                            .addAll(Objects.requireNonNull(itemFlags.getList(String.class)));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            } else {
                item.getItemFlags().add(itemFlags.getString());
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
                    final var list = Objects.requireNonNull(lore.getList(String.class));
                    list.forEach(next -> item.getLore().add(AdventureHelper.toComponent(next)));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            } else {
                item.getLore().add(AdventureHelper.toComponent(Objects.requireNonNull(lore.getString())));
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
                item.getPotionEffects().addAll(potionEffects.childrenList().stream()
                        .map(PotionEffectMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
            } else {
                PotionEffectMapping.resolve(potionEffects).ifPresent(item.getPotionEffects()::add);
            }
        }

        var attributes = node.node("attributes");
        if (!attributes.empty()) {
            if (attributes.isList()) {
                attributes.childrenList().stream()
                        .map(AttributeMapping::wrapItemAttribute)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(item::addItemAttribute);
            } else {
                AttributeMapping.wrapItemAttribute(attributes).ifPresent(item::addItemAttribute);
            }
        }

        var recipes = node.node("recipes");
        if (!recipes.empty()) {
            if (recipes.isList()) {
                attributes.childrenList().stream()
                        .map(ConfigurationNode::getString)
                        .map(NamespacedMappingKey::ofOptional)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(item::addRecipe);
            } else {
                NamespacedMappingKey.ofOptional(recipes.getString()).ifPresent(item::addRecipe);
            }
        }

        var color = node.node("color");
        if (!color.empty()) {
            var c = TextColor.fromCSSHexString(color.getString(""));
            if (c != null) {
                item.setColor(c);
            } else {
                var c2 = NamedTextColor.NAMES.value(color.getString("").trim().toLowerCase());
                if (c2 != null) {
                    item.setColor(c2);
                }
            }
        }

        var fireworkEffects = node.node("firework-effects");
        if (!fireworkEffects.empty()) {
            if (fireworkEffects.isList()) {
                item.getFireworkEffects().addAll(fireworkEffects.childrenList().stream()
                        .map(FireworkEffectMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
            } else {
                FireworkEffectMapping.resolve(fireworkEffects).ifPresent(item.getFireworkEffects()::add);
            }
        }

        var power = node.node("power");
        if (!power.empty()) {
            item.setPower(power.getInt());
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

    protected ItemFactory() {
        if (factory != null) {
            throw new UnsupportedOperationException("ItemFactory is already initialized.");
        }

        factory = this;
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
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactor is not initialized yet!");
        }

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
            item.setDisplayName(AdventureHelper.toComponent(name.trim()));
        }
        item.getLore().clear();
        lore.forEach(next -> item.getLore().add(AdventureHelper.toComponent(next)));

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

    public static Item normalize(Item item) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.itemConverter.normalize(item);
    }

    public static <C extends Container> Optional<C> wrapContainer(Object container) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.wrapContainer0(container);
    }

    public abstract <C extends Container> Optional<C> wrapContainer0(Object container);

    public static <C extends Container> Optional<C> createContainer(InventoryTypeHolder type, Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.createContainer0(type, name);
    }

    public abstract <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type, Component name);

    public static ItemData createNewItemData() {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.createNewItemData0();
    }

    public abstract ItemData createNewItemData0();

}
