package org.betonquest.betonquest.modules.logger.custom;

import org.betonquest.betonquest.modules.logger.BetonQuestLogRecord;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * This {@link Handler} can hold all {@link LogRecord}s from the configured log history time frame.
 * <br>
 * If the filter returns true, a LogRecord will be passed to the target {@link Handler},
 * otherwise it will be written to the history.
 * The history can then be pushed to the target handler at any time.
 * It is automatically pushed if the filter returns true for any subsequent LogRecord.
 */
public class HistoryLogHandler extends Handler {
    /**
     * The message printed before the history is printed.
     */
    public static final String START_OF_HISTORY = "=====START OF HISTORY=====";
    /**
     * The message printed after the history is printed.
     */
    public static final String END_OF_HISTORY = "=====END OF HISTORY=====";
    /**
     * The {@link LogRecord} history.
     */
    private final Queue<LogRecord> records;

    /**
     * The target Handler to log the history to.
     */
    private final Handler target;

    /**
     * Creates a new {@link HistoryLogHandler}.
     * <p>
     * If expireAfterMinutes is 0, no history will be saved at all.
     *
     * @param target             The Handler to log the history to
     * @param expireAfterMinutes The time a {@link LogRecord} stays in the cache
     */
    public HistoryLogHandler(final Plugin plugin, final Handler target, final double expireAfterMinutes) {
        super();
        if (expireAfterMinutes == 0) {
            this.records = null;
        } else {
            this.records = new ConcurrentLinkedQueue<>();
            final long expireAfterMillis = (long) (expireAfterMinutes * 60 * 1000);
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                while (isExpired(records.peek(), expireAfterMillis)) {
                    records.remove();
                }
            }, 20, 20);
        }
        this.target = target;
    }

    private boolean isExpired(final LogRecord record, final long afterMillis) {
        return record != null && record.getMillis() < System.currentTimeMillis() - afterMillis;
    }

    /**
     * Logs a LogRecord to the history or the target handler.
     *
     * @param record The {@link LogRecord} to log
     */
    @Override
    public void publish(final LogRecord record) {
        if (!(record instanceof BetonQuestLogRecord)) {
            return;
        }
        if (isLoggable(record)) {
            push();
            target.publish(record);
        } else if (records != null) {
            records.add(record);
        }
    }

    /**
     * Publishes any available history to the target handler.
     */
    public void push() {
        if (records != null && !records.isEmpty()) {
            target.publish(new LogRecord(Level.INFO, START_OF_HISTORY));
            while (records.peek() != null) {
                target.publish(records.poll());
            }
            target.publish(new LogRecord(Level.INFO, END_OF_HISTORY));
        }
    }

    /**
     * Flushes the target handler.
     */
    @Override
    public void flush() {
        target.flush();
    }

    /**
     * Closes the target handler.
     */
    @Override
    public void close() {
        target.close();
    }
}