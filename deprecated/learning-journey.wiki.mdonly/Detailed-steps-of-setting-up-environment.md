## Getting started


Below are the steps of getting the hypervisor, and the OS image, and then creating a VM and installing the required tools inside that VM.

Get VirtualBox from here: 
https://www.virtualbox.org/wiki/Downloads

Get Mint 18 from here:
https://www.linuxmint.com/edition.php?id=217

Install Virtualbox, and create a VM (choose "Ubuntu 64bit" as the type), at least 40GB of virtual disk, and install Mint into that VM

* Select the new VM, 
* click "settings" then "system" then "motherboard" to configure it with a large amount of RAM (at least 2GB), then "Processor" to configure with at least half of the CPUs on your machine, then "Acceleration" to turn on VT-x and nested paging.
* click "Display"->"screen" and set video memory to 64MB and enable 3D acceleration
* click "Storage"  -- on the right side of the popup is "Optical Drive" next to that is a little icon of a CD click on that and select the .iso file of the Linux distribution that you downloaded.
* (double check that the virtual hard disk size is larger than 40GB).
* click "save" or "ok"
* at the top, is a settings icon, click on that, then click on tab labelled "shared folders".  In the dialog, on the left you'll see a folder with a "+" on it.. click on that and choose a folder that is outside of the VM. Check Auto-Mount field. You will use this folder for transferring files between VM and your host. On your host side, that shared folder should have appropriate permission level. If your host OS is Windows, right-click on the folder -> share with -> specific people -> select Everyone from drop-down menu and click on Add. Permission level should be set to **_Read_**. Finally, click Share -> Done. You can find more details in [this](https://www.youtube.com/watch?v=XjbxFRUoPDQ) video. 

Then start the VM.  This will cause it to boot into the Linux install disk.  Go through the linux install procedure.

In the window where the VM is running:

* click "Devices"->"shared clipboard" and select "bidirectional" 
* select "Devices"->"drag and drop"->"bidirectional"

Install the Guest Additions and git and add permissions for shared folder in the Linux Mint that is installed inside the VM:

* Devices->"Insert Guest Additions CD"   if issues, see  https://www.virtualbox.org/manual/ch04.html
* $ sudo apt-get install git
* $ sudo usermod -a -G vboxsf <your_username>

Restart the VM (important!) then enable clipboard copy-paste:

in the VM _manager_, click the settings button for the VM.  Under General, select Advanced tab, select bi-directional. https://www.liberiangeek.net/2013/09/copy-paste-virtualbox-host-guest-machines/

Finally, run the setup script.

