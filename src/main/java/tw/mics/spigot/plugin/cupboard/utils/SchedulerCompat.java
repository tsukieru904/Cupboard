package tw.mics.spigot.plugin.cupboard.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Small compatibility layer for Bukkit / Paper / Purpur / Folia scheduling.
 *
 * The implementation prefers Folia's schedulers when they exist, and falls back
 * to the classic Bukkit scheduler otherwise.
 */
public final class SchedulerCompat {
    private SchedulerCompat() {
    }

    public interface TaskHandle {
        void cancel();
    }

    private static final class BukkitTaskHandle implements TaskHandle {
        private final BukkitTask task;

        private BukkitTaskHandle(BukkitTask task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            task.cancel();
        }
    }

    private static final class ReflectionTaskHandle implements TaskHandle {
        private final Object handle;
        private final Method cancelMethod;

        private ReflectionTaskHandle(Object handle) throws NoSuchMethodException {
            this.handle = handle;
            this.cancelMethod = handle.getClass().getMethod("cancel");
        }

        @Override
        public void cancel() {
            try {
                cancelMethod.invoke(handle);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Best-effort cancel only.
            }
        }
    }

    public static boolean isFolia() {
        try {
            Bukkit.class.getMethod("getGlobalRegionScheduler");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static TaskHandle runLater(Plugin plugin, Runnable runnable, long delayTicks) {
        if (isFolia()) {
            Object scheduler = getGlobalRegionScheduler();
            Object handle = invokeScheduler(scheduler, "runDelayed", plugin, runnable, delayTicks);
            if (handle != null) {
                return toHandle(handle);
            }
        }
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks));
    }

    public static TaskHandle runRepeating(Plugin plugin, Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia()) {
            Object scheduler = getGlobalRegionScheduler();
            Object handle = invokeScheduler(scheduler, "runAtFixedRate", plugin, runnable, delayTicks, periodTicks);
            if (handle != null) {
                return toHandle(handle);
            }
        }
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks));
    }

    public static TaskHandle runAsync(Plugin plugin, Runnable runnable) {
        if (isFolia()) {
            Object scheduler = getAsyncScheduler();
            Object handle = invokeScheduler(scheduler, "runNow", plugin, runnable);
            if (handle != null) {
                return toHandle(handle);
            }
        }
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public static TaskHandle runSync(Plugin plugin, Runnable runnable) {
        if (isFolia()) {
            Object scheduler = getGlobalRegionScheduler();
            Object handle = invokeScheduler(scheduler, "runNow", plugin, runnable);
            if (handle != null) {
                return toHandle(handle);
            }
        }
        return new BukkitTaskHandle(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    private static Object getGlobalRegionScheduler() {
        try {
            Method method = Bukkit.class.getMethod("getGlobalRegionScheduler");
            return method.invoke(null);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static Object getAsyncScheduler() {
        try {
            Method method = Bukkit.class.getMethod("getAsyncScheduler");
            return method.invoke(null);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static Object invokeScheduler(Object scheduler, String methodName, Plugin plugin, Runnable runnable, long... args) {
        if (scheduler == null) {
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            Consumer<Object> consumer = ignored -> runnable.run();
            if (args.length == 0) {
                Method method = scheduler.getClass().getMethod(methodName, Plugin.class, Consumer.class);
                return method.invoke(scheduler, plugin, consumer);
            }
            if (args.length == 1) {
                Method method = scheduler.getClass().getMethod(methodName, Plugin.class, Consumer.class, long.class);
                return method.invoke(scheduler, plugin, consumer, args[0]);
            }
            Method method = scheduler.getClass().getMethod(methodName, Plugin.class, Consumer.class, long.class, long.class);
            return method.invoke(scheduler, plugin, consumer, args[0], args[1]);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static TaskHandle toHandle(Object handle) {
        try {
            return new ReflectionTaskHandle(handle);
        } catch (NoSuchMethodException e) {
            return () -> { };
        }
    }
}
