# coding: UTF-8

# Blenderの組み込みPythonを利用するのでPathがBlender内部に設定される
# そのままだと自作のモジュールが読み込めないので明示的にパスを通す

import time
import sys
import bpy
import traceback

sys.path.append('./')
sys.path.append('/pythonMacro')
sys.path.append('/pythonMacro/Controller')

import BlenderMacro_wall

start = time.time()
print(f"##### start-time:{start} #####")

args = sys.argv

try:
    walpaar = BlenderMacro_wall.BlenderMacro_wall(args[5], args[6], args[7], args[8], args[9])
    walpaar.run()
except Exception:
    print('BlenderMacro_wall-Exception')
    traceback.print_exc()
finally:
    bpy.ops.wm.quit_blender()

elapsed_time = time.time() - start
print(f"##### result-time:{elapsed_time} #####")
