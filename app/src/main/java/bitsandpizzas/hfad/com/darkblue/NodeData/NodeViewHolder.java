package bitsandpizzas.hfad.com.darkblue.NodeData;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import bitsandpizzas.hfad.com.darkblue.R;

public class NodeViewHolder extends RecyclerView.ViewHolder {
    public TextView nodeTitle;
    public ToggleButton btn1, btn2, btn3, btn4;

    public NodeViewHolder(View view) {
        super(view);
        nodeTitle = (TextView) view.findViewById(R.id.nodenametxt);
        btn1 = (ToggleButton) view.findViewById(R.id.btn1);
        btn2 = (ToggleButton) view.findViewById(R.id.btn2);
        btn3 = (ToggleButton) view.findViewById(R.id.btn3);
        btn4 = (ToggleButton) view.findViewById(R.id.btn4);

    }
}