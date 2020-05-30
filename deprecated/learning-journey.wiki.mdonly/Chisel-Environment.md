We have put together a known-good environment inside a VM.  There's a script that installs everything you need for the rest of the learning journey into the VM, and then all the other steps of the journey assume that environment.  

The [detailed steps to set up the VM and environment are here](https://github.com/librecores/riscv-sodor/wiki/Detailed-steps-of-setting-up-environment).

However, if you don't mind hacking on your own, and would like to proceed without setting up the VM, you can run the script in any flavor of Linux, but there may be incompatibilities that cause issues later in the Journey.  Installing the VM and setting up the environment avoid tool related issues down the road. If you want to try on your own, the [Chisel 3 readme setup directions](https://github.com/freechipsproject/chisel3) may help. 

Set up the virtual machine approach in the following way:

### VirtualBox

First, get VirtualBox and install it.  [VirtualBox](https://www.virtualbox.org/Downloads) is available for both Windows and a number of flavors of GNU/Linux. Please take care to install Guest Additions, which allow you to copy-paste and to share a folder on your host OS with the VM, as well as get internet access from inside the VM.  Finally, when creating a virtual machine, please follow the advice given [here](http://blog.jdpfu.com/2012/09/14/solution-for-slow-ubuntu-in-virtualbox) concerning the chipset, acceleration and other virtual hardware. For the virtual RAM and hard drive, please select 4 GB if available (2 GB should also do in most cases) and at least 40 GB of disk, respectively.

### Mint 18

[Mint 18](https://www.linuxmint.com/download.php) was chosen for its simplicity.  It is an Ubuntu derivate.  The standard Journey flow assumes the 64 bit version. If problems arise during the OS installation to the created virtual machine, check your host BIOS settings - you might have to enable virtualization there. One example, just to give the basic idea, is given [here](https://www.howtogeek.com/213795/how-to-enable-intel-vt-x-in-your-computers-bios-or-uefi-firmware/).

Again, [detailed steps to set up the VM and environment are here](https://github.com/librecores/riscv-sodor/wiki/Detailed-steps-of-setting-up-environment).

[This video](https://www.youtube.com/watch?v=mvlMDAxgMsQ) also walks you through creating a VM and installing Linux Mint into it.

### Next Steps

Once the VM is up and running, go back to the setup page and continue: [back to setting up Chisel](https://github.com/librecores/riscv-sodor/wiki/Setting-up-Chisel#tools-to-use-with-chisel).
