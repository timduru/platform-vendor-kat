#
# Copyright (C) 2011 The Android Open-Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This file includes all definitions that apply to ALL eos builds

ADDITIONAL_DEFAULT_PROPERTIES += \
    ro.adb.secure=0

PRODUCT_PACKAGES += \
    TerminalEmulator \
    SuperSU \
    GooManager \
    thtt \
    ntfs-3g.probe \
    ntfsfix \
    ntfs-3g \
    PhaseBeam \
    HoloSpiral \
    fstrim \
    libnl \
    iw \
    tcpdump \
    dropbear \
    scp \
    sftp \
    Launcher3 \
    libbt-vendor \
    fsck.exfat \
    mount.exfat \
    mkfs.exfat \
    busybox \
    #ssh-keygen
    #powertop \
    #ControlCenter \
    #com.tmobile.themes \
    #ThemeChooser \
    #ThemeManager \

PRODUCT_COPY_FILES += \
    vendor/eos/proprietary/terminalemulator/libjackpal-androidterm4.so:system/lib/libjackpal-androidterm4.so \

PRODUCT_COPY_FILES += \
    vendor/eos/proprietary/supersu/su:system/xbin/su \
    vendor/eos/proprietary/supersu/su:system/xbin/daemonsu \
    vendor/eos/proprietary/supersu/99SuperSUDaemon:system/etc/init.d/99SuperSUDaemon \

#Bring in camera media effects
$(call inherit-product-if-exists, frameworks/base/data/videos/VideoPackage2.mk)

$(call inherit-product-if-exists, vendor/eos/filesystem_overlay/overlay.mk)
DEVICE_PACKAGE_OVERLAYS += vendor/eos/resource_overlay

ifeq ($(EOS_RELEASE),)
    PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_DISPLAY_ID="$(BUILD_ID) KatKiss-$(PLATFORM_VERSION)_$(EOS_BUILD_NUMBER)"
else
    PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_DISPLAY_ID="KatKiss Stable release $(EOS_RELEASE)"
endif

#### Goo Manager support
## If EOS_RELEASE is not defined by the user, assume the build is a nightly release.
## If EOS_RELEASE is defined, use the environment variable EOS_RELEASE_GOOBUILD as the build number.
PRODUCT_PROPERTY_OVERRIDES += \
#    ro.goo.developerid=teameos \
#    ro.goo.board=$(subst full_,,$(TARGET_PRODUCT)) \

#ifeq ($(EOS_RELEASE),)
#	PRODUCT_PROPERTY_OVERRIDES += \
	ro.goo.rom=eosJB42Nightlies \
	ro.goo.version=$(shell date +%s)
#else
#	PRODUCT_PROPERTY_OVERRIDES += \
	ro.goo.rom=eos \
	ro.goo.version=$(EOS_RELEASE_GOOBUILD)
#endif

$(call inherit-product, vendor/eos/bootanimations/bootanimation.mk)
