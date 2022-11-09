package com.mohammedalmajhali.aladhkar;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    ArrayList<Dekr> data;
    int sizeOfList;
    DialogFragment dialogFragment;


    public MyRecyclerAdapter(ArrayList<Dekr> data, DialogFragment dialogFragment) {
        this.data = new ArrayList<>();
        this.data = data;
        this.sizeOfList = data.size();
        this.dialogFragment = dialogFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dhkr, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Dekr d = data.get(position);


        // طبعا به معي مصفوفة static في كلاس ال sections ال index حقها هو الـ id حق العنصر والقيمه حقها هي عدد الضغطات
        // في الحدث كلما اضغط ضغطه يشل الـ id حق العنصر ويزيد 1 في موقعه على المصفوفة
        // يعني لو قد ضغطت على هذا العنصر رقم 3 ، ثمان ضغطات يكون قيمته 8
        // الان اول ما ارسم العنصر اشل عدد التكرار حقه وانقص منه عدد الضغطات اللي قد ضغطها اثنا ما الاكتفتي sections مفتوحه
        holder.countOFDhekr.setText(String.valueOf(d.getNumber() - SectionsActivity.counterClicked[d.getId()]));
        holder.textDhekr.setText(d.getDhekrText());
        holder.txt_virtue_of_dhekr.setText(d.getVirtueOfDhekrText().toString());


        // هذا الحدث اذا ضغط على الذكر
        holder.textDhekr.setOnClickListener(new View.OnClickListener() {

            // ارتفاع العنصر
            private int originalHeight = 0;

            @Override
            public void onClick(View view) {

                // لزيادة الامان اتأكد ان عدد الضغطات اللي مسجله هناك اصغر من العدد الفعلي لتكرار الذكر هذا
                if (SectionsActivity.counterClicked[d.getId()] < d.getNumber())
                    // ازيد في الذكر هذا ضغطة
                    SectionsActivity.counterClicked[d.getId()] += 1;


                // يصنع اهتزاز بسيط بمقدار 20
                Vibrator vibe = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);
                //*****************************

                // نجيب العدد الذي في الذكر وننقصه ونرسمه في العنصر ونشوف اذا كان اكبر من صفر يخرج من الداله
                // اما اذا كان صفر فيسوي الانميشن الذي في باقي الاسطر
                int count = Integer.parseInt(holder.countOFDhekr.getText().toString());
                count--;
                holder.countOFDhekr.setText(String.valueOf(count));

                if (Integer.parseInt(holder.countOFDhekr.getText().toString()) > 0) {
                    return;
                }


                // ناخذ ارتفاع العنصر
                if (originalHeight == 0) {
                    originalHeight = view.getHeight();
                }


                // Declare a ValueAnimator object
                // هذا عشان يرجع باقي العناصر في مكان هذا
                // يعني يسوي لهن shift للاعلى
                ValueAnimator valueAnimator;
                {
                    // كم مقدار الـ shift الذي يسوي لهن
                    valueAnimator = ValueAnimator.ofInt((int) (originalHeight), 0);


                    // نعرف الانميشن الذي يطلع العنصر الى اليسار
                    Animation a = AnimationUtils.loadAnimation(view.getContext(), R.anim.move_anim); // Fade out

                    a.setDuration(4000);
                    // Set a listener to the animation and configure onAnimationEnd
                    // حدث لما ينتهي الانميشن يلغي العنصر
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                            // كان من قبل لما الانميشن بيتنفذ يعني والعنصر جالس يختفي اذا المستخدم ضغط عليه وهو بيختفي يعلق الفراقمنت ويطلع منه
                            // الان سويت اثناء ما الانميشن بيتطبق يعني والعنصر بيختفي نلغي اي ضغطه للعنصر
                            holder.itemView.setOnClickListener(v->{});
                            holder.textDhekr.setOnClickListener(v->{});
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.itemView.setEnabled(false);
                            holder.itemView.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    // Set the animation on the custom view
                    // نبدأ الانميشن هذا اول شي ثم تحت نسوي الانميشن حق الـ shift
                    holder.itemView.startAnimation(a);

                }
                // تجهيزات للانميشن حق ال shift
                valueAnimator.setDuration(400);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                // حدث اول ما يتحدث الانميشن يشل ارتفاع ال recycler ويعدلها
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        holder.itemView.getLayoutParams().height = value.intValue();
                        holder.itemView.requestLayout();
                    }
                });
                // ابدا الانميشن حق الـ shift
                valueAnimator.start();


                // حجم الليست يعني المصفوفة حق الاذكار ننقص هذا الذكر لانه خلاص قد خلص يقراه
                sizeOfList = sizeOfList - 1;

                // اذا كانت صفر : ينهي الـ fragment ويمسح المصفوفة بعد نص ثانية
                if (sizeOfList == 0) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Write whatever to want to do after delay specified (1 sec)
                            data.clear();
                            dialogFragment.dismiss();
                        }
                    }, 500);
                }

            }
        });


        // لما اضغط على زر فضل الذكر
        holder.btn_virtue_of_dhekr.setOnClickListener(new View.OnClickListener() {

            private int originalHeight = 0;


            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), "سوف يتم اضافته قريباً", Toast.LENGTH_SHORT).show();

                // ناخذ ارتفاع عنصر فضل الذكر
                if (originalHeight == 0) {
                    originalHeight = holder.textDhekr.getHeight();
                }

                // Declare a ValueAnimator object
                ValueAnimator valueAnimator;
                // اذا كان مخفي نظهرة
                if (!holder.isViewVirtueExpanded) {
                    holder.txt_virtue_of_dhekr.setVisibility(View.VISIBLE);
                    holder.txt_virtue_of_dhekr.setEnabled(true);
                    // نقول ان العنصر ظاهر
                    holder.isViewVirtueExpanded = true;
                    // التمدد يكون الانميشن من صفر الى ارتفاع العنصر
                    valueAnimator = ValueAnimator.ofInt(0, originalHeight); // These values in this method can be changed to expand however much you like
                } else {
                    // نقول ان العنصر مخفي
                    holder.isViewVirtueExpanded = false;
                    // نخلي الانميشن من ارتفاع العنصر الى صفر
                    valueAnimator = ValueAnimator.ofInt(originalHeight, 0);

                    // ********************** انميشن الاختفاء ************
                    Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                    a.setDuration(200);
                    // Set a listener to the animation and configure onAnimationEnd
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.txt_virtue_of_dhekr.setVisibility(View.INVISIBLE);
                            holder.txt_virtue_of_dhekr.setEnabled(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    // Set the animation on the custom view
                    holder.txt_virtue_of_dhekr.startAnimation(a);

                    // ************************* نهاية انميشن الاختفاء *****************
                }

                // انميشن الطلوع والنزول
                valueAnimator.setDuration(200);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        holder.txt_virtue_of_dhekr.getLayoutParams().height = value.intValue();
                        holder.txt_virtue_of_dhekr.requestLayout();
                    }
                });

                // بداية انميشن الطلوع والنزول
                valueAnimator.start();
            }
        });


        holder.txt_virtue_of_dhekr.setOnClickListener(v -> { });



        // اذا ضغط على الذكر ضغطة طويله ينسخة
        holder.textDhekr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) dialogFragment.requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("mohammed", (((TextView)v).getText().toString()) + dialogFragment.getString(R.string.download_this_app));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(dialogFragment.getContext(),"تم نسخ النص", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        // اذا ضغط على فضل الذكر ضغطة طويله ينسخة
        holder.txt_virtue_of_dhekr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) dialogFragment.requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("mohammed", (((TextView)v).getText().toString()) + dialogFragment.getString( R.string.download_this_app));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(dialogFragment.getContext(),"تم نسخ النص", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        // هل فضل الذكر ظاهر او لا
        boolean isViewVirtueExpanded = false;

        TextView textDhekr, countOFDhekr, btn_virtue_of_dhekr, txt_virtue_of_dhekr;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            countOFDhekr = itemView.findViewById(R.id.counter_of_dhekr);
            textDhekr = itemView.findViewById(R.id.text_dhekr);
            btn_virtue_of_dhekr = itemView.findViewById(R.id.btn_virtue_of_dhekr);
            txt_virtue_of_dhekr = itemView.findViewById(R.id.virtue_of_dhekr);






            // في اول مرة نخفي فضل الذكر حق العنصر هذا
            if (isViewVirtueExpanded == false) {
                // Set Views to View.GONE and .setEnabled(false)
                txt_virtue_of_dhekr.setVisibility(View.GONE);
                txt_virtue_of_dhekr.setEnabled(false);
            }

        }

    }

}