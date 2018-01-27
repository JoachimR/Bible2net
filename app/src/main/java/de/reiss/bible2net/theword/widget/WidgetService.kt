package de.reiss.bible2net.theword.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            WidgetRemoteViewsFactory(this.applicationContext)

}