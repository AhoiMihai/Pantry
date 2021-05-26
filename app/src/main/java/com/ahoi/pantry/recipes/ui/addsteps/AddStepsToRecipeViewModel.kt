package com.ahoi.pantry.recipes.ui.addsteps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AddStepsToRecipeViewModel {

    private val _steps = MutableLiveData<MutableList<String>>()
    val steps: LiveData<MutableList<String>> = _steps

    fun addSteps(step: List<String>) {
        val currentSteps = steps.value?: mutableListOf()
        currentSteps.addAll(step)
        _steps.postValue(currentSteps)
    }

    fun addStep(step: String) {
        if (step.isEmpty()) {
            return
        }
        val currentSteps = steps.value?: mutableListOf()
        currentSteps.add(step)
        _steps.postValue(currentSteps)
    }

    fun removeStep() {
        val currentSteps = steps.value
        if (currentSteps.isNullOrEmpty()) {
            return
        }
        currentSteps.removeAt(currentSteps.size - 1)
        _steps.postValue(currentSteps!!)
    }
}