package com.example.joeyb.remotecontrol;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joeyb on 8/15/2017.
 */

public class ProjectsAdapter extends ArrayAdapter<Project> {
    private Context mContext;
    private int mResource;
    private ArrayList<Project> mProjects;
    public ProjectsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Project> projects) {
        super(context, resource, projects);
        mContext = context;
        mResource = resource;
        mProjects = projects;
    }

    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public Project getItem(int position) {
        return mProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String name = "Device Name: " + getItem(position).getName();
        String address = "Device Address: " + getItem(position).getAddress();
        String type = "Device Type: " + getItem(position).getType();
        Project project = new Project(title, name, address, type);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView projectName = (TextView) convertView.findViewById(R.id.projectName);
        TextView deviceName = (TextView) convertView.findViewById(R.id.deviceName);
        TextView deviceAddress = (TextView) convertView.findViewById(R.id.deviceAddress);
        TextView deviceType = (TextView) convertView.findViewById(R.id.deviceType);
        projectName.setText(title);
        deviceName.setText(name);
        deviceAddress.setText(address);
        deviceType.setText(type);
        return convertView;
    }
}
