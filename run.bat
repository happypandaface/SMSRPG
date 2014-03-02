REM adb uninstall com.me.smsrpg
adb install -r bin/SMSRPG-debug.apk
adb shell am start -n com.me.smsrpg/com.me.smsrpg.SMSRPG
pause