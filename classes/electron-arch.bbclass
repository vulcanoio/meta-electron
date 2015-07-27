valid_archs = "ia32 x64 arm"

def map_electron_arch(a, d):
    import re

    valid_archs = d.getVar('valid_archs', True).split()

    if re.match('i.86$', a):   return 'ia32'
    if re.match('x86_64$', a): return 'x64'
    if re.match('arm64$', a):  return 'arm'
    if a in valid_archs:       return a

    bb.error("cannot map %s to electron architecture" % a)

export ELECTRON_ARCH = "${@map_electron_arch(d.getVar('TARGET_ARCH', True), d)}"
