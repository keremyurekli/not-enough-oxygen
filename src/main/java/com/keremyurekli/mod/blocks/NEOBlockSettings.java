package com.keremyurekli.mod.blocks;


import net.minecraft.block.AbstractBlock;
import net.minecraft.sound.BlockSoundGroup;

import java.lang.reflect.InvocationTargetException;

public class NEOBlockSettings {

    public AbstractBlock.Settings settings;
    public boolean registerItem;
    public boolean isAnimated;


    public NEOBlockSettings(BlockSoundGroup group, boolean registerItem) {
        settings = AbstractBlock.Settings.create();
//        settings = AbstractBlock.Settings.o
        settings.sounds(group);
//        settings.notSolid();
        this.registerItem = registerItem;
        this.isAnimated = false;
    }


    public NEOBlock block() {
        return new NEOBlock(settings);
    }

    public Object block(Class<? extends NEOBlock> block) {
        Object o;
        try {
            o = block.getConstructor(AbstractBlock.Settings.class).newInstance(settings);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

}
