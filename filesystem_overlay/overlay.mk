
PRODUCT_COPY_FILES += \
    vendor/kat/filesystem_overlay/bin/sysinit:system/bin/sysinit \
    vendor/kat/filesystem_overlay/bin/sysrw:system/bin/sysrw \
    vendor/kat/filesystem_overlay/bin/sysro:system/bin/sysro \
    vendor/kat/filesystem_overlay/bin/rootrw:system/bin/rootrw \
    vendor/kat/filesystem_overlay/bin/rootro:system/bin/rootro \
    vendor/kat/filesystem_overlay/etc/init.d/20_kernel_controls:system/etc/init.d/20_kernel_controls \
    vendor/kat/filesystem_overlay/media/LMprec_508.emd:system/media/LMprec_508.emd \
    vendor/kat/filesystem_overlay/media/PFFprec_600.emd:system/media/PFFprec_600.emd \
    vendor/kat/filesystem_overlay/init.kat.rc:root/init.kat.rc \

# Proper zoneinfo files
# don't know why the proper ZoneinfoDB classes aren't updated yet
PRODUCT_COPY_FILES += \
    vendor/kat/filesystem_overlay/usr/share/zoneinfo/zoneinfo.dat:system/usr/share/zoneinfo/zoneinfo.dat \
    vendor/kat/filesystem_overlay/usr/share/zoneinfo/zoneinfo.idx:system/usr/share/zoneinfo/zoneinfo.idx \
    vendor/kat/filesystem_overlay/usr/share/zoneinfo/zoneinfo.version:system/usr/share/zoneinfo/zoneinfo.version

