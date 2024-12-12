package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class Adapter_week extends RecyclerView.Adapter<Adapter_week.ViewHolder> {

    public ArrayList<Item_day> 날짜목록;
    String TAG = "Week_Adapter";
    public OnItemClickListener mListener;
    String 날짜데이터;

    SparseBooleanArray 체크여부 = new SparseBooleanArray(7);

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    public Adapter_week(ArrayList<Item_day> 받은날짜목록) {
        this.날짜목록 = 받은날짜목록;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_week, parent, false);    // 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        Adapter_week.ViewHolder vh = new Adapter_week.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int safeposition= holder.getAdapterPosition();
        String 날짜 = 날짜목록.get(safeposition).getDaynum();//오늘 몇일인지 나타내줌
        String 요일 = 날짜목록.get(safeposition).getDayname();
        holder.date.bringToFront();
        holder.date.setText(날짜);
        holder.date.setTextColor(Color.WHITE);
        holder.dayname.setText(요일);
        if (safeposition == 5) {
            holder.image.setImageResource(R.drawable.bluecircle);

        }
        if (safeposition == 6) {
            holder.image.setImageResource(R.drawable.redcircle);
        }
        if (날짜목록.get(safeposition).checked == false) {
            holder.itemView.setBackgroundResource(R.drawable.whiteback);

        } else {
            holder.itemView.setBackgroundResource(R.drawable.edge);
        }


    }//여기에 날짜 기준 만들어서 추가 해야 된다.


    @Override
    public int getItemCount() {
        return 날짜목록.size();
    }//그려줄 뷰의 갯수를 리턴해줌


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView dayname;
        ImageView image;


        ViewHolder(View itemView) {//뷰홀더는 그냥 뷰와 클래스의 객체를 연결하는 역할만 한다.
            super(itemView);
            //뷰홀더에 필요한 아이템데이터 findview
            date = itemView.findViewById(R.id.주일자텍스트뷰);//아이템에 들어갈 텍스트
            image = itemView.findViewById(R.id.배경동그라미);//아이템에 들어갈 텍스트
            dayname = itemView.findViewById(R.id.요일텍스트뷰);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if(mListener !=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });


        }
    }//커스텀 뷰홀더

    //    itemView.setBackgroundResource(R.drawable.edge);
    @Override
    public int getItemViewType(int position) {
        return position;
    }


}