@ECHO OFF
SET DISCOVER_DISK=%~d0
SET DISCOVER_DIRECTORY=%~p0
SET DISCOVER_PATH=%DISCOVER_DISK%%DISCOVER_DIRECTORY%
java -Xmx1024m -jar "%DISCOVER_PATH%\lib\discover.jar" --nic=eth3
pause
