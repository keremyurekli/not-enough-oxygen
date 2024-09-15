package com.keremyurekli.mod.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.LinkedList;
import java.util.Queue;

public class Scheduler
{
    private static final Queue<Runnable> atEnd = new LinkedList<>();
    private static final Queue<Runnable> atStart = new LinkedList<>();

    private static final Queue<Runnable> atEndWorld = new LinkedList<>();
    private static final Queue<Runnable> atStartWorld = new LinkedList<>();

    public static void init() {
        startTick();
        endTick();

//        startTickWorld();
//        endTickWorld();
    }

    private static void endTick() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!atEnd.isEmpty()) {
                Queue<Runnable> var = new LinkedList<>(atEnd);
                atEnd.clear();
                while (!var.isEmpty()) {
                    Runnable task = var.poll();
                    server.execute(task);
                }
            }

        });
    }

    private static void startTick() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (!atStart.isEmpty()) {
                Queue<Runnable> var = new LinkedList<>(atStart);
                atStart.clear();
                while (!var.isEmpty()) {
                    Runnable task = var.poll();
                    server.execute(task);
                }
            }
        });
    }

//    private static void endTickWorld() {
//        ServerTickEvents.END_WORLD_TICK.register(server -> {
//            while (!atEndWorld.isEmpty()) {
//                Runnable task = atEndWorld.poll();
//                server.execute(task);
//            }
//        });
//    }
//
//    private static void startTickWorld() {
//        ServerTickEvents.START_WORLD_TICK.register(server -> {
//            while (!atStartWorld.isEmpty()) {
//                Runnable task = atStartWorld.poll();
//                server.
//                server.execute(task);
//            }
//        });
//    }

    public static void atServerEnd(Runnable task) {
        atEnd.add(task);
    }

    public static void atServerStart(Runnable task) {
        atStart.add(task);
    }
}
