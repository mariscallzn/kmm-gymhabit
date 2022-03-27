package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.utils.wrap

fun ExerciseStore.watchState() = observeState().wrap()
fun ExerciseStore.watchSideEffect() = observeSideEffect().wrap()