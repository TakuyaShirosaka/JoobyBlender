import logging

logger = logging.getLogger()
logging.basicConfig(
    level=logging.DEBUG,
    filename='/work/logs/pythonMacro/blender_python.log',
    format="[%(asctime)s] %(levelname)s [%(name)s/%(funcName)s() at line %(lineno)d]: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S")
