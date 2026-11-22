package org.cheragh.torch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

public class TorchWidget extends AppWidgetProvider {
    static void update(Context context, boolean on) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, TorchWidget.class));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setImageViewResource(R.id.icon, on ? R.drawable.torch_on : R.drawable.torch_off);
        Intent intent = new Intent(context, ToggleActivity.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= 23) flags |= 0x04000000; // FLAG_IMMUTABLE
        views.setOnClickPendingIntent(R.id.icon, PendingIntent.getActivity(context, 0, intent, flags));
        manager.updateAppWidget(ids, views);
    }
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        update(context, TorchService.on);
    }
}
