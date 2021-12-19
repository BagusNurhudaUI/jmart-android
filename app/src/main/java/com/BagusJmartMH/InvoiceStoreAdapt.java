package com.BagusJmartMH;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.BagusJmartMH.model.Invoice;
import com.BagusJmartMH.model.Payment;
import com.BagusJmartMH.model.Product;
import com.BagusJmartMH.request.AccPaymentRequest;
import com.BagusJmartMH.request.DeclinePaymentRequest;
import com.BagusJmartMH.request.RequestFactory;
import com.BagusJmartMH.request.SubmitPaymentRequest;
import com.BagusJmartMH.request.TopupRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * merupakan function untuk adapter dari tablayout store di invoice activity
 */
public class InvoiceStoreAdapt extends RecyclerView.Adapter<InvoiceStoreAdapt.CardViewStoreHolder> {
    private ArrayList<Payment> listP = new ArrayList<>();
    private Product product;
    private static final Gson gson = new Gson();
    double discount;
    Payment.Record lastRec;
    Dialog dialog;


    public InvoiceStoreAdapt(ArrayList<Payment> list) {
        this.listP = list;
    }

    /**
     * mengambil informasi cardview dari xml khusus untuk invoice adaptor
     */
    @NonNull
    @Override
    public InvoiceStoreAdapt.CardViewStoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_adapt_store, parent, false);
        return new CardViewStoreHolder(view);
    }

    /**
     * merupakan function untuk inisialisasi model pada cardview
     */
    public class CardViewStoreHolder extends RecyclerView.ViewHolder {
        TextView ISName, ISStatus, ISDate, ISAddress, ISHarga, ISOrderId;
        Button acceptBtn, declineBtn, receiptBtn;
        LinearLayout storeDeclineAcc;
        public CardViewStoreHolder(@NonNull View itemView) {
            super(itemView);
            ISName = itemView.findViewById(R.id.nama_produk4);
            ISStatus = itemView.findViewById(R.id.status_ship4);
            ISDate = itemView.findViewById(R.id.date_ship4);
            ISAddress = itemView.findViewById(R.id.address_beli4);
            ISHarga = itemView.findViewById(R.id.totalPrice4);
            ISOrderId = itemView.findViewById(R.id.id_order4);
            acceptBtn = itemView.findViewById(R.id.accept);
            declineBtn = itemView.findViewById(R.id.decline);
            receiptBtn = itemView.findViewById(R.id.input_receipt);
            storeDeclineAcc = itemView.findViewById(R.id.decline_acc_LL);
        }
    }

    /**
     * merupakan function untuk inisialisasi model pada cardview
     * @param holder merupakan parameter yang digunakan
     * @param position posisi untuk menampilkan cardview lainnya
     */
    @Override
    public void onBindViewHolder(@NonNull CardViewStoreHolder holder, int position) {
        Payment payment = listP.get(position);
        lastRec = payment.history.get(payment.history.size() - 1);
        getProductData(holder, payment);
        holder.ISStatus.setText(lastRec.status.toString());
        holder.ISDate.setText(lastRec.date.toString());
        holder.ISAddress.setText(payment.shipment.address);
        holder.ISOrderId.setText("" + (position + 1));

        if (lastRec.status.equals(Invoice.Status.WAITING_CONFIRMATION)) {
            holder.storeDeclineAcc.setVisibility(View.VISIBLE);
        } else {
            holder.storeDeclineAcc.setVisibility(View.GONE);
        }

        if (lastRec.status.equals(Invoice.Status.ON_PROGRESS)) {
            holder.receiptBtn.setVisibility(View.VISIBLE);
        } else {
            holder.receiptBtn.setVisibility(View.GONE);
        }

        /**
         * click listener untuk mengambil data dari accept button
         * jika diterima, maka akan mensubmit payment yang berhasil
         */
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> listenerAcceptPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);
                        if (isAccepted) {
                            Toast.makeText(holder.ISName.getContext(), "Payment telah diterima!", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.ON_PROGRESS, "Payment Accepted!"));
                            payment.history.add(new Payment.Record(Invoice.Status.ON_DELIVERY,"Payment Submited"));
                            lastRec = payment.history.get(payment.history.size() - 1);
                            holder.ISStatus.setText(lastRec.status.toString());
                            holder.storeDeclineAcc.setVisibility(View.GONE);
                            holder.receiptBtn.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(holder.ISName.getContext(), "payment gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerAcceptPayment = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.ISName.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                };
                AccPaymentRequest acceptPaymentRequest = new AccPaymentRequest(payment.id, listenerAcceptPayment, errorListenerAcceptPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.ISName.getContext());
                queue.add(acceptPaymentRequest);
            }
        });

        /**
         * click listener untuk mengambil data dari decline button
         * jika di klik akan mendecline order dan akan mengmbalikkan balance
         */
        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> listenerCancelPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);
                        if (isAccepted) {
                            double price = Double.valueOf(holder.ISHarga.getText().toString().trim().substring(3));
                            Response.Listener<String> listener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Boolean object = Boolean.valueOf(response);
                                    if (object) {
                                        Toast.makeText(holder.ISName.getContext(), "balance berhasil dikembalikan!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.ISName.getContext(), "Balance sudah diambil!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            Response.ErrorListener errorListener = new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(holder.ISName.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            };
                            TopupRequest topUpRequest = new TopupRequest(payment.buyerId, price, listener, errorListener);
                            RequestQueue queue = Volley.newRequestQueue(holder.ISName.getContext());
                            queue.add(topUpRequest);
                            Toast.makeText(holder.ISName.getContext(), "Payment telah dicancel!", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.CANCELLED, "Payment is cancelled!"));
                            lastRec = payment.history.get(payment.history.size() - 1);
                            holder.ISStatus.setText(lastRec.status.toString());
                            holder.storeDeclineAcc.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(holder.ISName.getContext(), "payment gagal dibatalkan !", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerCancelPayment = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.ISName.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                };
                DeclinePaymentRequest declinePaymentRequest = new DeclinePaymentRequest(listenerCancelPayment, payment.id, errorListenerCancelPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.ISName.getContext());
                queue.add(declinePaymentRequest);
            }
        });

        /**
         * digunakan untuk mengambil data dari receipt button
         * akan mengisi nomor resi pada payment
         */
        holder.receiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(holder.acceptBtn.getContext());
                dialog.setContentView(R.layout.input_resi);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText isiResi = dialog.findViewById(R.id.input_receipt_text);
                Button submitButton = dialog.findViewById(R.id.submit_btn);
                dialog.show();

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String noReceipt = isiResi.getText().toString().trim();
                        Response.Listener<String> listenerSubmit = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Boolean isSubmit = Boolean.valueOf(response);
                                if (isSubmit) {
                                    Toast.makeText(holder.ISName.getContext(), "Payment has been submitted!", Toast.LENGTH_SHORT).show();
                                    payment.history.add(new Payment.Record(Invoice.Status.ON_DELIVERY, "Payment has been Submitted!"));
                                    lastRec = payment.history.get(payment.history.size() - 1);
                                    holder.ISStatus.setText(lastRec.status.toString());
                                    holder.receiptBtn.setVisibility(view.GONE);
                                } else {
                                    Toast.makeText(holder.ISName.getContext(), "Payment not submitted!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        Response.ErrorListener errorListenerSubmit = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(holder.ISName.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        };
                        SubmitPaymentRequest submitPaymentRequest = new SubmitPaymentRequest(payment.id, noReceipt, listenerSubmit, errorListenerSubmit);
                        RequestQueue queue = Volley.newRequestQueue(holder.ISName.getContext());
                        queue.add(submitPaymentRequest);
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    /**
     * mengambil data list dari item
     * @return size dari list
     */
    @Override
    public int getItemCount() {
        return listP.size();
    }

    /**
     * function untuk membuat data payment dan setText
     * @param holder untuk membuat data yang telah di inisialisasi
     * @param payment paramteer yang diambil dari model Payment
     */
    public void getProductData(InvoiceStoreAdapt.CardViewStoreHolder holder, Payment payment) {
        Response.Listener<String> ListenerProduct = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    product = gson.fromJson(response, Product.class);
                    holder.ISName.setText(product.name);
//                    holder.ISHarga.setText("Rp " + ((product.price - (product.price * (product.discount / (100)))) * payment.productCount));
                    holder.ISHarga.setText("Rp " + (product.price));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.ISName.getContext(), "ERROR!", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(holder.ISName.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, ListenerProduct, errorListener));
    }

}

