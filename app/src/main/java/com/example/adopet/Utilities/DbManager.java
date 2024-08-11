package com.example.adopet.Utilities;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.adopet.Interfaces.UserDetailsCallBack;
import com.example.adopet.Models.Category;
import com.example.adopet.Models.Pet;
import com.example.adopet.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class DbManager {

    private static DbManager instance;
    private ArrayList<Category> categories = new ArrayList<>(); // for recyclerview
    private static ArrayList<Pet> popular = new ArrayList<>(); // for recyclerview

    public interface CallBack<T> {
        void res(T res);
    }

    private DbManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("CATEGORIES");
        DatabaseReference petsRef = database.getReference("PETS");
        getCategoriesFromDB(categoriesRef);
        getPetsFromDB(petsRef);
    }

    public static void deleteUserImageFromDB(String userId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("Users_img").child(userId + ".jpg").delete();
    }

    public static void deleteUserImage(CallBack callBack) {
        updateUserImage("", res -> callBack.res(null));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageUrl = User.getInstance().getId();
        storageRef.child("Users_img").child(imageUrl + ".jpg").delete();
    }

    public static void updateUserImage(String imageUrl, CallBack callBack) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("USERS");
        usersRef.child(userUid).child("image").setValue(imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.res(null);
                    }
                });
    }

    public static void updatePetImage(String imageUrl, String petId, CallBack callBack) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference petsRef = database.getReference("PETS");
        petsRef.child(petId).child("img").setValue(imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.res(null);
                    }
                });
    }

    public static Pet getPetById(String petId) {
        for (Pet pet : popular) {
            if (pet.getId().equals(petId)) {
                return pet;
            }
        }
        return null;
    }

    public void sortCategories() {
        categories.sort((c1, c2) -> {
            if (c1.isPopular() && !c2.isPopular()) {
                return -1; // c1 comes first
            } else if (!c1.isPopular() && c2.isPopular()) {
                return 1; // c2 comes first
            } else {
                // Both are either popular or not popular, so sort by petsCount in descending order
                return Integer.compare(c2.getPetsCount(), c1.getPetsCount());
            }
        });
    }

    public static void sortPets() {
        popular.sort((p1, p2) -> Integer.compare(p2.getFavoriteCount(), p1.getFavoriteCount()));
    }

    public void getCategoriesFromDB(DatabaseReference categoriesRef) {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Firebasee", "DataSnapshot: " + snapshot.toString());
                categories.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = dataSnapshot.getKey();
                    String img = dataSnapshot.child("img").getValue(String.class);
                    int petsCount = dataSnapshot.child("petsCount").getValue(Integer.class);
                    boolean isPopular = dataSnapshot.child("isPopular").getValue(Boolean.class);
                    Category category = new Category(name, img, petsCount, isPopular);
                    categories.add(category);
                    Log.d("Firebasee", "Category added: " + name + ", " + img);
                }
                Log.d("Firebasee", "Total categories loaded: " + categories.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static ArrayList<Pet> getPopular() {
        return popular;
    }


    public void getPetsFromDB(DatabaseReference petsRef) {
        Log.d("Fiirebase", "inside getDogsFromDB");
        petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Fiirebasee", "DataSnapshot: " + snapshot.toString());
                Log.d("Fiirebase", "inside onDataChange");
                popular.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("Fiirebase", "inside for");
                    String type = dataSnapshot.child("type").getValue(String.class);
                    String id = dataSnapshot.child("id").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String breed = dataSnapshot.child("breed").getValue(String.class);
                    String about = dataSnapshot.child("about").getValue(String.class);
                    String img = dataSnapshot.child("img").getValue(String.class);
                    Log.d("Fiirebase", "before int");
                    int age = dataSnapshot.child("age").getValue(Integer.class);
                    int favoriteCount = dataSnapshot.child("favoriteCount").getValue(Integer.class);
                    Log.d("Fiirebase", "after int");
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String owner = dataSnapshot.child("owner").getValue(String.class);
                    String sex = dataSnapshot.child("sex").getValue(String.class);
                    Pet pet = new Pet().setId(id).setName(name).setBreed(breed).setAbout(about)
                            .setImg(img).setAge(age).setFavoriteCount(favoriteCount)
                            .setLocation(location).setPhone(phone).setOwner(owner)
                            .setType(type).setSex(sex);
                    Log.d("Fiirebase", "Pet added: " + pet.toString());
                    popular.add(pet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }


    public static void init() {
        if (instance == null)
            instance = new DbManager();
    }

    public static DbManager getInstance() {
        return instance;
    }

    public static void checkNewUser(String uid, CallBack<Boolean> callBack) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("USERS");
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callBack.res(true);
                } else {
                    callBack.res(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }


    public static void newPet(Pet pet) {
        popular.add(pet);
        sortPets();
        Log.d("newPet", "Pet added: " + pet.toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userPetsRef = database.getReference("USERS").child(pet.getOwnerId()).child("pets");
        DatabaseReference petsRef = database.getReference("PETS");
        DatabaseReference categoriesRef = database.getReference("CATEGORIES");
        incrementPetCount(pet.getType(), categoriesRef, true);
        petsRef.child(pet.getId()).setValue(pet);
        userPetsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPetsRef.child(pet.getId()).setValue(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public static void deletePet(String petId, String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("CATEGORIES");
        DatabaseReference petsRef = database.getReference("PETS");
        DatabaseReference usersRef = database.getReference("USERS");
        DatabaseReference userPetsRef = usersRef.child(userId).child("pets");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("Pets_img").child(petId + ".jpg").delete(); // remove pet's image from storage
        Log.d("deletePet", "Pet image deleted: " + petId);
        incrementPetCount(getPetById(petId).getType(), categoriesRef, false); // lower pet count
        Log.d("deletePet", "Pet count lowered: " + getPetById(petId).getType());
        petsRef.child(petId).removeValue(); // remove pet reference
        Log.d("deletePet", "Pet removed: " + petId);
        userPetsRef.child(petId).removeValue(); // remove pet from user's pets list
        Log.d("deletePet", "Pet removed from user's pets list: " + petId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() { // remove all favorites mentions of this pet
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("favorites").hasChild(petId)) {
                        dataSnapshot.child("favorites").child(petId).getRef().removeValue();
                        Log.d("deletePet", "Pet removed from user's favorites: " + petId);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        popular.removeIf(pet -> pet.getId().equals(petId));


    }

    public static void toggleFavoritePet(String userId, String petId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference favoritesRef = database.getReference("USERS").child(userId).child("favorites");
        DatabaseReference favoriteCountRef = database.getReference("PETS").child(petId).child("favoriteCount");
        favoriteCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int currCount = snapshot.getValue(Integer.class);
                // Now, toggle the favorite status based on whether the pet is already a favorite
                favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(petId)) {
                            favoritesRef.child(petId).setValue(1); // Add to favorites
                            favoriteCountRef.setValue(currCount + 1); // Increment favoriteCount
                        } else {
                            favoritesRef.child(petId).removeValue(); // Remove from favorites
                            favoriteCountRef.setValue(currCount - 1); // Decrement favoriteCount
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error: " + error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    public static void loadUserDetails(UserDetailsCallBack callback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = User.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("USERS").child(firebaseUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.child("id").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);
                HashMap<String, Integer> favorites = snapshot.child("favorites").getValue(new GenericTypeIndicator<HashMap<String, Integer>>() {
                });
                HashMap<String, Integer> pets = snapshot.child("pets").getValue(new GenericTypeIndicator<HashMap<String, Integer>>() {
                });

                user.setId(id);
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setImage(image);
                if (favorites != null) {
                    user.setFavorites(favorites);
                } else {
                    user.setFavorites(new HashMap<>());
                }
                if (pets != null) {
                    user.setPets(pets);
                } else {
                    user.setPets(new HashMap<>());
                }

                if (callback != null) {
                    callback.onUserDetailsLoaded(user);
                }

                Log.d("FFFirebase", "Favorites: " + favorites);
                Log.d("FFFirebase", "user: " + user);
                Log.d("FFFirebase", "User.getInstance(): " + User.getInstance().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private static void incrementPetCount(String type, DatabaseReference categoriesRef, boolean add) {
        DatabaseReference petRef;
        if (type.equalsIgnoreCase("Dog"))
            petRef = categoriesRef.child("Dogs");
        else if (type.equalsIgnoreCase("Cat"))
            petRef = categoriesRef.child("Cats");
        else if (type.equalsIgnoreCase("Fish"))
            petRef = categoriesRef.child("Fish");
        else if (type.equalsIgnoreCase("Parrot"))
            petRef = categoriesRef.child("Parrots");
        else if (type.equalsIgnoreCase("Rabbit"))
            petRef = categoriesRef.child("Rabbits");
        else
            petRef = categoriesRef.child("Turtles");
        petRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (add)
                    snapshot.child("petsCount").getRef().setValue(snapshot.child("petsCount").getValue(Integer.class) + 1);
                else
                    snapshot.child("petsCount").getRef().setValue(snapshot.child("petsCount").getValue(Integer.class) - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getUserImage(CallBack<String> callBack) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserImage(userUid, callBack);
    }

    public static void getUserImage(String userId, CallBack<String> callBack) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("USERS");

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    callBack.res(user.getImage());
                } else {
                    callBack.res(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void deleteUser(String userId, HashMap<String, Integer> favorites, CallBack<String> callBack) {
        Log.d("DbManager", "Deleting user with ID: " + userId);
        Log.d("DbManager", "Favorites: " + favorites);
        deleteUserImageFromDB(userId);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("USERS").child(userId);
        DatabaseReference petsRef = database.getReference("PETS");
        petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DbManager", " petsRef Snapshot: " + snapshot);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("DbManager", "petsRef Snapshot Child: " + dataSnapshot);
                    String petId = dataSnapshot.getKey();
                    Log.d("DbManager", "favoriteCount: " + dataSnapshot.child("favoriteCount").getValue(Integer.class));
                    int favoriteCount = dataSnapshot.child("favoriteCount").getValue(Integer.class);
                    if (favorites.containsKey(petId)) {
                        Log.d("DbManager", "PetId: " + petId);
                        dataSnapshot.getRef().child("favoriteCount").setValue(favoriteCount - 1);
                    }
                }
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("DbManager", "userRef Snapshot: " + snapshot);
                        DatabaseReference userPetsRef = snapshot.getRef().child("pets");
                        Log.d("DbManager", "userPetsRef: " + userPetsRef);
                        deleteUserPets(userPetsRef, userId, callBack);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private static void deleteUserPets(DatabaseReference userPetsRef, String userId, CallBack<String> callBack) {
        userPetsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                    String petId = petSnapshot.getKey();
                    Log.d("deleteUserPets", "PetId: " + petId);
                    deletePet(petId, userId);
                }
                callBack.res(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public static void updatePet(Pet pet) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference petsRef = database.getReference("PETS");
        petsRef.child(pet.getId()).setValue(pet);
    }

}
