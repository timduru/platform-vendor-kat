# Copyright (C) 2011 Team Eos
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

ifeq ($(BOOTANIMATION_RESOLUTION), KatKiss)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/KatKiss.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 1920x1200)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/1280x800.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 1280x800)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/1280x800.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 1280x720)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/1280x720.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 1280x720_small)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/1280x720_small.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 720x1280)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/720x1280.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 800x1280)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/800x1280.zip:system/media/bootanimation.zip
endif

ifeq ($(BOOTANIMATION_RESOLUTION), 480x480)
PRODUCT_COPY_FILES += \
    vendor/eos/bootanimations/480x480.zip:system/media/bootanimation.zip
endif
