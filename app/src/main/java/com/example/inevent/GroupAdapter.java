package com.example.inevent;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<Group> {

    private Context context;
    private Filter groupFilter;
    private List<Group> groupList;

    public GroupAdapter(Context context, List<Group> groups){
        super(context,0,groups);
        this.context = context;
        this.groupList = groups;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = inflater.inflate(R.layout.list_group,null,true);

        if (listItemView == null) {
            listItemView = inflater.inflate(R.layout.list_group,null,true);
        }


        TextView tvGroupName = (TextView) listItemView.findViewById(R.id.groupName);
        TextView tvGroupCapacity = (TextView) listItemView.findViewById(R.id.groupCapacity);
        TextView tvGroupPlace = (TextView) listItemView.findViewById(R.id.groupPlace);
        TextView tvGroupDate = (TextView) listItemView.findViewById(R.id.groupDate);

        Group currentGroup = getItem(position);

        GradientDrawable quantityCircle = (GradientDrawable) tvGroupCapacity.getBackground();

        assert currentGroup != null;
        int quantityColor = getQuantityColor(currentGroup.getgAffectedCount());

        quantityCircle.setColor(quantityColor);

        assert currentGroup != null;
        tvGroupName.setText(currentGroup.getgName());
        tvGroupCapacity.setText(String.valueOf(currentGroup.getgAffectedCount()));
        tvGroupPlace.setText(currentGroup.getgPlace());
        tvGroupDate.setText(currentGroup.getgDate().toString());

        return listItemView;
    }


    public int getQuantityColor(int quantity){
        int quanColor = 0;
        if(quantity == 0){
            quanColor = ContextCompat.getColor(getContext(), R.color.quantity0);
        }
        else if(quantity > 0 && quantity <= 10){
            quanColor = ContextCompat.getColor(getContext(), R.color.quantity1);
        }
        else if(quantity > 10 && quantity <= 30){
            quanColor = ContextCompat.getColor(getContext(), R.color.quantity2);
        }
        else if(quantity > 30 && quantity <= 100){
            quanColor = ContextCompat.getColor(getContext(), R.color.quantity3);
        }
        else{
            quanColor = ContextCompat.getColor(getContext(), R.color.quantity4);
        }

        return quanColor;
    }
}
