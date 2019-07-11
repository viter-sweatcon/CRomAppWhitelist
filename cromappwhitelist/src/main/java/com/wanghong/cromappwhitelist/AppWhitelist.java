/*
 * Copyright (C) 2017 wanghong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanghong.cromappwhitelist;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghong on 3/2/17.
 */

public class AppWhitelist {

    private static final Map<String, Class<? extends AbstractDevice>> DEVICES_CLASS_MAP = new HashMap<>();
    private static final Map<Class, AbstractDevice> MANUFACTURE_CACHE_MAP = new HashMap<>();

    static {
        DEVICES_CLASS_MAP.put("XIAOMI", XiaomiDevice.class);
        DEVICES_CLASS_MAP.put("HUAWEI", HuaweiDevice.class);
        DEVICES_CLASS_MAP.put("MEIZU", MeizuDevice.class);
        DEVICES_CLASS_MAP.put("OPPO", OppoDevice.class);
        DEVICES_CLASS_MAP.put("SAMSUNG", SamsungDevice.class);
    }

    private @NonNull static <T extends AbstractDevice> T createForDevice(Class<? extends T> clz) {
        if (clz == null) {
            return (T) new DefaultDevice();
        }

        AbstractDevice manufacture = MANUFACTURE_CACHE_MAP.get(clz);
        if (manufacture != null) {
            return (T) manufacture;
        }

        try {
            T result = clz.newInstance();
            MANUFACTURE_CACHE_MAP.put(clz, result);
            return result;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        DefaultDevice defaultDevice = new DefaultDevice();
        MANUFACTURE_CACHE_MAP.put(clz, defaultDevice);
        return (T) defaultDevice;
    }

    public static void settingForAutoStart(Context context) {
        createForDevice(DEVICES_CLASS_MAP.get(Build.MANUFACTURER.toUpperCase())).performAutoStartSetting(context);
    }

    public static void settingForBatterySaver(Context context) {
        createForDevice(DEVICES_CLASS_MAP.get(Build.MANUFACTURER.toUpperCase())).performBatterySaverSetting(context);
    }

    public static void settingForMemoryAcceleration(Context context) {
        createForDevice(DEVICES_CLASS_MAP.get(Build.MANUFACTURER.toUpperCase())).performMemoryAccelerationSetting(context);
    }

    public static void settingForNotification(Context context) {
        createForDevice(DEVICES_CLASS_MAP.get(Build.MANUFACTURER.toUpperCase())).performNotificationSetting(context);
    }
}
