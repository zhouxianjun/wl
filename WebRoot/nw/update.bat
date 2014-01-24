taskkill /f /im "wl.exe"
@ping -n 1 127.1>nul
move "%cd%\update\%1" wl.exe
start wl.exe