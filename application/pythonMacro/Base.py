# coding: UTF-8
import datetime
import logging
import os


class BaseClass:
    def __init__(self):
        self.logger = logging.getLogger()

        today = datetime.date.today()

        os.chdir('/')
        logging.basicConfig(
            level=logging.DEBUG,
            filename="/work/logs/py/" + str(today) + "_blender_python.log",
            format="[%(asctime)s] %(levelname)s [%(name)s/%(funcName)s() at line %(lineno)d]: %(message)s",
            datefmt="%Y-%m-%d %H:%M:%S")

        self.logger.info("★★★★★★★★★★Base-__init__★★★★★★★★★★")
