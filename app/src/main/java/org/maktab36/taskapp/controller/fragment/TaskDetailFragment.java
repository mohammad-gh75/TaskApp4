package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.controller.activity.TabViewPagerActivity;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.TaskState;
import org.maktab36.taskapp.repository.TaskRepository;
import org.maktab36.taskapp.repository.UserRepository;
import org.maktab36.taskapp.util.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TaskDetailFragment extends DialogFragment {
    public static final String ARG_TASK_ID = "TaskId";
    public static final String DIALOG_FRAGMENT_TAG = "Dialog";
    public static final String BUNDLE_VIEWS_ENABLED = "ViewsEnabled";
    public static final int DATE_PICKER_REQUEST_CODE = 0;
    public static final int TIME_PICKER_REQUEST_CODE = 1;
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 2;
    private static final int PICK_PHOTO_REQUEST_CODE = 3;
    public static final String FILEPROVIDER_AUTHORITY = "org.maktab36.taskapp.fileprovider";
    private Task mCurrentTask;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonDate;
    private Button mButtonTime;
    private RadioGroup mRadioGroupStates;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private Button mButtonSave;
    private Button mButtonCancel;
    private ImageButton mButtonShare;
    private SimpleDateFormat mDateFormatter;
    private SimpleDateFormat mTimeFormatter;
    private SimpleDateFormat mDateTimeFormatter;
    private TaskRepository mTaskRepository;
    private UserRepository mUserRepository;
    private boolean mButtonVisibility;
    private boolean mViewsEnabled;
    private ImageView mImageViewTaskPhoto;
    private ImageButton mButtonTakePhoto;
    private File mPhotoFile;
    private ViewTreeObserver mImageViewObserver;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    public static TaskDetailFragment newInstance(UUID taskId) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID,taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskRepository =TaskRepository.getInstance(getActivity());
        mUserRepository=UserRepository.getInstance(getActivity());

        UUID taskId= (UUID) getArguments().getSerializable(ARG_TASK_ID);
        if(taskId!=null) {
            mCurrentTask = mTaskRepository.get(mUserRepository.getCurrentUser().getUUID(),taskId);
            mButtonVisibility=false;
            mViewsEnabled=false;
        }else{
            mViewsEnabled=true;
            mButtonVisibility=true;
            mCurrentTask=new Task(mUserRepository.getCurrentUser().getUUID());
            mCurrentTask.setState(TaskState.TODO);
            mCurrentTask.setDate(new Date());
        }
        mPhotoFile = mTaskRepository.getPhotoFile(getActivity(), mCurrentTask);

        mDateFormatter=new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        mTimeFormatter=new SimpleDateFormat("HH:mm", Locale.US);
        mDateTimeFormatter=new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss",Locale.US);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_task_detail, container, false);
        findViews(view);
        setButtonsVisibility(mButtonVisibility);
        loadState(savedInstanceState);
        mImageViewObserver=mImageViewTaskPhoto.getViewTreeObserver();
        setUI();
        setListeners();
        setViewEnabled(mViewsEnabled);
        return view;
    }

    private void findViews(View view) {
        mEditTextTitle=view.findViewById(R.id.edit_text_detail_title);
        mEditTextDescription=view.findViewById(R.id.edit_text_detail_description);
        mButtonDate=view.findViewById(R.id.button_detail_date);
        mButtonTime=view.findViewById(R.id.button_detail_time);
        mRadioGroupStates=view.findViewById(R.id.radio_group_states);
        mButtonCancel=view.findViewById(R.id.button_cancel);
        mButtonDelete=view.findViewById(R.id.button_delete);
        mButtonEdit=view.findViewById(R.id.button_edit);
        mButtonSave=view.findViewById(R.id.button_save);
        mButtonShare=view.findViewById(R.id.button_share);
        mButtonTakePhoto=view.findViewById(R.id.button_take_photo);
        mImageViewTaskPhoto=view.findViewById(R.id.image_view_task_photo);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_VIEWS_ENABLED,mViewsEnabled);
    }
    private void loadState(Bundle saveInstanceState){
        if(saveInstanceState!=null){
            mViewsEnabled=saveInstanceState.getBoolean(BUNDLE_VIEWS_ENABLED);
        }
    }

    private void setButtonsVisibility(boolean visibility) {
        if(visibility){
            mButtonDelete.setVisibility(View.GONE);
            mButtonEdit.setVisibility(View.GONE);
            mButtonCancel.setVisibility(View.VISIBLE);
            mButtonShare.setVisibility(View.GONE);
        }else{
            mButtonDelete.setVisibility(View.VISIBLE);
            mButtonEdit.setVisibility(View.VISIBLE);
            mButtonCancel.setVisibility(View.GONE);
            mButtonShare.setVisibility(View.VISIBLE);
        }
    }

    private void setUI() {
        mEditTextTitle.setText(mCurrentTask.getName());
        mEditTextDescription.setText(mCurrentTask.getDescription());
        mButtonDate.setText(mDateFormatter.format(mCurrentTask.getDate()));
        mButtonTime.setText(mTimeFormatter.format(mCurrentTask.getDate()));
        switch (mCurrentTask.getState()){
            case DOING:
                mRadioGroupStates.check(R.id.radio_button_state_doing);
                break;
            case DONE:
                mRadioGroupStates.check(R.id.radio_button_state_done);
                break;
            case TODO:
                mRadioGroupStates.check(R.id.radio_button_state_todo);
                break;
        }

        mImageViewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhotoView();
            }
        });
    }
    private void getUI(){
        mCurrentTask.setName(mEditTextTitle.getText().toString());
        mCurrentTask.setDescription(mEditTextDescription.getText().toString());
        switch (mRadioGroupStates.getCheckedRadioButtonId()){
            case R.id.radio_button_state_doing:
                mCurrentTask.setState(TaskState.DOING);
                break;
            case R.id.radio_button_state_done:
                mCurrentTask.setState(TaskState.DONE);
                break;
            case R.id.radio_button_state_todo:
                mCurrentTask.setState(TaskState.TODO);
                break;
        }
    }
    private void setListeners() {
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTaskRepository.delete(mCurrentTask);
                setResult();
                dismiss();
            }
        });

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewsEnabled=true;
                setViewEnabled(true);
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonVisibility){
                    getUI();
                    mTaskRepository.insert(mCurrentTask);
                    ((TabViewPagerActivity)getActivity()).updateFragments();
                }else{
                    getUI();
                    mTaskRepository.update(mCurrentTask);
                    setResult();
                }
                dismiss();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment=DatePickerFragment.newInstance(mCurrentTask.getDate());
                datePickerFragment.setTargetFragment(TaskDetailFragment.this,DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(),DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment=TimePickerFragment.newInstance(mCurrentTask.getDate());
                timePickerFragment.setTargetFragment(TaskDetailFragment.this,TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(),DIALOG_FRAGMENT_TAG);
            }
        });

        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent= ShareCompat
                        .IntentBuilder
                        .from(getActivity())
                        .setText(getShareText())
                        .setSubject(getString(R.string.task_share_subject))
                        .setType("text/plain")
                        .getIntent();
                Intent shareIntent=Intent.createChooser(sendIntent,null);
                if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(shareIntent);
            }
        });
        mButtonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("choose app")
                        .setItems(new String[]{"Camera", "Gallery"} , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                switch (which){
                                    case 0:
                                        takePhotoFromCamera();
                                        break;
                                    case 1:
                                        pickPhotoFromGallery();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (mPhotoFile == null)
                return;

            Uri photoURI = FileProvider.getUriForFile(
                    getActivity(),
                    FILEPROVIDER_AUTHORITY,
                    mPhotoFile);

            grantTemPermissionForTakePicture(takePictureIntent, photoURI);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
        }
    }

    private void pickPhotoFromGallery(){
        Intent pickPhoto=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(pickPhoto,PICK_PHOTO_REQUEST_CODE);
        }
    }

    private void setViewEnabled(boolean enabled) {
        mEditTextTitle.setEnabled(enabled);
        mEditTextDescription.setEnabled(enabled);
        mButtonDate.setEnabled(enabled);
        mButtonTime.setEnabled(enabled);
        for (int i = 0; i <mRadioGroupStates.getChildCount() ; i++) {
            mRadioGroupStates.getChildAt(i).setEnabled(enabled);
        }
    }

    private void setResult() {
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Date userSelectedDate=mCurrentTask.getDate();
        Date userSelectedTime=mCurrentTask.getDate();
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            userSelectedDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            mButtonDate.setText(mDateFormatter.format(userSelectedDate));
        }
        if (requestCode == TIME_PICKER_REQUEST_CODE) {
            userSelectedTime = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            mButtonTime.setText(mTimeFormatter.format(userSelectedTime));
        }
        setTaskDate(userSelectedDate, userSelectedTime);

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            updatePhotoView();

            Uri photoUri = FileProvider.getUriForFile(
                    getActivity(),
                    FILEPROVIDER_AUTHORITY,
                    mPhotoFile);
            getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        if (requestCode == PICK_PHOTO_REQUEST_CODE) {
            Uri pickedPhoto=data.getData();
            mImageViewTaskPhoto.setImageURI(pickedPhoto);
        }
    }

    private void setTaskDate(Date userSelectedDate , Date userSelectedTime) {
        Calendar calendar=Calendar.getInstance();

        calendar.setTime(userSelectedTime);
        int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        calendar.setTime(userSelectedDate);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mCurrentTask.setDate(calendar.getTime());
    }

    private String getShareText(){
        return "Task name: "+mCurrentTask.getName()+
                "\nTask description: "+mCurrentTask.getDescription()+
                "\nTask state: "+mCurrentTask.getState().toString()+
                "\nTask date: "+mDateTimeFormatter.format(mCurrentTask.getDate());
    }

    private void grantTemPermissionForTakePicture(Intent takePictureIntent, Uri photoURI) {
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                takePictureIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity: activities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageViewTaskPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_task_photo));
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),mImageViewTaskPhoto);
            mImageViewTaskPhoto.setImageBitmap(bitmap);
        }
    }
}