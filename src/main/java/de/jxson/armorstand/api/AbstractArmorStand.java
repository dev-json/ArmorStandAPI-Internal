package de.jxson.armorstand.api;

import de.jxson.armorstand.api.adapter.ArmorStandVersionAdapter;
import de.jxson.armorstand.api.enums.ArmorStandMetaData;
import de.jxson.armorstand.api.enums.EquipmentSlot;
import de.jxson.armorstand.nms.ArmorStandVersionHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractArmorStand implements ArmorStand {

    /* ------------ Abstraction ------------ */
    private final ArmorStandVersionAdapter adapter;

    /* ------------ Custom-Logic ------------ */
    private final List<Player> exposedForPlayers;

    /* ------------ Armor-stand Attributes/Properties ------------ */
    private Location armorStandLocation;
    private int id;
    private UUID uniqueId;
    private final List<Entity> passenger;

    /* --> Items/Equipment <-- */
    private ItemStack mainHandItem;
    private ItemStack offHandItem;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    /* --> Poses <-- */
    private EulerAngle headPose;
    private EulerAngle bodyPose;
    private EulerAngle rightArmPose;
    private EulerAngle leftArmPose;
    private EulerAngle rightLegPose;
    private EulerAngle leftLegPose;

    /* --> Properties (Optionals) <-- */
    private boolean hasArms;
    private boolean hasBasePlate;
    private boolean isMarker;
    private boolean isSmall;
    private boolean isVisible;

    public AbstractArmorStand(int id, boolean randomIds)
    {
        this.adapter = ArmorStandVersionHandler.getAdapter(this);
        this.exposedForPlayers = new ArrayList<>();
        this.passenger = new ArrayList<>();
        this.id = id;
        this.uniqueId = UUID.randomUUID();
        if(randomIds)
            this.id = new Random().nextInt(1, 100000);

        /* Armorstand Attributes Initializing */
        this.headPose =  new EulerAngle(0, 0, 0);
        this.bodyPose =  new EulerAngle(0, 0, 0);
        this.rightArmPose =  new EulerAngle(0, 0, 0);
        this.leftArmPose =  new EulerAngle(0, 0, 0);
        this.rightLegPose =  new EulerAngle(0, 0, 0);
        this.leftLegPose =  new EulerAngle(0, 0, 0);

        this.mainHandItem = new ItemStack(Material.AIR);
        this.offHandItem = new ItemStack(Material.AIR);
        this.helmet = new ItemStack(Material.AIR);
        this.chestplate = new ItemStack(Material.AIR);
        this.leggings = new ItemStack(Material.AIR);
        this.boots = new ItemStack(Material.AIR);

        this.hasArms = false;
        this.hasBasePlate = false;
        this.isMarker = false;
        this.isSmall = false;
        this.isVisible = true;
    }

    public AbstractArmorStand(boolean randomIds)
    {
        this(-1, randomIds);
    }

    public AbstractArmorStand(int id)
    {
        this(id, false);
    }
    public AbstractArmorStand()
    {
        this(-1, false);
    }

    protected boolean validateArmorStand()
    {
        if(getId() < 0)
            throw new RuntimeException("Unable to create an armorstand with negative ids! Please set an id for the armorstand or use the random id flag.");
        return true;
    }

    public void destroy() {
        adapter.destroy();
    }
    public Location getLocation() {
        return armorStandLocation;
    }

    public void setLocation(Location armorStandLocation) {
        this.armorStandLocation = armorStandLocation;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public byte getBitMask()
    {
        return (byte) ((this.isSmall ? 0x01 : 0)
                | (this.hasArms ? 0x04 : 0)
                | (this.hasBasePlate ? 0x08 : 0)
                | (this.isVisible ? 0x10 : 0));
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public ArmorStandVersionAdapter getAdapter() {
        return adapter;
    }

    @Override
    public float getYaw() {
        return getAdapter().getHeadRotation()[0];
    }

    @Override
    public float getPitch() {
        return getAdapter().getHeadRotation()[1];
    }

    @Override
    public void setEquipment(ItemStack item, EquipmentSlot slot)
    {
        //Maybe adjust this part, that the "updateMetaData" Packet needs to be send seperatly
        //In order to disable multi sending of the same packet.. depends on usage!
        switch (slot)
        {
            case HELMET -> this.helmet = item;
            case CHESTPLATE -> this.chestplate = item;
            case LEGGINGS -> this.leggings = item;
            case BOOTS -> this.boots = item;
            case MAIN_HAND -> this.mainHandItem = item;
            case OFF_HAND -> this.offHandItem = item;
            default -> throw new RuntimeException("Invalid equipment slot given!");
        }
        this.updateMetaData();
    }

    @Override
    public ItemStack getEquipment(EquipmentSlot slot)
    {
        switch (slot)
        {
            case HELMET -> {
                return this.helmet;
            }
            case CHESTPLATE -> {
                return this.chestplate;
            }
            case LEGGINGS -> {
                return this.leggings;
            }
            case BOOTS -> {
                return this.boots;
            }
            case MAIN_HAND -> {
                return this.mainHandItem;
            }
            case OFF_HAND -> {
                return this.offHandItem;
            }
            default -> throw new RuntimeException("Invalid equipment slot given!");
        }
    }

    @Override
    public void setAttribute(ArmorStandMetaData metaData, boolean state)
    {
        switch (metaData)
        {
            case IS_SMALL -> this.isSmall = state;
            case HAS_ARMS -> this.hasArms = state;
            case HAS_BASE_PLATE -> this.hasBasePlate = state;
            case IS_VISIBLE -> this.isVisible = state;
            default -> throw new RuntimeException("Invalid equipment slot given!");
        }
        this.updateMetaData();
    }

    @Override
    public boolean getAttribute(ArmorStandMetaData metaData)
    {
        switch (metaData)
        {
            case IS_SMALL -> {
                return this.isSmall;
            }
            case HAS_ARMS -> {
                return this.hasArms;
            }
            case HAS_BASE_PLATE -> {
                return this.hasBasePlate;
            }
            case IS_VISIBLE -> {
                return this.isVisible;
            }
            default -> throw new RuntimeException("Invalid equipment slot given!");
        }
    }
}
