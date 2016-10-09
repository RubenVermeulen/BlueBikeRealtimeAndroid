package be.rubenvermeulen.android.bluebikert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ruben on 8/10/2016.
 */

public class CustomAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater inflater;
    private List<CustomObject> objects;

    private class ViewHolder {
        TextView tvOne;
        TextView tvTwo;
    }

    public CustomAdapter(Context ctx, List<CustomObject> objects) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);

            holder.tvOne = (TextView) convertView.findViewById(R.id.key);
            holder.tvTwo = (TextView) convertView.findViewById(R.id.value);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        int resourceKey = 0;

        switch (objects.get(position).getPropOne()) {
            case "CapacityTotal": resourceKey = R.string.capacity;
                break;
            case "CapacityInUse": resourceKey = R.string.in_use;
                break;
            case "CapacityAvailable": resourceKey = R.string.available;
                break;
            case "CapacityInMaintenance": resourceKey = R.string.in_maintance;
                break;
        }

        holder.tvOne.setText(ctx.getResources().getString(resourceKey));
        holder.tvTwo.setText(objects.get(position).getPropTwo());

        return convertView;
    }
}
