package com.keremyurekli.mod.fluids;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;

import java.awt.*;

public class NEOFluidSettings{

    private AbstractBlock.Settings settings;

    public Color COLOR;


    public NEOFluidSettings(int luminance, int strength, MapColor color, Color tint, boolean opaque) {
        settings = AbstractBlock.Settings.create();
        settings.liquid();
        //settings.pistonBehavior(PistonBehavior.PUSH_ONLY);
        settings.pistonBehavior(PistonBehavior.DESTROY);
        settings.replaceable();
        settings.dropsNothing();

        //settings.blockVision();

        settings.luminance(blockState -> luminance);
        settings.strength(strength,strength);
        settings.mapColor(color);
        this.COLOR = tint;
        if (!opaque) {
            settings.nonOpaque();
        }
    }

    public AbstractBlock.Settings getSettings() {
        return settings;
    }
}
