# AndroidPermission
这是一个自己写的6.0权限工具类
========================================================================
### 这个工具类有三种使用方法
* 初始化授权
* 申请授权，失败后再次授权提示去设置dialog，这个dialog会强制杀死当前activity
* 拒绝授权弹出dialog ，这个不会杀死activity，
--------------------------------------------------------------------------------
#### 使用一：
```Java
PermissionUtils.getInstance().applyInitPermission(MainActivity.this);
```
#### 使用二：
```Java
PermissionUtils.getInstance().setIsAuthor(IsAuthor.INSPECT).setPermission(per)
                .applyPermission(MainActivity.this, new PermissionHelper.OnPermission() {
                    @Override
                    public void agreePermission() {
                        show("授权成功");
                    }
                });
 ```
 ##### 还需要在activity中实现
 ```Java
     @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_PERMISSION){
            if (!PermissionUtils.getInstance().isAuthorized(grantResults)){
                //弹出dialog
                PermissionUtils.getInstance().showDialog(MainActivity.this,permissions);
            }else {
                show("授权成功，执行");
            }
        }else if (requestCode == PermissionUtils.ALL_PERMISSION){

        }
    }
```
#### 使用三：
````Java
 PermissionUtils.getInstance().setIsAuthor(IsAuthor.DEFAULT).setPermission(err)
                        .applyPermission(MainActivity.this, new PermissionHelper.OnPermission() {
                            @Override
                            public void agreePermission() {
                                show("授权成功");
                            }
                        });
```                        




