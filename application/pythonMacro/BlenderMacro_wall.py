# coding: UTF-8

import bpy
import Base


class BlenderMacro_wall(Base.BaseClass):
    def __init__(self, blenderFilePath, imageFilePath, glbFileExportPath, width, height):
        super(BlenderMacro_wall, self).__init__()
        self.blenderFilePath = blenderFilePath
        self.imageFilePath = imageFilePath
        self.glbFileExportPath = glbFileExportPath
        self.width = float(width)
        self.height = float(height)

    def run(self):
        self.logger.info("BlenderMacro_wall-run-start")
        self._blenderProcessing()
        self._glbFileExport()
        self.logger.info("BlenderMacro_wall-run-end")

    def _blenderProcessing(self):
        self.logger.info("BlenderMacro_wall-blenderProcessing")

        # .blenderファイルを開く
        bpy.ops.wm.open_mainfile(filepath=self.blenderFilePath)

        # 選択を外す
        for obj in bpy.context.scene.objects:
            obj.select_set(False)

        # Boxオブジェクトを指定
        obj = bpy.data.objects['Box']
        obj.select_set(True)

        # 寸法の変更
        self._change_object_dimensions(obj)

        # アクティブなマテリアルの選択、Nodeを有効にする
        bpy.context.view_layer.objects.active = obj
        mat = bpy.context.view_layer.objects.active.active_material
        mat.use_nodes = True

        # 3Dオブジェクトに画像テクスチャを付与する
        imageNode = mat.node_tree.nodes["画像テクスチャ"]
        imageNode.image = bpy.data.images.load(self.imageFilePath)

        for node in mat.node_tree.nodes:
            self.logger.info(node)

    def _change_object_dimensions(self, selectob='Default'):

        # 各辺の寸法を取得する
        selectdimX = selectob.dimensions[0]
        selectdimY = selectob.dimensions[1]
        selectdimZ = selectob.dimensions[2]

        # リサイズするための倍率を取得する
        # X:1 Y:0.01 Z:2
        magnificationX = self.width / selectdimX
        magnificationZ = self.height / selectdimZ
        self.logger.info("self.width:" + str(self.width))
        self.logger.info("self.height:" + str(self.height))

        # 拡大後の寸法を取得します
        changedimX = selectdimX * magnificationX
        changedimY = selectdimY
        changedimZ = selectdimZ * magnificationZ

        changedimensions = (changedimX, changedimY, changedimZ)
        self.logger.info("changedimX:" + str(changedimX))
        self.logger.info("changedimY:" + str(changedimY))
        self.logger.info("changedimZ:" + str(changedimZ))

        # 寸法を変更する
        selectob.dimensions = changedimensions

    def _glbFileExport(self):
        self.logger.info("BlenderMacro_wall-glbFileExport-start")
        bpy.ops.export_scene.gltf(export_format='GLB', filepath=self.glbFileExportPath)
        self.logger.info("BlenderMacro_wall-glbFileExport-end")
