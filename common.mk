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
    ro.adb.secure=1

PRODUCT_PACKAGES += \
    TerminalEmulator \
    SuperSU \
    GooManager \
    thtt \
    ntfs-3g.probe \
    ntfsfix \
    ntfs-3g \
    fsck.exfat \
    mount.exfat \
    mkfs.exfat \
    PhaseBeam \
    HoloSpiral \
    fstrim \
    libnl \
    iw \
    tcpdump \
    powertop \
    dropbear \
    scp \
    sftp \
    Trebuchet \
    ssh-keygen
    #ControlCenter \
    #com.tmobile.themes \
    #ThemeChooser \
    #ThemeManager \
    #busybox \

PRODUCT_COPY_FILES += \
    vendor/eos/filesystem_overlay/etc/permissions/com.tmobile.software.themes.xml:system/etc/permissions/com.tmobile.software.themes.xml \
    vendor/eos/proprietary/terminalemulator/libjackpal-androidterm4.so:system/lib/libjackpal-androidterm4.so \
    vendor/eos/proprietary/supersu/su:system/xbin/su \

#Bring in camera media effects
$(call inherit-product-if-exists, frameworks/base/data/videos/VideoPackage2.mk)

$(call inherit-product-if-exists, vendor/eos/filesystem_overlay/overlay.mk)
DEVICE_PACKAGE_OVERLAYS += vendor/eos/resource_overlay

PRODUCT_PROPERTY_OVERRIDES += \
    ro.eos.majorversion=3

ifeq ($(EOS_RELEASE),)
    PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_DISPLAY_ID="JDQ39 KatKiss_$(EOS_BUILD_NUMBER)"
else
    PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_DISPLAY_ID="EOS Stable release $(EOS_RELEASE)"
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
