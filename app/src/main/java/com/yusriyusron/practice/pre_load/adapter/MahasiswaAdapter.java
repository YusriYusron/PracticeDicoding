package com.yusriyusron.practice.pre_load.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.pre_load.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder> {

    private ArrayList<MahasiswaModel> mahasiswaModels = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public MahasiswaAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MahasiswaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mahasiswa_row,viewGroup,false);
        return new MahasiswaHolder(view);
    }

    public void addItem(ArrayList<MahasiswaModel> mahasiswaModels){
        this.mahasiswaModels = mahasiswaModels;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaHolder mahasiswaHolder, int i) {
        mahasiswaHolder.textNim.setText(mahasiswaModels.get(i).getNim());
        mahasiswaHolder.textNama.setText(mahasiswaModels.get(i).getName());
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mahasiswaModels.size();
    }

    public class MahasiswaHolder extends RecyclerView.ViewHolder {
        private TextView textNama,textNim;
        public MahasiswaHolder(@NonNull View itemView) {
            super(itemView);
            textNim = itemView.findViewById(R.id.tv_nim);
            textNama = itemView.findViewById(R.id.tv_nama);
        }
    }
}
