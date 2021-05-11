package com.ahoi.pantry.recipes.ui

class RecipeStepsFormatter {

    fun formatRecipeSteps(steps: List<String>): String {
        val builder = StringBuilder()
        steps.forEach {
            builder.append(steps.indexOf(it))
                .append(". ")
                .append(it)
                .append("\n")
        }

        return builder.toString()
    }
}