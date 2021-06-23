SUMMARY = "Extremely basic live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	file://init-restore-mode.sh \
"

do_install() {
	install -m 0755 ${WORKDIR}/init-restore-mode.sh ${D}/init
}

do_install_append_x64() {
	install -m 0644 ${WORKDIR}/grub.cfg ${D}/
}

RDEPENDS_${PN} += "bash ni-provisioning"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} += "/init"
