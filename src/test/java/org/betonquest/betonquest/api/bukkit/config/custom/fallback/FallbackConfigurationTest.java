package org.betonquest.betonquest.api.bukkit.config.custom.fallback;

import org.betonquest.betonquest.api.bukkit.config.util.ConfigurationBaseTest;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link FallbackConfiguration} class.
 */
@SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
public class FallbackConfigurationTest extends ConfigurationBaseTest {
    /**
     * The fallback {@link Configuration that should not be modified.
     */
    protected Configuration fallback;
    /**
     * The values in the configuration before the test did run.
     */
    private Map<String, Object> values;
    /**
     * The values of the default section in the configuration before the test did run.
     */
    private Map<String, Object> valuesDefault;

    @Override
    public Configuration getConfig() {
        final Configuration original = YamlConfiguration.loadConfiguration(new File("src/test/resources/api/bukkit/fallback/original.yml"));
        fallback = YamlConfiguration.loadConfiguration(new File("src/test/resources/api/bukkit/fallback/fallback.yml"));

        final Configuration defaults = super.getDefaultConfig().getDefaults();
        assertNotNull(defaults);
        original.setDefaults(defaults);

        return new FallbackConfiguration(original, fallback);
    }

    /**
     * Get a copy of the values in the config, before the test did run.
     */
    @BeforeEach
    public void beforeEach() {
        values = fallback.getValues(true);
        final ConfigurationSection defaultSection = fallback.getDefaultSection();
        valuesDefault = defaultSection == null ? null : defaultSection.getValues(true);
    }

    /**
     * Compare the start values with the values after the test.
     * They should not have been changed.
     */
    @AfterEach
    public void afterEach() {
        assertEquals(values, fallback.getValues(true));
        final ConfigurationSection defaultSection = fallback.getDefaultSection();
        assertEquals(valuesDefault, defaultSection == null ? null : defaultSection.getValues(true));
    }

}
