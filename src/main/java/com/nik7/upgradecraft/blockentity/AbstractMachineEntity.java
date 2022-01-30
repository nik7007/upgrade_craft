package com.nik7.upgradecraft.blockentity;

import com.google.common.collect.Lists;
import com.nik7.upgradecraft.tanks.EventFluidTank;
import com.nik7.upgradecraft.tanks.FluidTankWrapper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractMachineEntity extends BaseFluidHandlerEntity implements RecipeHolder, MenuProvider, Nameable {
    protected short tickNumber = 0;
    protected Component name;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    protected final ItemStackHandler inputsHandler;
    protected final ItemStackHandler outputsHandler;

    protected final LazyOptional<IItemHandler> inputs;
    protected final LazyOptional<IItemHandler> outputs;

    protected final LazyOptional<IItemHandler> combinedHandler = LazyOptional.of(this::createCombinedItemHandler);

    public AbstractMachineEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int initialCapacity) {
        super(blockEntityType, blockPos, blockState);
        this.inputsHandler = createInputsHandler();
        this.outputsHandler = createOutputsHandler();
        this.inputs = LazyOptional.of(() -> this.inputsHandler);
        this.outputs = LazyOptional.of(() -> this.outputsHandler);
        this.tank = new FluidTankWrapper<>(new EventFluidTank(initialCapacity * FluidAttributes.BUCKET_VOLUME, this::onFluidChange, this::isFluidValid));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        tickNumber = tag.getShort("tickNumber");
        if (tag.contains("inputsHandler")) {
            inputsHandler.deserializeNBT(tag.getCompound("inputsHandler"));
        }
        if (tag.contains("outputsHandler")) {
            inputsHandler.deserializeNBT(tag.getCompound("outputsHandler"));
        }

        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
        CompoundTag compoundtag = tag.getCompound("RecipesUsed");

        for (String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putShort("tickNumber", tickNumber);
        tag.put("inputsHandler", inputsHandler.serializeNBT());
        tag.put("outputsHandler", outputsHandler.serializeNBT());
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        CompoundTag compoundTag = new CompoundTag();
        this.recipesUsed.forEach((location, quantity) -> compoundTag.putInt(location.toString(), quantity));
        tag.put("RecipesUsed", compoundTag);
    }

    protected abstract @NotNull ItemStackHandler createInputsHandler();

    protected abstract @NotNull ItemStackHandler createOutputsHandler();

    public void setCustomName(Component name) {
        this.name = name;
    }

    protected void onFluidChange(Void unused) {
        notifyBlockUpdate();
    }

    protected boolean isFluidValid(FluidStack fluidStack) {
        return true;
    }

    protected void updateContent() {
        this.updateComparator();
        this.setChanged();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.inputs.invalidate();
        this.outputs.invalidate();
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(serverPlayer.getLevel(), serverPlayer.position());
        serverPlayer.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 vec3) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            serverLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                createExperience(serverLevel, vec3, entry.getIntValue(), ((AbstractCookingRecipe) recipe).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel serverLevel, Vec3 vec3, int quantity, float experience) {
        int i = Mth.floor((float) quantity * experience);
        float f = Mth.frac((float) quantity * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        ExperienceOrb.award(serverLevel, vec3, i);
    }

    protected @NotNull IItemHandler createCombinedItemHandler() {
        return new CombinedInvWrapper(inputsHandler, outputsHandler) {
            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        };
    }

    @Override
    public final void tick() {
        if (getLevel() == null) {
            return;
        }
        this.tickNumber++;
        machineTick();
    }

    protected abstract void machineTick();

    public NonNullList<ItemStack> itemStacks() {
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        for (int i = 0; i < this.inputsHandler.getSlots(); i++) {
            itemStacks.add(this.inputsHandler.getStackInSlot(i));
        }

        for (int i = 0; i < this.outputsHandler.getSlots(); i++) {
            itemStacks.add(this.outputsHandler.getStackInSlot(i));
        }

        return itemStacks;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    protected abstract Component getDefaultName();

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int windowsIs, Inventory inventory, Player player);

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Nullable
    protected LazyOptional<IItemHandler> getItemHandler(@Nullable Direction facing) {
        if (facing == null) {
            return combinedHandler.cast();
        } else if (facing == Direction.DOWN) {
            return outputs.cast();
        } else {
            return inputs.cast();
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            LazyOptional<IItemHandler> itemHandler = getItemHandler(facing);
            if (itemHandler != null) {
                return itemHandler.cast();
            }
        }
        return super.getCapability(capability, facing);
    }
}