package de.reiss.bible2net.theword.main.viewpager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.main.content.TheWordFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount() = DaysPositionUtil.DAYS_OF_TIME

    override fun getItem(position: Int) = TheWordFragment.createInstance(position)

}
