package com.nepshirts.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nepshirts.android.models.OrderModel;
import com.nepshirts.android.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.searchViewHolder> {
    List<ProductModel> list;
    ArrayList<OrderModel> cartlist;
        private Context mContext;

    public SearchAdapter(List<ProductModel> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    public SearchAdapter(ArrayList<OrderModel> cartlist, Context mContext) {
        this.cartlist = cartlist;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchViewHolder holder,final int i) {
        int initialPrice  = Integer.parseInt(list.get(i).getPrice());
        int discPrice =Integer.parseInt(list.get(i).getDisPrice());
        try {
            int percent = (discPrice * 100) / initialPrice;
            if(percent <=0){
                holder.price2.setText(list.get(i).getPrice());
                holder.price1.setVisibility(View.GONE);
                holder.percentage.setVisibility(View.GONE);
            }else if(percent ==100){
                holder.percentage.setText("FREE!!");
                holder.price1.setText(list.get(i).getPrice());
                holder.price1.setPaintFlags(holder.price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price2.setText(list.get(i).getDisPrice());
            }else {
                String p = String.valueOf(percent);
                holder.percentage.setText(p + "%" + " Discount");
                holder.price1.setText(list.get(i).getPrice());
                holder.price1.setPaintFlags(holder.price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price2.setText(list.get(i).getDisPrice());
            }
        }catch (Exception e){


        }

        Uri img = Uri.parse(list.get(i).getImageUrl());


        holder.title.setText(list.get(i).getProductNames());




        holder.category.setText(list.get(i).getProductCategory());

        Picasso.get().load(img).into(holder.productImage);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewProduct.class);
                intent.putExtra("selected_product", list.get(i));

                mContext.startActivity(intent);
            }
        });

//        holder.desc.setText(list.get(i).getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class  searchViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView category,title,price1,price2,percentage,desc;
        CardView parentLayout;

        public searchViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.search_result_image);
            category = view.findViewById(R.id.search_result_item_category);
            title = view.findViewById(R.id.search_result_item_title);
            price1 = view.findViewById(R.id.search_result_price1);
            price2 = view.findViewById(R.id.search_result_price2);
            percentage   = view.findViewById(R.id.discount_percentage);
//            desc = view.findViewById(R.id.search_result_item_desc);
            parentLayout = view.findViewById(R.id.search_result_card);

        }
    }
}
