package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.data.model.FullExercise
import com.andymariscal.gymhabit.data.model.FullRoutinePlan
import com.andymariscal.gymhabit.data.model.FullRoutinePlanExercises
import com.andymariscal.gymhabit.data.model.FullSetPlan

fun convert(fullRoutinePlan: FullRoutinePlan): UiRoutinePlan =
    UiRoutinePlan(
        fullRoutinePlan.id,
        fullRoutinePlan.name,
        fullRoutinePlan.routinePlanExercises.map(::convert)
    )

fun convert(fullRoutinePlanExercises: FullRoutinePlanExercises): UiRoutinePlanExercise =
    UiRoutinePlanExercise(
        fullRoutinePlanExercises.id,
        fullRoutinePlanExercises.exercisesSets.mapKeys { convert(it.key) }
            .mapValues { it.value.map(::convert) }
    )

fun convert(fullExercise: FullExercise): UiExercise =
    UiExercise(
        fullExercise.exercise.id,
        fullExercise.exercise.name,
        fullExercise.muscles.map { uiM -> UiMuscles(uiM.id, uiM.name) },
        fullExercise.equipments.map { uiEq -> UiEquipment(uiEq.id, uiEq.name) })

fun convert(fullSetPlan: FullSetPlan): UiSetPlan =
    UiSetPlan(
        fullSetPlan.reps.toString(),
        fullSetPlan.weight.toString(),
        fullSetPlan.weightUnit
    )
