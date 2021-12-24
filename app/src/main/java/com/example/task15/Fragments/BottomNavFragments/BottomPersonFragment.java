package com.example.task15.Fragments.BottomNavFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.task15.Activity.HomePageActivity;
import com.example.task15.Activity.PeofileActivity;
import com.example.task15.Adapter.HomePageTabAdapter;
import com.example.task15.Classes.NotificationDialog;
import com.example.task15.Classes.ProfileModel;
import com.example.task15.MainActivity;
import com.example.task15.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class BottomPersonFragment extends Fragment {
    private TextView logout;
    public BottomPersonFragment() {

    }

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$");
    boolean check = false;

    public ProfileModel profileModel;
    private ProfileModel profileModel2;
    private FirebaseAuth auth;
    private static final int PICK_IMAGE = 1;
    static Uri imageUri = null;
    public static Bitmap bitmap = null;

    private EditText fName, email, password, phoneNumber, address;
    private ImageView back, notification;

    private CircleImageView circleImageView;
    Context applicationContext = MainActivity.getContextOfApplication();
    public static Boolean check123 = false;

    private Button save;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_person, container, false);
        profileModel = new ProfileModel();
        profileModel2 = new ProfileModel();
        initialize(view);
        storeProfileData();
        return view;

    }

    public void initialize(@NonNull View view) {
        auth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logoutText);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

        fName = view.findViewById(R.id.profileFullName);
        email = view.findViewById(R.id.profileEmail);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        password = view.findViewById(R.id.profilePassword);
        phoneNumber = view.findViewById(R.id.profilePhoneNumber);
        address = view.findViewById(R.id.profileAddress);
        address.setOnClickListener(this::ButtonClick);
        back = view.findViewById(R.id.profileBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HomePageTabAdapter.class));
                getActivity().finish();
            }
        });
        notification = view.findViewById(R.id.profileNnotificationBell);
        notification.setOnClickListener(this::noteClick);

        circleImageView = view.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(this::pickProfileImage);
        save = view.findViewById(R.id.profileSaveButton);

        save.setOnClickListener(this::saveClick);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    private void ButtonClick(View view) {

    }

    private void saveClick(View view) {
        String getPhone = phoneNumber.getText().toString().trim();
        String getAddress = address.getText().toString().trim();

        boolean t1, t2;
        t1 = validatePassword();
        t2 = validateEmail();
        confirmInput(view);
        if (getPhone.equals("") || getAddress.equals("")) {
            phoneNumber.setError("Filed Can't Be Empty ");
            address.setError("Filed Can't Be Empty ");
        } else if (getPhone.length() != 10) {
            Toast.makeText(getContext(), "The phone number must consist of 10 digits", Toast.LENGTH_SHORT).show();
        } else if (t2 == true && t1 == true) {
            saveProfileData();
        }

    }

    private void pickProfileImage(View view) {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "select picture"), PICK_IMAGE);

    }

    private void noteClick(View view) {
        NotificationDialog notificationDialog = new NotificationDialog(getActivity());
        notificationDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                circleImageView.setImageBitmap(bitmap);
                store_Image(imageUri);
            } catch (IOException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Image Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address \n" +
                    "Must have @ and .com ");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password is too weak" +
                    "\n" + "" + "at least 4 characters and no spaces " +
                    "\n" + "at least 1 special character");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        check = true;
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Initialize location
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    //Initialize address list
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //Set Address
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("location");

                        databaseReference.setValue(String.valueOf(Html.fromHtml(
                                "<font color='#6200EE'><b></b><br></font>"
                                        + addressList.get(0).getAddressLine(0))));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void store_Image(Uri uri) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
        StorageReference reference = FirebaseStorage.getInstance().getReference("profile pic/");
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();
        pd.setCanceledOnTouchOutside(true);
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = reference.child(System.currentTimeMillis() + "." + getFileExtinsion(uri));
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        check123 = true;
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference databaseReference213 = FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("imageUrl");

                                databaseReference213.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            databaseReference213.setValue(uri.toString());
                                        }else {
                                            databaseReference213.setValue(uri.toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(applicationContext, "Error\n"+error.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                                Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(applicationContext, "Failed To Upload", Toast.LENGTH_SHORT).show();
                        check123 = false;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

    private String getFileExtinsion(Uri mUri) {
        ContentResolver cr = applicationContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void storeProfileData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                        profileModel = new ProfileModel(
                                snapshot.child("name").getValue(String.class)
                                ,snapshot.child("email").getValue(String.class)
                                ,snapshot.child("pass").getValue(String.class)
                                ,snapshot.child("repass").getValue(String.class)
                                ,snapshot.child("location").getValue(String.class)
                                ,snapshot.child("order").getValue(String.class)
                                ,snapshot.child("complete").getValue(String.class)
                                ,snapshot.child("phoneNumber").getValue(String.class)
                        );
                    Glide.with(getContext()).load(snapshot.child("imageUrl").getValue(String.class)).into(circleImageView);

                    fName.setText(profileModel.getName());
                    password.setText(profileModel.getPass());
                    email.setText(profileModel.getEmail());
                    phoneNumber.setText(profileModel.getPhoneNumber());
                    address.setText(profileModel.getLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveProfileData(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //    public ProfileModel(String name, String email, String pass, String repass, String location, String order, String complete, String phoneNumber) {

                    profileModel2 = new ProfileModel(
                            fName.getText().toString().trim()
                            , email.getText().toString().trim()
                            , password.getText().toString().trim()
                            , profileModel.getRepass()
                            , address.getText().toString().trim()
                            , profileModel.getOrder()
                            , profileModel.getComplete()
                            , phoneNumber.getText().toString().trim()
                    );
                    databaseReference.setValue(profileModel2);
                    Toast.makeText(getContext(), "Data Modified Successfully", Toast.LENGTH_SHORT).show();

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("notification")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("note1");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                databaseReference1.setValue("Your Profile Has Been Modified ");
                            }else {
                                databaseReference1.setValue("Your Profile Has Been Modified ");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getImage(){

    }
}