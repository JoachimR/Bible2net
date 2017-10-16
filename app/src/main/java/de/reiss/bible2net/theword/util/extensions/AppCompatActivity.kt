package de.reiss.bible2net.theword.util.extensions

import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.replaceFragmentIn(@IdRes container: Int, fragment: Fragment) {
    supportFragmentManager
            .beginTransaction()
            .replace(container, fragment)
            .commit()
}

fun AppCompatActivity.findFragmentIn(@IdRes container: Int): Fragment? =
        supportFragmentManager.findFragmentById(container)

fun AppCompatActivity.displayDialog(dialogFragment: DialogFragment) {
    supportFragmentManager
            .beginTransaction()
            .add(dialogFragment, dialogFragment.javaClass.canonicalName)
            .commit()
}