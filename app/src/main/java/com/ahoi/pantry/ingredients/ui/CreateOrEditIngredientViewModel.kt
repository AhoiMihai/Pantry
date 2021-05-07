package com.ahoi.pantry.ingredients.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.google.firebase.firestore.FirebaseFirestoreException
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CreateOrEditIngredientViewModel(
    private val pantry: Pantry
) : LifecycleObserver {
    private val _createItemState = MutableLiveData<OperationState>()
    val createItemState: LiveData<OperationState> = _createItemState

    private val disposable = CompositeDisposable()

    fun createItem(displayIngredient: DisplayIngredient) {
        if (!isItemValid(displayIngredient)) {
            _createItemState.postValue(OperationState.VALIDATION_ERROR)
            return
        }
        val item = PantryItem(
            ingredientName = displayIngredient.name,
            quantity = Quantity(displayIngredient.amount, displayIngredient.unit),
            tags = emptyList()
        )
        disposable.add(pantry.updateOrCreateItems(listOf(item))
            .subscribe(
                {
                    _createItemState.postValue(OperationState.SUCCESS)
                },
                {
                    _createItemState.postValue(generateErrorState(it))
                }
            )
        )
    }

    private fun isItemValid(displayIngredient: DisplayIngredient): Boolean {
        return (displayIngredient.name.isNotEmpty() && displayIngredient.amount >= 0)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() {
        disposable.dispose()
    }

    private fun generateErrorState(exception: Throwable): OperationState {
        return if (exception is FirebaseFirestoreException) {
            when (exception.code) {
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> OperationState.VALIDATION_ERROR
                else -> OperationState.UNKNOWN_ERROR
            }
        } else {
            OperationState.UNKNOWN_ERROR
        }
    }
}

data class DisplayIngredient(
    val name: String,
    val amount: Double,
    val unit: Unit,
)

enum class OperationState {
    SUCCESS,
    UNKNOWN_ERROR,
    VALIDATION_ERROR,
}