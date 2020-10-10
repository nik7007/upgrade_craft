package com.nik7.upgradecraft.tileentity;

import com.google.common.collect.Lists;
import com.nik7.upgradecraft.capabilities.AbstractMachineItemHandler;
import com.nik7.upgradecraft.fluids.tanks.EventFluidTank;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractFluidMachineTileEntity<I extends AbstractMachineItemHandler>
        extends BaseFluidHandlerTileEntity implements ITickableTileEntity, IRecipeHolder, INamedContainerProvider, MachineTileEntity {

    protected final LazyOptional<ItemStackHandler> inventory;
    protected final I itemStackHandler;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public AbstractFluidMachineTileEntity(TileEntityType<?> tileEntityTypeIn, int capacity) {
        super(tileEntityTypeIn);
        this.tank = new EventFluidTank(capacity, this::onFluidChange, this::isFluidValid);
        this.itemStackHandler = createInventory();
        this.inventory = LazyOptional.of(() -> AbstractFluidMachineTileEntity.this.itemStackHandler);
    }

    private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
        int i = MathHelper.floor((float) craftedAmount * experience);
        float f = MathHelper.frac((float) craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrbEntity.getXPSplit(i);
            i -= j;
            world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
        }

    }

    protected abstract I createInventory();

    @SuppressWarnings("unused")
    protected void onInventoryChange(int slot) {
        this.markDirty();
        if (world != null && !world.isRemote()) {
            world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        CompoundNBT inventory = tag.getCompound("inventory");
        itemStackHandler.deserializeNBT(inventory);
        CompoundNBT compoundnbt = tag.getCompound("RecipesUsed");

        for (String s : compoundnbt.keySet()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);

        CompoundNBT inventory = itemStackHandler.serializeNBT();
        tag.put("inventory", inventory);

        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> compoundnbt.putInt(recipeId.toString(), craftedAmount));
        tag.put("RecipesUsed", compoundnbt);

        return tag;

    }

    protected void onFluidChange(Void unused) {
        notifyBlockUpdate();
    }

    protected boolean isFluidValid(FluidStack fluidStack) {
        return true;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return itemStackHandler.getItemStack();
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> recipes = this.grantStoredRecipeExperience(player.world, player.getPositionVec());
        player.unlockRecipes(recipes);
        this.recipes.clear();
    }

    public List<IRecipe<?>> grantStoredRecipeExperience(World world, Vector3d pos) {
        List<IRecipe<?>> recipes = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
            world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> {
                recipes.add(recipe);
                splitAndSpawnExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe) recipe).getExperience());
            });
        }

        return recipes;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void remove() {
        inventory.invalidate();
        super.remove();
    }
}
