SUMMARY = "Packagegroup that contains all of the components required for building the OE feeds"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} = "\
	packagegroup-base \
	packagegroup-core-boot \
	packagegroup-ni-base \
	packagegroup-ni-desktop \
	packagegroup-ni-ptest \
	packagegroup-ni-restoremode \
	packagegroup-ni-transconf \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'packagegroup-core-x11', '', d)} \
	packagegroup-core-standalone-sdk-target \
	packagegroup-kernel-module-build \
	apache2 \
	apache-websocket \
	apr-iconv \
	dkms \
"

RDEPENDS_${PN}_append_x64 = "\
	init-nilrt-ramfs \
"
