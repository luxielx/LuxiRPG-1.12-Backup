package Attr;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class Attributes_12 {
    private static final int TAG_COMPOUND = 10;
    private static volatile Field HANDLE_FIELD;
    // This may be modified
    public net.minecraft.server.v1_12_R1.ItemStack nmsStack;
    private NBTTagCompound parent;
    private NBTTagList attributes;

    public Attributes_12(ItemStack stack) {
        // There's no way around this ...
        if (stack instanceof CraftItemStack) {
            this.nmsStack = getNmsStack((CraftItemStack) stack);
        } else {
            // Create a CraftItemStack (under the hood)
            this.nmsStack = CraftItemStack.asNMSCopy(stack);
        }

        // Load NBT
        if (nmsStack.getTag() == null) {
            parent = new NBTTagCompound();
            nmsStack.setTag(new NBTTagCompound());
        } else {
            parent = nmsStack.getTag();
        }

        // Load attribute list
        if (parent.hasKey("AttributeModifiers")) {
            attributes = parent.getList("AttributeModifiers", TAG_COMPOUND);
        } else {
            attributes = new NBTTagList();
            parent.set("AttributeModifiers", attributes);
        }
    }

    /**
     * Retrieve the NMS stack associated with the given CraftItemStack.
     *
     * @param stack - the stack.
     * @return The associated NMS item.
     */
    private static net.minecraft.server.v1_12_R1.ItemStack getNmsStack(CraftItemStack stack) {
        try {
            Field field = HANDLE_FIELD;

            if (field == null) {
                field = CraftItemStack.class.getDeclaredField("handle");
                field.setAccessible(true);
                HANDLE_FIELD = field;
            }
            return (net.minecraft.server.v1_12_R1.ItemStack) field.get(stack);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read NMS stack.", e);
        }
    }

    /**
     * Retrieve the modified item stack.
     *
     * @return The modified item stack.
     */
    public ItemStack getStack() {
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    /**
     * Retrieve the number of attributes.
     *
     * @return Number of attributes.
     */
    public int size() {
        return attributes != null ? attributes.size() : 0;
    }

    /**
     * Add a new attribute to the list.
     *
     * @param attribute - the new attribute.
     */
    public void add(Attribute attribute) {
        Preconditions.checkNotNull(attribute.getName(), "must specify an attribute name.");
        attributes.add(attribute.data);
    }

    /**
     * Remove the first instance of the given attribute.
     * <p>
     * The attribute will be removed using its UUID.
     *
     * @param attribute - the attribute to remove.
     * @return TRUE if the attribute was removed, FALSE otherwise.
     */
    public boolean remove(Attribute attribute) {
        if (attributes == null)
            return false;
        UUID uuid = attribute.getUUID();

        for (Iterator<Attribute> it = values().iterator(); it.hasNext(); ) {
            if (Objects.equal(it.next().getUUID(), uuid)) {
                it.remove();

                // Last removed attribute?
                if (size() == 0) {
                    removeAttributes();
                }
                return true;
            }
        }
        return false;
    }

    public void clear() {
        removeAttributes();
    }

    /**
     * - * Remove the NBT list from the TAG compound. -
     */
    private void removeAttributes() {
        parent.remove("AttributeModifiers");
        this.attributes = null;
    }

    /**
     * Retrieve the attribute at a given index.
     *
     * @param index - the index to look up.
     * @return The attribute at that index.
     */
    public Attribute get(int index) {
        return new Attribute(attributes.get(index));
    }

    // We can't make Attributes itself iterable without splitting it up into
    // separate classes
    public Iterable<Attribute> values() {
        final List<NBTBase> list = getList();

        return new Iterable<Attribute>() {
            @Override
            public Iterator<Attribute> iterator() {
                // Handle the empty case
                if (size() == 0)
                    return Collections.emptyIterator();

                // Generics disgust me sometimes
                return Iterators.transform(list.iterator(), new Function<NBTBase, Attribute>() {

                    @Override
                    public Attribute apply(@Nullable NBTBase data) {
                        return new Attribute((NBTTagCompound) data);
                    }
                });
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getList() {
        try {
            Field listField = NBTTagList.class.getDeclaredField("list");
            listField.setAccessible(true);
            return (List<T>) listField.get(attributes);

        } catch (Exception e) {
            throw new RuntimeException("Unable to access reflection.", e);
        }
    }

    public enum Operation {
        ADD_NUMBER(0), MULTIPLY_PERCENTAGE(1), ADD_PERCENTAGE(2);
        private int id;

        Operation(int id) {
            this.id = id;
        }

        public static Operation fromId(int id) {
            // Linear scan is very fast for small N
            for (Operation op : values()) {
                if (op.getId() == id) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Corrupt operation ID " + id + " detected.");
        }

        public int getId() {
            return id;
        }
    }

    public static class AttributeType {
        private static ConcurrentMap<String, AttributeType> LOOKUP = Maps.newConcurrentMap();
        public static final AttributeType GENERIC_MAX_HEALTH = new AttributeType("generic.maxHealth").register();
        public static final AttributeType GENERIC_FOLLOW_RANGE = new AttributeType("generic.followRange").register();
        public static final AttributeType GENERIC_ATTACK_DAMAGE = new AttributeType("generic.attackDamage").register();
        public static final AttributeType GENERIC_MOVEMENT_SPEED = new AttributeType("generic.movementSpeed")
                .register();
        public static final AttributeType GENERIC_KNOCKBACK_RESISTANCE = new AttributeType(
                "generic.knockbackResistance").register();

        private final String minecraftId;

        /**
         * Construct a new attribute type.
         * <p>
         * Remember to {@link #register()} the type.
         *
         * @param minecraftId - the ID of the type.
         */
        public AttributeType(String minecraftId) {
            this.minecraftId = minecraftId;
        }

        /**
         * Retrieve the attribute type associated with a given ID.
         *
         * @param minecraftId The ID to search for.
         * @return The attribute type, or NULL if not found.
         */
        public static AttributeType fromId(String minecraftId) {
            return LOOKUP.get(minecraftId);
        }

        /**
         * Retrieve every registered attribute type.
         *
         * @return Every type.
         */
        public static Iterable<AttributeType> values() {
            return LOOKUP.values();
        }

        /**
         * Retrieve the associated minecraft ID.
         *
         * @return The associated ID.
         */
        public String getMinecraftId() {
            return minecraftId;
        }

        /**
         * Register the type in the central registry.
         *
         * @return The registered type.
         */
        // Constructors should have no side-effects!
        public AttributeType register() {
            AttributeType old = LOOKUP.putIfAbsent(minecraftId, this);
            return old != null ? old : this;
        }
    }

    public static class Attribute {
        private NBTTagCompound data;

        private Attribute(Builder builder) {
            data = new NBTTagCompound();
            setAmount(builder.amount);
            setOperation(builder.operation);
            setAttributeType(builder.type);
            setName(builder.name);
            setUUID(builder.uuid);
        }

        private Attribute(NBTTagCompound data) {
            this.data = data;
        }

        /**
         * Construct a new attribute builder with a random UUID and default
         * operation of adding numbers.
         *
         * @return The attribute builder.
         */
        public static Builder newBuilder() {
            return new Builder().uuid(UUID.randomUUID()).operation(Operation.ADD_NUMBER);
        }

        public double getAmount() {
            return data.getDouble("Amount");
        }

        public void setAmount(double amount) {
            data.setDouble("Amount", amount);
        }

        public Operation getOperation() {
            return Operation.fromId(data.getInt("Operation"));
        }

        public void setOperation(@Nonnull Operation operation) {
            Preconditions.checkNotNull(operation, "operation cannot be NULL.");
            data.setInt("Operation", operation.getId());
        }

        public AttributeType getAttributeType() {
            return AttributeType.fromId(data.getString("AttributeName"));
        }

        public void setAttributeType(@Nonnull AttributeType type) {
            Preconditions.checkNotNull(type, "type cannot be NULL.");
            data.setString("AttributeName", type.getMinecraftId());
        }

        public String getName() {
            return data.getString("Name");
        }

        public void setName(@Nonnull String name) {
            data.setString("Name", name);
        }

        public UUID getUUID() {
            return new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast"));
        }

        public void setUUID(@Nonnull UUID id) {
            Preconditions.checkNotNull("id", "id cannot be NULL.");
            data.setLong("UUIDLeast", id.getLeastSignificantBits());
            data.setLong("UUIDMost", id.getMostSignificantBits());
        }

        // Makes it easier to construct an attribute
        public static class Builder {
            private double amount;
            private Operation operation = Operation.ADD_NUMBER;
            private AttributeType type;
            private String name;
            private UUID uuid;

            private Builder() {
                // Don't make this accessible
            }

            public Builder amount(double amount) {
                this.amount = amount;
                return this;
            }

            public Builder operation(Operation operation) {
                this.operation = operation;
                return this;
            }

            public Builder type(AttributeType type) {
                this.type = type;
                return this;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder uuid(UUID uuid) {
                this.uuid = uuid;
                return this;
            }

            public Attribute build() {
                return new Attribute(this);
            }
        }
    }
}
