DESCRIPTION ?= "Base image setup used in all NILRT images"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "${DISTRO_VERSION}"

IMAGE_FSTYPES = "tar.gz"

IMAGE_FEATURES ??= ""
IMAGE_LINGUAS ??= ""

IMAGE_PREPROCESS_COMMAND += "rootfs_update_timestamp;"

# Do not install license subpackages if they are only recommended.
BAD_RECOMMENDATIONS_append_pn-${PN} += " *-lic"

# NILRT images expect the default kernel is an actual file
# (e.g. bzImage) and not a symbolic link; fix it up here.
ROOTFS_POSTPROCESS_COMMAND += "move_kernel;"
move_kernel() {
	for type in ${KERNEL_IMAGETYPES} ; do
		if [ -L "${IMAGE_ROOTFS}/${KERNEL_IMAGEDEST}/${type}" ]; then
			mv "${IMAGE_ROOTFS}/${KERNEL_IMAGEDEST}/$(readlink "${IMAGE_ROOTFS}/${KERNEL_IMAGEDEST}/${type}")" \
			   "${IMAGE_ROOTFS}/${KERNEL_IMAGEDEST}/${type}"
		fi
	done
}

inherit core-image
