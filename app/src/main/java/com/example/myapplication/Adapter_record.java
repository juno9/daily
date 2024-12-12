package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_record extends RecyclerView.Adapter<Adapter_record.ViewHolder> {
    ArrayList<Item_record> 기록리스트;

    OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mlistener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.item_record, parent, false);    // 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        Adapter_record.ViewHolder vh = new Adapter_record.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String 제목 = 기록리스트.get(position).get제목();

        if (제목.equals("")) {
            holder.title.setText("빈 제목");
            holder.title.setTextColor(Color.GRAY);
        } else {
            holder.title.setText(제목);
            holder.title.setTextColor(Color.BLACK);
        }

        SimpleDateFormat 시간형식 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // 일시 문자열을 읽고 출력하는 실습
        try {
            String 시작날짜시간 = 기록리스트.get(position).get시작날짜() + " " + 기록리스트.get(position).get시작시간();
            String 종료날짜시간 = 기록리스트.get(position).get시작날짜() + " " + 기록리스트.get(position).get종료시간();

            Date 시작날짜시간date = 시간형식.parse(시작날짜시간); // 문자열을 파싱해 Date형으로 저장한다
            Date 종료날짜시간date = 시간형식.parse(종료날짜시간);
            SimpleDateFormat 시간 = new SimpleDateFormat("aa hh:mm");
            String 시작시간만따로 = 시간.format(시작날짜시간date);
            String 종료시간만따로 = 시간.format(종료날짜시간date);


            holder.time.setText(시작시간만따로);
            holder.starttime.setText(시작시간만따로);

            holder.endtime.setText(종료시간만따로);


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }//묶어줄 데이터가 없다.


    @Override
    public int getItemCount() {
        return 기록리스트.size();//저장된 일정 갯수만큼 뿌리기
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView title;
        TextView starttime;
        TextView endtime;


        ViewHolder(View itemView) {
            super(itemView);
            //뷰홀더에 필요한 아이템데이터 findview
            time = itemView.findViewById(R.id.시간텍스트뷰);//기록 아이템에 들어갈 텍스트
            title = itemView.findViewById(R.id.내용텍스트뷰);//기록 아이템에 들어갈 텍스트
            starttime = itemView.findViewById(R.id.시작시간);
            endtime = itemView.findViewById(R.id.종료시간);
            //기록 아이템에 들어갈 텍스트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mlistener != null) {
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }//커스텀 뷰홀더

    public Adapter_record(ArrayList<Item_record> 기록리스트) {
        this.기록리스트 = 기록리스트;

    }

    public void setarraylist(ArrayList<Item_record> 기록리스트) {
        this.기록리스트 = 기록리스트;
    }
}