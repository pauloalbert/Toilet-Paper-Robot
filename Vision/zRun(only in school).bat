@echo off
cd C:\Users\Reali\workspace\Toilet-Paper-Robot\Vision
set /p a="Local or Stream? (l/s): "
python vision_class.py -%a%
pause 