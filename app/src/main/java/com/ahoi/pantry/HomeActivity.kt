package com.ahoi.pantry

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.ahoi.pantry.auth.details.ProfileActivity
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.profile.ui.MyInvitationsActivity
import com.ahoi.pantry.recipes.ui.myrecipes.MyRecipesFragment
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : PantryActivity() {

    private val pager: ViewPager2 by bind(R.id.pager)
    private val fab: FloatingActionButton by bind(R.id.fab)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val tabs: TabLayout by bind(R.id.tabs)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val fragments = listOf(
            MyRecipesFragment(),
            ShoppingListFragment()
        )

        val adapter = HomePagerAdapter(fragments, supportFragmentManager, lifecycle)
        pager.adapter = adapter
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = getString(adapter.pageTitleAt(position))
        }.attach()
        fab.setOnClickListener {
            adapter.fabClicked(pager.currentItem)
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