package com.atta.medicalcoversp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.ImagesAdapter;
import com.atta.medicalcoversp.LabTest;
import com.atta.medicalcoversp.LabTestAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.TestRequest;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewLabTestFragment extends Fragment {

    View root;

    private static final int PICK_IMAGE_REQUEST = 234;

    TestRequest testRequest;

    ArrayList<LabTest> labTests;
    List<Bitmap> imagesBitmap = new ArrayList<>();
    List<Uri> paths = new ArrayList<Uri>();
    List<String> imagesName = new ArrayList<>();
    ArrayList<String> uploadUrls = new ArrayList();

    RecyclerView testsRecyclerView, imagesRecyclerView;

    LabTestAdapter labTestAdapter;

    ImageView addLabTest, selectFile, selectedImg;

    TextInputEditText noteText;

    TextView dateTv;

    Button saveBtn;

    private Uri filePath;

    FirebaseStorage storage;

    FirebaseFirestore db;

    Date date;

    Timestamp timestamp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_new_lab_test, container, false);

        testRequest = NewLabTestFragmentArgs.fromBundle(getArguments()).getRequest();

        initiateViews();

        return root;
    }

    private void initiateViews() {

        labTests = new ArrayList<>();

        labTests.add(new LabTest());

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        dateTv = root.findViewById(R.id.test_date_tv);
        date = new Date();
        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTv.setText("Date: " + format.format(date));

        imagesRecyclerView = root.findViewById(R.id.img_recycler);

        testsRecyclerView = root.findViewById(R.id.recyclerView2);
        labTestAdapter = new LabTestAdapter(labTests, getContext(), false);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        testsRecyclerView.setAdapter(labTestAdapter);

        noteText= root.findViewById(R.id.radio_result);

        addLabTest = root.findViewById(R.id.addLabTestImg);
        addLabTest.setOnClickListener(view -> {

            labTests.add(new LabTest());
            labTestAdapter.notifyDataSetChanged();
        });

        //selectedImg = root.findViewById(R.id.img_recycler);

        selectFile = root.findViewById(R.id.selectFileImg);
        selectFile.setOnClickListener(v -> {
            selectFile();
        });

        saveBtn = root.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {
            if (paths.size() != 0){
                for (int i = 0; i < paths.size(); i++) {
                    uploadFile(i);
                }
            }else {
                saveRecord();
            }

        });
    }

    public void selectFile(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .enableLog(false) // disabling log
                .start();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                selectedImg.setImageBitmap(bitmap);
                selectedImg.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> imageList = ImagePicker.getImages(data);
            data.getData();

            if (!imageList.isEmpty()|| imageList != null){


                if (imagesBitmap.size() == 3){
                    imagesBitmap.clear();
                }
                for (int i = 0; i < imageList.size(); i++){
                    imagesBitmap.add(getBitmapFromPath(imageList.get(i).getPath()));
                    paths.add(Uri.parse("file://" + imageList.get(i).getPath()));
                    imagesName.add(imageList.get(i).getName());
                }

                if (imagesBitmap.size() == 3){
                    //addImages.setVisibility(View.GONE);
                }
                ImagesAdapter myAdapter = new ImagesAdapter(getContext(), imagesBitmap,
                        this, null);
                imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
                imagesRecyclerView.setAdapter(myAdapter);
            }

        }
    }

    private Bitmap getBitmapFromPath(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(filePath,bmOptions);

    }

    public void removeImage(int i){
        paths.remove(i);
        imagesName.remove(i);
    }

    private void uploadFile(int i) {
        //if there is a file to upload
        if (paths.size() != 0) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference ref = storage.getReference().child("Lab Tests/"+ imagesName.get(i));
            ref.putFile(paths.get(i))
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            uploadUrls.add(uri.toString());
                            Log.d("FB storage", uri.toString());

                            saveRecord();

                        });

                        //and displaying a success toast
                        //Toast.makeText(getContext(), storage.getReference(ref.getPath()).toString(), Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(exception -> {
                        //if the upload is not successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        Log.e("FB storage", exception.getMessage());

                        //and displaying error message
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void saveRecord() {

        if (uploadUrls.size() != paths.size()){
            return;
        }


        timestamp = new Timestamp(date);

        labTests = labTestAdapter.getData();


        Map<String, Object> labTestRecord = new HashMap<>();
        labTestRecord.put("labTests", labTests);
        labTestRecord.put("note", noteText.getText().toString().trim());
        labTestRecord.put("doctorId", testRequest.getDoctorId());
        labTestRecord.put("labId", testRequest.getCenterId());
        labTestRecord.put("doctorName", testRequest.getDoctorName());
        labTestRecord.put("labName", testRequest.getCenterName());
        labTestRecord.put("timestamp", timestamp);
        labTestRecord.put("images", uploadUrls);
        labTestRecord.put("userId", testRequest.getPatientId());

        db.collection("LabTest Records").document().set(labTestRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                    updateStatus("Finished");

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "An error", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void updateStatus(String status) {

        Map<String, Object> req = new HashMap<>();
        req.put("status", status);
        db.collection("Test Requests")
                .document(testRequest.getId())
                .update(req)
                .addOnSuccessListener(aVoid -> {

                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                            .navigate(NewLabTestFragmentDirections.actionNewLabTestFragmentToNavigationHome());

                }).addOnFailureListener(e -> {

                });
    }

}