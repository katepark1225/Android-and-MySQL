package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CustomViewHolder> {

    private ArrayList<cardlist> arrayList = null;
    private Activity context = null;

    public CardAdapter(Activity context, ArrayList<cardlist> list) {
        this.context = context;
        this.arrayList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView productName, productSupplier, productStock;
        protected LinearLayout linearLayout;
        protected ConstraintLayout layout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.productName = itemView.findViewById(R.id.productName);
            this.productSupplier = itemView.findViewById(R.id.productSupplier);
            this.productStock = itemView.findViewById(R.id.productStock);

            this.linearLayout = itemView.findViewById(R.id.linearLayout);
            this.layout = itemView.findViewById(R.id.layout);

            this.layout.setOnClickListener(view -> {
                Intent intent = new Intent(context, productpage.class);

                intent.putExtra("productName", this.productName.getText().toString());
                intent.putExtra("productSupplier", this.productSupplier.getText().toString());
                intent.putExtra("productStock", this.productStock.getText().toString());

                context.startActivity(intent);
            });

        }
    }

    @NonNull
    @Override
    public CardAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.CustomViewHolder holder, int position) {

        String productName = arrayList.get(position).getProductName();
        holder.productName.setText(productName);

        String productSupplier = arrayList.get(position).getProductSupplier();
        holder.productSupplier.setText(productSupplier);

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        float productStock = Float.parseFloat(arrayList.get(position).getProductStock());
        String finalProductStock = myFormat.format(productStock) + "개";

        holder.productStock.setText(finalProductStock);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }
}
