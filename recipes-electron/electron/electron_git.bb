SUMMARY = "Build cross platform desktop apps with web technologies"
HOMEPAGE = "http://electron.atom.io/"
LICENSE = "MIT"

LIC_FILES_CHKSUM = " \
  file://LICENSE;md5=dd413c962a5a67c951cc5dd842060ace \
"

DEPENDS += " \
  ninja-native \
  nodejs-native \
  clang-cross-${TARGET_ARCH} \
  util-linux \
  libnotify3 \
  gtk+ \
  gconf \
  dbus \
  alsa-lib \
  cups \
  xinput \
  nss \
  libxtst \
  libxi \
  libcap \
"

PV = "0.0-gitr${SRCPV}"

SRC_URI = " \
  git://github.com/atom/electron.git;protocol=https \
  file://fix_build.patch \
"
SRCREV = "135aca02af8aef5792984d47bf1c7a35a11f90a3"

S = "${WORKDIR}/git"

do_configure() {
    # Enable C++11 support at command line because the provided clang was compiled without C++11 support
    export CXX="${CXX} -std=c++11" 
    
    ./script/bootstrap.py -v --target_arch=ia32
}

do_compile() {
    ./script/build.py -c R
}

do_install() {
    install -d ${D}${libdir}/${PN}/locales
    install -m 0755 ${S}/out/R/${PN} ${D}${libdir}/${PN}/
    install -m 0644 ${S}/out/R/icudtl.dat ${D}${libdir}/${PN}/
    install -m 0644 ${S}/out/R/content_shell.pak ${D}${libdir}/${PN}/
    install -m 0644 ${S}/out/R/libffmpegsumo.so ${D}${libdir}/${PN}/
    install -m 0644 ${S}/out/R/libnode.so ${D}${libdir}/${PN}/

    for F in ${S}/out/R/locales/*; do
	install -m 0644 $F ${D}${libdir}/${PN}/locales/
    done

    install -d ${D}${bindir}/
    ln -sf ${libdir}/${PN}/${PN} ${D}${bindir}/${PN}
}

do_clean() {
    ./script/clean.py
}

FILES_${PN} = "${bindir}/${PN} ${libdir}/${PN}/*"

TOOLCHAIN = "clang"
