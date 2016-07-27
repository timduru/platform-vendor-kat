include device/asus/tf_unified/BoardConfig_common.mk


# Try to build the kernel
TARGET_KERNEL_SOURCE := kernel/asus/tf201
TARGET_KERNEL_CONFIG := katkernel_defconfig

# Prebuilt Kernel Fallback
TARGET_PREBUILT_KERNEL := device/asus/tf_unified/kernel

BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR ?= vendor/kat/tf201/bluetooth


