package org.kat.controlcenter;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class Utils {
    public static final boolean STATE_ON = true;
    public static final boolean STATE_OFF = false;

    public static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
    public static final String CONTROLCENTERNS = "http://schemas.android.com/apk/res/org.kat.controlcenter";
    public static final String URI_GRAVEYARD = "eos_graveyard_uri";

    public static final String INCOMING_FRAG_KEY = "eos_incoming_frag_key";
    public static final String FRAG_TITLE_KEY = "title";
    public static final String FRAG_POSITION_KEY = "position";
    public static final String INCOMING_LAST_FRAG_VIEWED = "eos_incoming_last_frag_viewed";
    public static final String SOFTKEY_FRAG_TAG = "eos_softkey_settings";
    public static final String SEARCH_PANEL_FRAG_TAG = "eos_search_panel_settings";
    public static final String PERFORMANCE_FRAG_TAG = "eos_performance_frag_tag";
    public static final String PRIVACY_FRAG_TAG = "eos_privacy_frag_tag";
    public static final String PRIVACY_LOG_PACKAGES = "eos_logger_packages";
    public static final String TEXT_FRAGMENT_TITLE_KEY = "eos_text_frag_title";
    public static final String TEXT_FRAGMENT_TEXT_RES_KEY = "eos_text_frag_res";
    public static final String LEGACY_TOGGLES_FRAGMENT_TAG = "eos_legacy_toggles_fragment";
    public static final String QUICK_SETTINGS_FRAGMENT_TAG = "eos_quick_settings_fragment";

    public static final String DEFAULT_TITLE = "EOS Control Center";
    public static final String INTERFACE_SETTINGS_TITLE = "Interface";
    public static final String NAVBAR_SETTINGS_TITLE = "Navigation";
    public static final String STATUSBAR_SETTINGS_TITLE = "Statusbar";
    public static final String SYSTEM_SETTINGS_TITLE = "System";
    public static final String INFO_TITLE = "Info";

    private static final String EOS_SHARED_PREFS = "eos_shared_prefs";

    public static final String QS_PREFS_KEY = "qs_prefs_key";
    public static final String LEGACY_TOGGLES_PREFS_KEY = "legacy_prefs_key";

    public static int LAST_FRAG_VIEWED = 0;

    /*
     * kernel constants and their pref flags We only use detected paths and
     * features
     */
    public static final String CPU_AVAIL_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    public static final String CPU_AVAIL_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    public static final String CPU_MIN_SCALE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    public static final String CPU_MAX_SCALE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    public static final String CPU_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static final String IO_SCHED = "/sys/block/mmcblk0/queue/scheduler";
    public static final String ZRAM = "/sys/block/zram0/disksize";
    public static final String S2W_PATH = "/sys/android_touch/sweep2wake";
    public static final String FFC_PATH = "/sys/kernel/fast_charge/force_fast_charge";

    public static final String CLOCKS_ON_BOOT_PREF = "clocks_on_boot";
    public static final String MIN_PREF = "clocks_min";
    public static final String MAX_PREF = "clocks_max";
    public static final String GOV_PREF = "clocks_gov";
    public static final String IOSCHED_PREF = "iosched_on_boot";
    public static final String ZRAM_PREF = "zram_on_boot";
    public static final String S2W_PREF = "s2w_on_boot";

    public static String getXdaUrl(Context context, String device) {
        if (device.equals("grouper")) {
            return context.getString(R.string.eos_information_rom_link_xda_grouper);
        } else if (device.equals("maguro")) {
            return context.getString(R.string.eos_information_rom_link_xda_maguro);
        } else if (device.equals("toro")) {
            return context.getString(R.string.eos_information_rom_link_xda_toro);
        } else if (device.equals("toroplus")) {
            return context.getString(R.string.eos_information_rom_link_xda_toroplus);
        } else if (device.equals("wingray")) {
            return context.getString(R.string.eos_information_rom_link_xda_wingray);
        } else if (device.equals("stingray")) {
            return context.getString(R.string.eos_information_rom_link_xda_stingray);
        } else if (device.equals("umts_everest")) {
            return context.getString(R.string.eos_information_rom_link_xda_umts_everest);
        } else if (device.equals("tf101")) {
            return context.getString(R.string.eos_information_rom_link_xda_tf101);
        } else if (device.equals("mako")) {
            return context.getString(R.string.eos_information_rom_link_xda_mako);
        } else if (device.equals("manta")) {
            return context.getString(R.string.eos_information_rom_link_xda_manta);
        }

        return "null";
    }

    public static String getRootzUrl(Context context, String device) {
        if (device.equals("grouper")) {
            return context.getString(R.string.eos_information_rom_link_rootz_grouper);
        } else if (device.equals("maguro")) {
            return context.getString(R.string.eos_information_rom_link_rootz_maguro);
        } else if (device.equals("toro")) {
            return context.getString(R.string.eos_information_rom_link_rootz_toro);
        } else if (device.equals("toroplus")) {
            return context.getString(R.string.eos_information_rom_link_rootz_toroplus);
        } else if (device.equals("wingray")) {
            return context.getString(R.string.eos_information_rom_link_rootz_wingray);
        } else if (device.equals("stingray")) {
            return context.getString(R.string.eos_information_rom_link_rootz_stingray);
        } else if (device.equals("umts_everest")) {
            return context.getString(R.string.eos_information_rom_link_rootz_umts_everest);
        } else if (device.equals("tf101")) {
            return context.getString(R.string.eos_information_rom_link_rootz_tf101);
        } else if (device.equals("mako")) {
            return context.getString(R.string.eos_information_rom_link_rootz_mako);
        }

        return "null";
    }

    public static boolean hasTorch(Context context) {
        if (context.getResources().getBoolean(R.bool.config_force_disable_torch)) {
            return false;
        } else {
            return EOSUtils.hasTorch();
        }
    }

    public static String readRawTextFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public static String getToggleOrderFromPrefs(Context context, String key) {
        return context.getSharedPreferences(EOS_SHARED_PREFS, Context.MODE_PRIVATE).getString(key,
                URI_GRAVEYARD);
    }

    public static void commitToggleOrder(Context context, String key, String str) {
        context.getSharedPreferences(EOS_SHARED_PREFS, Context.MODE_PRIVATE).edit()
                .putString(key, str).commit();
    }

    public static void restartLauncher(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        am.forceStopPackage(resolveInfo.activityInfo.packageName);
    }

    public static void setComponentEnabledState(Context context, ComponentName component,
            boolean enabled) {
        context.getPackageManager().setComponentEnabledSetting(
                component,
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static boolean getComponentEnabledState(Context context, ComponentName component) {
        return context.getPackageManager().getComponentEnabledSetting(component) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ? true
                : false;
    }

    /*
     * Kernel control utils We use two types of methods One for manipulating
     * preference file flags for keeping values and the other is for
     * reading/writing to the sysfs interface
     */

    public static boolean hasKernelFeature(String path) {
        return new File(path).exists();
    }

    public static boolean isKernelFeatureEnabled(String feature) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    new File(feature).getAbsolutePath()));
            String input = reader.readLine();
            reader.close();
            return input.contains("1");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setKernelFeatureEnabled(String feature, boolean enabled) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(feature)));
            String output = "" + (enabled ? "1" : "0");
            writer.write(output.toCharArray(), 0, output.toCharArray().length);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean writeKernelValue(String flag, String value) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(flag));
            writer.write(value.toCharArray(), 0, value.toCharArray().length);
            writer.close();
        } catch (Exception e) {
            Log.w("Settings", e.toString());
            return false;
        }
        return true;
    }

    public static String readKernelValue(Context context, String flag) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(flag)));
            String input = reader.readLine();
            reader.close();
            return input;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static String[] readKernelList(String flag) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(flag));
            String[] governors = reader.readLine().split(" ");
            reader.close();
            return governors;
        } catch (IOException e) {
            if (flag.equals(Utils.CPU_AVAIL_GOV)) {
                return new String[] {
                        "interactive", "ondemand"
                };
            } else {
                return null;
            }
        }
    }

    public static String[] getSchedulers() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    new File(IO_SCHED).getAbsolutePath()));
            String[] temp = reader.readLine().split(" ");
            reader.close();
            String[] schedulers = new String[temp.length];
            for (int i = 0; i < temp.length; i++) {
                String test = temp[i];
                if (test.contains("[")) {
                    schedulers[i] = test.substring(1, test.length() - 1);
                    continue;
                }
                schedulers[i] = test;
            }
            return schedulers;
        } catch (Exception e) {
            return new String[] {
                    "interactive", "ondemand"
            };
        }
    }

    public static String getCurrentScheduler() {
        String defSched = "cfq";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    new File(IO_SCHED).getAbsolutePath()));
            String[] temp = reader.readLine().split(" ");
            reader.close();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].contains("[")) {
                    defSched = temp[i].substring(1, temp[i].length() - 1);
                    break;
                }
            }

        } catch (Exception e) {
        }
        return defSched;
    }

    private static File getPrefFlag(Context context, String flag) {
        return new File(context.getDir("eos", Context.MODE_PRIVATE), flag);
    }

    public static boolean prefFlagExists(Context context, String flag) {
        return getPrefFlag(context, flag).exists();
    }

    public static boolean createPrefFlag(Context context, String flag) {
        try {
            getPrefFlag(context, flag).createNewFile();
        } catch (IOException e) {
            Log.d("Settings", e.toString());
            return false;
        }
        return true;
    }

    public static void deletePrefFlag(Context context, String flag) {
        getPrefFlag(context, flag).delete();
    }

    public static boolean writePrefValue(Context context, String flag, String value) {
        try {
            File pref = new File(context.getDir("eos", Context.MODE_PRIVATE), flag);
            BufferedWriter writer = new BufferedWriter(new FileWriter(pref));
            writer.write(value.toCharArray(), 0, value.toCharArray().length);
            writer.close();
        } catch (Exception e) {
            Log.w("Settings", e.toString());
            return false;
        }
        return true;
    }

    public static String readPrefValue(Context context, String flag) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getPrefFlag(context, flag)));
            String input = reader.readLine();
            reader.close();
            return input;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static String appendClockSuffix(String val) {
        return "" + (Integer.parseInt(val) / 1000) + " MHz";
    }

    public static String removeClockSuffix(String val) {
        return "" + (Integer.parseInt(val.substring(0,
                val.indexOf(" MHz"))) * 1000);
    }

    public static void log(String val) {
        Log.w("Eos_Utils", val);
    }
}
