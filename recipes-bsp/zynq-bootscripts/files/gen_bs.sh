#!/bin/bash

# salt reads the uboot env via fw_printenv to populate its hw info grains,
# so we need to set any env vars not set directly by factory uboot here.
# NI doesn't currently support NILRT on non-NI ARM hardware, so setting
# this globally is safe. If/when NI decides to support non-NI ARM hardware,
# we need to check if the hardware is NI-produced (maybe a cpld register?)
# and conditionally set these variables.
SET_HW_ENV_INFO='
test -n "$manufacturer" ||
    setenv manufacturer National Instruments &&
    setenv .flags manufacturer:so &&
    saveenv;
'

COMMON_BOOTARGS='console=ttyS0,115200 $mtdparts ubi.mtd=boot-config ubi.mtd=root $othbootargs'
RESTORE_BOOTARGS="setenv bootargs $COMMON_BOOTARGS ramdisk_size=135168 root=/dev/ram rw"
DEFAULT_BOOTARGS="setenv bootargs $COMMON_BOOTARGS root=ubi1:rootfs rw rootfstype=ubifs \${usb_gadget_args}"
BW_MIGRATE_BOOTARGS="setenv bootargs $COMMON_BOOTARGS ramdisk_size=135168 root=/dev/ram rw"

load_restore_boot()
{
    RESTORE_BOOTMODE=$1
RESTORE_BOOTCMD="
if ubi part boot-config &&
    ubifsmount ubi:bootfs &&
    ubifsload \$loadaddr .restore/restore.scr;
then
    restore=$RESTORE_BOOTMODE;
    source \$loadaddr;
else
    echo Restore mode error.;
fi;
"
    echo $RESTORE_BOOTCMD
}

DEFAULT_BOOTCMD="
$SET_HW_ENV_INFO
if ubi part root &&
    ubifsmount ubi:rootfs &&
    ubifsload \$loadaddr /boot/uImage &&
    ubifsload \$verifyaddr /boot/ni-\$DeviceCode.dtb;
then
    $DEFAULT_BOOTARGS safe_mode=false;
    bootm \$loadaddr - \$verifyaddr;
elif
    ubifsload \$loadaddr /boot/safemode.scr;
then
    source \$loadaddr;
else
    "`load_restore_boot restore`"
fi;"


SAFEMODE_BOOTCMD="
if ubi part root &&
    ubifsmount ubi:rootfs &&
    ubifsload \$loadaddr /boot/uImage &&
    ubifsload \$verifyaddr /boot/ni-\$DeviceCode.dtb;
then
    $DEFAULT_BOOTARGS safe_mode=true;
    bootm \$loadaddr - \$verifyaddr;
else
    "`load_restore_boot restore`"
fi;"

RESTORE_BOOTCMD="
if ubifsload \$loadaddr .restore/uImage &&
    ubifsload 0x8500000 .restore/ni-\$DeviceCode.dtb &&
    ubifsload 0x8700000 .restore/ramdisk;
then
    $RESTORE_BOOTARGS
    restore=\$restore;
    bootm \$loadaddr 0x8700000 0x8500000;
else
    echo Restore mode error;
fi;"

CHECK_RESTOREMODE='
i2c read 0x40 1 1 $verifyaddr;
setexpr.b cpld_safemode *$verifyaddr \& 0x01;
if test $cpld_safemode -eq 1;
then
    '`load_restore_boot restore`'
fi;
'

BOOTCMD='
i2c read 0x40 0x1F 1 $verifyaddr;
setexpr.b next_bootmode *$verifyaddr \& 0x3;
if test $next_bootmode -eq 0;
then
    if ubifsload $loadaddr /boot/default.scr;
    then
        source $loadaddr;
    elif ubifsload $loadaddr /boot/safemode.scr;
    then
        source $loadaddr;
    else
        '`load_restore_boot restore`'
    fi;
elif test $next_bootmode -eq 2;
then
    '`load_restore_boot restore`'
elif test $next_bootmode -eq 3;
then
    '`load_restore_boot auto-restore`'
else
    echo FATAL ERROR!;
fi;
'

BW_MIGRATION_BOOTCMD="
if ubi part root &&
    ubifsmount ubi:rootfs &&
    ubifsload 0x8500000 boot/.oldNILinuxRT/uImage &&
    ubifsload 0x9000000 boot/.oldNILinuxRT/dtbs/ni-\$DeviceCode.dtb &&
    ubifsload 0x9200000 boot/.oldNILinuxRT/ramdisk;
then
    $BW_MIGRATE_BOOTARGS restore=backward-migrate;
    bootm 0x8500000 0x9200000 0x9000000;
else
    echo Migration image corrupt!;
fi;
"

# cRIO-9068 doesn't have USB gadget, so skip it on that device
USB_GADGET_ARGS='
if test ${DeviceCode} != 0x76D6;
then
    usb_gadget_args="g_ether.idVendor=${USBVendorID} g_ether.idProduct=${USBProductID} g_ether.iProduct=\"${USBProduct} [${hostname}]\" g_ether.iSerialNumber=${serial#} g_ether.dev_addr=${usbgadgetethaddr} g_ether.bcdDevice=${USBDevice}";
fi;'

echo $USB_GADGET_ARGS > top_level_bootscript
echo $CHECK_RESTOREMODE >> top_level_bootscript
echo $BOOTCMD >> top_level_bootscript
echo $DEFAULT_BOOTCMD > default_bootscript
echo $SAFEMODE_BOOTCMD > safemode_bootscript
echo $RESTORE_BOOTCMD > restore_bootscript
echo $BW_MIGRATION_BOOTCMD > bw_migrate_bootscript
echo $BOOTCMD >> bw_migrate_bootscript
