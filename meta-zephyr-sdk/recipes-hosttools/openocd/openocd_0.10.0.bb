LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS = "libusb-compat hidapi libftdi"
RDEPENDS_${PN} = "libusb1 hidapi"

# The various arc files are based on the commit e781e73a39bc5c845b73dc96b751d867278a7583
# of https://github.com/foss-for-synopsys-dwc-arc-processors/openocd

SRC_URI = " \
	git://github.com/zephyrproject-rtos/openocd.git;protocol=https;nobranch=1 \
	"
SRCREV = "f79c902686f3ae500f3426dc2e552fdec80796d1"

S = "${WORKDIR}/git"

inherit pkgconfig autotools gettext

BBCLASSEXTEND += "nativesdk"

EXTRA_OECONF = "--enable-ftdi --enable-cmsis-dap --enable-jlink --enable-stlink --disable-doxygen-html --enable-sysfsgpio --enable-bcm2835gpio"

do_configure() {
    cd ${S}
    export ALL_PROXY="${ALL_PROXY}"
    export GIT_PROXY_COMMAND=${GIT_PROXY_COMMAND}
    ./bootstrap
    oe_runconf ${EXTRA_OECONF}
}

do_compile() {
    :
}

do_install() {
    cd ${S}
    oe_runmake DESTDIR=${D} install
    if [ -e "${D}${infodir}" ]; then
      rm -Rf ${D}${infodir}
    fi
    if [ -e "${D}${mandir}" ]; then
      rm -Rf ${D}${mandir}
    fi
    if [ -e "${D}${bindir}/.debug" ]; then
      rm -Rf ${D}${bindir}/.debug
    fi
}

