package app.utility

import app.common.Const

object BlenderUtility {
    data class ModelSize(val width: Double, val height: Double)
    data class ARMaterials(val pythonFilePath: String, val blenderFilePath: String)

    /* Blender */
    const val BLENDER_COMMAND = "blender"
    val glbFileExportPath = Const.env.getString("tmp.3dModelFilePath")


    /**
     * ARMaterials
     * 壁紙をデフォルトとして処理を行う
     */
    fun getARMaterials(objectType: String = "1"): ARMaterials {
        val pythonFilePath = Const.WebAR.pythonFilePath[objectType]
                ?: "/pythonMacro/Controller/BlenderMacro_01_Controller.py"
        val blenderFilePath = Const.WebAR.blenderFilePath[objectType]
                ?: "/pythonMacro/3dModel/wall_1m.blend"

        return ARMaterials(pythonFilePath, blenderFilePath)
    }
}





