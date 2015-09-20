package us.talabrek.ultimateskyblock.util;

import org.xnap.commons.i18n.I18nFactory;
import us.talabrek.ultimateskyblock.Settings;
import us.talabrek.ultimateskyblock.uSkyBlock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

/**
 * Convenience util for supporting static imports.
 */
public enum I18nUtil {;
    private static I18n i18n;
    public static String tr(String s) {
        return getI18n().tr(s);
    }
    public static String tr(String s, Object... args) {
        return getI18n().tr(s, args);
    }

    public static I18n getI18n() {
        if (i18n == null) {
            i18n = new I18n(I18nFactory.getI18n(I18nUtil.class, getLocale()));
        }
        return i18n;
    }

    public static Locale getLocale() {
        return Settings.locale;
    }

    public static void clearCache() {
        try {
            i18n = null;
            Method clearCache = I18nFactory.class.getDeclaredMethod("clearCache");
            if (!clearCache.isAccessible()) {
                clearCache.setAccessible(true);
            }
            clearCache.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            uSkyBlock.getInstance().getLogger().warning("Unable to clear i18n: " + e);
        }
    }

    /**
     * Proxy between uSkyBlock and org.xnap.commons.i18n.I18n
     */
    public static class I18n  {
        private final org.xnap.commons.i18n.I18n proxy;
        private Properties messages;

        I18n(org.xnap.commons.i18n.I18n proxy) {
            this.proxy = proxy;
            messages = FileUtil.readProperties("messages.properties");
        }

        public String tr(String key, Object... args) {
            if (messages != null && messages.containsKey(key)) {
                if (args.length > 0) {
                    return MessageFormat.format(messages.getProperty(key), args);
                } else {
                    return messages.getProperty(key);
                }
            }
            return proxy.tr(key, args);
        }

        public Locale getLocale() {
            return proxy.getResources().getLocale();
        }
    }
}
