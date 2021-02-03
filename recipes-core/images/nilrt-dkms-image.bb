DESCRIPTION = "Runmode image for ethernet-based NI Linux Real-Time targets running XFCE (DKMS)."

IMAGE_INSTALL = "\
	dkms \
	crda \
	iw \
	libnl \
	openssl \
	rfkill \
	ti-wifi-utils \
	wpa-supplicant \
	wireless-regdb \
	"

require niconsole-image.inc
require nilrt-xfce.inc
require nilrt-initramfs-legacy.inc
require include/licenses.inc
require nilrt-proprietary.inc

IMAGE_FSTYPES += "squashfs"
