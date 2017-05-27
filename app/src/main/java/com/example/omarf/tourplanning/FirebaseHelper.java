package com.example.omarf.tourplanning;

import com.example.omarf.tourplanning.Model.Expense;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by omarf on 2/3/2017.
 */

public class FirebaseHelper<T> {
    public static final String USER_REF="user";
    public static final String EXPENSE_REF = "expense";
    public static final String EVENT_REF="event";
    public static final String EVENT_PIC_REF = "event_pic";
    private FirebaseDatabase mDatabase;


    public FirebaseHelper() {
        mDatabase=FirebaseDatabase.getInstance();

    }

    public  void insertDataFB(String ParentRef, T model,String id){

        DatabaseReference reference=mDatabase.getReference(ParentRef);
        reference.child(id).setValue(model);
    }

    public void isUserExist(final String userId){
        DatabaseReference reference=mDatabase.getReference().child(USER_REF).child("asda");
       /* reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ()
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    public static StorageReference getReference(){
        FirebaseStorage storage=FirebaseStorage.getInstance();
        return storage.getReferenceFromUrl("gs://tourplanning-6e0cc.appspot.com");
    }

    public static String getUserId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }




}
