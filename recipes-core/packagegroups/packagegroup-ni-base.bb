# (C) Copyright 2013,
#  National Instruments Corporation.
#  All rights reserved.

SUMMARY = "Base set of packages for NI Linux Realtime distribution"
LICENSE = "MIT"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

#
# Set by the machine configuration with packages essential for device bootup
#
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS ?= ""

ALL_DISTRO_ARM_PACKAGES = "\
	mtd-utils \
	u-boot-fw-utils \
"

ALL_DISTRO_x64_PACKAGES = "\
	linux-firmware-i915 \
	linux-firmware-radeon \
	dmidecode \
	efivar \
	fw-printenv \
"

NILRT_NXG_ARM_PACKAGES = "\
	kernel-devicetree \
"

NILRT_NXG_x64_PACKAGES = "\
	efibootmgr \
"

NILRT_ARM_PACKAGES = "\
	jitterentropy-rngd \
	mtd-utils-ubifs \
"

NILRT_x64_PACKAGES = "\
	e2fsprogs \
	e2fsprogs-mke2fs \
	phc2sys \
	nilrtdiskcrypt \
"

NILRT_NXG_PACKAGES = "\
	modutils-initscripts \
	rauc \
	rauc-mark-good \
	packagegroup-ni-minimal-transconf \
	rtctl \
	salt-minion \
	connman \
	${@bb.utils.contains('TARGET_ARCH', 'arm', \
		'${NILRT_NXG_ARM_PACKAGES}', \
		'${NILRT_NXG_x64_PACKAGES}', d)} \
"

NILRT_PACKAGES = "\
	busybox-ifplugd \
	busybox-zcip \
	glibc-gconv-utf-16 \
	init-ifupdown \
	libstdc++ \
	logrotate \
	niwatchdogpet \
	openvpn \
	pigz \
	usbutils \
	${@bb.utils.contains('COMBINED_FEATURES', 'pci', 'pciutils-ids', '',d)} \
	${@bb.utils.contains('TARGET_ARCH', 'arm', \
		'${NILRT_ARM_PACKAGES}', \
		'${NILRT_x64_PACKAGES}', d)} \
"

RDEPENDS_${PN} = "\
	avahi-daemon \
	base-files \
	base-files-nilrt \
	base-passwd \
	busybox \
	coreutils-hostname \
	crio-support-scripts \
	crda \
	cronie \
	curl \
	daemonize \
	dhcp-client \
	dosfstools \
	dpkg-start-stop \
	e2fsprogs \
	e2fsprogs-e2fsck \
	e2fsprogs-mke2fs \
	e2fsprogs-tune2fs \
	efibootmgr \
	ethtool \
	eudev \
	gdbserver \
	glibc-gconv-cp932 \
	glibc-gconv-cp936 \
	glibc-gconv-iso8859-1 \
	gptfdisk-sgdisk \
	hwclock-init \
	initscripts \
	initscripts-nilrt \
	iproute2 \
	iproute2-tc \
	iptables \
	iw \
	jq \
	kernel-modules \
	kmod \
	libavahi-client \
	libavahi-common \
	libavahi-core \
	libcap-bin\
	libnl \
	libnss-mdns \
	libpam \
	librtpi \
	librtpi \
	lldpd \
	lsbinitscripts \
	netbase \
	ni-systemformat \
	ni-utils \
	ni-safemode-utils \
	niacctbase \
	niwatchdogpet \
	openssh-scp \
	openssh-sftp-server \
	openssh-ssh \
	openssh-sshd \
	openssl \
	opkg \
	opkg-keyrings \
	opkg-utils \
	os-release \
	parted \
	rauc \
	rfkill \
	rtctl \
	run-postinsts \
	sudo \
	syslog-ng \
	systemimageupdateinfo \
	sysvinit \
	tar \
	tbtadm \
	ti-wifi-utils \
	tzdata \
	tzdata-africa \
	tzdata-americas \
	tzdata-asia \
	tzdata-atlantic \
	tzdata-australia \
	tzdata-europe \
	tzdata-misc \
	tzdata-pacific \
	udev-extraconf \
	util-linux-agetty \
	util-linux-hwclock \
	util-linux-mount \
	util-linux-runuser \
	util-linux-sfdisk \
	util-linux-umount \
	vlan \
	wireless-regdb \
	wpa-supplicant \
	zip \
	${@bb.utils.contains('MACHINE_FEATURES', 'keyboard', 'keymaps', '', d)} \
	${@bb.utils.contains('MACHINE_FEATURES', 'acpi', 'busybox-acpid', '', d)} \
	${VIRTUAL-RUNTIME_mountpoint} \
	${MACHINE_ESSENTIAL_EXTRA_RDEPENDS} \
	${@bb.utils.contains('TARGET_ARCH', 'arm', \
		'${ALL_DISTRO_ARM_PACKAGES}', \
		'${ALL_DISTRO_x64_PACKAGES}', d)} \
	${@oe.utils.conditional('DISTRO', 'nilrt-nxg', \
		'${NILRT_NXG_PACKAGES}', \
		'${NILRT_PACKAGES}', d)} \
"

# Packages required by SystemLink
RDEPENDS_${PN} += "\
	cpp \
	cpp-symlinks \
	libmpc \
	libpython3 \
	libyaml \
	mpfr \
	python3 \
	python3-aiodns \
	python3-aiohttp \
	python3-asn1crypto \
	python3-async-timeout \
	python3-asyncio \
	python3-attrs \
	python3-avahi \
	python3-certifi \
	python3-cffi \
	python3-chardet \
	python3-codecs \
	python3-compile \
	python3-compression \
	python3-configparser \
	python3-core \
	python3-crypt \
	python3-cryptography \
	python3-ctypes \
	python3-datetime \
	python3-dateutil \
	python3-dbus \
	python3-difflib \
	python3-distutils \
	python3-email \
	python3-fcntl \
	python3-html \
	python3-idna \
	python3-idna-ssl \
	python3-io \
	python3-jinja2 \
	python3-json \
	python3-logging \
	python3-markupsafe \
	python3-math \
	python3-mime \
	python3-misc \
	python3-mmap \
	python3-msgpack \
	python3-multidict \
	python3-multiprocessing \
	python3-ndg-httpsclient \
	python3-netclient \
	python3-netserver \
	python3-numbers \
	python3-pickle \
	python3-pika \
	python3-pkgutil \
	python3-plistlib \
	python3-ply \
	python3-pprint \
	python3-profile \
	python3-psutil \
	python3-pyasn1 \
	python3-pycares \
	python3-pycparser \
	python3-pycrypto \
	python3-pyiface \
	python3-pyinotify \
	python3-pyopenssl \
	python3-pyroute2 \
	python3-pysocks \
	python3-pyyaml \
	python3-requests \
	python3-resource \
	python3-setuptools \
	python3-shell \
	python3-six \
	python3-smtpd \
	python3-stringold \
	python3-terminal \
	python3-threading \
	python3-tornado \
	python3-typing \
	python3-typing-extensions \
	python3-unittest \
	python3-unixadmin \
	python3-urllib3 \
	python3-xml \
	python3-xmlrpc \
	python3-yarl \
	salt-common \
	salt-minion \
	sysconfig-settings \
	sysconfig-settings-ui \
"
