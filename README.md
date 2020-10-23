# Monitored Wellbeing

[Portfolio Website](https://carolinexia97.wixsite.com/project-portfolio/home)

Gym loyalty program app, get rewards for being active at a gym.

This app will measure heartrate and location. Current version includes one geofence at Chalmers University of Technology, Campus Lindholmen, at Kuggen.

The app uses SHA-1 encryption. There is an included credentials.json, but if the app still returns an error with "Code 10", please contact kevin.solovjov@gmail.com with your SHA1 key (Most easily found in Android Studio, in the gradle tab on the right under Monitored-Wellbeing > android > signinReport) so it can be approved ASAP.


## Physical Device
Tested and working on OnePlus 6

The app requires a working camera and flash to run correctly. If testing outside of kuggen, may require GPS spoofing on the device. Open the project in android studio and run. Log in with either e-mail or google account to get to the main part of the application.

## Emulator

Tested on Emulated Nexus 4, please use the Nexus 4 Branch for emulating with this device.

Heartrate measuring is difficult on an emulator. It is possible to press the ALT key (On Windows) to move the camera and "trick" the app into measuring the simulated image. There is a known issue where the app encounters an error if the camera is not moved at all in an emulator.

Test on larger devices, the UI does not look right on all screen sizes. Some phones (Especially those with an AMOLED display) do not show gradients correctly in the emulator, the image appears banded or low-res. App otherwise works correctly.


