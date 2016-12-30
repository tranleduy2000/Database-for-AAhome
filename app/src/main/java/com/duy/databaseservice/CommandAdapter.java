package com.duy.databaseservice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.duy.databaseservice.data.Database;
import com.duy.databaseservice.items.MessengeItem;

import java.util.ArrayList;

/**
 * Created by tranleduy on 14-May-16.
 */
public class CommandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessengeItem> messengeItems = new ArrayList<>();
    private Context context;
    private Database database;
    private EditText editTextCommnad = null;

    public CommandAdapter(Context context, ArrayList<MessengeItem> arrayList) {
        this.context = context;
        this.messengeItems = arrayList;
    }

    public CommandAdapter(Context context, Database database) {
        this.context = context;
        this.database = database;
        this.messengeItems = this.database.getAllMsg();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        if (viewType == MessengeItem.TYPE_IN) {
            view = LayoutInflater.from(context).inflate(R.layout.item_messenge_in, parent, false);
            viewHolder = new ViewHolderIn(view);
        } else if (viewType == MessengeItem.TYPE_OUT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_messenge_out, parent, false);
            viewHolder = new ViewHolderOut(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessengeItem messengeItem = messengeItems.get(position);
        if (holder instanceof ViewHolderIn) {
            ((ViewHolderIn) holder).txtMessenge.setText(messengeItem.getBody());
            ((ViewHolderIn) holder).txtMessenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextCommnad != null) {
                        editTextCommnad.setText(messengeItem.getBody());
                        editTextCommnad.selectAll();

                    }
                }
            });
            ((ViewHolderIn) holder).txtDate.setText(messengeItem.getDate());
        } else if (holder instanceof ViewHolderOut) {
            ((ViewHolderOut) holder).txtMessenge.setText(messengeItem.getBody());
            ((ViewHolderOut) holder).txtMessenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextCommnad != null) {
                        editTextCommnad.setText(messengeItem.getBody());
                        editTextCommnad.selectAll();
                    }
                }
            });
            ((ViewHolderOut) holder).txtDate.setText(messengeItem.getDate());

        }
    }

    public void addMessengeItem(MessengeItem messengeItem) {
        messengeItems.add(messengeItem);
        database.addMsg(messengeItem);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return messengeItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messengeItems.get(position).getType();
    }

    public void setEditTextCommand(EditText editTextCommnad) {
        this.editTextCommnad = editTextCommnad;
    }

    public static class ViewHolderIn extends RecyclerView.ViewHolder {
        TextView txtMessenge, txtDate;

        public ViewHolderIn(View itemView) {
            super(itemView);
            txtMessenge = (TextView) itemView.findViewById(R.id.tv_messenge);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);

        }

    }

    public static class ViewHolderOut extends RecyclerView.ViewHolder {
        TextView txtMessenge, txtDate;

        public ViewHolderOut(View itemView) {
            super(itemView);
            txtMessenge = (TextView) itemView.findViewById(R.id.tv_messenge);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
        }

    }


}