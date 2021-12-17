package com.BagusJmartMH;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.BagusJmartMH.model.Payment;
import com.BagusJmartMH.model.Product;
import com.BagusJmartMH.request.RequestFactory;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvoiceAccAdapt extends RecyclerView.Adapter<InvoiceAccAdapt.CardViewHolder> {
    private ArrayList<Payment> listP = new ArrayList<>();
    private Product product;
    private static final Gson gson = new Gson();

    public InvoiceAccAdapt(ArrayList<Payment> list){
        this.listP = list;
    }
    @NonNull
    @Override
    public InvoiceAccAdapt.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_adapt_acc, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAccAdapt.CardViewHolder holder, int position) {
        Payment payment = listP.get(position);
        Payment.Record lastRec = payment.history.get(payment.history.size() - 1);
        getProductData(holder, payment);
        holder.tvIdOrder.setText("" + (position + 1));
        holder.tvStatus.setText(lastRec.status.toString());
        holder.tvDate.setText(lastRec.date.toString());
        holder.tvAddress.setText(payment.shipment.address);
    }

    @Override
    public int getItemCount() {
        return listP.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameProduct, tvStatus, tvDate, tvAddress, tvPrice, tvIdOrder;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.nama_produk3);
            tvAddress = itemView.findViewById(R.id.address_beli3);
            tvDate = itemView.findViewById(R.id.date_ship3);
            tvPrice = itemView.findViewById(R.id.totalPrice3);
            tvStatus = itemView.findViewById(R.id.status_ship3);
            tvIdOrder = itemView.findViewById(R.id.id_order);
        }
    }

    public void getProductData (InvoiceAccAdapt.CardViewHolder holder, Payment payment){
        Response.Listener<String> ListenerProduct = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    product = gson.fromJson(response, Product.class);
                    holder.tvNameProduct.setText(product.name);
//                    holder.tvPrice.setText("Rp " + (product.price * payment.productCount * (product.discount/100)));
                    holder.tvPrice.setText("Rp " + (product.price));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.tvNameProduct.getContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(holder.tvNameProduct.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, ListenerProduct, errorListener));
    }
}