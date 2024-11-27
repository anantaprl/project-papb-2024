package sf.mobile.wantsscreen.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import sf.mobile.wantsscreen.model.DataItem;

public class RealtimeDatabase {

    private final DatabaseReference mDatabase;
    private static final String databaseName = "wants_item";
    private static final String tableName = "items";

    public RealtimeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference(databaseName);
    }

    public void listenData(ValueEventListener listener){
        mDatabase.addValueEventListener(listener);
    }
    public void writeItem(DataItem dataItem, DatabaseListener dbListener) {
        mDatabase.child(UUID.randomUUID().toString()).setValue(dataItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dbListener.onSuccess(unused);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dbListener.onError(e);
            }
        });
    }

    public void deleteItem(DataItem dataItem, DatabaseListener dbListener){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String uid = snapshot1.child("uid").getValue(String.class);
                    if (dataItem.getUid().equals(uid)) {
                        // Delete the item
                        snapshot1.getRef().removeValue()
                                .addOnSuccessListener(dbListener::onSuccess)
                                .addOnFailureListener(dbListener::onError);
                        break; // Exit the loop once the item is found
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error reading data", error.toException());
            }
        });

    }


}
