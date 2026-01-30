package de.reiss.bible2net.theword2.util.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

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
