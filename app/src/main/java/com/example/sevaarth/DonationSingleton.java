//package com.example.sevaarth;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class DonationSingleton {
//    private FirebaseUser user;
//    private DatabaseReference mDataBase_user;
//    private String userKey,email;
//    private static DonationSingleton don;
////    private ArrayList<Donation_Data_Org> currentdonations;
////    private ArrayList<Donation_Data_Org> previousdonations;
//    private DatabaseReference mDatabase;
//
//    private DonationSingleton(Context context){
//
//    }
//
//    public static DonationSingleton get(Context context){
//        if(don == null){
//            don = new DonationSingleton(context);
//        }
//        return don;
//    }
//
//    public static String EncodeString(String string) {
//        return string.replace(".", ",");
//    }
//
//    protected void displaying_status(DatabaseReference reference, String email){
//        Log.d("RUN", "4");
//
//        reference.child(email).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot childsnapshot : snapshot.getChildren()) {
//                        Donation_Data_Org l = childsnapshot.getValue(Donation_Data_Org.class);
////                        int complete = l.getComplete();
////                        if (complete == 1){
////                            currentdonations.add(l);
////                        }
////                        else{
////                            previousdonations.add(l);
////                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//
//    public void CreateList(){
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("OrgDonations");
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        mDataBase_user = FirebaseDatabase.getInstance().getReference("Users");
//        userKey = user.getUid();
//        mDataBase_user.child(userKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                email = snapshot.child("email").getValue(String.class);
//                email = EncodeString(email);
//                for(int i=0; i<5; i++){
////                    Donation_Data_Org org = new Donation_Data_Org(namelist[i], typelist[i],datelist[i], completed[i]);
////            studentItems.add(new Donation_Data_Org());
////                    mDatabase.child(email).push().setValue(org);
//                }
//                displaying_status(mDatabase,email);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("RUN", "error"+error);
//            }
//        });
//
//    }
//
//    public ArrayList<Donation_Data_Org> getcurrentlist(){
//        return currentdonations;
//    }
//
//    public ArrayList<Donation_Data_Org> getpreviouslist(){
//        return previousdonations;
//    }
//
////    public Donation_Data_Org getcurrentsingle(int id){
////        for(Donation_Data_Org s : currentdonations){
////            if (s.getId() == id){
////                return s;
////            }
////        }
////        return null;
////    }
//
//}