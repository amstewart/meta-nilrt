FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "shadow-native pseudo-native niacctbase"

RDEPENDS:${PN} += "niacctbase"

do_install:append() {
	chmod 4550 ${D}${base_sbindir}/halt
	chown 0:${LVRT_GROUP} ${D}${base_sbindir}/halt
}
