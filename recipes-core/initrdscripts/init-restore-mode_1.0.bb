SUMMARY = "Extremely basic live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	file://init-restore-mode.sh \
	file://00-init-restore-mode.sh \
	file://efifix \
	file://mmc_storage_device_codes.allow \
	file://ni_provisioning \
	file://ni_provisioning.common \
	file://ni_provisioning.answers.default \
"

SRC_URI_append_xilinx-zynqhf = "\
	file://disk_config_xilinx-zynqhf \
"

SRC_URI_append_x64 = "\
	file://ni_provisioning.safemode \
	file://disk_config_x64 \
	file://grub.cfg	\
"

package_namespace = "ni-provisioning"

RDEPENDS_${PN} += "bash rauc"

D_datadir    = ${D}${datadir}/${package_namespace}
D_libdir     = ${D}${libdir}/${package_namespace}
D_sysconfdir = ${D}${sysconfdir}/${package_namespace}

do_install() {
	# etc files
	install -d ${D_sysconfdir}
	install -m 0644 ${WORKDIR}/mmc_storage_device_codes.allow ${D_sysconfdir}/
	install -d ${D}/${sysconfdir}/profile.d
	install -m 0644 ${WORKDIR}/00-init-restore-mode.sh ${D}/${sysconfdir}/profile.d/

	# lib files
	install -d ${D_libdir}
	install -m 0644 ${WORKDIR}/ni_provisioning.common ${D_libdir}/

	# sbin files
	install -m 0755 ${WORKDIR}/efifix          ${D}${sbindir}/efifix
	install -m 0755 ${WORKDIR}/ni_provisioning ${D}${sbindir}/ni_provisioning

	# share files
	install -d ${D_datadir}
	install -m 0644 ${WORKDIR}/ni_provisioning.answers.default ${D_datadir}/

	install -m 0755 ${WORKDIR}/init-restore-mode.sh ${D}/init
}

do_install_append_x64() {
	install -m 0644 ${WORKDIR}/ni_provisioning.safemode ${D_libdir}/
	install -m 0755 ${WORKDIR}/disk_config_x64 ${D_libdir}/disk_config

	install -m 0644 ${WORKDIR}/grub.cfg ${D_datadir}/
}

do_install_append_xilinx-zynqhf() {
	install -m 0755 ${WORKDIR}/disk_config_xilinx-zynqhf ${D_libdir}/disk_config
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} += "/init /etc/profile.d/00-init-restore-mode.sh"
FILES_${PN}_append_x64 += " /grub.cfg "
