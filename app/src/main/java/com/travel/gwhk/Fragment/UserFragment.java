package com.travel.gwhk.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.gwhk.LoginActivity;
import com.travel.gwhk.MainActivity;
import com.travel.gwhk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private Button login;
    private Button btn_sign_out;
    private Button btn_register;
    ListView v;
    FirebaseUser user;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setTitle("我的帳戶");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
/*

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getActivity(), Sign_Up.class);
                startActivity(intent3);

            }
        });


        if (user!=null)
        {
            login.setEnabled(false);
            btn_sign_out.setEnabled(true);
        }
        else
        {
            login.setEnabled(true);
            btn_sign_out.setEnabled(false);
        }

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent intent2 = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent2);
                                btn_sign_out.setEnabled(false);
                                //showSigninOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
*/
        final String[] strGroup;

        if (user!=null)
        {
            strGroup = new String[]{"登出", "關於我們"};
        }
        else
        {
            strGroup = new String[]{"登入", "關於我們"};
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                strGroup);

        v=(ListView)view.findViewById(R.id.list_view);
        v.setAdapter(adapter);
        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                // 利用索引值取得點擊的項目內容。
                String text = strGroup[index];
                // 整理要顯示的文字。
                //String result = "索引值: " + index + "\n" + "內容: " + text;
                // 顯示。
                //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                if (text=="登入"){
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                }
                else if(text=="登出"){
                    AuthUI.getInstance().signOut(getActivity())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Intent intent2 = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent2);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        return view;
    }
}
