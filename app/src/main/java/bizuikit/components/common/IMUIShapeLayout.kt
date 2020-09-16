/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bizuikit.components.common

import androidx.annotation.ColorInt
import androidx.annotation.IntDef

/**
 * Created by cgspine on 2018/3/23.
 */
interface IMUIShapeLayout {
    @IntDef(value = [HIDE_RADIUS_SIDE_NONE, HIDE_RADIUS_SIDE_TOP, HIDE_RADIUS_SIDE_RIGHT, HIDE_RADIUS_SIDE_BOTTOM, HIDE_RADIUS_SIDE_LEFT])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class HideRadiusSide

    companion object {
        const val HIDE_RADIUS_SIDE_NONE = 0
        const val HIDE_RADIUS_SIDE_TOP = 1
        const val HIDE_RADIUS_SIDE_RIGHT = 2
        const val HIDE_RADIUS_SIDE_BOTTOM = 3
        const val HIDE_RADIUS_SIDE_LEFT = 4
    }

    fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float)

    fun setRadiusAndShadow(
        radius: Int,
        @IMUIShapeLayout.HideRadiusSide hideRadiusSide: Int,
        shadowElevation: Int,
        shadowAlpha: Float
    )

    fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowColor: Int,
        shadowAlpha: Float
    )

    fun setShadowElevation(elevation: Int)

    fun setShadowAlpha(shadowAlpha: Float)

    fun setShadowColor(shadowColor: Int)

    fun setRadius(radius: Int)

    fun setRadius(radius: Int, @IMUIShapeLayout.HideRadiusSide hideRadiusSide: Int)
}