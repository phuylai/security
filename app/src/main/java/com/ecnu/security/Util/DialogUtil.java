package com.ecnu.security.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.common.design.MaterialDialog;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.Model.TrustedContact;
import com.ecnu.security.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.ecnu.security.Util.ResourceUtil.getString;

/**
 * Created by Phuylai on 2017/5/5.
 */

public class DialogUtil {
    public static void showDeviceDetailDialog(Context context, DeviceModel deviceModel,
                                              DialogListener dialogListener){
        final DialogListener listener = dialogListener;
        View view = View.inflate(context, R.layout.device_detail,null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvId = (TextView) view.findViewById(R.id.tv_id);
        TextView tvSub = (TextView) view.findViewById(R.id.tv_sub);
        TextView tvMac = (TextView) view.findViewById(R.id.tv_mac);
        TextView tvRole = (TextView) view.findViewById(R.id.tv_role);
        tvName.setText(deviceModel.getName());
        tvId.setText(deviceModel.getDevId());
        tvSub.setText(deviceModel.getIsSub());
        tvMac.setText(deviceModel.getMac());
        String role = "";
        switch (Integer.parseInt(deviceModel.getRole())){
            case 1:
                role = getString(R.string.role_1);
                break;
            case 2:
                role = getString(R.string.role_2);
                break;
            case 3:
                role = getString(R.string.role_3);
                break;
        }
        tvRole.setText(role);
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.device_edit), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.yes();
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.no();
                        return false;
                    }
                }).show();

    }

    public static void deviceListDialog(final Context context, final List<DeviceModel> models,
                                        final int id, final DeviceListener listener){
        final int[] position = {0};
        View view = View.inflate(context,R.layout.device_list_layout,null);
        ArrayList<String> str = new ArrayList<>();
        for(DeviceModel model:models){
            str.add(model.getName());
        }
        final ListViewCompat listViewCompat = (ListViewCompat) view.findViewById(R.id.listview);
        listViewCompat.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_single_choice
                ,str));
        listViewCompat.setChoiceMode(ListViewCompat.CHOICE_MODE_SINGLE);
        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position[0] = i;
            }
        });
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setCanceledOnTouchOutside(false)
                .setPositiveButton(getString(R.string.yes), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null){
                            listener.select(models.get(position[0]),id);
                        }
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null){
                            listener.cancel();
                        }
                        return false;
                    }
                }).show();
    }

    public static void doneDialog(final Context context){
        if(context == null)
            return;
        View view = View.inflate(context,R.layout.image_layout,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        imageView.setImageResource(R.drawable.ic_done);
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        return false;
                    }
                })
                .show();
    }

    public static void callContactDialog(final Context context, final String phone, final CallDialog callDialog){
        View view = View.inflate(context,R.layout.image_layout,null);
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.call), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        callDialog.callPhone(phone);
                        return false;
                    }
                })
                .setNeutralButton(getString(R.string.sos), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        callDialog.setSOS(phone);
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
            @Override
            public boolean onClick(DialogInterface dialog, int which) {
                return false;
            }
        }).show();
    }

    public static void addContactDialog(final Context context, final DialogContactListener listener){
        View view = View.inflate(context,R.layout.add_contact_layout,null);
        final EditText et_name = (EditText) view.findViewById(R.id.et_name);
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        final String[] name = {null} ;
        final String[] phone = {null};
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.add), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        name[0] = et_name.getText().toString().trim();
                        phone[0] = et_phone.getText().toString().trim();
                        if(StringUtil.isNull(name[0]) || StringUtil.isNull(phone[0])){
                            ToastUtil.showToastShort(context,R.string.fail_empty);
                            return false;
                        }
                        if( listener != null)
                            listener.yes(new TrustedContact(phone[0],name[0]));
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.no();
                        return false;
                    }
                }).show();
    }

    public static void EditContactDialog(final Context context, TrustedContact contact,
                                         final int position, final EditContactListener listener){
        View view = View.inflate(context,R.layout.add_contact_layout,null);
        final EditText et_name = (EditText) view.findViewById(R.id.et_name);
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(getString(R.string.device_edit));
        et_name.setText(contact.getName());
        et_phone.setText(contact.getPhonenumber());
        final String[] name = {et_name.getText().toString().trim()};
        final String[] phone = {et_phone.getText().toString().trim()};
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.device_edit), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        name[0] = et_name.getText().toString().trim();
                        phone[0] = et_phone.getText().toString().trim();
                        if(StringUtil.isNull(name[0]) || StringUtil.isNull(phone[0])){
                            ToastUtil.showToastShort(context,R.string.fail_empty);
                            return false;
                        }
                        if( listener != null)
                            listener.yes(new TrustedContact(phone[0],name[0]),position);
                        return false;
                    }
                })
                .setNeutralButton(getString(R.string.delete), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.delete(position);
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        return false;
                    }
                }).show();
    }

    public static void showSpinnerBarDialog(Context context,final ActionType actionType,
                                            String defValue,final DialogDataListener dialogDataListener){
        View view = View.inflate(context,R.layout.spinner_layout,null);
        AppCompatSpinner spinner = (AppCompatSpinner) view.findViewById(R.id.sp_redirect);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        final TextView value = (TextView) view.findViewById(R.id.tv_volume);
        String text = String.format(getString(R.string.redirect_min),"5");
        final String[] editValue = {defValue};
        if(!StringUtil.isNull(defValue))
            text = String.format(getString(R.string.redirect_min),defValue);
        value.setText(text);
        title.setText(getString(R.string.redirect));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(dialogDataListener != null)
                    editValue[0] = adapterView.getItemAtPosition(i).toString();
                    value.setText(String.format(getString(R.string.redirect_min),
                            adapterView.getItemAtPosition(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.yes), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(dialogDataListener != null)
                            dialogDataListener.yes(editValue[0],actionType);
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(dialogDataListener != null)
                            dialogDataListener.no();
                        return false;
                    }
                }).show();

    }

    public static void showSwitchBarDialog(Context context,final ActionType actionType,
                                            String defValue,final DialogDataListener dialogDataListener){
        View view = View.inflate(context,R.layout.switch_layout,null);
        final SwitchCompat sw = (SwitchCompat) view.findViewById(R.id.sw_noti);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        final TextView value = (TextView) view.findViewById(R.id.tv_volume);
        String text = String.format(getString(R.string.noti_on_off),getString(R.string.on));
        if(!StringUtil.isNull(defValue)) {
            text = String.format(getString(R.string.noti_on_off), defValue);
            if(defValue.equals("On")){
                sw.setChecked(true);
            }else{
                sw.setChecked(false);
            }
        }
        value.setText(text);
        title.setText(getString(R.string.noti));
        final String[] editValue = {defValue};
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw.isChecked()){
                    editValue[0] = "on";
                    value.setText(String.format(getString(R.string.noti_on_off),getString(R.string.on)));
                }else{
                    editValue[0] = "off";
                    value.setText(String.format(getString(R.string.noti_on_off),getString(R.string.off)));
                }
            }
        });

        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.yes), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(dialogDataListener != null)
                            dialogDataListener.yes(editValue[0],actionType);
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(dialogDataListener != null)
                            dialogDataListener.no();
                        return false;
                    }
                }).show();

    }

    public static void showSeekBarDialog(Context context, final ActionType actionType,String defValue,
                                         final DialogDataListener dialogListener){
        View view = View.inflate(context,R.layout.seekbar_layout,null);
        final TextView value = (TextView) view.findViewById(R.id.tv_volume);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        AppCompatSeekBar seekBar = (AppCompatSeekBar) view.findViewById(R.id.s1);
        final String[] editValue = {defValue};
        String text = String.format(getString(R.string.value),"5");
        if(!StringUtil.isNull(editValue[0])){
            text = String.format(getString(R.string.value),defValue);
            seekBar.setProgress(Integer.parseInt(editValue[0]));
        }
        value.setText(text);
        switch (actionType){
            case ALARM:
                title.setText(getString(R.string.danger_volume));
                break;
            case LED:
                title.setText(getString(R.string.led_speed));
                break;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editValue[0] = String.valueOf(i);
                value.setText(String.format(getString(R.string.value),i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new MaterialDialog.Builder(context)
                .setContentView(view)
                .setPositiveButton(getString(R.string.yes), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(dialogListener != null)
                            dialogListener.yes(editValue[0],actionType);
                        return false;
                    }
                })
                .setNegativeButton(getString(R.string.back), new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(dialogListener != null)
                            dialogListener.no();
                        return false;
                    }
                }).show();
    }


    public static void showDialog(Context context, int title, int message,DialogListener dialogListener){
        final DialogListener listener = dialogListener;
        new MaterialDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.yes();
                        return false;
                    }
                })
                .setNegativeButton(R.string.no, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int which) {
                        if(listener != null)
                            listener.no();
                        return false;
                    }
                }).show();
    }


    public interface CallDialog{
        void callPhone(String phone);
        void setSOS(String phone);
    }
    public interface DialogListener{
        void yes();
        void no();
    }

    public interface DialogDataListener{
        void yes(String number,ActionType actionType);
        void no();
    }

    public interface DialogContactListener{
        void yes(TrustedContact contact);
        void no();
    }

    public interface EditContactListener{
        void yes(TrustedContact contact,int position);
        void delete(int position);
    }

    public interface DeviceListener{
        void select(DeviceModel deviceModel,int id);
        void cancel();
    }

}
