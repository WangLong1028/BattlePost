package com.shuo.ba.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.shuo.ba.R;
import com.shuo.ba.databinding.DialogMsgEditBinding;
import com.shuo.ba.utils.SystemUtils;
import com.shuo.ba.viewmodels.DialogMsgViewModel;
import com.shuo.chatmodule.beans.UserBean;

import java.util.Objects;

public class EditMsgDialogFragmentHelper {

    public static EditMsgDialogFragment getDialog(DialogMsgViewModel.GetSendData getSendData, UserBean userBean) {
        return new EditMsgDialogFragment(getSendData, userBean);
    }

    public static class EditMsgDialogFragment extends DialogFragment {

        private DialogMsgViewModel viewModel;
        private DialogMsgViewModel.GetSendData getSendData;

        private UserBean userBean;


        private EditMsgDialogFragment(DialogMsgViewModel.GetSendData getSendData, UserBean userBean) {
            this.getSendData = getSendData;
            this.userBean = userBean;
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                Window window = dialog.getWindow();
                if (window != null) {
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.MATCH_PARENT;
                    window.setLayout(width, height);
                }
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.EditMsgTheme);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes().windowAnimations = R.style.BottomDialogAnimation;
            Objects.requireNonNull(getDialog().getWindow()).getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            DialogMsgEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_msg_edit, container, false);
            viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DialogMsgViewModel.class);
            binding.setEditMsg(viewModel);
            binding.setLifecycleOwner(getActivity());

            viewModel.setDialog(this);
            viewModel.setGetSendData(getSendData);

            viewModel.setIvHeadshot(binding.ivHeader, userBean);

            int statusBarHeight = SystemUtils.getStatusBarHeight(Objects.requireNonNull(getContext()));
            binding.llRoot.setTranslationY(statusBarHeight);


            return binding.getRoot();
        }

        @Override
        public void onDismiss(@NonNull DialogInterface dialog) {
            super.onDismiss(dialog);
            if (viewModel != null) {
                viewModel.clearData();
            }
        }
    }
}
