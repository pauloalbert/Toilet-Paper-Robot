@echo off
set /p a="Local or Stream? (l/s): "
python3 vision_class.py -%a%
pause 