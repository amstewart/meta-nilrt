SUMMARY = "NI Linux Realtime distribution packages for recovery media images"
LICENSE = "MIT"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} += "\
	base-passwd \
	bash \
	binutils \
	bzip2 \
	coreutils \
	dmidecode \
	dosfstools \
	e2fsprogs \
	e2fsprogs-mke2fs \
	e2fsprogs-tune2fs \
	efibootmgr \
	efivar \
	eudev \
	findutils \
	fw-printenv \
	gawk \
	gptfdisk-sgdisk \
	grep \
	grub \
	grub-editenv \
	grub-efi \
	init-restore-mode \
	kmod \
	ni-smbios-helper \
	nilrtdiskcrypt \
	parted \
	procps \
	sed \
	sysvinit \
	tar \
	util-linux \
	util-linux-agetty \
	vim-tiny \
	xz \
"

RRECOMMENDS_${PN} += "\
	kernel-module-atkbd \
	kernel-module-i8042 \
	kernel-module-tpm-tis \
"
