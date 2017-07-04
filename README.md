## Firebase Storage

- 데이터 업로드 또는 다운로드, 메타데이터 가져오기 또는 업데이트, 파일 삭제 등을 수행
- 실시간 데이터 베이스와 경로가 비슷
- 파일은 storage에 저장됨

```java
// 참조 만들기
private StorageReference mStorageRef;  
mStorageRef = FirebaseStorage.getInstance().getReference(); // getReference를 기준으로 객체들이 들어감
```

> 파일 업로드하기
```java
// storage _upload a file
public void upLoadFile(String filePath){
    File file = new File(filePath);  // 스마트폰에있는 파일 업로드 경로
    Uri uri = Uri.fromFile(file);
    // 파이어 베이스의 파일 업로드 경로
    String fileName = file.getName();
    // 데이터 베이스의 키가 값과 동일한 구조
    StorageReference riversRef = mStorageRef.child(fileName);

    riversRef.putFile(uri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    // 파이어 베이스 스토리지에 방금 업로드한 파일의 경로
                    @SuppressWarnings("VisibleForTests")
                    Uri upLoadedUri = taskSnapshot.getDownloadUrl();
                    afterUploadFile(upLoadedUri);
                    Log.e("storage","success");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                    Log.e("storage","fail"+exception.getMessage());
                }
            });
}
```

> 이미지 업로드, 파일 경로
```java
// 데이터 전송
public void postData(View v ){

    String imgPath = txtImgName.getText().toString();
    // 이미지가 있으면 이미지 경로를 받아서 저장해야하기 때문에 이미지 경로를 먼저 받아서 업로드
    if(imgPath != null && !"".equals(imgPath)){
        upLoadFile(imgPath);
        // 없으면 실행
    }else{
        afterUploadFile(null);
    }

}
// 화면의 gallery버튼 자동 링크
public void openGallery(View v){
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    // 가. 이미지 선택창 호출
    startActivityForResult( Intent.createChooser(intent, "앱을 선택하세요") , 100);  // 코드가 100
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK){
        switch (requestCode) {
            // 나. 이미지 선택창에서 선택된 이미지의 경로 추출
            case 100:
                Uri imageUri = data.getData();
                // file전체 경로를 가져 옴
                String filePath = getPsthFromUri(this,imageUri);
                txtImgName.setText(filePath);
                break;
        }
    }
}
// Uri 에서 실제 경로 꺼내는 함수
public static String getPsthFromUri(Context context, Uri uri){
    String realPath = "";
    Cursor cu = context.getContentResolver().query(uri,null,null,null,null);
    if(cu.moveToNext()){
        realPath = cu.getString(cu.getColumnIndex("_data"));
    }
    cu.close();
    return realPath;
}
```
[전체소스보기](https://github.com/daaa08/FirebaseBbs/blob/master/app/src/main/java/com/example/da08/firebasebbs/WriteActivity.java)
> rule에서 재정의 해줘야 함
![스크린샷 2017-07-05 오전 12.19.09](http://i.imgur.com/RjMuAuX.png)

## Firebase Authentication
- 로그인 처리

```java
// 인증처리를 하기 위한 것
private FirebaseAuth mAuth = FirebaseAuth.getInstance();  
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("Auth", "onAuthStateChanged:signed_out");
            }
        }
    };

    public void signUp(View v){

       String email = editEmail.getText().toString();
       String pw = editPw.getText().toString();

       mAuth.createUserWithEmailAndPassword(email, pw)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());

                       if (!task.isSuccessful()) {
                           Toast.makeText(MainActivity.this, "사용자 등록이되지 않았습니다",
                                   Toast.LENGTH_SHORT).show();
                       }

                       // ...
                   }
               });
   }

   public void signIn(View v){
       String email = editEmail.getText().toString();
       String pw = editPw.getText().toString();

       mAuth.signInWithEmailAndPassword(email, pw)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());

                       if (!task.isSuccessful()) {
                           Log.w("Auth", "signInWithEmail:failed", task.getException());
                           Toast.makeText(MainActivity.this, "SignIn이 되지 않았습니다",
                                   Toast.LENGTH_SHORT).show();
                       }else {
                           Toast.makeText(MainActivity.this, "SignIn이 되었습니다",
                                   Toast.LENGTH_SHORT).show();

                           goMain();
                       }
                   }
               });
   }
```
[전체소스보기](https://github.com/daaa08/FirebaseBbs/blob/master/app/src/main/java/com/example/da08/firebasebbs/MainActivity.java)
## Firebase Notification(FCM)
- 푸쉬 전송

```java
// 사용자가 가입을하면 토큰을 생성해야함
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Message", "Refreshed token:" + refreshedToken);
```

[메세지 수신 및 처리 소스보기](https://github.com/daaa08/FirebaseBbs/blob/master/app/src/main/java/com/example/da08/firebasebbs/MyFirebaseMessagingService.java)
[토큰 생성 모니터링 소스보기](https://github.com/daaa08/FirebaseBbs/blob/master/app/src/main/java/com/example/da08/firebasebbs/MyFirebaseInstanceIDService.java)

![스크린샷 2017-07-05 오전 12.17.35](http://i.imgur.com/xUYRpQB.png)




> Token : 변수를 사용하여 실제 단말에서 생성된 토큰 값을 넣어 지정 URL로 보내는 것

> splash 화면은 backstack에 남아있으면 안되므로 finish()해줘야 함
