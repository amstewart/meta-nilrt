# (C) Copyright 2021,
#  NI
#  All rights reserved.

SUMMARY = "Desktop environment packages for NI Linux Realtime distribution"
LICENSE = "MIT"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} += "packagegroup-xfce-base"

RDEPENDS_${PN} += "\
	font-cursor-misc \
	font-misc-misc \
	fontconfig-overrides \
	mousepad \
	ttf-pt-sans \
	xf86-input-evdev \
	xf86-video-ati \
	xf86-video-intel \
	xf86-video-vesa \
	xfce-nilrt-settings \
	xfce4-xkb-plugin \
	xfontsel \
	xorg-fonts-100dpi \
	xrdb \
	xserver-xfce-init \
	xserver-xorg-udev-rules \
"
