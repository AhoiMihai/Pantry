package com.ahoi.pantry

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ahoi.pantry.auth.details.ProfileActivity
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.profile.ui.MyInvitationsActivity
import com.ahoi.pantry.recipes.ui.myrecipes.MyRecipesActivity
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListsActivity

class HomeActivity : PantryActivity() {

    private val recipes: TextView by bind(R.id.recipes)
    private val shoppingLists: TextView by bind(R.id.shopping_lists)
    private val toolbar: Toolbar by bind(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recipes.setOnClickListener {
            startActivity(Intent(this, MyRecipesActivity::class.java))
        }
        shoppingLists.setOnClickListener {
            startActivity(Intent(this, ShoppingListsActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.invitations) {
            startActivity(Intent(this, MyInvitationsActivity::class.java))
            return true
        }
        if (item.itemId == R.id.profile) {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}